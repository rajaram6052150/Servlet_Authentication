package Controller;

import Service.ForgetPwdSerice;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.AppLogger;
import util.ResWriter;
import java.util.logging.Logger;
import Exception.RequestFormatException;

@WebServlet("/forget-password")
public class ForgetPasswordServlet extends HttpServlet {
    Logger logger = AppLogger.getLogger(ForgetPasswordServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        ForgetPwdSerice fpwd = new ForgetPwdSerice();
        String email = request.getParameter("email");
        String res = "";

        try{
            if (fpwd.fpwdHandler(email)){
                logger.info("Mail sent successfully");
                res += "Mail sent successfully";
            }
            else{
                res += "mail not found in db";
            }
            ResWriter.write(response , res);
        }
        catch(RequestFormatException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResWriter.write(response , e.getMessage());
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ResWriter.write(response , e.getMessage());
        }
    }
}


