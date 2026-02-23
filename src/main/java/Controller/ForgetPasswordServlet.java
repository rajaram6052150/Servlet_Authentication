package Controller;

import Service.ForgetPwdSerice;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.AppLogger;
import util.ResWriter;
import java.util.logging.Logger;

@WebServlet("/forget-password")
public class ForgetPasswordServlet extends HttpServlet {
    Logger logger = AppLogger.getLogger(ForgetPasswordServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ForgetPwdSerice fpwd = new ForgetPwdSerice();
        String email = request.getParameter("email");
        String res = "";
        if (fpwd.fpwdHandler(email)){
            logger.info("Mail sent successfully");
            res += "fpwd sucess";
        }
        else{
            res += "fpwd failed";
        }
        ResWriter.write(response , res);
    }
}
