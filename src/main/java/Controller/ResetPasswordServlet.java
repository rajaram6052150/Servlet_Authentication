package Controller;

import Service.ForgetPwdSerice;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ForgetPassword;
import util.PasswordProp;
import util.ResWriter;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res){
        String email = req.getParameter("email");
        String resetToken = req.getParameter("token");
        String newpwd = req.getParameter("newPassword");

        if (!PasswordProp.isPasswordValid(newpwd)){
            ResWriter.write(res , "Password empty");
            return;
        }

        ForgetPassword fdb = new ForgetPassword(email, resetToken);
        ForgetPwdSerice fps = new ForgetPwdSerice();
        String ans = "";
        if (fps.resetPwdHandler(fdb , newpwd)){
            ans += "Password changed successfully";
        }
        else ans += "Password not changed";
        ResWriter.write(res , ans);
    }
}



