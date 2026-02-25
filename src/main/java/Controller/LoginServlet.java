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
import Exception.RequestFormatException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    public Logger logger = AppLogger.getLogger(LoginServlet.class);
    StrategyService strategy = new StrategyService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Users user = new Users(email , password);
            strategy.setStrategy(new PasswordStrategy());

            if (strategy.authenticate(user)){
                String jwtToken = JwtUtil.generateToken(user.getEmail());
                response.setContentType("application/json");
                ResWriter.write(response, jwtToken);
                ResWriter.write(response, "\nSucess");
            }
            else{
                ResWriter.write(response, "Password Wrong");
            }
        }
        catch (RequestFormatException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResWriter.write(response, e.getMessage());
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ResWriter.write(response, e.getMessage());
        }
    }
}

