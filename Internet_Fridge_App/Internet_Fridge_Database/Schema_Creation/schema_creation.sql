-- This is used to create the schema used within the database.

-- Creating the table of fridge contents.
DROP TABLE IF EXISTS FridgeContents;

CREATE TABLE FridgeContents
( 
	UPCBarcode VARCHAR(12) NOT NULL PRIMARY KEY
  , ItemName VARCHAR(255) NOT NULL
  , FridgeUserID INT NOT NULL
);

-- Creating the table of users.
DROP TABLE IF EXISTS FridgeUsers;

CREATE TABLE FridgeUsers
( 
	FridgeUserID INT NOT NULL AUTO_INCREMENT PRIMARY KEY
  ,	Username VARCHAR(255) NOT NULL 
  , Password VARCHAR(255) NOT NULL
  , EmailAddress VARCHAR(255) NOT NULL
  , Salt VARCHAR(255) NOT NULL
);