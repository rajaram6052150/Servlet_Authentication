package Controller;

import Service.StrategyService;
import Strategy.GoogleStrategy;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;
import util.AppLogger;
import util.Gconfig;
import util.ResWriter;

@WebServlet("/google-callback")
public class GtokenServlet extends HttpServlet {
    public Logger logger = AppLogger.getLogger(GtokenServlet.class);
    public static HttpClient client = HttpClient.newHttpClient();
    public StrategyService ss = new StrategyService();

    protected void doGet(HttpServletRequest req, HttpServletResponse res){
        String code = req.getParameter("code");

        if (code == null){
            ResWriter.write(res , "Google Auth code is empty");
            return;
        }

        try{
            String acode = "code=" + code + "&client_id=" + Gconfig.client_id + "&client_secret=" + System.getenv("google_client_secret") + "&redirect_uri=" + Gconfig.redirect +"&grant_type=authorization_code";

            HttpRequest tokenReq = HttpRequest.newBuilder().uri(new URI("https://oauth2.googleapis.com/token")).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(acode)).build();

            HttpResponse<String> tokenRes = client.send(tokenReq , HttpResponse.BodyHandlers.ofString());
            String googleRes = tokenRes.body();

            logger.info("Token res" + googleRes);

            String[] temp = googleRes.split("\"access_token\"")[1].split("\"");

            String accessToken = temp[1];

            HttpRequest reqUserInfo = HttpRequest.newBuilder().uri(new URI("https://www.googleapis.com/oauth2/v2/userinfo")).header("Authorization" , "Bearer " + accessToken).GET().build();

            HttpResponse<String> userinfo = client.send(reqUserInfo , HttpResponse.BodyHandlers.ofString());

            String infoRes = userinfo.body();
            logger.info("email res" + infoRes);
            String email = infoRes.split("\"email\"")[1].split("\"")[1];

            Users user = new Users(email);
            ss.setStrategy(new GoogleStrategy());

            String c = "";
            if (ss.authenticate(user)) c += "Google login success";
            else c += "Google login failed";
            ResWriter.write(res , c);
            logger.info(email);

        }
        catch (Exception e){
            logger.severe("Google redirect issue from callack" + e.getMessage() );
        }
    }
}
