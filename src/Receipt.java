public class Receipt {

    private int orderID;
    private int userID;

    private String content;

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getOrderID() {
        return orderID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }


}
