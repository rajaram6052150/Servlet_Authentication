package Strategy;

import Repository.UserRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import model.Users;
import util.AppLogger;
import java.util.logging.Logger;
import Exception.RequestFormatException;
import util.RequestValidation;

public class PasswordStrategy implements AuthStrategy{

    UserRepository userdb = new UserRepository();
    Logger logger = AppLogger.getLogger(PasswordStrategy.class);

    public boolean authenticate(Users user) throws RequestFormatException {

        if (!RequestValidation.isRequestValid(user)) {
            logger.warning("Email | Password is INvalid");
            throw new RequestFormatException("Email | Password is INvalid");
        }

        if (!userdb.isEmailExist(user.getEmail())){
            logger.warning("Account does not exist");
            throw new RequestFormatException("Ex Account does not exist");
        }

        String org_pwd = userdb.getPassword(user.getEmail());

        BCrypt.Result result = BCrypt.verifyer()
                .verify(user.getPassword().toCharArray(), org_pwd);
        logger.info("Password verified " + result.verified);
        return result.verified;
    }
}
