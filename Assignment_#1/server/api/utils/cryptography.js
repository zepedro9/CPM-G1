const crypto = require('crypto');

const checkSignature = (content, signature, publicKey) => {
    try{
        const verifier = crypto.createVerify('RSA-SHA256')
        verifier.update(content)
        result = verifier.verify(publicKey, signature, 'hex')
    } catch(err){
        result = false
    }
    return result
}

const encrypt = (content, publicKey) => {
    try{
        const keyObj = crypto.createPublicKey(publicKey);
        var encrypted = crypto.publicEncrypt(
            {key: keyObj, padding: crypto.constants.RSA_PKCS1_PADDING}, 
            Buffer.from(content));
        return {status: 0, result: encrypted}
    } catch(err){
        return {status: -1}
    }
}

module.exports = {
    checkSignature,
    encrypt
}
