-- This is used to populate all of the tables in the newly created schema.

-- Adds a test entry into fridge contents.
INSERT INTO FridgeContents
	(UPCBarcode, ItemName, FridgeUserID)
VALUES
	('1', 'Eggs', 1);

INSERT INTO FridgeUsers
	(Username, Password)
VALUES
	('Test Username', 'Test Password');