-- This is used to create the schema used within the database.

DROP TABLE IF EXISTS FridgeContents;

CREATE TABLE FridgeContents
( 
	UPCBarcode VARCHAR(12) NOT NULL PRIMARY KEY
  , ItemName VARCHAR(255) NOT NULL
);
