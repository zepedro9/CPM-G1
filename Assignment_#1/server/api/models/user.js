const mongoose = require("mongoose");
const uuid = require('node-uuid');

const UserSchema = mongoose.Schema(
  {
    _id: { type: String, default: ()=>{return uuid.v1()}},
    pk: {
      exponent: [Number],
      modulus: [Number],
    },
    name: String,
    address: String,
    NIF: Number,
    email: String,
    password: String, // Encrypted
    card: {
      cardType: String,
      number: String,
      expirationDate: String,
    },
  },
  { id: false }
);

const User = mongoose.model("User", UserSchema);

exports.User = User;
