package id.kido1611.firebase.example.model;

/**
 * Created by abdusy on 09/04/17.
 */

public class Users {
    public Users(){

    }

    public Users(String user, String pass){
        this.username = user;
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;
    private String password;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
