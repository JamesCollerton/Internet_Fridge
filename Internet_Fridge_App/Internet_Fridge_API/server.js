var express = require('express');        
var bodyParser = require('body-parser');

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
router.route('/bears')

	// General post to the API (accessed at POST http://localhost:8080/api/bears).
	.post(function(req, res) {

	    res.json({ message: 'Posted with information: ' + req.body.name})
	    
	})

	// General get from the API (accessed at GET http://localhost:8080/api/bears)
	.get(function(req, res) {
	    
		res.json({ message: 'Get request (for all data) posted.'})

	});

router.route('/bears/:bear_id')

    // Get request with paramters (accessed at GET http://localhost:8080/api/bears/:bear_id)
    .get(function(req, res) {
        
        res.json({ message: 'Get request (for id:' + req.params.bear_id + ') posted.'})

    })

	// Put request with paramters (accessed at PUT http://localhost:8080/api/bears/:bear_id)	
    .put(function(req, res) {

        res.json({ message: 'Put request (for id:' + req.params.bear_id + ') posted.'})

    })

    // Delete request with paramters (accessed at DELETE http://localhost:8080/api/bears/:bear_id)
    .delete(function(req, res) {
        
    	res.json({ message: 'Delete request (for id:' + req.params.bear_id + ') posted.'})

    });

// This is used to register routes. All of our routes will be prefixed with '/api'
app.use('/api', router);

// -----------------------------------------------------------------------------
// Starts the server

app.listen(port);
console.log('Server started on port: ' + port);