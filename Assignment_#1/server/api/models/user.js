const mongoose = require('mongoose'); 

const User = mongoose.model('Product', mongoose.Schema({
        uuid: Decimal128,               // 128-bit uuid
        publicKey: String,
        name: String,
        address: String, 
        NIF: Number,
        email: String,
        password: String,           // encrypted
        card: {
            type: String, 
            number: String,
            expirationDate: String,
        }
    }));

exports.User = User; 