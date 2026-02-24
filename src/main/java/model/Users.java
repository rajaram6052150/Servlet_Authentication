package model;

import enums.AuthProvider;
import util.PasswordProp;

public class Users {

    private AuthProvider authProvider;
    private String email;
    private String hashedPassword = "";

    public Users(String email) {
        this.email = email;
    }

    public Users(String email, String password) {
        this.email = email;
        if (!(password == null || password.strip().length() == 0)) {
            this.hashedPassword = password;
        }
    }

    public void hashPassword(String password) {
        this.hashedPassword = PasswordProp.hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public boolean isPasswordEmpty() {
        return (this.hashedPassword == null || this.hashedPassword.isEmpty());
    }

    public String getPassword() {
        return hashedPassword;
    }
}
