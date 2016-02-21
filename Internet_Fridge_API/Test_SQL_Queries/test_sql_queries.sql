-- --------------------------------------------------------------------------------------

-- Test SQL Queries

-- These queries were used to to try out some of the ideas in the finished
-- script. I thought it might be helpful to include them as they might be
-- easier to read than the ones embedded in the python code.

-- --------------------------------------------------------------------------------------

-- Query to find the stock relating to a username.

SELECT * FROM product_info INNER JOIN stock
    ON stock.barcode = product_info.barcode
    WHERE username = "StevenSpielberg";

-- Query to find the low fat option of a named food.

SELECT * FROM product_info INNER JOIN ( 
    SELECT * FROM low_fat INNER JOIN (
        SELECT * FROM product_info
            WHERE prod_name = "eggs"
    ) AS milk_info
    ON milk_info.barcode = low_fat.original_barcode
) AS low_fat
ON low_fat.low_fat_barcode = product_info.barcode;

-- Query to find the name of all recipes with ingredients
-- found in the person's fridge.

SELECT * FROM ingredients INNER JOIN (
    SELECT * FROM product_info INNER JOIN (
        SELECT barcode AS stock_barcode FROM stock WHERE username = 'JamesCollerton'
        ) AS fridge
        ON fridge.stock_barcode = product_info.barcode
    ) AS prod_info
    ON prod_info.prod_name = ingredients.ingredient;

-- Query to find all of the product info for JamesCollerton

SELECT * FROM product_info INNER JOIN (
    SELECT barcode AS stock_barcode FROM stock WHERE username = 'JamesCollerton'
    ) AS fridge
    ON fridge.stock_barcode = product_info.barcode;

-- Query to find all of the items with the name milk from the stock
-- belonging to James Collerton.

SELECT * FROM product_info INNER JOIN (
    SELECT expiry, purchase, barcode FROM stock
        WHERE username = "JamesCollerton"
) AS stock_user_info
ON product_info.barcode = stock_user_info.barcode
WHERE prod_name = "milk";

-- Query to find all of the expired products belonging to JamesCollerton.

SELECT * FROM product_info INNER JOIN (
    SELECT expiry, barcode FROM stock
        WHERE username = "JamesCollerton" AND expiry < DATE(NOW())
    ) AS expired_info
    ON expired_info.barcode = product_info.barcode; 