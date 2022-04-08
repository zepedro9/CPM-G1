/**
 * To run this script execute:
 * node populate.js
 * 
 * This is will populate the database with the information necessary to run the 
 * project.   
 */

const mongoose = require('mongoose'); 
const dataProducts = require("./data/products.json");

// Set up the mongoDB connection.  
let mongoDB = 'mongodb://root:root@localhost:27017/shop?authSource=admin';
mongoose.connect(mongoDB)
.then(() => console.log("Connected to MongoDB"))
.catch(err => console.error("Could not connect to MongoDB...", err));


// Creating the schema 
const productSchema = mongoose.Schema({
    id: Number,
    name: String,
    brand: String, 
    price: Number,
    description: String
}); 


const Product = mongoose.model('Product', productSchema);

async function createProducts() {
  for (let productJSON of dataProducts){
    console.log(productJSON)
  
    const product = new Product({
      id: productJSON.id,
      name: productJSON.name,
      brand: productJSON.brand,
      price: productJSON.price,
      description: productJSON.description
    });

    const result = await product.save();
  }
}

createProducts();

