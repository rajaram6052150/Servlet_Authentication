package util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import Exception.RequestFormatException;
import model.Users;

public class RequestValidation {
    public static  Logger logger = Logger.getLogger(RequestValidation.class.getName());
    private static Pattern Email_pattern = Pattern.compile("^[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]+$");

    public static String hashPassword(String password) {
        if (!isPasswordValid(password))  throw new RequestFormatException("Password Cant be empty");
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean isPasswordValid(String password){
        return !(password == null || password.strip().isEmpty());
    }

    public static boolean isEmailvalid(String email){
        return Email_pattern.matcher(email).matches();
    }

    public static boolean isRequestValid(Users user){
        return isPasswordValid(user.getPassword()) && isEmailvalid(user.getEmail());
    }
}

