-- -------------------------------------------------------------------------------

    -- SQL Table Creation.

    -- There are some things that I would like to have used, but wasn't able to
    -- due to the fact that this was done in SQLite. I have commented these 
    -- properties out and made comments where appropriate.

    -- I did a lot of experimenting in MySQL before I moved to implementing in
    -- SQLite, so you can run this file in MySQL to create and populate the tables
    -- there. Just uncomment the dropping tables section and the populating section.

    -- You can also use some of the test queries in Test_SQL_Queries. The queries
    -- within the API itself are based on these.

-- -------------------------------------------------------------------------------

-- ------------------------- DROPPING THE TABLES IF THEY EXIST -------------------

-- ONLY UNCOMMENT FOR USE IN MYSQL

-- DROP TABLE ingredients, liquid, low_fat, pack,
--            price_location, recipe_info,
--            solid, stock;

-- DROP TABLE product_info, users;

-- ------------------------- CREATING THE TABLES ---------------------------------

-- Table for usernames and passwords. When
-- implemented in SQLite it didn't seem as if
-- the fixed length of character was enforced.

CREATE TABLE IF NOT EXISTS users (

    username VARCHAR(20) NOT NULL,
    -- Note, CHAR fixed length not enforced.
    salt CHAR(10) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email_address VARCHAR(100) NOT NULL,

    CONSTRAINT prim_key_users
        PRIMARY KEY (username)

);

-- Table to hold information for products.
-- The barcode is a foreign key to the stock
-- info. The one character is for the type,
-- L: liquid, S: solid or P: pack. It would have
-- been nice to enumerate the type, but this
-- was not enforced in SQLite3.

-- What I would also like to have done was to make
-- the barcode unsigned so it was always above zero,
-- but this didn't seem to be implemented in SQLite
-- either.

-- When I researched the unsigned problem a bit further,
-- according to one source SQLite does integer compacting,
-- so only uses the amount of memory necessary. If it is a
-- 4 byte number it only uses 4 bytes etc. Not sure how
-- reliable it was, but thought it interesting.

CREATE TABLE IF NOT EXISTS product_info (

    barcode INT(20) NOT NULL,
    -- barcode INT(20) UNSIGNED NOT NULL,
    prod_name VARCHAR(30) NOT NULL,
    prod_manuf VARCHAR(30) NOT NULL,
    prod_type CHAR(1) NOT NULL,
    -- prod_type ENUM('liquid', 'solid', 'pack') NOT NULL,

    CONSTRAINT prim_key_prod_info
        PRIMARY KEY (barcode)

);

-- Holds the volumes of all liquid objects. Same
-- comment as before for the volume and barcode. The
-- foreign key constraints are as for us to put
-- a volume of a liquid in the table, it must refer
-- to an actual product within the product info
-- table.

CREATE TABLE IF NOT EXISTS liquid (

    barcode INT(20) NOT NULL,
    volume INT(10) NOT NULL,
    -- barcode INT(20) UNSIGNED NOT NULL,
    -- volume INT(10) UNSIGNED NOT NULL,

    CONSTRAINT prim_key_liquid
        PRIMARY KEY (barcode),

    CONSTRAINT for_key_prod_info_liquid
        FOREIGN KEY (barcode)
        REFERENCES product_info (barcode)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT

);

-- Holds the weights of all solid objects. Works
-- identically to the above.

CREATE TABLE IF NOT EXISTS solid (

    barcode INT(20) NOT NULL,
    weight INT(10) NOT NULL,
    -- barcode INT(20) UNSIGNED NOT NULL,
    -- weight INT(10) UNSIGNED NOT NULL,

    CONSTRAINT prim_key_solid
        PRIMARY KEY (barcode),

    CONSTRAINT for_key_prod_info_solid
        FOREIGN KEY (barcode)
        REFERENCES product_info (barcode)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT

);

-- Holds the number of all units per pack. Works
-- identically to the above.

CREATE TABLE IF NOT EXISTS pack (

    barcode INT(20) NOT NULL,
    num_units INT(10) NOT NULL,
    -- barcode INT(20) UNSIGNED NOT NULL,
    -- num_units INT(10) UNSIGNED NOT NULL,

    CONSTRAINT prim_key_pack
        PRIMARY KEY (barcode),

    CONSTRAINT for_key_prod_info_pack
        FOREIGN KEY (barcode)
        REFERENCES product_info (barcode)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT

);

-- Table to hold what we have in stock and when
-- it expires and was bought. There is no point 
-- putting the constraint expiry > purchase as we
-- can purchase discounted expired goods.

-- Although originally I did have a foreign key
-- to product info, this meant that I couldn't add
-- stock without knowing all of its information,
-- which I thought was too limiting for my application.

CREATE TABLE IF NOT EXISTS stock (

    stock_id INT(11) NOT NULL,
    barcode INT(20) NOT NULL,
    -- stock_id INT(11) UNSIGNED NOT NULL,
    -- barcode INT(20) UNSIGNED NOT NULL,
    expiry DATE NOT NULL,
    purchase DATE NOT NULL,
    username VARCHAR(20) NOT NULL,

    CONSTRAINT prim_key_stock
        PRIMARY KEY (stock_id),

    CONSTRAINT for_key_users_stock
            FOREIGN KEY (username)
            REFERENCES users (username)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT

    -- CONSTRAINT for_key_prod_info_stock
    --     FOREIGN KEY (barcode)
    --     REFERENCES product_info (barcode)
    --     ON DELETE RESTRICT
    --     ON UPDATE RESTRICT

);

-- Tables to hold information regarding recipes. This
-- first one is to hold the recipe info. We could have
-- used memory better by having the recipe feeding an 
-- UNSIGNED TINYINT amount, but this wasn't implemented
-- in SQLite.

CREATE TABLE IF NOT EXISTS recipe_info (

    web_address VARCHAR(200) NOT NULL,
    recipe_name VARCHAR(50) NOT NULL,
    recipe_feeds TINYINT NOT NULL,
    -- recipe_feeds TINYINT UNSIGNED NOT NULL,

    CONSTRAINT prim_key_recipe_info
        PRIMARY KEY (web_address)

);

-- This is to hold the ingredients for each recipe. The
-- foreign key is so we can only add in ingredients
-- for recipes that are contained in the recipe table.

CREATE TABLE IF NOT EXISTS ingredients (

    web_address VARCHAR(200) NOT NULL,
    ingredient VARCHAR(30) NOT NULL,

    CONSTRAINT prim_key_ingredients
        PRIMARY KEY (web_address, ingredient),

    CONSTRAINT for_key_recipe_info
        FOREIGN KEY (web_address)
        REFERENCES recipe_info (web_address)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT

);

-- Table to hold the price and location information.
-- The foreign key is so we only reference products
-- in our product info table.

CREATE TABLE IF NOT EXISTS price_location (

    barcode INT(20) NOT NULL,
    -- barcode INT(20) UNSIGNED NOT NULL,
    price DECIMAL(3,2) NOT NULL,
    location VARCHAR(100) NOT NULL,

    CONSTRAINT prim_key_price_loc
        PRIMARY KEY (barcode, location),

    CONSTRAINT for_key_prod_info_price_loc
        FOREIGN KEY (barcode)
        REFERENCES product_info (barcode)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT

);

-- Table to hold all of the information for the 
-- low fat options. This acts as an association
-- table between different products in product_info,
-- so we can look up one against the other.

CREATE TABLE IF NOT EXISTS low_fat (

    original_barcode INT(20) NOT NULL,
    low_fat_barcode INT(20) NOT NULL,
    -- original_barcode INT(20) UNSIGNED NOT NULL,
    -- low_fat_barcode INT(20) UNSIGNED NOT NULL,

    CONSTRAINT prim_key_low_fat
        PRIMARY KEY (original_barcode, low_fat_barcode),

    CONSTRAINT for_key_prod_info_low_fat_origin
        FOREIGN KEY (original_barcode)
        REFERENCES product_info (barcode)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,

    CONSTRAINT for_key_prod_info_low_fat_lowfat
        FOREIGN KEY (low_fat_barcode)
        REFERENCES product_info (barcode)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT

);

-- ------------------------- POPULATING THE TABLES ---------------------------------

-- -- ONLY UNCOMMENT FOR USE IN MYSQL!

-- -- User table.

-- INSERT INTO users VALUES ('JamesCollerton', 'aaaaaaaaaa', '1', '1');
-- INSERT INTO users VALUES ('StevenSpielberg', 'bbbbbbbbbb', '2', '2');

-- -- Product info.

-- INSERT INTO product_info VALUES ('10101010', 'milk', 'Cravendale', 'L');
-- INSERT INTO product_info VALUES ('20202020', 'eggs', 'Sainsbury', 'P');
-- INSERT INTO product_info VALUES ('30303030', 'butter', 'ASDA', 'S');
-- INSERT INTO product_info VALUES ('01010101', 'low fat milk', 'Cravendale', 'L');
-- INSERT INTO product_info VALUES ('02020202', 'low fat eggs', 'Sainsbury', 'P');
-- INSERT INTO product_info VALUES ('03030303', 'low fat butter', 'ASDA', 'S');

-- -- -- Liquid.

-- INSERT INTO liquid VALUES ('10101010', '1000');
-- INSERT INTO liquid VALUES ('01010101', '1000');

-- -- Pack.

-- INSERT INTO pack VALUES ('20202020', '6');
-- INSERT INTO pack VALUES ('02020202', '6');

-- -- Solid.

-- INSERT INTO solid VALUES ('30303030', '400');
-- INSERT INTO solid VALUES ('03030303', '400');

-- -- Stock

-- INSERT INTO stock VALUES ('11', '10101010', '2015-04-01', '2015-04-01', 'JamesCollerton');
-- INSERT INTO stock VALUES ('22', '20202020', '2015-04-21', '2015-04-15', 'JamesCollerton');
-- INSERT INTO stock VALUES ('33', '01010101', '2015-04-01', '2015-04-01', 'StevenSpielberg');
-- INSERT INTO stock VALUES ('44', '02020202', '2015-04-21', '2015-04-15', 'StevenSpielberg');

-- -- Low Fat.

-- INSERT INTO low_fat VALUES ('10101010', '01010101');
-- INSERT INTO low_fat VALUES ('20202020', '02020202');
-- INSERT INTO low_fat VALUES ('30303030', '03030303');

-- -- Recipe Info

-- INSERT INTO recipe_info VALUES ('http://www.bbcgoodfood.com/recipes/quick-kimchi', 
--                                 'Quick Kimchi', '4');
-- INSERT INTO recipe_info VALUES ('http://www.bbcgoodfood.com/recipes/amaranth-porridge-green-tea-ginger-compote',
--                                 'Amaranth Porridge with Green Tea Ginger Compote', '2');

-- -- Ingredients

-- INSERT INTO ingredients VALUES ('http://www.bbcgoodfood.com/recipes/quick-kimchi',
--                                 'milk');
-- INSERT INTO ingredients VALUES ('http://www.bbcgoodfood.com/recipes/quick-kimchi',
--                                 'eggs');
-- INSERT INTO ingredients VALUES ('http://www.bbcgoodfood.com/recipes/amaranth-porridge-green-tea-ginger-compote',
--                                 'eggs');
-- INSERT INTO ingredients VALUES ('http://www.bbcgoodfood.com/recipes/amaranth-porridge-green-tea-ginger-compote',
--                                 'butter');

-- -- Price and Location.

-- INSERT INTO price_location VALUES ('10101010', '0.90', 'Clifton, Sainsbury');
-- INSERT INTO price_location VALUES ('10101010', '0.80', 'Cribbs Causeway, ASDA');
-- INSERT INTO price_location VALUES ('20202020', '1.70', 'Bedminster, ASDA');
-- INSERT INTO price_location VALUES ('20202020', '1.80', 'Clifton Mini Mart, Cotham Hill');