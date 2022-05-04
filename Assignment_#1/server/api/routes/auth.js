const { User } = require("../models/user");
const express = require('express'); 
const router = express.Router(); 

router.post('/signup', (req, res) => {
    console.log(req.body); 

    let user = new User(req.body);
    console.log(user)
    res.send("User registered!");
});

module.exports = router; 