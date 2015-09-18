#!/bin/bash
# Example test script for the template that you are given

# This initialises the database:
# sqlite3 fridge.db < fridge.sql

# This is used to demonstrate being able to create an account, login, and
# use the admin tools to put data into tables directly. It also demonstrates
# being able to delete data from the tables when you are using the correct
# login.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_users_success.json

# This is used to demonstrate the error checking with the login system. It
# contains being able to check if the primary key has already been added, 
# trying to use tables that aren't there, inserting users without the 
# correct amount of information surrounding them, and also trying to delete or list
# users without the correct amount of information.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_users_fail.json

# This is used to demonstrate being able to add, list and delete recipes from 
# the recipe table as needed.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_recipes_success.json

# This is used to demo the error checking done when adding recipes to the
# recipe list. Contained in this is adding replica recipes, recipes without
# the correct information, attempting to delete recipes that aren't there
# and delete recipes without the correct information.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_recipes_fail.json

# This shows how it is possible to add items to, list and delete from
# the products table.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_products_success.json

# This is used to demonstrate how errors with adding products are dealt
# with. Stock IDs 6 - 10 are special cases. I didn't want to overly limit
# what was put in the fridge, so I designed it so even if you didn't
# have access to name, manufacturer etc. you could still just add the stock
# info. It deals with adding in duplicate primary keys, deleting things that
# aren't there and giving incomplete information.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_products_fail.json

# This is used to demonstrate being able to add low fat items onto the
# low fat association table. Adds, lists and deletes items from the table.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_low_fat_success.json

# Here is the error handling for adding things to the low fat list.
# It deals with duplicate entries, deleting things that aren't there and
# not giving the correct amount of information. Also included are foreign key checks.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_low_fat_fail.json

# This is to demonstrate being able to put values into the price and location
# table. It shows successfully, adding, listing and deleting the elements.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_price_location_success.json

# These are the errors the price and location entry deals with. Includes
# adding duplicates, deleting things that aren't there and not giving complete
# information. Also includes foreign keys errors.

# python iotfridge.py fridge.db < ./Test_Files/add_list_delete_price_location_fail.json

# Demonstrates the very basic functionality of being able to create an account,
# login and then delete the account.

# python iotfridge.py fridge.db < ./Test_Files/user_accounts_success.json

# These are the errors as caught by the program. Trying to create an account
# without specifying the correct information, trying to log in to accounts without
# the right details etc. As well as entering the wrong password.

# python iotfridge.py fridge.db < ./Test_Files/user_accounts_fail.json

# Gives a typical user's interactions with the my stock command.
# Two accounts are created, and one is logged into, items are added and
# then the core 'my stock' functionality is shown. Being able to list the stock
# with its attributes. From there it is shown you can delete from my stock
# and then all items are removed from the whole database.

# Next it shows that stock is actually assigned to specific people. In doing so
# it also demonstrates the overarching idea of the different databases. When
# you add stock to your fridge, it is saved as your stock and you can view it
# as such. However the general information stored on it is permanently put into
# the product info database. The idea is that the product info database will be
# built up over time using everything that everyone has ever put in the fridge.
# That is why you have some statements printed about already having items in the
# fridge, even when you have deleted them from your stock.

# Finally the two user accounts are deleted.

# python iotfridge.py fridge.db < ./Test_Files/my_stock_success.json

# Used to show how the low fat command works. You request the name and it will
# return a list of low fat alternatives. The example works by creating an
# account, adding items and low fat versions and then printing the results to
# screen. When there are no options available it prints nothing.

# python iotfridge.py fridge.db < ./Test_Files/low_fat_success.json

# Used to show how the recipe system works. It creates an account, adds some
# ingredients and then looks for recipes with what's in the fridge. It will
# pick out recipes with any of the ingredients that you have in the fridge and
# then tell you what you need to buy to complete the recipe.

# The first test is looking to see it doesn't find anything when it shouldn't.
# The second test is looking to make sure it identifies recipes when it has all
# the ingredients, and the third test is making sure it works when you only
# have half.

# python iotfridge.py fridge.db < ./Test_Files/recipe_success.json

# This tests the usage plotting function. At the moment this is hardcoded
# to only work with the given example. This is due to the fact that the purchase
# dates are set to be the same as the current time, so if you don't harcode
# those then the graph doesn't really show a lot.

# It creates a profile, adds a few milk items bought and expiring at different
# times and then plots the usage of them over the maximum period they have been
# bought in.

# Note, because we are adding the same items at different times, that item will
# already have its information contained within the product_info database, so
# there will be some messages warning of that.

# python iotfridge.py fridge.db < ./Test_Files/plot_usage_success.json

# This will check all of the dates for your items and print out the ones going
# out of date today, as well as the ones going out of date in the next three
# days. It creates an account, adds all the items and then runs the check.
# The results should be quite self-explanatory. 

# PLEASE NOTE: IF YOU DON'T CHANGE THE EMAIL ADDRESS IN THE ACCOUNT CREATION THE EMAIL
# WILL BE SENT TO ME. If the email doesn't seem to have worked either wait a bit,
# check your junk, or try a different email address. My hotmail address stopped
# recieving them after a bit because it thought they were junk. My gmail account
# has been fine. The mailgun account I use for some other things as well, so try
# not to send too many!

# python iotfridge.py fridge.db < ./Test_Files/run_check_success.json

# This shows how you can use the fridge to find the closest places to you to
# buy things from. It gives a list of options and the prices at each place.
# You search with a generic name term (eg 'milk') and it returns all items 
# of that name, regardless of their size.

# python iotfridge.py fridge.db < ./Test_Files/price_location_success.json

# Used to demo how the fridge can rip information from Outpan, it uses the API
# to collect the attributes of different items according to their barcode and
# put them in stock. It is worth mentioning that the Outpan data has to
# be in the specific form or it rejects it.

# python iotfridge.py fridge.db < ./Test_Files/data_from_internet_success.json

# Demos the database health function, which tells you how big your database has
# got and the performance effects.

# python iotfridge.py fridge.db < ./Test_Files/database_health_success.json

# Lets you post a fun, fridge related facebook status to impress your friends.
# I made the app on facebook and only I have access to it, because it is currently
# in developer mode. Because of this, I will need to generate an API key for you
# to use. They expire after 24 hours, but if you email me when you want to test it
# I can send you one.

# If you run this then it will post a status to my facebook
# about the delicious Perfect Pancakes I'm going to make. 100% test it out so you
# can see that it works, just warn me before you do!

# python iotfridge.py fridge.db < ./Test_Files/facebook_update_success.json