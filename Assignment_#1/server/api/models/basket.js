const mongoose = require('mongoose');
const uuid = require('node-uuid');

const Basket = mongoose.model('Basket', mongoose.Schema({
    userUuid: String, 
    token: { type: String, default: ()=>{return uuid.v1()}},
    products: [{
        id: Number,
        quantity: Number 
    }],
    total: String, 
    date: String,
    hour: String
}));

exports.Basket = Basket; 