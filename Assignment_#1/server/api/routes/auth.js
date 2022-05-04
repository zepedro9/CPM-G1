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

    const result = await user.save(function (err, doc) {
        if (err) return console.error(err);
        console.log("Document inserted succussfully!");
    });

    console.log(result);
    res.send(user.uuid);
});

router.post('/signin', async (req, res) => {
    try {
        let user = await User.findOne({ uuid: req.body.uuid });
        console.log(user.password);
        if (!(await bcrypt.compare(req.body.password, user.password)))
            res.status(401).send("Wrong credentials.");
        else 
            res.status(200).send("Logged with success!");
    } catch (err) {
        console.log(err);
        res.status(400).send("Something went wrong. Check the credentials.");
    }
});

const encryptHash = async (password) => {
    return await bcrypt.hash(password, 10);
}

module.exports = router; 