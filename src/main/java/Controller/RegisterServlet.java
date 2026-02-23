package Controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Service.RegisterService;
import model.Users;
import util.AppLogger;
import util.JwtUtil;
import java.util.logging.Logger;
import util.PasswordHashing;
import util.ResWriter;

@WebServlet ("/register")
public class RegisterServlet extends HttpServlet {
    public Logger logger = AppLogger.getLogger(RegisterServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse res) {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (password.isEmpty()) {
            ResWriter.write(res, "Password is Empty");
            return;
        }
        String pwd_hash = PasswordHashing.hashPassword(password);

        RegisterService registerService = new RegisterService();
        Users user = new Users(email, pwd_hash);
        if (registerService.register(user)){
            String token = JwtUtil.generateToken(user.getEmail());
            res.setContentType("application/json");
            ResWriter.write(res , token);
            logger.info("Register local successful");
            ResWriter.write(res , "Register local successful");
        }
        else{
            ResWriter.write(res , "Account already exists");
            logger.info("Register local failed");
        }
    }
}






