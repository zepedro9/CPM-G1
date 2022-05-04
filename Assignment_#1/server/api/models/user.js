const mongoose = require('mongoose');
const autoIncrement = require('mongoose-auto-increment');

const UserSchema = mongoose.Schema({
    uuid: Number,
    pk: {
        exponent: [Number],
        modulus: [Number]
    },
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
})


UserSchema.plugin(autoIncrement.plugin, { model: 'User', field: 'uui', startAt: 1 })
const User = mongoose.model('User', UserSchema);

exports.User = User; 