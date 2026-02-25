package Strategy;

import Repository.UserRepository;
import enums.AuthProvider;
import model.Users;
import Exception.ServerException;

public class GoogleStrategy implements AuthStrategy{
    UserRepository userdb;
    public GoogleStrategy() {
        userdb = new UserRepository();
    }

    public boolean authenticate(Users user) throws ServerException {

        if (userdb.isEmailExist(user.getEmail())){
            AuthProvider provider = userdb.getProvider(user.getEmail());
            if (provider == AuthProvider.LOCAL){
                return userdb.changeProvider(user.getEmail());
            }
            //JWT token add
        }

        return userdb.insert(user , AuthProvider.GOOGLE);
    }
}
