public class Card {

    private int cardID;
    private int userID;
    private Card card;
    private String cardNumber;
    private String expiryDate;
    private String cvv; // Do not store CVV in a real-world application
    private String cardHolderName;
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getCardID() {
        return cardID;
    }
    public void setCardID(int cardID) {
        this.cardID = cardID;
    }
    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }
    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    //card holder name
    public String getCardHolderName() {
        return cardHolderName;
    }
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public void setExpirationDate(String string) {
        this.expiryDate = string;
    }


    public int getCvv() {
        return Integer.parseInt(cvv);
    }

    public void setCvv(int i) {
        this.cvv = Integer.toString(i);
    }
}
