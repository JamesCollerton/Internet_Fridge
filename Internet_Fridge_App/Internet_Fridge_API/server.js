var express = require('express');        
var bodyParser = require('body-parser');
var mySQL = require('mysql');

// -----------------------------------------------------------------------------

// Testing reading in the credentials from the .json

var fs = require('fs');
var obj = JSON.parse(fs.readFileSync('ignore/mySQLConnectionDetails.json', 'utf8'));

var connection = mySQL.createConnection({
  host     : obj["localhost"],
  user     : obj["username"],
  password : obj["password"],
  database : obj["database"]
});

// connection.query(queryString, function(err, rows, fields) {
//     if (err) throw err;
 
//     for (var i in rows) {
//         console.log('Post Titles: ', rows[i].post_title);
//     }
// });

connection.connect(function(err) {
  // connected! (unless `err` is set)
  // console.log(err)
  // process.exit(1);
});

// connection.connect();
 
var queryString = 'SELECT * FROM FridgeContents';
 
connection.query(queryString, function(err, rows, fields) {
    if (err) throw err;
    console.log(rows);
    // for (var i in rows) {
    //     console.log('Post Titles: ', rows[i].post_title);
    // }
});

// -----------------------------------------------------------------------------

// This defines the app using Express, then configures it to use bodyParser().
// bodyParser lets us get data from the POST request.
var app = express();                 

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// This sets the port on the local host to 8080 to use for the API.
var port = process.env.PORT || 8080;        

// -----------------------------------------------------------------------------
// API ROUTING

// This creates an instance of the express router in order to route the API 
// traffic to the correct place.
var router = express.Router();              

// Middleware to be used for all of the requests. Logs that a request that 
// something has been asked for, then looks for the next request.
router.use(function(req, res, next) {

    console.log('Request recieved.');
    next(); 

});

// This just routes all of the traffic with the url suffix '/bears'
router.route('/MyFridge')

	// General post to the API (accessed at POST http://localhost:8080/api/bears).
	.post(function(req, res) {

	    res.json({ message: 'Posted with information: ' + req.body.name})
	    
	})

	// General get from the API (accessed at GET http://localhost:8080/api/bears)
	.get(function(req, res) {
	    
		res.json({ message: 'Get request (for all data) posted.'})

	});

// This just routes all of the traffic with the url suffix 'bears/' and a bear_id
// parameter.
router.route('/MyFridge/:fridgeItemID')

    // Get request with paramters (accessed at GET http://localhost:8080/api/bears/:bear_id)
    .get(function(req, res) {
        
        res.json({ message: 'Get request (for id:' + req.params.fridgeItemID + ') posted.'})

    })

	// Put request with paramters (accessed at PUT http://localhost:8080/api/bears/:bear_id)	
    .put(function(req, res) {

        res.json({ message: 'Put request (for id:' + req.params.fridgeItemID + ') posted.'})

    })

    // Delete request with paramters (accessed at DELETE http://localhost:8080/api/bears/:bear_id)
    .delete(function(req, res) {
        
    	res.json({ message: 'Delete request (for id:' + req.params.fridgeItemID + ') posted.'})

    });

// This is used to register routes. All of our routes will be prefixed with '/api'
app.use('/api', router);

// -----------------------------------------------------------------------------
// START SERVER

app.listen(port);
console.log('Server started on port: ' + port);