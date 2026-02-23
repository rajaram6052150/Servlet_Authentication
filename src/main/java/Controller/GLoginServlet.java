package Controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.AppLogger;
import util.Gconfig;
import java.util.logging.Logger;

@WebServlet("/google-login")
public class GLoginServlet extends HttpServlet {
    public Logger logger = AppLogger.getLogger(GLoginServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        String url = "https:accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + Gconfig.client_id
                + "&redirect_uri=" + Gconfig.redirect
                + "&response_type=code"
                + "&scope=openid%20email%20profile";
        try{
            res.sendRedirect(url);
        }
        catch (Exception e){
            logger.severe("Google Login error");
        }
    }
}

