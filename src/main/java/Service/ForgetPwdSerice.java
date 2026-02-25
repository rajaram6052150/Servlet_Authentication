package Service;

import Repository.ForgetPasswordRepository;
import Repository.UserRepository;
import model.ForgetPassword;
import model.Users;
import util.AppLogger;
import util.RequestValidation;
import java.util.logging.Logger;
import Exception.RequestFormatException;

public class ForgetPwdSerice {
    Logger logger = AppLogger.getLogger(ForgetPwdSerice.class);
    UserRepository userdb = new UserRepository();
    ForgetPasswordRepository forgetpwddb = new ForgetPasswordRepository();
    MailService mailService = new MailService();
    String subject = "Password Reset link";
    String body = "Clink the link below to reset password";

    public boolean fpwdHandler(String email) throws Exception {

        if (!RequestValidation.isEmailvalid(email)) throw new RequestFormatException("Invalid email address");

        if (userdb.isEmailExist(email)){
            String token = forgetpwddb.fpwdInsert(email);
            if (RequestValidation.isPasswordValid(token)){
                body += "\n" + "http://localhost:8080/reset-password?email=" + email + "&token=" + token;
                logger.info("Entry created in forget password db");
                mailService.sentMail(email , subject , body);
                return true;
            }
            else logger.info("Entry not created in forget password db");
        }
        throw new RequestFormatException("Account not exist");
    }

    public boolean resetPwdHandler(ForgetPassword fdb , String newpwd) throws Exception {

        if (!RequestValidation.isPasswordValid(fdb.getResetToken())){
            throw new RequestFormatException("Access Restricted");
        }

        if (!RequestValidation.isPasswordValid(newpwd) || !RequestValidation.isEmailvalid(fdb.getEmail())) {
            throw new RequestFormatException("Email | Password NOt valid");
        }

        String pwd_hash = RequestValidation.hashPassword(newpwd);
        Users user = new Users(fdb.getEmail(), pwd_hash);

        String act_token = forgetpwddb.getResetToken(fdb);
        if (!act_token.equals(fdb.getResetToken())) throw new RequestFormatException("Token not matched");
        return userdb.updatePassword(user) && forgetpwddb.deleteFPwd(fdb);

    }
}
