// -----------------------------------------------------------------------------
// REQUIREMENTS

var sendEmail = require('./sendEmail.js');
var fs = require('fs');

// -----------------------------------------------------------------------------
// FUNCTIONS

// This function is used to register the user. It takes in their username, 
// password and email address and then inserts them into the MySQL DB.

// TODO: Add some checking of an email address and an error message.
this.registerUser = function(username, password, emailAddress, mySQLConnection){

	MySQLUpdateUserTable(username, password, emailAddress, mySQLConnection);

	var registerUserEmailBody = fs.readFileSync('email_bodies/registerUserEmailBody.txt', 'utf8').toString();
	registerUserEmailBody = registerUserEmailBody.replace('<USERNAME>', username);
	registerUserEmailBody = registerUserEmailBody.replace('<PASSWORD>', password);
	registerUserEmailBody = registerUserEmailBody.split('\n').join('<br/>');

	sendEmail.initialiseNodeMailer();
	sendEmail.nodeMailerSendEmail(emailAddress, "My Fridge User Registered!", registerUserEmailBody);

}

// This function is used to update the user table. It takes in the username,
// password, email address. It then hashes the password and adds salt, adding
// all of them to the MySQL table.

// TODO: Add the hashing and the salt.
function MySQLUpdateUserTable(username, password, emailAddress, mySQLConnection){

    var queryString = 'INSERT INTO FridgeUsers SET ?';
    var fridgeUserInformation = {
    	Username: username,
    	Password: password,
    	EmailAddress: emailAddress,
    	Salt: 'Salt'
    }
     
    mySQLConnection.query(queryString, fridgeUserInformation, function(err, result) {

        if (err) throw err;

    });

}