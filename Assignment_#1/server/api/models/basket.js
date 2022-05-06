const mongoose = require('mongoose');

const Basket = mongoose.model('Basket', mongoose.Schema({
    userUuid: String, 
    products: [{
        id: Number,
        quantity: Number 
    }],
    total: String, 
    date: String,
    hour: String
}));

exports.Basket = Basket; 