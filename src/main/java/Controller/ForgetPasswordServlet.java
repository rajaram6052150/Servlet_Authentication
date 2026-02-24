package Controller;

import Service.ForgetPwdSerice;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.AppLogger;
import util.PasswordProp;
import util.ResWriter;
import java.util.logging.Logger;

@WebServlet("/forget-password")
public class ForgetPasswordServlet extends HttpServlet {
    Logger logger = AppLogger.getLogger(ForgetPasswordServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        ForgetPwdSerice fpwd = new ForgetPwdSerice();
        String email = request.getParameter("email");
        String res = "";

        if (!PasswordProp.isPasswordValid(email)){
            ResWriter.write(response , "Email is invalid");
            return;
        }

        if (fpwd.fpwdHandler(email)){
            logger.info("Mail sent successfully");
            res += "Mail sent successfully";
        }
        else{
            res += "mail not found in db";
        }
        ResWriter.write(response , res);
    }
}


