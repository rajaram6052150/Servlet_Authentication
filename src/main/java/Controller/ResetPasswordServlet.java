package Controller;

import Service.ForgetPwdSerice;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ForgetPassword;
import util.ResWriter;
import Exception.RequestFormatException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res){
        String[] email = req.getParameterValues("email");
        String[] resetToken = req.getParameterValues("token");
        String[] newpwd = req.getParameterValues("newPassword");

        if (email == null || newpwd == null || resetToken == null || email.length > 1 || resetToken.length > 1 || newpwd.length > 1) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResWriter.write(res , "Invalid arguments");
            return;
        }

        ForgetPassword fdb = new ForgetPassword(email[0], resetToken[0]);
        ForgetPwdSerice fps = new ForgetPwdSerice();
        try{
            String ans = "";
            if (fps.resetPwdHandler(fdb , newpwd[0])){
                ans += "Password changed successfully";
            }
            else ans += "Password not changed";
            ResWriter.write(res , ans);
        }
        catch (RequestFormatException e){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResWriter.write(res, e.getMessage());
        }
        catch (Exception e){
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ResWriter.write(res, e.getMessage());
        }
    }
}




