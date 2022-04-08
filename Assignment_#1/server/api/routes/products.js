const { Product } = require("../models/product");
const express = require('express'); 
const mongoose = require('mongoose'); 
const router = express.Router(); 

router.get("/", async(req, res) => {
  const products = await Product.find();
  res.send(products); 
});

router.get("/:id", async(req, res) => {
    const products = await Product.find({id: req.params.id});
    res.send(products);
});
module.exports = router; 