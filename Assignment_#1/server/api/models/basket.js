const mongoose = require('mongoose');
const uuid = require('node-uuid');

const Basket = mongoose.model('Basket', mongoose.Schema({
    userUUID: String, 
    token: { type: String, default: ()=>{return uuid.v1()}},
    usedToken: {Boolean, default: false},
    products: [{
        id: Number,
        quantity: Number 
    }],
    total: String, 
    date: String,
    hour: String
}));

exports.Basket = Basket; 