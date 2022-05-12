

const isValidExpirationCard = (expCard) => {
    try {
        let today = new Date();
        let cardYear = parseInt(expCard.split("-")[1]);
        let cardMonth = parseInt(expCard.split("-")[0]);
        if (today.getFullYear() > cardYear) return false;
        else if (today.getFullYear() == cardYear && today.getMonth()+1 > cardMonth) return false;
    } catch (err) {
        console.log(err)
        return false;
    }
    return true
}

module.exports = {
    isValidExpirationCard
}