const { User } = require("../models/user");
const express = require('express'); 
const router = express.Router(); 

router.post('/signup', async (req, res) => {
    //console.log(req.body); 
    //let user = User.create(req.body);
    let user = new User(req.body);
    const result = await user.save(function(err, doc) {
        if (err) return console.error(err);
        console.log("Document inserted succussfully!");
      });
      
    console.log(result);
    console.log(user);
    res.send("User registered!"); 
});

module.exports = router; 