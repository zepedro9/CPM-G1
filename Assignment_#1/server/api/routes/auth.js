//const { User } = require("../models/user");
const express = require('express'); 
const router = express.Router(); 

router.post('/signup', (req, res) => {
    console.log(req.body); 
    res.send("User registered!");
});

module.exports = router; 