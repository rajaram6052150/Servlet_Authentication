package Controller;

import Service.StrategyService;
import Strategy.PasswordStrategy;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;
import util.AppLogger;
import util.JwtUtil;
import util.ResWriter;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    public Logger logger = AppLogger.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (password.equals("")){
            ResWriter.write(response , "Password is empty");
            return;
        }
        StrategyService ss = new StrategyService(new PasswordStrategy());
        Users user = new Users(email , password);

        if (ss.authenticate(user)) {
            String jwtToken = JwtUtil.generateToken(user.getEmail());
            response.setContentType("application/json");
            ResWriter.write(response , jwtToken);
            ResWriter.write(response , "\nSucess");
        }
        else ResWriter.write(response , "Fail");
    }
}
