package util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHashing {
    public static String hashPassword(String password) {

        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
