const { User } = require("../models/user");
const { Product } = require("../models/product");
const { ObjectId } = require('mongodb');
const express = require('express');
const bcrypt = require('bcrypt');
const crypto = require('crypto');
const router = express.Router();

router.post('/checkout', async (req, res) => {

    // Validate request body params
    if(!req.body.basket || !req.body.signature){
        return res.status(400).send({message: "Please provide the signed basket and uuid"})
    }   

    const uuid = req.body.basket.userUUID
    const products = req.body.basket.products

    // Check siganature
    try {
        const verifier = crypto.createVerify('RSA-SHA256')

        let user = await User.findOne({ _id: uuid});
        let jsonBasket = JSON.stringify(req.body.basket)
        console.log(jsonBasket)
        verifier.update(jsonBasket)

        const result = verifier.verify(user.pk, req.body.signature, 'hex')

        console.log(result)
        return res.status(200).send({"message": "Niceeeeee"})
    } catch(err){
        console.log(err)
        return res.status(400).send({"message": "Something went wrong"})
    }

    //crypto.verify()

    // try {
    //     let user = await User.findOne({ _id: uuid});
    //     if (!(await bcrypt.compare(req.body.password, user.password)))
    //         res.status(401).send({message: "Wrong credentials."});
    //     else {
    //         res.status(200).send({
    //             message: "Logged with success!", 
    //             uuid: user._id
    //         });     
    //     }
    // } catch (err) {
    //     console.log(err);
    //     res.status(400).send({message: "Something went wrong. Check the credentials."});
    // }
});

router.get("/products", async(req, res) => {
    try {
        const ids = req.query.ids.split(",").map(item => parseInt(item, 10))
        const products = await Product.find().where('id').in(ids)
        return res.status(200).send({"products": products}); 
    } catch(err){
        //console.log(err)
        return res.status(400).send({"products": []}); 
    }
});

module.exports = router; 