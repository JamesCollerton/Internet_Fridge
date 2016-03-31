// -----------------------------------------------------------------------------
// REQUIREMENTS

var fs = require('fs');
var crypto = require('crypto');
var sendEmail = require('./sendEmail.js');

// -----------------------------------------------------------------------------
// FUNCTIONS

// This function is used to register the user. It takes in their username, 
// password and email address and then inserts them into the MySQL DB.
this.registerUser = function(username, password, emailAddress, mySQLConnection){

    var validEntry = checkEntries(username, password, emailAddress);
    var responseJSON;

    if(validEntry){

    	MySQLUpdateUserTable(username, password, emailAddress, mySQLConnection, function(MySQLSuccess){

            if(MySQLSuccess){
                
                sendUserRegistrationEmail(username, password, emailAddress, function(sendEmailSuccess){

                    if(sendEmailSuccess){

                        responseJSON = { success: 'true', message: 'User successfully registered.'};

                    } else{ responseJSON = { success: 'false', message: 'Sending email to user failed.'}; }

                });

            } else{ responseJSON = { success: 'false', message: 'Entering user in database failed.'}; }

        });

    } else{ responseJSON = { success: 'false', message: 'User details invalid.'}; }

    return(responseJSON);

}

// This function is used to check the entries the person has made into the
// database. Checks the username and email address are less than 255 characters
// long and that the email address is valid.
function checkEntries(username, password, emailAddress){

    var validEntry = true;

    if(username.length > 255 || emailAddress.length > 255 || validateEmail(emailAddress) == false){ 
        validEntry = false; 
    }

    return(validEntry);
}

// This is a regex function for testing whether or not an email address is a 
// valid one or not.
function validateEmail(emailAddress) {

    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    return re.test(emailAddress);

}

// This function is used to update the user table. It takes in the username,
// password, email address. It then hashes the password and adds salt, adding
// all of them to the MySQL table. If there is an error then calls back with
// false (function did not succeed), else returns true.
function MySQLUpdateUserTable(username, password, emailAddress, mySQLConnection, callback){

    var queryString = 'INSERT INTO FridgeUsers SET ?';

    var salt = crypto.randomBytes(128).toString('base64');
    var hashedPassword = crypto.createHash("md5").update(password + salt).digest('hex');

    var fridgeUserInformation = {
    	Username: username,
    	Password: hashedPassword,
    	EmailAddress: emailAddress,
    	Salt: salt
    }
     
    mySQLConnection.query(queryString, fridgeUserInformation, function(err, result) {

        if (err) callback(false);

    });

    callback(true);
}

// This function is used to send an email to the user informing them that they
// have been registered to the MyFridge app. It takes the .txt file which is
// the body of the email, then puts in their username and password, replaces the
// line breaks with HTML line breaks and pings off an email. The callback is used
// so the function returns true if it executes successfully or false if not.
function sendUserRegistrationEmail(username, password, emailAddress, callback){

    var registerUserEmailBody = fs.readFileSync('email_bodies/registerUserEmailBody.txt', 'utf8').toString();
    registerUserEmailBody = registerUserEmailBody.replace('<USERNAME>', username);
    registerUserEmailBody = registerUserEmailBody.replace('<PASSWORD>', password);
    registerUserEmailBody = registerUserEmailBody.split('\n').join('<br/>');

    sendEmail.initialiseNodeMailer();
    sendEmail.nodeMailerSendEmail(emailAddress, "My Fridge User Registered!", registerUserEmailBody, function(success){

        if(!success){
            callback(false);
        }

    });

    callback(true);

}