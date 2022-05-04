const express = require('express');
const app = express();
const product = require("./routes/products"); 
const path = require('path');
const auth = require("./routes/auth"); 
const mongoose = require('mongoose'); 

let mongoDB = 'mongodb://root:root@mongo:27017/shop?authSource=admin';
mongoose.connect(mongoDB)
.then(() => console.log("Connected to MongoDB"))
.catch(err => console.error("Could not connect to MongoDB...", err));

app.use(express.json()); 
app.use('/api', express.static(path.join(__dirname, 'public')))
app.use('/api/products', product);  // The products API
app.use('/api/auth', auth);         // The user authentication API

const port = process.env.PORT || 3000
app.listen(port, () => console.log(`Listening on port ${port}...`));