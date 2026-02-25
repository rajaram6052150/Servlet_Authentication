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
        String email = req.getParameter("email");
        String resetToken = req.getParameter("token");
        String newpwd = req.getParameter("newPassword");

        ForgetPassword fdb = new ForgetPassword(email, resetToken);
        ForgetPwdSerice fps = new ForgetPwdSerice();
        try{
            String ans = "";
            if (fps.resetPwdHandler(fdb , newpwd)){
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




