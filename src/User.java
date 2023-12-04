public class User {
    private int userID;
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        //randomly generate a user id that never repeats
        this.userID = (int) (Math.random() * 1000000000);
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public User() {

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
