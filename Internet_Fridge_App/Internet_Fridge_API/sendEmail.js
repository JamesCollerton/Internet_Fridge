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
// to screen, otherwise logs the email is sent.
this.nodeMailerSendEmail = function (){

    nodemailerMailgun.sendMail({

        from: 'jc1175@my.bristol.ac.uk',
        to: 'jc1175@my.bristol.ac.uk',
        subject: 'Email test',
        html: 'Email html',

    }, function (err, info) {

        if (err) { console.log('Error: ' + err); }
        else { console.log('Email sent.'); }

      });

}