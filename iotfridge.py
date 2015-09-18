#!/usr/bin/env python

"""
    iotfridge.py - An IoT fridge API

    This software acts as the API between an SQLite database
    and the interfaces to the fridge.

    Remember to initialise a database first!

    James Collerton
    46114

    This makes a lot more sense when read from the bottom to the top, so
    I would start there and then read up.

    IMPORTANT NOTE: I used some extra libraries which may need to be installed.

    They include:
        matplotlib
        requests
        OutpanApi
"""

# ############################### Libraries ###################################

import sys
import sqlite3 as sql
import json
import time
import datetime
import random
import hashlib
import matplotlib as mpl
import matplotlib.pyplot as plt
import re
import requests
import os

from outpan import OutpanApi
from facepy import GraphAPI
from facepy.exceptions import OAuthError

# #############################################################################

# ############################### CONSTANTS ###################################

ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

outpan_api = OutpanApi('a9d9e45901e40b1704a92e4e0fceb487') 

# #############################################################################

class IoTFridge:
    """
        Implements the IoT Fridge API
    """

    # When we initialise give it the database to connect to,
    # a cursor within the db, stdin as the input and stdout
    # as the output. We also set the login to false and the
    # information regarding the user as empty.
    def __init__(self, dbpath, infile, outfile):
        self.db = sql.connect(dbpath)
        self.cur = self.db.cursor()
        self.infile = infile
        self.outfile = outfile
        self.username = ""
        self.logged_in = False
        self.email_address = ""
        self.db.execute("PRAGMA foreign_keys = ON")

    # Begin API requests

    # ######################### GENERAL ###############################

    # Generic function to print out a failure message, with a reason for failure.

    def failed_req(self, arg):
        
        resp = {'response' : arg, 'success' : False}
        print >> self.outfile, "\n", resp, "\n"

    # Generic function to print out a success message.

    def success_rec(self):
        resp = {'response': 'OK', 'success': True}
        print >> self.outfile, "\n", json.dumps(resp), "\n"

    # Function for counting results.

    def num_results(self, res_cursor):

        num_rows = 0
        for row in res_cursor:
            num_rows += 1
        return(num_rows)

    # This counts the number of results returned from a query. If there
    # is more than zero then the primary key is taken.

    def pk_taken_result(self, sql_cmmd, fail):
        search_res = self.cur.execute(sql_cmmd)
        if(self.num_results(search_res) > 0):
            if(fail == True): self.failed_req('primary key taken.') 
            return(True)
        return(False)

    # Does the same as the function below, but for where there are
    # two values in the primary key.

    def comb_pk_taken(self, pk_val_one, pk_val_two, table, pk_one, pk_two, fail):

        sql_cmmd = "SELECT * FROM " + table +  " WHERE " + pk_one + " = '" 
        sql_cmmd += str(pk_val_one) + "'" + " AND " + pk_two + " = '"
        sql_cmmd += str(pk_val_two) + "'"
        return( self.pk_taken_result(sql_cmmd, fail) )

    # Looks for primary key values, and if they are taken returns this.

    def pk_taken(self, pk_val, table, pk, fail):

        sql_cmmd = "SELECT * FROM " + table +  " WHERE " + pk + " = '" + str(pk_val) + "'"
        return( self.pk_taken_result(sql_cmmd, fail) )

    # Used as a general way of executing sql commands and then printing a message.

    def insert(self, data, sql_cmmd, message):

        self.cur.execute(sql_cmmd, data)
        self.db.commit()
        if(message == True):
            self.success_rec();

    def check_barcode(self, barcode):

        if( 0 < len(barcode) and len(barcode) < 20 and barcode.isdigit() ):
            return True

        return False

    # Whenever we want to accept a json command, to stop it from crashing we run
    # this with the expected arguments to see if they are there and can be assigned,

    def check_info(self, reqj, info_list):

        for item in info_list:
            try:
                test = reqj[item]
            except KeyError:
                self.failed_req('error with user input.')
                return(False)
        return(True)

    # These are a set of functions used pretty widely throughout the program.

    # ################################ GENERIC FUNCTIONS #############################

    # ################################################################################

    # This returns the type for the product. So if it is solid it returns weight, liquid
    # gives volume etc.

    def return_type(self, barcode, metric, table):

        sql_cmmd = "SELECT " + metric + " FROM " + table + " WHERE barcode = "
        sql_cmmd += str(barcode)

        for row in self.cur.execute(sql_cmmd):
            return(row[0])

    # This is used to find the price and location from the table. Unlike the previous
    # price location request this synthesises a lot more information into the one
    # table and then returns it in a more user friendly way.

    def find_price_location(self, product):

        sql_cmmd = "SELECT * FROM price_location INNER JOIN ( "
        sql_cmmd += "SELECT * FROM product_info WHERE prod_name = "
        sql_cmmd += "'" + product + "'" + " ) AS prod_info ON "
        sql_cmmd += " price_location.barcode = prod_info.barcode"

        i = 0
        items = []

        for row in self.cur.execute(sql_cmmd):
            items.append({ 'item' : {} })
            items[i]['item'].update({'barcode' : row[0]})
            items[i]['item'].update({'price' : row[1]})
            items[i]['item'].update({'location' : row[2]})
            items[i]['item'].update({'name' : row[4]})
            items[i]['item'].update({'manufacturer' : row[5]})
            items[i]['item'].update({'type' : row[6]})
            i += 1

        return(items)

    # This is the find request. When users want to find a product by name they can use
    # this and it will tell them where it is available and for what price. Although
    # this loop may seem quite similar to the other ones for going through the different
    # tables of solid, liquid and pack, they are all different enough to merit their
    # own loops.

    def req_find(self, reqj):

        if(self.check_info(reqj, ['product']) == True):
            items = self.find_price_location(reqj['product'])

            i = 0

            for item in items:
                if (item['item']['type'] == 'S'): 
                    metric = self.return_type(item['item']['barcode'], 'weight', 'solid')
                    metric_name = 'weight'
                elif (item['item']['type'] == 'L'):
                    metric = self.return_type(item['item']['barcode'], 'volume', 'liquid')
                    metric_name = 'volume'
                elif (item['item']['type'] == 'P'):
                    metric = self.return_type(item['item']['barcode'], 'num_units', 'pack')
                    metric_name = 'pack size'
                items[i]['item'].update({metric_name : metric})
                i += 1
            
            print >> self.outfile, "\n", json.dumps(items, indent = 1), "\n"

    # This cycles through lists of expired items and then adds them to the message with
    # the appropriate headings attached. It returns the message to have more info added to
    # it.

    def cycle_expired_items(self, message, expired_list):

        for item in expired_list:
            message += "\n Barcode: " + str(item['item']['barcode']) + "\n"
            message += "\n Item name: " + str(item['item']['name']) + "\n"
            message += "\n Item manufacturer: " + str(item['item']['manufacturer']) + "\n"
            message += "\n ------------------------------- \n"

        return(message)

    # This actually sends the email component with the message. It uses the mailgun
    # site and the request library to put a request in to the site to send the email.
    # It then sends the email to whichever address you're logged in as.

    def send_email(self, today_expired, three_days_expired):

        key = 'key-17a8bba64262bd0139ac29b6d77e6f58'
        sandbox = 'sandbox03d7770e45654ce5a58c47f7f8a49647.mailgun.org'
        recipient = 'jc1175@my.bristol.ac.uk'

        # So the message is initialised if no items have expired before today.
        message = ""

        if( len(today_expired) > 0 ):
            message = 'Hello! \n\nWe recieved a check request for your fridge! '
            message += 'We had a quick look for you, and it looks like these '
            message += 'items expired today:\n'

            message = self.cycle_expired_items(message, today_expired)

        if( len(three_days_expired) > 0 ):
            message += '\nWhile you\'re at the shops, you might want to pick up: \n'

            message = self.cycle_expired_items(message, three_days_expired)

            message += '\nBecause they\'ll be going out of date in the next three '
            message += 'days. Remember you can use the recipe function on your fridge '
            message += 'to find recipes to use them up with! '

        if( len(today_expired) == 0 and len(three_days_expired) == 0 ):
            message += "Congratulations! \n\n Nothing out of date or going out of date!"

        message += '\n\nWarmest regards (not really, lol)\n\n Your fridge.'

        request_url = 'https://api.mailgun.net/v2/{0}/messages'.format(sandbox)
        request = requests.post(request_url, auth=('api', key), data={
            'from': 'friendlyfoodreminder@fridge.com',
            'to': self.email_address,
            'subject': 'Check request!',
            'text': message
        })

    # ######################### PRINTING ##############################

    # Used to check the number of rows in each of the tables.

    def check_record_numbers(self, table):

        sql_cmmd = "SELECT * FROM " + table
        search_res = self.cur.execute(sql_cmmd)
        num_results = self.num_results(search_res)
        return(num_results)

    # Used to check the database size and how many records are in each table.
    # Generates a warning if it detects that performance may be affected. I
    # wanted to set up a way of automatically deleting the oldest stuff from
    # tables when performance was beginning to suffer, but my original schema
    # weren't set out to deal with this particularly well and I didn't have time
    # to refactor.

    # This was my compromise. Now you can check on your fridge's current memory
    # use and then use it to reason about what you should delete.

    def req_database_health(self, reqj):

        database_size = os.path.getsize("fridge.db")
        prod_records = self.check_record_numbers("product_info")
        user_records = self.check_record_numbers("users")
        stock_records = self.check_record_numbers("stock")
        low_fat_records = self.check_record_numbers("low_fat")
        price_location_records = self.check_record_numbers("price_location")

        database_message = 'Your current database size is: ' + str(database_size)
        database_message += ' bytes, with a recommended size of 2GB. 2MB'
        database_message += ' is the default data cache size, after which'
        database_message += ' performance may suffer.'

        database_health = {'database_size' : database_message,
                           'records in product_info table' : prod_records,
                           'records in user table' : user_records,
                           'records in stock table' : stock_records,
                           'records in low_fat table' : low_fat_records,
                           'records in price_location table' : price_location_records}

        # Set as 0 so the warning is generated to prove it works. Normally would 
        # be ~2MB.
        if(database_size > 0):
            database_warning = "WARNING: EXCEEDED DATA CACHE. Performance may"
            database_warning += " suffer for some queries."
            database_health.update( { 'warning' : database_warning } )

        print >> self.outfile, "\n", json.dumps(database_health, indent = 1), "\n"

    # This is used to find all the expired items between dates or before a
    # certain date. The two dates argument is a boolean telling the function
    # whether or not it should look for a lower bound. It then creates
    # an array of answers and passes them back to the function to be printed
    # or emailed.

    def find_expired_items(self, date_one, date_two, two_dates):

        sql_cmmd = "SELECT * FROM product_info INNER JOIN ( "
        sql_cmmd += "SELECT expiry, barcode "
        sql_cmmd += "FROM stock WHERE username = " + "'" + self.username + "'"
        sql_cmmd += " AND expiry < " + "'" + date_two.strftime("%Y-%m-%d") + "' "

        if(two_dates == True):
            sql_cmmd += "AND expiry > " + "'" + date_one.strftime("%Y-%m-%d") + "' "

        sql_cmmd += " ) AS expired_info ON expired_info.barcode = product_info.barcode"

        i = 0
        expired_items = []

        # Creates an array of expired items.
        for row in self.cur.execute(sql_cmmd):
            expired_items.append({ 'item' : {} })
            expired_items[i]['item'].update({'barcode' : row[0]})
            expired_items[i]['item'].update({'name' : row[1]})
            expired_items[i]['item'].update({'manufacturer' : row[2]})
            expired_items[i]['item'].update({'expiry' : row[5]})
            i += 1

        return(expired_items)

    # Now we move to checking the dates of the items. Normally this would be done off today's
    # date but for the sake of demonstrating I fixed it so you can see how it would work.
    # From today's date we also work out a date in three days time, this is so we can warn
    # the user not only of items that have gone off, but ones that will go off.

    # It then passes to functions to collect the item information and finally sends an email
    # to the user telling them to update their stock.

    def req_check_dates(self, reqj):

        today = "2015-01-02"
        today = datetime.datetime.strptime(today, "%Y-%m-%d")
        three_days_time = today + datetime.timedelta(days = 3)

        today_expired = self.find_expired_items(None, today, False)
        three_days_expired = self.find_expired_items(today, three_days_time, True)

        self.send_email(today_expired, three_days_expired)

        print >> self.outfile, "\n", json.dumps(today_expired, indent = 1), "\n"
        print >> self.outfile, "\n", json.dumps(three_days_expired, indent = 1), "\n"

    # From the range and the y values we now make the graph. This was surprisingly hard to
    # get to work because of the combination of different graph types and the legend. It
    # uses matplotlib but is pretty self explanatory.

    def make_plot(self, dates_range, y_vec, product):

        plt.figure(figsize = (14,7))
        plt.ylim([0, max(y_vec['expiry'] + y_vec['purchase'] + y_vec['total']) + 0.5])

        plot_title = product.title() + " Usage"

        ax = plt.subplot(111)
        ax.set_title(plot_title)
        ax.set_xlabel('Dates')
        ax.set_ylabel('Units')
        p_bar = ax.bar(dates_range, y_vec['purchase'], width = 0.2, color = 'green')
        e_bar = ax.bar(dates_range, y_vec['expiry'], width = 0.2, color = 'red')
        t_line = ax.plot(dates_range, y_vec['total'], 'b--')
        leg = ax.legend([p_bar, e_bar, t_line])
        leg.get_texts()[0].set_text('Total units')
        leg.get_texts()[1].set_text('Purchased units')
        leg.get_texts()[2].set_text('Expired units')
        ax.xaxis_date()

        plt.show()

    # This is to print the json information for the API to use. I made the plot because
    # I wanted to learn a little bit about the Scipy style stuff and was keen to try 
    # and get this to work.

    def json_for_api(self, product, plot_dates):

        product_dates = { 'product' : product,
                          'expiry' : plot_dates['expiry'],
                          'purchase' : plot_dates['purchase'] }

        print >> self.outfile, "\n", json.dumps(product_dates, indent = 1), "\n"

    # This is used to find the y values. It starts off with everything as zero. Then it goes
    # through the range of dates and checks how much milk was purchased or expired on that
    # date. It increments that day's counter by that amount. It also creates a graph of the
    # total milk in the fridge.

    def find_y_vec(self, dates_range, plot_dates):

        expiry_y = [0] * len(dates_range)
        purchase_y = [0] * len(dates_range)
        total_y = [0] * len(dates_range)

        expiry_dates = [datetime.datetime.strptime(date, "%Y-%m-%d") for date in plot_dates['expiry']]
        purchase_dates = [datetime.datetime.strptime(date, "%Y-%m-%d") for date in plot_dates['purchase']]

        i = 0

        for date in dates_range:
            expiry_count = expiry_dates.count(date)
            purchase_count = purchase_dates.count(date)
            expiry_y[i] += expiry_count
            purchase_y[i] += purchase_count
            if( i != 0 ):
                total_y[i] = total_y[i - 1] + purchase_count - expiry_count
            else:
                total_y[i] = purchase_count - expiry_count
            i += 1

        y_vec = {'expiry' : expiry_y, 'purchase' : purchase_y, 'total' : total_y}
        return(y_vec)

    # This is used to find the range of dates. It starts by putting all of the
    # expiry and purchase dates in one vector. Then it orders them, and finds the
    # highest and lowest date. From there it creates the range by going from
    # the lowest to the highest and appending every date in between to a vector.

    def find_low_high_date(self, plot_dates):

        timestamps = []

        for date in plot_dates['purchase']:
            timestamps.append(date)

        for date in plot_dates['expiry']:
            timestamps.append(date)

        dates = [datetime.datetime.strptime(ts, "%Y-%m-%d") for ts in timestamps]
        dates.sort()
        sorted_dates = [datetime.datetime.strftime(ts, "%Y-%m-%d") for ts in dates]

        lowest_date = sorted_dates[0]
        highest_date = sorted_dates[len(sorted_dates) - 1]

        dates_range = []

        date = datetime.datetime.strptime(lowest_date, '%Y-%m-%d') - datetime.timedelta(days = 1)
        dates_range.append(date)

        while(date != datetime.datetime.strptime(highest_date, '%Y-%m-%d') + datetime.timedelta(days = 1) ):
            date += datetime.timedelta(days = 1)
            dates_range.append(date)

        return(dates_range)

    # This is the plottting part of the function. It creates a range of dates and uses them
    # as the x axis. Then for every date within this range it queries the database to see
    # if the product was purchased or expired on that date. If so it will increment
    # or decrement the counters in the above function accordingly.

    # NOTE: I have the purchase date being automatically registered as the
    # moment the item is put in the fridge, so to make a decent graph I've
    # done a swap here. If you actually used the fridge you wouldn't need it.

    def plot_exp_purch(self, plot_dates, product):

        temp_purch_dates = ['2013-12-30', '2013-12-31', '2014-01-04', '2014-01-12', '2014-01-18']
        plot_dates['purchase'] = temp_purch_dates

        dates_range = self.find_low_high_date(plot_dates)
        y_vec = self.find_y_vec(dates_range, plot_dates)
        self.json_for_api(product, plot_dates)
        self.make_plot(dates_range, y_vec, product)

    # This gets the necessary data for the plot. For the product it gets all of the 
    # stock for that user and the expiry and purchase dates. It then puts all of these
    # into a dictionary to be used by other functions.

    def get_plot_data(self, reqj):

        sql_cmmd = "SELECT * FROM product_info INNER JOIN ( "
        sql_cmmd += "SELECT expiry, purchase, barcode FROM stock "
        sql_cmmd += "WHERE username = " + "'" + self.username + "'"
        sql_cmmd += " ) AS stock_user_info ON product_info.barcode = "
        sql_cmmd += " stock_user_info.barcode"
        sql_cmmd += " WHERE prod_name = " + "'" + reqj['product'] + "'"

        expiry_dates = []
        purchase_dates = []

        for row in self.cur.execute(sql_cmmd):
            expiry_dates.append(row[4])
            purchase_dates.append(row[5])

        plot_dates = { 'expiry' : expiry_dates, 'purchase' : purchase_dates }

        return(plot_dates)

    # These are the functions for plotting the usage of items. Check all the 
    # necessary information is available in the request and then runs it.

    def req_plot(self, reqj):

        if(self.logged_in == True):
            if(self.check_info(reqj, ['product'])):
                plot_dates = self.get_plot_data(reqj)
                self.plot_exp_purch(plot_dates, reqj['product'])
            else: self.failed_req('you must specify a product.')
        else: self.failed_req('you must be logged_in.')

    # This posts a status to facebook for you. The access token here will probably
    # have expired by the time you come to use it, but please email me and I'll send
    # you one to use. This will post a facebook status on my page, but I'm happy
    # to sort out some way of proving it works.

    def post_status(self, web_address, name, fridge_ingredients, need_to_buy):

        status = "Hi guys! Tonight I'm going to be cooking delicious " + name
        status += " with the " + ', '.join(fridge_ingredients) + " I've got lying around..."
        status += " Just popping to the shops to get some " + ', '.join(need_to_buy) + " first!"

        try:
            ACCESS_TOKEN = 'CAACEdEose0cBAEICMon6cf65gJBOIDUswc13x6PVyKj7D3bmVrFvJCL6MMGgTCBPnPReLb3ZCHqshDWuXxBiS81A2mHEt3FKlg9410ujo81qf5U0E7WH9REAe5NNxJMJVZAMeYZCUSwbhDZAiQy5UNummwZC42hoqidiisBIiKDtF8MivXYZAaBhsj1ZCFiOZCZBF7FkFLZAX24ax3pSSyfHFmRK4oerAEt84ZD'
            graph = GraphAPI(ACCESS_TOKEN)
            graph.post('me/feed', message = status, link = web_address)
        except OAuthError as error:
            print error.message

    # Now for each recipe we can make with some of the ingredients in the fridge
    # we find that information and return it to the prior function to be printed
    # out.

    def recipe_information(self, recipe):

        sql_cmmd = "SELECT * FROM recipe_info WHERE web_address = "
        sql_cmmd += "'" + recipe + "'"

        for row in self.cur.execute(sql_cmmd):
            name = row[1]
            feeds = row[2]

        return( {'name' : name, 'feeds' : feeds} )

    # This finds all of the ingredients needed for the recipes not
    # contained within the fridge.

    def need_to_buy(self, ingredients, fridge_ingredients):
        
        need_to_buy = []

        for ingredient in ingredients:
            if(ingredient not in fridge_ingredients):
                need_to_buy.append(ingredient)

        return(need_to_buy)        

    # This gives the fridge ingredients needed for the recipes.

    def fridge_ingredients(self, recipe, ingredients):

        sql_cmmd = "SELECT * FROM product_info INNER JOIN ( "
        sql_cmmd += "SELECT barcode AS stock_barcode FROM stock "
        sql_cmmd += "WHERE username = " + "'" + self.username + "'"
        sql_cmmd += ") AS fridge ON fridge.stock_barcode = product_info.barcode"

        fridge_ingredients = []

        for row in self.cur.execute(sql_cmmd):
            fridge_ingredients.append(row[1])

        return(fridge_ingredients)

    # This is used to classify the ingredients into ones we have in the fridge
    # and ones we don't have in the fridge. Once we've done that it compiles
    # them all into a json and prints it to screen.

    # If the facebook option is turned on then it posts a status.

    def classify_ingredients(self, recipe, ingredients, facebook):

        fridge_ingredients = self.fridge_ingredients(recipe, ingredients)
        need_to_buy = self.need_to_buy(ingredients, fridge_ingredients)
        recipe_information = self.recipe_information(recipe)

        resp = { 'response': [], 'success': True }

        resp['response'].append({ 'web address': recipe })
        resp['response'].append({ 'name': recipe_information['name'] })
        resp['response'].append({ 'feeds': recipe_information['feeds'] })
        resp['response'].append({ 'in your fridge': fridge_ingredients })
        resp['response'].append({ 'need to buy': need_to_buy })

        print >> self.outfile, "\n", json.dumps(resp, indent = 1), "\n"

        if(facebook == True):
            self.post_status(recipe, recipe_information['name'], fridge_ingredients,
                             need_to_buy)

    # From the recipes we have found, now we find the ingredients necessary
    # in order to make it.

    def find_ingredients(self, recipe):

        sql_cmmd = "SELECT * FROM ingredients WHERE "
        sql_cmmd += "web_address = " + "'" + recipe + "'"

        ingredients = []

        for row in self.cur.execute(sql_cmmd):
            ingredients.append(row[1])

        return(ingredients)

    # This pulls recipes from recipe database that have at least one ingredient
    # in the fridge at the moment. It adds recipe web addresses (the primary key)
    # to a list if it is not contained already.

    def recipes_fridge_ing(self):

        sql_cmmd = "SELECT * FROM ingredients INNER JOIN ( "
        sql_cmmd += "SELECT * FROM product_info INNER JOIN ( "
        sql_cmmd += "SELECT barcode AS stock_barcode FROM stock WHERE "
        sql_cmmd += "username = " + "'" + self.username + "'" + ") AS fridge "
        sql_cmmd += "ON fridge.stock_barcode = product_info.barcode"
        sql_cmmd += ") AS prod_info "
        sql_cmmd += "ON prod_info.prod_name = ingredients.ingredient"

        recipes = []

        for row in self.cur.execute(sql_cmmd):
            if( row[0] not in recipes ):
                recipes.append(row[0])           

        return(recipes) 

    # Firstly it finds all the recipes in the recipe database that have at least one
    # ingredient in the fridge. Then for each of these recipes it finds the ingredients
    # and classifies them as being already in the fridge, or needing to be bought.

    def fridge_recipe(self, facebook):

        recipes = self.recipes_fridge_ing()

        for recipe in recipes:
            ingredients = self.find_ingredients(recipe)
            self.classify_ingredients(recipe, ingredients, facebook)

    # This is called when the user wants to be able to pull up a recipe with
    # some of the ingredients they have in their fridge. It checks the necessary
    # info is there, and the command is for my fridge (I originally wanted
    # to extend it so you could type what ingredients you wanted to use, but
    # ran out of time). It then passes to the next function.

    # The facebook term determines wether or not we want to post this to FaceBook.
    # You can do this, but you'll need to email me for an API key as they expire,
    # and so I'll need to generate one for you.

    def req_recipe(self, reqj):

        if(self.logged_in == True):
            if(self.check_info(reqj, ['ingredients', 'facebook']) == True):
                if(reqj['ingredients'] == 'my fridge'):
                    if( reqj['facebook'].lower() == 'true'):
                        self.fridge_recipe(True)
                    else: self.fridge_recipe(False)
                else: self.failed_req('please specify the ingredients location')
        else: self.failed_req('you must be logged in.')

    # Here is the function that returns diet versions of your requested item.
    # It creates the necessary SQL statement and then pulls the required info
    # from it and adds it to an array of items to be returned. From there it
    # finds the item types as in the previous function.

    def req_diet(self, reqj):

        if(self.logged_in == True):
            if(self.check_info(reqj, ['product']) == True):
                sql_cmmd = "SELECT * FROM product_info INNER JOIN ( "
                sql_cmmd += "SELECT * FROM low_fat INNER JOIN ( "
                sql_cmmd += "SELECT * FROM product_info "
                sql_cmmd += "WHERE prod_name =" + "'" + reqj['product'] + "'"
                sql_cmmd += ") AS prod_info "
                sql_cmmd += "ON prod_info.barcode = low_fat.original_barcode"
                sql_cmmd += ") AS low_fat "
                sql_cmmd += "ON low_fat.low_fat_barcode = product_info.barcode"

                resp = { 'response': [], 'success': True }

                for row in self.cur.execute(sql_cmmd):

                    item = []

                    item.append({ 'barcode': row[0] })
                    item.append({ 'name': row[1] })
                    item.append({ 'manufacturer': row[2] })
                    item.append({ 'type': row[3] })

                    resp['response'].append({ 'item': item })

                    self.find_item_types(resp)

            else: self.failed_req('you need to specify a product')

        else: self.failed_req('you must be logged in')

    # This is the final stage in the my stock request. It adds the type on and
    # then prints it to the screen.

    def add_type(self, item, metric, table):

        sql_cmmd = "SELECT " + metric + " FROM " + table + " WHERE barcode = "
        sql_cmmd += str(item['item'][0]['barcode'])

        for row in self.cur.execute(sql_cmmd):
            item['item'].append({ metric: row[0] })

        print >> self.outfile, "\n", json.dumps(item, indent = 1), "\n"

    # Once we have found the stock information we need to pull up the necessary type info
    # so we can give the weight/ volume/ number in pack. This accesses the type and then
    # adds the according metric.

    def find_item_types(self, resp):

        for item in resp['response']:

            if(item['item'][3]['type'] == "S"):
                self.add_type(item, 'weight', 'solid')

            elif(item['item'][3]['type'] == "L"):
                self.add_type(item, 'volume', 'liquid')

            elif(item['item'][3]['type'] == "P"):
                self.add_type(item, 'num_units', 'pack')

    # This is the my stock command for users wanting to see what they have in their
    # fridge. As opposed to the list product command which just lists the tables as is,
    # this synthesises the information into something more user friendly. It creates 
    # a SQL statement to pull out the necessary info, and then chooses the parts it would
    # like to present, whilst giving them headers for the user.

    def req_my_stock(self, reqj):

        if(self.logged_in == True):
            sql_cmmd = "SELECT * FROM product_info INNER JOIN stock "
            sql_cmmd += "ON stock.barcode = product_info.barcode "
            sql_cmmd += "WHERE username = " + "'" + self.username + "'"

            resp = { 'response': [], 'success': True }

            for row in self.cur.execute(sql_cmmd):

                item = []

                item.append({ 'barcode': row[0] })
                item.append({ 'name': row[1] })
                item.append({ 'manufacturer': row[2] })
                item.append({ 'type': row[3] })
                item.append({ 'expiry date': row[6] })
                item.append({ 'purchase date': row[7] })

                resp['response'].append({ 'item': item })

            self.find_item_types(resp)

        else: self.failed_req('you must be logged in')

    # As different types of product have different amount metrics, this detects what type
    # of product it is and finds that information accordingly. It then returns this as a
    # dictionary with the name of the metric (weight/ volume/ number in pack) and then the
    # actual measurement.

    def find_metrics(self, prod_type, attribs):

        if(prod_type == 'L'): 
            metric = 'volume'
            if(self.check_info(attribs['attributes'], ['Volume']) == True):
                metric_value = attribs['attributes']['Volume']
        elif(prod_type == 'S'):
            metric = 'weight'
            if(self.check_info(attribs['attributes'], ['Weight']) == True):
                metric_value = attribs['attributes']['Weight']
        elif(prod_type == 'P'):
            metric = 'number in pack'
            if(self.check_info(attribs['attributes'], ['Number in Pack']) == True):
                metric_value = attribs['attributes']['Number in Pack']

        return({ 'metric' : metric, 'value' : metric_value })

    # This finds the expiry date of the food according to the information given on Outpan.
    # Outpan will supply an average length of time until food expires, and so we add that
    # amount of time onto now, when we put the food into the fridge to come up with
    # its expiry date.

    def find_expiry(self, expiry_length):

        today = time.strftime("%Y-%m-%d")
        today = datetime.datetime.strptime(today, "%Y-%m-%d")
        expiry = today + datetime.timedelta(days = int(expiry_length) )
        expiry = expiry.strftime("%Y-%m-%d")

        return(expiry)

    # This is the find id function used when adding new elements by their barcode
    # and Outpan. We need to find a unique stock_id for them, and so we just start
    # at one and increment through until we find one that is free. I appreciate there
    # are quicker ways (storing the last id we put in, binomial search etc.) but it 
    # seemed a bit of overkill for the simple examples I was giving.

    def find_id(self):

        i = 1
        free = False

        while free == False:
            if(self.pk_taken(i, 'stock', 'stock_id', False) == False):
                free = True
                return(i)
            else: i += 1

    # This function is for the user to add stock according to its barcode and source the
    # information from Outpan. It checks a barcode is specified and then gets the info
    # using the Outpan API, It then checks that the necessary information has been retrieved
    # and creates a pseudo json using that in order to feed it into the earlier insert
    # product function.

    def req_add(self, reqj):

        if(self.check_info(reqj, ['barcode']) == True ):

            name = outpan_api.name(reqj['barcode'])
            attribs = outpan_api.attributes(reqj['barcode'])
            prod_id = self.find_id()

            info = ['Average Expiry Length', 'Manufacturer', 'Type']

            if(self.check_info(attribs['attributes'], info) == True):

                expiry_date = self.find_expiry(attribs['attributes']['Average Expiry Length'])
                prod_type = attribs['attributes']['Type'][0].upper()
                metric_info = self.find_metrics(prod_type, attribs)

                pseudo_json = {'table' : 'product'}
                pseudo_json.update({'id' : prod_id, 'data' : {} })
                pseudo_json['data'].update({'expiry date' : expiry_date, 
                                            'barcode' : reqj['barcode'], 
                                            'name' : name['name'],
                                            'manufacturer' : attribs['attributes']['Manufacturer'],
                                            'type' : prod_type,
                                            metric_info['metric'] : metric_info['value'] })

                self.insert_product(pseudo_json)

            else: self.failed_req('insufficient online information to fulfill the request')

        else: self.failed_req('no barcode included with request.')

    # This is the login sequence. We select from the username table according to the username
    # we have been given. Then we take out the necessary data and hash the password they
    # have given us. If the hashed password and the stored password match then we save their
    # data locally (username and successful log in) to be used later.

    def login_seq(self, reqj):

        password = ""

        sql_cmmd = "SELECT * FROM users WHERE username = " + "'" + reqj['data']['username'] 
        sql_cmmd += "'"

        for row in self.cur.execute(sql_cmmd):
            salt = row[1]
            password = row[2]
            email_address = row[3]

        hashed_input = hashlib.sha512(reqj['data']['password'] + salt).hexdigest()

        if(password == hashed_input):

            self.username = reqj['data']['username']
            self.logged_in = True
            self.email_address = email_address
            self.success_rec()

        else: self.failed_req('incorrect password')

    # The next few functions are to do with logging in. When a login request is detected we
    # check the necessary components are there and then pass to the next function.

    def req_login(self, reqj):

        info_list_one = ['data']
        info_list_two = ['username', 'password']

        if(self.check_info(reqj, info_list_one) == True and 
            self.check_info(reqj['data'], info_list_two) == True):

            self.login_seq(reqj)

    # This is used to generate random codes of length ten to be used as the
    # salt for the password.

    def gen_random_code(self):

        chars = []
        for i in range(10): chars.append(random.choice(ALPHABET))
        random_code = "".join(chars)
        return(random_code)

    # This is the create account function. It checks the person creating the account
    # has specified the right information in order to do so and then sets about putting
    # the information into the table.

    # It generates random salt and a random stock code 
    # Then takes the user's password and email address and combines it all
    # into one json. The point of doing this is that the earlier function for making users
    # directly accepts a json of this form, and so we can reuse the function this way.
    # Once this pseudo-json is fed to the insert user function the password is hashed.

    def req_create_account(self, reqj):
            
        if(self.check_info(reqj['data'], ['username', 'password', 'email address']) == True):
            salt = self.gen_random_code()
            password = reqj['data']['password']
            email_address = reqj['data']['email address']
            
            data = reqj['data']
            data['password'] = password
            data.update({'salt' : salt, 'email address' : email_address})
            pseudo_json = {'data' : data}

            self.insert_users(pseudo_json)
        else: self.failed_req('must specify username, password and email address')

    ###########################################################################
    # 
    #   Below here is what I mostly thought of as developing functions, which
    #   could be used to put things directly into tables, list tables and add
    #   to them. From here upwards is more geared towards what I would have the
    #   user access and represents some of the more sophisticated functionality
    #   of the API. 
    # 
    ###########################################################################

    # This is a generic function used to print the tables. It takes in arguments
    # corresponding to a table and a list of headers for the elements in the 
    # table and then selects everything from that table, assigns the components
    # headers and prints them to screen.

    def print_list(self, table, responses):

        resp = { 'response': [], 'success': True }
        sql_cmmd = "SELECT * FROM " + table
        for row in self.cur.execute(sql_cmmd):
            i = 0
            for response in responses:
                resp['response'].append({ response: row[i] })
                i += 1
        print >> self.outfile, "\n", json.dumps(resp, indent = 1), "\n"

    # Same as below but for printing the low fat association table.

    def low_fat_list(self):
        responses = ['original barcode', 'low fat equivalent barcode']
        self.print_list("low_fat", responses)

    # Same as below, but to print complete product information you need
    # to print the stock table, the product table and the types tables.

    def product_list(self):
        responses = ['id', 'barcode', 'expiry', 'purchase', 'stock code']
        self.print_list("stock", responses)
        responses = ['barcode', 'name', 'manufacturer', 'type']
        self.print_list("product_info", responses)
        responses = ['barcode', 'weight']
        self.print_list("solid", responses)
        responses = ['barcode', 'volume']
        self.print_list("liquid", responses)
        responses = ['barcode', 'number in pack']
        self.print_list("pack", responses)

    # Works the same as below pretty much, but as all of the recipe information is
    # spread over two tables you need to print out both tables.

    def recipes_list(self):
        responses = ['web address', 'name', 'feeds']
        self.print_list("recipe_info", responses)
        responses = ['web_address', 'ingredients']
        self.print_list("ingredients", responses)

    # Same as below but with users.

    def users_list(self):
        responses = ['user name', 'salt', 'password', 'email address']
        self.print_list("users", responses)

    # Lists the price location table. Specifies a vector of titles for each component
    # of the list and then feeds the table name and the vector to the print list function.

    def price_location_list(self):
        responses = ['barcode', 'price', 'location']
        self.print_list("price_location", responses)

    # This marks the start of the listing functions, which are mainly used to directly
    # view the contents of the tables. Other functions defined above are more user
    # friendly and synthesise the information a little more, these are useful for checking
    # if things have been inserted where they should have been.

    # We check if we are logged in and a table is specified. Then according to the 
    # table we list different components.

    def req_list(self, reqj):

        if(self.check_info(reqj, ['table']) == True):
            if(self.logged_in == True):

                table = reqj['table']
                if(table == "users"): self.users_list()
                elif(table == "recipes"): self.recipes_list()
                elif(table == "product"): self.product_list()
                elif(table == "low fat"): self.low_fat_list()
                elif(table == "price location"): self.price_location_list()
                else: self.failed_req('unidentified table')

            else: self.failed_req('not logged in')
        else: self.failed_req('no table information')

    # ############################ Listing Functions #################################

    # ############################### LISTING ########################################

    # ################################################################################

    # This is used in directly inserting users into tables. It checks all of the
    # data is present and noone with that username exists, then it adds all of the
    # necessary information to a tuple and puts it in the table. The password is
    # also hashed before being put in.

    # NOTE: This is different to creating an account! The functions below here are
    # more dev tools in order to be able to put directly into, and take them directly
    # out of the necessary tables. There are more user friendly versions above.

    def insert_users(self, reqj):

        info_list_one = ['data']
        info_list_two = ['username', 'salt', 'password', 'email address']

        if(self.check_info(reqj, info_list_one) == True and 
            self.check_info(reqj['data'], info_list_two) == True and
             self.pk_taken(reqj['data']['username'], "users", "username", True) == False):

                for_hashing = str(reqj['data']['password']) + str(reqj['data']['salt'])

                data = (reqj['data']['username'], 
                        reqj['data']['salt'], 
                        hashlib.sha512(for_hashing).hexdigest(),
                        reqj['data']['email address'])
                sql_cmmd = "INSERT INTO users VALUES (?, ?, ?, ?)"
                self.insert(data, sql_cmmd, True)

    # ################################ Inserting Users ###############################

    # This deals with the rest of it where it adds the necessary ingredients for each recipe.

    def insert_ingredient_info(self, reqj):

        ingredients = reqj['data']['ingredients'].split(",")
        sql_cmmd = "INSERT INTO ingredients VALUES (?, ?)"
        for ing in ingredients:
            ingredient_data = (reqj['web address'], ing.strip())
            self.insert(ingredient_data, sql_cmmd, False)

    # This takes care of the first part of the insertion into the recipe_info
    # table, where it inserts the web address and the number of people it feeds.

    def insert_recipe_info(self, reqj):

        rec_info_data = (reqj['web address'], reqj['data']['name'],
                                reqj['data']['feeds'])
        sql_cmmd = "INSERT INTO recipe_info VALUES (?, ?, ?)"
        self.insert(rec_info_data, sql_cmmd, True)

    # Used in inserting recipes. We specify we need all of the recipe info before
    # we can insert it and that the primary key is not taken. The recipe information
    # is split into two sections. The name of the recipe and the web address and then
    # the ingredients. This is to reduce redundancy. The insertion is then split over
    # those two tables.

    def insert_recipe(self, reqj):

        info_list_one = ['request', 'web address', 'data']
        info_list_two = ['name', 'feeds', 'ingredients']

        if(self.check_info(reqj, info_list_one) == True and
            self.check_info(reqj['data'], info_list_two) == True and
             self.pk_taken(reqj['web address'], 'recipe_info', 'web_address', True) == False):

                self.insert_recipe_info(reqj)
                self.insert_ingredient_info(reqj)

    # ############################ Inserting Recipes ################################

    # This is used to insert values into the low fat table. As usual, it
    # checks the info, checks the primary keys and then creates a SQL statement
    # and a tuple of values to insert. Also checks that what has been put
    # in the association table references real things.

    # Also contained are foreign key checks to make sure that the program doesn't
    # error.
    def insert_low_fat(self, reqj):

        info_list_one = ['request', 'data']
        info_list_two = ['original barcode', 'low fat barcode']

        if(self.check_info(reqj, info_list_one) == True and
            self.check_info(reqj['data'], info_list_two) == True and
             self.comb_pk_taken(reqj['data']['original barcode'],
                                reqj['data']['low fat barcode'],
                                'low_fat', 'original_barcode',
                                'low_fat_barcode', True) == False):

            if(self.pk_taken(reqj['data']['original barcode'], 'product_info', 'barcode', False) == True and
                self.pk_taken(reqj['data']['low fat barcode'], 'product_info', 'barcode', False) == True):

                if(self.check_barcode(reqj['data']['original barcode']) == True and
                    self.check_barcode(reqj['data']['low fat barcode']) == True):

                    low_fat_info = (reqj['data']['original barcode'], reqj['data']['low fat barcode'])
                    sql_cmmd = "INSERT INTO low_fat VALUES (?, ?)"
                    self.insert(low_fat_info, sql_cmmd, True)

                else: self.failed_req('incorrect barcode format')

            else: self.failed_req('foreign key constraint failed')

    # This is a generic function for inserting different amounts of different types into
    # the solid, liquid and pack tables. It creates a SQL statement from the given inputs
    # and executes it.

    def insert_a_type(self, reqj, metric, table):

        if(self.check_info(reqj['data'], ['barcode', metric]) == True):
            if( reqj['data'][metric] > 0):

                type_info = (reqj['data']['barcode'], reqj['data'][metric])
                sql_cmmd = "INSERT INTO " + table + " VALUES (?, ?)"
                self.insert(type_info, sql_cmmd, False)

            else: self.failed_req('weight/ volume/ number of units must be positive')
        else: self.failed_req('correct weight metric not specified')

    # This is the final stage of inserting a product, inserting its type and amount. In order
    # to deal with the different types of product (solid/ liquid/ pack), three tables were 
    # created. This checks what type of product the inserted one is and then fits it in the 
    # correct table. If it doesn't fit in, then it prints the message that the other info
    # was correctly added to the corresponding tables, but this was not.

    def insert_type_info(self, reqj):

        info_list_one = ['id', 'data']
        info_list_two = ['type', 'barcode']

        if(self.check_info(reqj, info_list_one) == True and
            self.check_info(reqj['data'], info_list_two) == True):

            if(reqj['data']['type'] == 'S' and
                self.pk_taken(reqj['data']['barcode'], 'solid', 'barcode', True) == False):

                self.insert_a_type(reqj, 'weight', 'solid')

            elif(reqj['data']['type'] == 'L' and
                self.pk_taken(reqj['data']['barcode'], 'liquid', 'barcode', True) == False):

                self.insert_a_type(reqj, 'volume', 'liquid')

            elif(reqj['data']['type'] == 'P' and
                self.pk_taken(reqj['data']['barcode'], 'pack', 'barcode', True) == False):

                self.insert_a_type(reqj, 'number in pack', 'pack')

            else:
                self.failed_req('stock info added, type info not.')

    # This is the second stage of inserting a product. If the user specifies a name, manufacturer
    # and type for the product, as well as the barcode, then it is entered into the product_info
    # DB by creating a SQL statement and executing the insert.

    def insert_product_info(self, reqj):

        info_list_one = ['id', 'data']
        info_list_two = ['barcode', 'name', 'manufacturer', 'type']

        if(self.check_info(reqj, info_list_one) == True and
            self.check_info(reqj['data'], info_list_two) == True and
             self.pk_taken(reqj['data']['barcode'], 'product_info', 'barcode', True) == False):

            prod_info = (reqj['data']['barcode'], reqj['data']['name'],
                         reqj['data']['manufacturer'], reqj['data']['type'])
            sql_cmmd = "INSERT INTO product_info VALUES (?, ?, ?, ?)"
            self.insert(prod_info, sql_cmmd, False)

        else:
            self.failed_req('stock info added, product info not.')

    # This is used to insert the stock info into the stock table. The stock table requires
    # the username, the barcode, the expiry date, the purchase date and the stock code. The
    # stock code is used to associate items with their owners. The username and barcode are 
    # taken from the json, the purchase date is automatically set to whenever they put the 
    # item in the fridge (convenient for them, not so convenient for us when we're trying 
    # to give examples of the fridge working over time) and the username is local.

    def insert_basic_product(self, reqj):

        basic_prod_info = (reqj['id'], reqj['data']['barcode'],
                           reqj['data']['expiry date'],
                           time.strftime("%Y-%m-%d"), self.username)
        sql_cmmd = "INSERT INTO stock VALUES (?, ?, ?, ?, ?)"
        self.insert(basic_prod_info, sql_cmmd, True)

    # Used in the below to make sure that the date entered is in the format specified by
    # the SQLite table.

    def correct_date_form(self, date_str):

        try:
            datetime.datetime.strptime(date_str, '%Y-%m-%d')
        except ValueError:
            self.failed_req('incorrect date format')
            return(False)

        return(True)

    # Used to insert products into the product_info table and stock tables. The
    # way it is structured is that it checks to make sure there is some stock info
    # (expiry date and barcode) and that the date is in the right form. From there
    # it splits the insertions into three types: Inserting the stock info (basic product)
    # inserting the product info (name, manufacturer etc.) and inserting the type info.

    # The good thing about this is that it allows some flexibility with what the user 
    # enters. If they don't know the name and manufacturer they can still enter the stock
    # info and know they have something in their fridge that expires at a certain time.

    def insert_product(self, reqj):

        info_list_one = ['id', 'data']
        info_list_two = ['expiry date', 'barcode']

        if(self.check_info(reqj, info_list_one) == True and
            self.check_info(reqj['data'], info_list_two) == True and
                self.correct_date_form(reqj['data']['expiry date']) == True and
                    self.pk_taken(reqj['id'], 'stock', 'stock_id', True) == False):

                    if(reqj['id'] >= 0):
                        if(self.check_barcode(reqj['data']['barcode']) == True):

                            self.insert_basic_product(reqj)
                            self.insert_product_info(reqj)
                            self.insert_type_info(reqj)

                        else: self.failed_req('incorrect barcode format')
                    else: self.failed_req('All IDs must be greater than zero.')

    # This is used to insert values into the price and location table. Checks to see
    # whether there is enough info in the json, then if the primary key is taken (note,
    # the primary key consists of two values). Then it creates the SQL statement and
    # passes it to insert to run.

    def insert_price_loc(self, reqj):
        
        info_list_one = ['data']
        info_list_two = ['barcode', 'price', 'location']

        if(self.check_info(reqj, info_list_one) == True and
            self.check_info(reqj['data'], info_list_two) == True and
               self.comb_pk_taken(reqj['data']['barcode'],
                                  reqj['data']['location'],
                                  'price_location',
                                  'barcode', 'location', True) == False):

            if(self.pk_taken(reqj['data']['barcode'], 'product_info', 'barcode', False) == True):
                if(self.check_barcode(reqj['data']['barcode']) == True):

                    price_loc_info = (reqj['data']['barcode'],
                                       reqj['data']['price'],
                                       reqj['data']['location'])
                    sql_cmmd = "INSERT INTO price_location VALUES (?, ?, ?)"
                    self.insert(price_loc_info, sql_cmmd, True)

                else: self.failed_req('barcode format incorrect')
            else: self.failed_req('foreign key constraint broken')

    # Now we've moved out of the deleting functions and into the inserting ones.
    # This works the same as before, but inserts values straight into the relevant
    # tables. First of all it checks the relevant info is in place, and if the
    # user is logged in. Unless they are logged in they have no priviledges.

    def req_insert(self, reqj):

        if(self.check_info(reqj, ['table']) == True):
            if(self.logged_in == True):

                table = reqj['table']
                if(table == "users"): self.insert_users(reqj)
                elif(table == "recipes"): self.insert_recipe(reqj)
                elif(table == "product"): self.insert_product(reqj)
                elif(table == "low fat"): self.insert_low_fat(reqj)
                elif(table == "price location"): self.insert_price_loc(reqj) 
                else: self.failed_req('unidentified table')

            else: self.failed_req('not logged in')
        else: self.failed_req('no table information')

    # ############################ Inserting Products ################################

    # ################################# INSERTING ####################################

    # ################################################################################

    # This is the delete stock command the user can make. Previously
    # the deleting has mapped directly to the tables and gotten rid of all
    # the necessary parts. This is for the user so they can delete the instance
    # of their stock, but NOT all of the information relating to it in
    # the product table. This is important as we want to keep all of the info
    # in the product table.

    def req_delete_stock(self, reqj):

        if(self.check_info(reqj, ['id']) == True):
            sql_cmmd = "DELETE FROM stock WHERE stock_id = " + str(reqj['id'])
            sql_cmmd += " AND username = " + "'" + self.username + "'"

            self.delete_commit(sql_cmmd, True)

    # This commits the sql command and prints the necessary response.

    def delete_commit(self, sql_cmmd, message):

        self.cur.execute(sql_cmmd);
        self.db.commit()
        if(message == True):
            self.success_rec();

    # This is a generic function in order to delete items with a single primary key.
    # Works identically to below.

    def delete(self, pk_val, table, pk, message):

        if(self.pk_taken(pk_val, table, pk, False) == True):

            sql_cmmd = "DELETE FROM " + table + " WHERE " + pk + " = '" + str(pk_val) + "'"
            self.delete_commit(sql_cmmd, message)

        else:
            if(message == True):
                self.failed_req('primary key value does not exist.')

    # This is a generic function used to delete any entries that use more than one
    # value as their primary key. It takes in the expected arguments, formulates them
    # into a sql statement and then executes it. There's also an argument of whether
    # you want a message printed or not. This is so when we delete all the different 
    # bits of info from different tables corresponding to one request we do not get
    # loads of messages.

    def delete_comb_pk(self, pk_val_one, pk_val_two, table, pk_one, pk_two, message):

        if(self.comb_pk_taken(pk_val_one, pk_val_two, table, pk_one, pk_two, False) == True):

            sql_cmmd = "DELETE FROM " + table +  " WHERE " + pk_one + " = '" 
            sql_cmmd += str(pk_val_one) + "'" + " AND " + pk_two + " = '"
            sql_cmmd += str(pk_val_two) + "'"
            self.delete_commit(sql_cmmd, message)

        else:
            if(message == True):
                self.failed_req('primary key value does not exist.')

    # Used to delete user information. It sees if we have a username specified,
    # and then checks if it is equal to our own username. I made the decision that
    # you could only delete your own username as that seemed sensible.

    def delete_user(self, reqj):

        if(self.check_info(reqj, ['username']) == True):
            if(reqj['username'] == self.username):
                self.delete(reqj['username'], "users", "username", True)
            else: self.failed_req('you can only delete your own username.')

    # This is used for deleting recipes. It checks to make sure we have the json
    # that is the primary key and if we do it deletes the data from the ingredients
    # and from the list of recipes.

    def delete_recipe(self, reqj):

        if(self.check_info(reqj, ['web address']) == True):
            self.delete(reqj['web address'], "ingredients", "web_address", False)
            self.delete(reqj['web address'], "recipe_info", "web_address", True)

    # This is used to delete products. If an ID is specified then it will
    # delete the stock according to that ID. If a barcode is specified then
    # it will delete all of the product information relating to that barcode.
    # The idea is to rarely delete that product information and to have it
    # accrue over time in order to create a large DB of products we can use.

    # The last few if statements are so we don't get errors from trying to 
    # delete from the product table with dependent foreign keys still there.

    def delete_product(self, reqj):

        if(self.check_info(reqj, ['id']) == True):
            self.delete(reqj['id'], "stock", "stock_id", True)
        if(self.check_info(reqj, ['barcode']) == True):
            self.delete(reqj['barcode'], "solid", "barcode", False)
            self.delete(reqj['barcode'], "liquid", "barcode", False)
            self.delete(reqj['barcode'], "pack", "barcode", False)
            
            if( self.pk_taken(reqj['barcode'], 'price_location', 'barcode', False) == False and
                 self.pk_taken(reqj['barcode'], 'low_fat', 'original_barcode', False) == False and 
                   self.pk_taken(reqj['barcode'], 'low_fat', 'low_fat_barcode', False) == False):
                self.delete(reqj['barcode'], "product_info", "barcode", False)
            else: self.failed_req('foreign key error')

    # This deletes from the low fat table. Works exactly the same as price
    # location except it uses the components for the low fat table.

    def delete_low_fat(self, reqj):

        info_list_one = ['request', 'data']
        info_list_two = ['original barcode', 'low fat barcode']

        if(self.check_info(reqj, info_list_one) == True and
            self.check_info(reqj['data'], info_list_two) == True):

            self.delete_comb_pk(reqj['data']['original barcode'],
                                reqj['data']['low fat barcode'],
                                'low_fat', 'original_barcode',
                                'low_fat_barcode', True)

    # Used for deleting components from the price and location table.
    # Checks the necessary parts of the json are there and then deletes
    # the entry according to the primary key specified.

    def delete_price_loc(self, reqj):

        info_list_one = ['data']
        info_list_two = ['barcode', 'location']

        if(self.check_info(reqj, info_list_one) == True and
            self.check_info(reqj['data'], info_list_two) == True):

            self.delete_comb_pk(reqj['data']['barcode'],
                                reqj['data']['location'],
                                'price_location', 'barcode',
                                'location', True)

    # These are all delete requests that map directly to tables.
    # When a delete request is registered, it feeds into here which
    # acts as a switch and redirects the json data to the necessary
    # part. Check info is used to check the part of the json we need
    # is actually there before proceeding, and I have it so you need
    # to be logged in to do any deleting.

    def req_delete(self, reqj):

        if(self.check_info(reqj, ['table']) == True):
            if(self.logged_in == True):

                table = reqj['table']
                if(table == "users"): self.delete_user(reqj)
                elif(table == "recipes"): self.delete_recipe(reqj)
                elif(table == "product"): self.delete_product(reqj)
                elif(table == "low fat"): self.delete_low_fat(reqj)
                elif(table == "price location"): self.delete_price_loc(reqj) 
                else: self.failed_req('unidentified table')

            else: self.failed_req('not logged in')
        else: self.failed_req('no table information')

    # ################################# DELETING  ####################################

    # ############################ REQUEST PROCESSING ################################

    # ################################################################################

    def processRequest(self, req):
        """
            Takes a JSON request, does some simple checking, and tries to call
            the appropriate method to handle the request. The called method is
            responsible for any output.
        """
        # This first part is so incorrect json does not crash the program.
        correct_json = True

        try:
            jsonData = json.loads(req)
        except ValueError:
            correct_json = False
            print >> sys.stderr, 'Incorrect json Input.'
        if(correct_json == True and "request" in jsonData):
            reqstr = 'req_{0}'.format(jsonData['request'])
            # Echo the request for easier output debugging
            print req

            if reqstr in dir(self):
                getattr(self,reqstr)(jsonData)
            else:
                print >> sys.stderr, "ERROR: {0} not implemented".format(
                    jsonData['request'])
                errorResp = {
                        'response': "{0} not implemented".format(
                            jsonData['request']),
                        'success': False}
                print >> self.outfile, json.dumps(errorResp)
        else:
                print >> sys.stderr, "ERROR: No request attribute in JSON"

    # Main function being run in the class.
    def run(self):
        """
            Read data input, assume a blank line signifies that the buffered
            data should now be parsed as JSON and acted upon
        """
        lines = []
        while True:
            line = self.infile.readline()
            if line == '': break
            lines.append(line.strip())
            if len(lines) > 1 and lines[-1] == '':
                self.processRequest( ''.join(lines) )
                lines = []

# ################################### MAIN  ######################################

if __name__ == '__main__':
    """
        Connect stdin and stdout to accept and emit JSON data

        Non-API content is printed to stderr, so it can be redirected
        independently.
    """

    # If two arguments are not entered, reminds them to put in the db.
    if len(sys.argv) != 2:
        print >> sys.stderr, "Usage: python iotfridge.py dbfilename"
        sys.exit(1)

    # Creates fridge with the database, and looks for standard in and out.
    print >> sys.stderr, "Starting IoTFridge..."
    IOTF = IoTFridge(sys.argv[1], sys.stdin, sys.stdout)
    print >> sys.stderr, "Ready."

    # Runs unless we get a keyboard interrupr.
    try:
        IOTF.run()
    except KeyboardInterrupt:
        print >> sys.stderr, "Received interrupt, quitting..."
        
    print >> sys.stderr, "Done"

    # ################################### MAIN  ######################################

    # ################################################################################
