

const isValidExpirationCard = (req) => {
    try {
        let today = new Date();
        let expCard = req.body.card.expirationDate;
        let cardYear = parseInt(expCard.split("-")[1]);
        let cardMonth = parseInt(expCard.split("-")[0]);
        if (today.getFullYear() > cardYear) return false;
        else if (today.getFullYear() == cardYear && today.getMonth()+1 > cardMonth) return false;
    } catch (err) {
        return false;
    }
    return true
}

module.exports = {
    isValidExpirationCard
}