package uk.ac.abertay.notsnapchat;

import android.os.Bundle;
import android.util.Patterns;

public class User {
    private int _id;
    private String _email;
    private String _username;

    public User() {

    }

    public User(int id) {
        set_id(id);
    }

    public User(String value) {
        if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            // is an email
            set_email(value);
        } else {
            set_username(value);
        }
    }

    public User(int id, String email, String username) {
        set_id(id);
        set_email(email);
        set_username(username);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            this._email = email;
        else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String username) {
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches())
            throw new IllegalArgumentException("Invalid username - cannot be an email address!");
        else {
            this._username = username;
        }
    }

    public static Bundle to_bundle(User user){
        Bundle output = new Bundle();
        output.putInt("id",user.get_id());
        output.putString("email",user.get_email());
        output.putString("username",user.get_username());
        return output;
    }

    public static User parse_bundle(Bundle bundle){
        User user = new User();
        user.set_id(bundle.getInt("id"));
        user.set_email(bundle.getString("email"));
        user.set_username(bundle.getString("username"));
        return user;
    }
}
