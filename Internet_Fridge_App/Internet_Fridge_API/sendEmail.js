// -----------------------------------------------------------------------------
// REQUIREMENTS

var nodemailer = require("nodemailer");
var mg = require('nodemailer-mailgun-transport');
var validator = require('validator');
var fs = require('fs');

// -----------------------------------------------------------------------------
// GLOBAL VARIABLES

var nodeMailerConnectionDetails;
var nodemailerMailgun;

// -----------------------------------------------------------------------------
// AUTHORISATION FOR THE MAILGUN CLIENT

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