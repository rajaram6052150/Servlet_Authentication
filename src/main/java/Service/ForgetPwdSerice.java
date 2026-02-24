package Service;

import Repository.ForgetPasswordRepository;
import Repository.UserRepository;
import model.ForgetPassword;
import model.Users;
import util.AppLogger;
import util.PasswordProp;
import java.util.logging.Logger;

public class ForgetPwdSerice {
    Logger logger = AppLogger.getLogger(ForgetPwdSerice.class);
    UserRepository userdb = new UserRepository();
    ForgetPasswordRepository forgetpwddb = new ForgetPasswordRepository();
    MailService mailService = new MailService();
    String subject = "Password Reset link";
    String body = "Clink the link below to reset password";

    public boolean fpwdHandler(String email) {
        if (userdb.isEmailExist(email)){
            String token = forgetpwddb.fpwdInsert(email);
            if (!(token == null || token.trim().isEmpty())){
                body += "\n" + "http://localhost:8080/reset-password?email=" + email + "&token=" + token;
                logger.info("Entry created in forget password db");
                mailService.sentMail(email , subject , body);
                return true;
            }
            else logger.info("Entry not created in forget password db");
        }
        logger.info("Email not exist in parent db");
        return false;
    }

    public boolean resetPwdHandler(ForgetPassword fdb , String newpwd) {
        if (!(newpwd == null || newpwd.trim().isEmpty())){
            String pwd_hash = PasswordProp.hashPassword(newpwd);
            Users user = new Users(fdb.getEmail(), pwd_hash);
            if (forgetpwddb.validateResetToken(user , fdb)){
                return forgetpwddb.deleteFPwd(fdb);
            }
        }
        else{
            logger.info("new pwd empty");
        }
        return false;
    }
}


