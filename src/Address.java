public class Address {
    private int addressID;
    private int userID;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    public Address() {
    }

    public Address(int addressID, String street, String city, String state, String postalCode) {
        this.addressID = addressID;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getAddress() {
        return street + ", " + city + ", " + state + ", " + postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setAddress(String street, String city, String state, String postalCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }


    public String getStreet() {
        return street;
    }

    public String getCity(){
        return city;
    }

    public String getState(){
        return state;
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int ID){
        userID = ID;
    }
}