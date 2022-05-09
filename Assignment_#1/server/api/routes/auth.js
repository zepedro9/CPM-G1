const { User } = require("../models/user");
const express = require('express');
const bcrypt = require('bcrypt');
const router = express.Router();

router.post('/signup', async (req, res) => {

    try { 
        // Checking if the user is already registered. 
        let isRegisteredUser = await User.findOne({ email: req.body.email}); 
        console.log(isRegisteredUser); 
        if(isRegisteredUser) return res.status(409).send({"message": "The user is already registered"});
        
        // Saving and encrypting password. 
        let hashedPassword = await encryptHash(req.body.password);
        let user = new User(
            {
                ...req.body,
                password: hashedPassword
            });
        
        await user.save(function (err, doc) {
            if (err) return res.status(400).send({message: err})
            console.log("User registered with success!");
        });
        
        return res.status(200).send({
            message: "Registered with success!", 
        });   
    } catch(err){
        return res.status(400).send({message: err})
    }
});

router.post('/signin', async (req, res) => {
    // TODO: verify all required data exists
    try {
        // Checking if registered user.
        let user = await User.findOne({ email: req.body.email}); 
        if(!user) return res.status(400).send({message: "Uknown user"})

        if (!(await bcrypt.compare(req.body.password, user.password)))
            return res.status(401).send({message: "Wrong credentials."});
        else {
            return res.status(200).send({
                message: "Logged with success!", 
                uuid: user._id
            });     
        }
    } catch (err) {
        console.log(err);
        return res.status(400).send({message: "Something went wrong. Check the credentials."});
    }
});

const encryptHash = async (password) => {
    return await bcrypt.hash(password, 10);
}


module.exports = router; 