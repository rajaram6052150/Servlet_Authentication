package model;

import enums.AuthProvider;
import util.RequestValidation;
import Exception.RequestFormatException;

public class Users {

    private AuthProvider authProvider;
    private String email;
    private String hashedPassword = "";

    public Users(String email) {
        this.email = email;
    }

    public Users(String email, String password) {
        this.email = email;
        if (!RequestValidation.isPasswordValid(password)) {
            throw new RequestFormatException("Passsword is empty");
        }
        this.hashedPassword = password;
    }

    public void hashPassword(String password) {
        this.hashedPassword = RequestValidation.hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return hashedPassword;
    }
}
