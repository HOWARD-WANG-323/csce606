public class Address {
    private int addressID;

    private int userID;

    private String address;

    public int getUserID() {
        return userID;
    }
    public int getAddressID() {
        return addressID;
    }
    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
