// -----------------------------------------------------------------------------
// REQUIREMENTS

var nodemailer = require("nodemailer");
var mg = require('nodemailer-mailgun-transport');
var validator = require('validator');
var fs = require('fs');

// -----------------------------------------------------------------------------
// GLOBAL VARIABLES

// These are used for connecting to the node mailgun client.
var nodeMailerConnectionDetails;
var nodemailerMailgun;

// -----------------------------------------------------------------------------
// AUTHORISATION FOR THE MAILGUN CLIENT

// This sets up the nodemailer functionality with the API key and the domain
// taken from the untracked file. It then creates a transporter using that file.
this.initialiseNodeMailer = function (){ 

    nodeMailerConnectionDetails = JSON.parse(fs.readFileSync('ignore/nodeMailerConnectionDetails.json', 'utf8'));

    var auth = {
      auth: {
        api_key: nodeMailerConnectionDetails['api_key'],
        domain: nodeMailerConnectionDetails['domain']
      }
    }

    nodemailerMailgun = nodemailer.createTransport(mg(auth));

}

// -----------------------------------------------------------------------------
// EMAILING FUNCTIONS

// This is actually used to send the email. It sets the from, to, subject and
// body (html) parameters and then sends it. If there is an error prints it
// to screen, otherwise logs the email is sent. The callback is used, if there
// is an error then it returns false (error with the function). Otherwise returns
// true (function executed successfully).
this.nodeMailerSendEmail = function (emailRecipient, emailSubject, emailHTMLContent, callback){

    nodemailerMailgun.sendMail({

        from: 'jc1175@my.bristol.ac.uk',
        to: emailRecipient,
        subject: emailSubject,
        html: emailHTMLContent,

    }, function (err, info) {

        if (err) { callback(false) }

    });

    callback(true);

}