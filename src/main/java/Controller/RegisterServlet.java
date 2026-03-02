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
import util.RequestValidation;
import util.ResWriter;
import Exception.RequestFormatException;

@WebServlet ("/register")
public class RegisterServlet extends HttpServlet {
    public Logger logger = AppLogger.getLogger(RegisterServlet.class);
    RegisterService registerService = new RegisterService();

    protected void doPost(HttpServletRequest req, HttpServletResponse res) {

        String[] email = req.getParameterValues("email");
        String[] password = req.getParameterValues("password");
        if (email == null || password == null || email.length > 1 || password.length > 1){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResWriter.write(res , "Invalid arguments");
            return;
        }

        try{
            String pwd_hash = RequestValidation.hashPassword(password[0]);
            Users user = new Users(email[0], pwd_hash);
            if (registerService.register(user)){
                String token = JwtUtil.generateToken(user.getEmail());
                res.setContentType("application/json");
                ResWriter.write(res , token);
                logger.info("Register local successful");
                ResWriter.write(res , "\nRegister local successful");
            }
        }
        catch(RequestFormatException e){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResWriter.write(res, e.getMessage());
        }
        catch(Exception e){
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ResWriter.write(res, e.getMessage());
        }
    }
}



