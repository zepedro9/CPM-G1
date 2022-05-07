const { User } = require("../models/user");
const { Product } = require("../models/product");
const { ObjectId } = require('mongodb');
const express = require('express');
const bcrypt = require('bcrypt');
const crypto = require('crypto');
const router = express.Router();
const node_uuid = require('node-uuid');

router.post('/checkout', async (req, res) => {

    // Validate request body params
    if(!req.body.basket || !req.body.signature){
        return res.status(400).send({message: "Please provide the signed basket and uuid"})
    }   

    const uuid = req.body.basket.userUUID
    const products = req.body.basket.products

    if(products.length == 0)
        return res.status(401).send({"message": "Empty basket"})

    // Check signature
    try {
        const verifier = crypto.createVerify('RSA-SHA256')

        let user = await User.findOne({ _id: uuid});
        if(!user) return res.status(400).send({"message": "Uknown user"})

        let jsonBasket = JSON.stringify(req.body.basket)
        verifier.update(jsonBasket)

        const result = verifier.verify(user.pk, req.body.signature, 'hex')
        
        if(result){  
            // TODO: verify credit card, save basket in the server with new identifier(new uuid)
            const basket_uuid = node_uuid.v1()
            const keyObj = crypto.createPublicKey(user.pk)

            var encrypted = crypto.publicEncrypt({key: keyObj, padding: crypto.constants.RSA_PKCS1_PADDING}, Buffer.from(basket_uuid))
            return res.status(200).send({"message": encrypted.toString('base64')})
        } else {
            return res.status(401).send({"message": "No authorization"})
        }
        
    } catch(err){
        console.log(err)
        return res.status(400).send({"message": "Something went wrong"})
    }
});

router.get("/products", async(req, res) => {
    try {
        const ids = req.query.ids.split(",").map(item => parseInt(item, 10))
        const products = await Product.find().where('id').in(ids)
        return res.status(200).send({"message": "Successfully retrieved all your products", "products": products}); 
    } catch(err){
        console.log(err)
        return res.status(400).send({"message": "Error retrieving your products", "products": []}); 
    }
});

module.exports = router; 