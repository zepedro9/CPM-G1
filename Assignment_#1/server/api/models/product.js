const mongoose = require('mongoose'); 

const Product  = mongoose.model('Product', mongoose.Schema({
        id: Number,
        name: String,
        brand: String, 
        price: Number,
        description: String
    }));

exports.Product = Product; 