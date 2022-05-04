const { User } = require("../models/user");
const express = require('express');
const bcrypt = require('bcrypt');
const router = express.Router();

router.post('/signup', async (req, res) => {
    let hashedPassword = await encryptHash(req.body.password);
    let user = new User(
        {
            ...req.body,
            password: hashedPassword
        });

    await user.save(function (err, doc) {
        if (err) return res.status(400).send(err)
        console.log("User registered with success!");
    });

    res.status(200).send({
        message: "Registered with success!", 
    });   
});

router.post('/signin', async (req, res) => {
    try {
        let user = await User.findOne({ email: req.body.email});
        console.log(user.password);
        if (!(await bcrypt.compare(req.body.password, user.password)))
            res.status(401).send({message: "Wrong credentials."});
        else {
            res.status(200).send({
                message: "Logged with success!", 
                uuid: user.uuid
            });     
        }
    } catch (err) {
        console.log(err);
        res.status(400).send({message: "Something went wrong. Check the credentials."});
    }
});

const encryptHash = async (password) => {
    return await bcrypt.hash(password, 10);
}

module.exports = router; 