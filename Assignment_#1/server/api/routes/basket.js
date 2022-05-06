const { User } = require("../models/user");
const { Basket } = require("../models/basket");

const express = require('express');
const bcrypt = require('bcrypt');
const crypto = require('crypto');
const router = express.Router();

router.post('/checkout', async (req, res) => {
    if (!req.body.basket || !req.body.signature) {
        return res.status(400).send({ message: "Please provide the signed basket and uuid" });
    }
    console.log(req.body);
    const uuid = req.body.basket.userUUID;
    try {
        await checkSignature(uuid, req);
        await addToDatabase(req);

        return res.status(200).send({ "message": "Products purchased with success" });
    } catch (err) {
        console.log(err)
        return res.status(400).send({ "message": "Something went wrong" })
    }
});

/**
 * Get requests may have a body, but it shouldn't have any meaning.
 * Thus using post seems to be the more correct option. 
 */
router.post('/history', async (req, res) => {
    console.log(req.body)
    if (!req.body.userUUID || !req.body.signature) {
        return res.status(400).send({ message: "Please provide the signed basket and uuid" });
    }

    try {
        let history = await Basket.find({ userUUID: req.params.userUUID });
        console.log(history)
        return res.status(200).send(history);
    } catch (err) {
        console.log(err);
        return res.status(400).send({ message: "Couldn't proceed with the request" });
    }
});

const checkSignature = async (uuid, req) => {
    const verifier = crypto.createVerify('RSA-SHA256')

    let user = await User.findOne({ _id: uuid });
    let jsonBasket = JSON.stringify(req.body.basket);
    verifier.update(jsonBasket);

    return verifier.verify(user.pk, req.body.signature, 'hex')
}

const addToDatabase = async (req) => {
    // Get hour 
    const now = new Date();
    const current = now.getHours() + ':' + now.getMinutes();

    var today = new Date().toISOString().slice(0, 10);
    let basket = new Basket({ ...req.body.basket, date: today, hour: current });
    // TODO : verify price  

    await basket.save(function (err, doc) {
        if (err) return res.status(400).send({ message: err })
        console.log("Basket saved with successs!");
    });
}

module.exports = router; 