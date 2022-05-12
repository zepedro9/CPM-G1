/**
 * To run this script execute:
 * node populate.js
 * 
 * This is will populate the database with the information necessary to run the 
 * project.   
 */
const { Product } = require("../models/product");
const mongoose = require('mongoose'); 
const dataProducts = require("./products.json");


// Set up the mongoDB connection.  
let mongoDB = 'mongodb://root:root@mongo:27017/shop?authSource=admin';
mongoose.connect(mongoDB)
.then(() => console.log("Connected to MongoDB"))
.catch(err => console.error("Could not connect to MongoDB...", err));


async function createProducts() { 
  for (let productJSON of dataProducts){
    console.log(productJSON)
  
    const product = new Product({
      id: productJSON.id,
      name: productJSON.name,
      brand: productJSON.brand,
      price: productJSON.price,
      description: productJSON.description,
      image_url: productJSON.image_url
    });

    const result = await product.save();
  } 
  process.exit(0);
}

setTimeout(() => createProducts(), 4000);

