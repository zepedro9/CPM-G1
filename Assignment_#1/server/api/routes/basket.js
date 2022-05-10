const { User } = require("../models/user");
const { Basket } = require("../models/basket");
const { Product } = require("../models/product");
const crypto_utils = require('../utils/cryptography')
const express = require('express');
const router = express.Router();


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
        let user = await User.findOne({ _id: uuid});
        if(!user) return res.status(400).send({"message": "Unknown user"})

        let jsonBasket = JSON.stringify(req.body.basket)

        const signResult = crypto_utils.checkSignature(jsonBasket, req.body.signature, user.pk)
        if(signResult){   
            let addResponse = await addToDatabase(req); 
    
            if (addResponse.status == -1) 
                return res.status(400).send({"message": "Checkout failed"});

            // TODO: verify credit card, save basket in the server with new identifier(new uuid)

            const basket_uuid = addResponse.basket.token; 
            const encryptResult = crypto_utils.encrypt(basket_uuid, user.pk)
            if(encryptResult.status == -1)
                return res.status(401).send({"message": "No authorization"});
            return res.status(200).send({"message": encryptResult.result.toString('base64')});
        } else {
            return res.status(401).send({"message": "No authorization"});
        }
    } catch(err){
        console.log(err);
        return res.status(400).send({"message": "Something went wrong"});
    }
});


const addToDatabase = async (req) => {
    // Get hour 
    const now = new Date();

    const current = now.getHours().toString().padStart(2, 0) + ':' + now.getMinutes().toString().padStart(2, '0');

    var today = new Date().toISOString().slice(0, 10);
    let basket = new Basket({ ...req.body.basket, date: today, hour: current });
    // TODO : verify price  

    await basket.save(function (err, doc) {
        if (err) {
            console.log("Not possible to save the basket"); 
            return {status: -1} 
        }
        console.log("Basket saved with successs!");
        return {status: 0 , basket: doc};
    });


    return {status: 0, basket: basket}; 
}

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
        // Get user
        let user = await User.findOne({ _id: req.body.userUUID});
        if(!user) return res.status(400).send({"message": "Unknown user"})
        
        // Check signature
        const signResult = crypto_utils.checkSignature(req.body.userUUID, req.body.signature, user.pk)
        if(!signResult) return res.status(401).send({"message": "No authorization"})
        
        // Get history
        let history = await Basket.find({ userUUID: req.params.userUUID });
        console.log(history)
        return res.status(200).send({message:"Authorized", history:history});
    } catch (err) {
        console.log(err);
        return res.status(400).send({ message: "Couldn't proceed with the request" });
    }
});



router.post('/receipt', async (req, res) => {
    console.log(req.body)
    if (!req.body.basketUUID) {
        return res.status(400).send({ message: "Please provide the basket UUID" });
    }

    // Missing a way to check its the actual user..

    try {        
        // Get basket
        let basket = await Basket.find({ token: req.body.basketUUID });
        console.log(basket)
        return res.status(200).send({message:"Authorized", basket});
    } catch (err) {
        console.log(err);
        return res.status(400).send({ message: "Couldn't proceed with the request" });
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
