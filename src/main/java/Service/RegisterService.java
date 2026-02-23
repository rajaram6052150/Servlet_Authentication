package Service;

import Repository.UserRepository;
import enums.AuthProvider;
import model.Users;
import util.AppLogger;
import java.util.logging.Logger;

public class RegisterService {
    public Logger logger;
    UserRepository userdb;

    public RegisterService() {
        this.userdb = new UserRepository();
        logger = AppLogger.getLogger(RegisterService.class);
    }

    public boolean register(Users user) {

        if (userdb.isEmailExist(user.getEmail())){
            AuthProvider p = userdb.getProvider(user.getEmail());
            if (p == AuthProvider.GOOGLE){
                return userdb.updatePassword(user) && userdb.changeProvider(user.getEmail());
            }
            else logger.info("Account already exists");
        }
        else return userdb.insert(user , AuthProvider.LOCAL);
        return false;
    }
}




