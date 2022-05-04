const mongoose = require('mongoose');
const AutoIncrement = require('mongoose-sequence')(mongoose);

const UserSchema = mongoose.Schema({
    pk: {
        exponent: [Number],
        modulus: [Number]
    },
    name: String,
    address: String,
    NIF: Number,
    email: String,
    password: String,           // Encrypted
    card: {
        type: String,
        number: String,
        expirationDate: String,
    }
},
    { typeKey: '$type' }        // Type is a reserved word. 
);


UserSchema.plugin(AutoIncrement, { inc_field: 'uuid' });
const User = mongoose.model('User', UserSchema);

exports.User = User; 