// -----------------------------------------------------------------------------
// REQUIREMENTS

var sendEmail = require('./sendEmail.js');
var fs = require('fs');

// -----------------------------------------------------------------------------
// FUNCTIONS

// This function is used to register the user. It takes in their username, 
// password and email address and then inserts them into the MySQL DB.
this.registerUser = function(username, password, emailAddress){

	var registerUserEmailBody = fs.readFileSync('email_bodies/registerUserEmailBody.txt', 'utf8');
	registerUserEmailBody = registerUserEmailBody.replace('<USERNAME>', username);
	registerUserEmailBody = registerUserEmailBody.replace('<PASSWORD>', password);

	sendEmail.initialiseNodeMailer();
	sendEmail.nodeMailerSendEmail(emailAddress, "My Fridge User Registered!", registerUserEmailBody);

}