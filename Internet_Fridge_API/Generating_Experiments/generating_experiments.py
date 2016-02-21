# ------------------------------------------------------------------------------
# 
#  Generating the tests for the database system.
# 
#  The below generate three .json files in the current directory that can be 
#  used to see how well the database system deals with having to add and remove
#  values.
# 
# ------------------------------------------------------------------------------

def speed_test(j, filename):

    experiment_file = open(filename, 'w')

    experiment_file.write(' \n \
                            {"request": "create_account",                               \n \
                             "data": { "username" : "JamesCollerton",                   \n \
                                       "password" : "password123",                      \n \
                                       "email address" : "jc1175@my.bris.ac.uk" } }     \n \
                                                                                        \n \
                            {"request": "login",                                        \n \
                             "data": { "username" : "JamesCollerton",                   \n \
                                       "password" : "password123" } } ')
    for i in range(0, j):
        experiment_file.write(' \n \n \
                                {"request": "insert",                                       \n \
                                 "table" : "product",                                       \n \
                                 "id" :' + str(i) + ',                                      \n \
                                 "data": { "expiry date" : "2014-04-01",                    \n \
                                           "barcode" : "01010101",                          \n \
                                           "name" : "chinese cabbage",                      \n \
                                           "manufacturer" : "chinese cabbage company",      \n \
                                           "type" : "S",                                    \n \
                                           "weight" : "400" } } ' )

    for i in range(0, j):
        experiment_file.write(' \n \n \
                                                                                            \n \
                                {"request": "delete",                                       \n \
                                 "table" : "product",                                       \n \
                                 "id":' + str(i) + ',                                       \n \
                                 "barcode" : "01010101"} ')

    experiment_file.write(' \n \n \
                            {"request" : "list", "table" : "product"}                               \n \
                                                                                                    \n \
                            {"request": "delete", "table" : "users", "username" : "JamesCollerton"} \n \
                                                                                                    \n \
                            ')

    experiment_file.close()


# Creates the three tests for 1000, 100,000 and 1,000,000 products.
speed_test(1000, 'speed_test_1000.json')
speed_test(100000, 'speed_test_100000.json')
speed_test(1000000, 'speed_test_1000000.json')

# EOF