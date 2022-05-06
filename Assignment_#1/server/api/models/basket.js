const mongoose = require('mongoose');

const Basket = mongoose.model('Basket', mongoose.Schema({
    userUuid: String, 
    products: [{
        id: Number,
        quantity: Number 
    }],
    totalPrice: String, 
    date: String
}));

exports.Basket = Basket; 