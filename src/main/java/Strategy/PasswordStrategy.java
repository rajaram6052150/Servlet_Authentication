package Strategy;

import Repository.UserRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import model.Users;
import util.AppLogger;
import java.util.logging.Logger;

public class PasswordStrategy implements AuthStrategy{

    UserRepository userdb = new UserRepository();
    Logger logger = AppLogger.getLogger(PasswordStrategy.class);

    public boolean authenticate(Users user){

        if (!userdb.isEmailExist(user.getEmail())){
            logger.warning("Account does not exist");
            return false;
        }

        String org_pwd = userdb.getPassword(user.getEmail());
        if (org_pwd.equals("") || org_pwd == null){
            logger.warning("DB Password is empty");
            return false;
        }
        BCrypt.Result result = BCrypt.verifyer()
                .verify(user.getPassword().toCharArray(), org_pwd);
        return result.verified;
    }
}
