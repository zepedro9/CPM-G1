const { Product } = require("../models/product");
const express = require('express'); 
const router = express.Router(); 

router.get("/", async(req, res) => {
  const products = await Product.find();
  return res.send(products); 
});

router.get("/:id", async(req, res) => {
    const products = await Product.find({id: req.params.id});
    return res.send(products);
});

module.exports = router; 