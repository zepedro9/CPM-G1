const mongoose = require('mongoose'); 

// Set up the mongoDB connection.  
let mongoDB = 'mongodb://root:root@localhost:27017/shop?authSource=admin';
mongoose.connect(mongoDB)
.then(() => console.log("Connected to MongoDB"))
.catch(err => console.error("Could not connect to MongoDB...", err));


// Creating the schema 
const productSchema = mongoose.Schema({
    name: String,
    brand: String, 
    price: Number,
    description: String
}); 


const Product = mongoose.model('Product', productSchema);

async function createProduct() {
  const product = new Product({
    name: "Surface Keyboard",
    brand: "Microsoft",
    price: 100,
    description: "A keyboard that will not let your fingers down"
  });

  const result = await product.save();
  console.log(result); 
}

createProduct();