package model;

import enums.AuthProvider;

public class Users {

    private AuthProvider authProvider;
    private String email;
    private String password = "";

    public Users(String email) {
        this.email = email;
    }

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

