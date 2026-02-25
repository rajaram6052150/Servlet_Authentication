package Service;

import Repository.UserRepository;
import enums.AuthProvider;
import model.Users;
import util.AppLogger;
import java.util.logging.Logger;
import Exception.RequestFormatException;
import util.RequestValidation;
import Exception.ServerException;

public class RegisterService {
    public Logger logger;
    UserRepository userdb;

    public RegisterService() {
        this.userdb = new UserRepository();
        logger = AppLogger.getLogger(RegisterService.class);
    }

    public boolean register(Users user) throws ServerException {

        if (!RequestValidation.isRequestValid(user)) {
            throw new RequestFormatException("Email | Password  Not Valid");
        }

        if (userdb.isEmailExist(user.getEmail())){
            AuthProvider p = userdb.getProvider(user.getEmail());
            if (p == AuthProvider.GOOGLE){
                return userdb.updatePassword(user) && userdb.changeProvider(user.getEmail());
            }
            else{
                logger.info("Account already exists");
                throw new RequestFormatException("Account already exist");
            }
        }
        else return userdb.insert(user , AuthProvider.LOCAL);
    }
}

