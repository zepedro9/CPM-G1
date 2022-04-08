const mongoose = require('mongoose'); 
const {Schema} = mongoose; 

// Set up the mongoDB connection.  
let mongoDB = 'mongodb://root:root@localhost:27017';
mongoose.connect(mongoDB); 

let db = mongoose.connection; 

//Bind connection to error event (to get notification of connection errors)
db.on('error', console.error.bind(console, 'MongoDB connection error:')); 


// Creating the schema 
const productSchema = Schema({
    _id: Schema.Types.ObjectId,
    name: String,
    brand: String, 
    price: Number,
    description: String
}); 


// "Compile" model from schema.
const Product = mongoose.model('Product', productSchema); 


// Create an instance of model SomeModel
var awesome_instance = new Product({ 
    _id: mongoose.Types.ObjectId(),
    name: 'awesome', 
    brand: 'awesome2', 
    price: 1, 
    description: 'just awesome'
});


// Save the new model instance, passing a callback
awesome_instance.save(function (err) {
  if (err) console.log(`error ${err}`);
  // saved!
});

// process.exit(1);
