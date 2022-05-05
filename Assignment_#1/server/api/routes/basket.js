const { User } = require("../models/user");
const express = require('express');
const bcrypt = require('bcrypt');
const crypto = require('crypto');
const router = express.Router();

router.post('/checkout', async (req, res) => {
  
    // Validate request body params
    if(!req.body.data || !req.body.signature){
        return res.status(400).send({message: "Please provide the signed basket and uuid"})
    }   

    // Check siganature
    try {
        const verifier = crypto.createVerify('RSA-SHA256')

        let user = await User.findOne({ _id: req.body.data.uuid});
        verifier.update(JSON.stringify(req.body.data))

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

module.exports = router; 