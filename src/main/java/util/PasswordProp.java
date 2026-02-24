package util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordProp {
    public static String hashPassword(String password) {

        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    public static boolean isPasswordValid(String password){
        return !(password == null || password.strip().isEmpty());
    }
}

