package Repository;

import model.ForgetPassword;
import model.Users;
import util.AppLogger;
import util.DBConfig;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

public class ForgetPasswordRepository {

    public Logger logger = AppLogger.getLogger(ForgetPassword.class);
    UserRepository userdb = new UserRepository();

    public boolean validateResetToken(Users user , ForgetPassword fdb){

        String sql = "select * from forget_password where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();
            try{
                if (rs.next()) {
                    logger.info("db token " + rs.getString("token"));
                    logger.info("user token "  + fdb.getResetToken());
                    Timestamp expiry = rs.getTimestamp("expiry");
                    if (expiry.after(new Timestamp(System.currentTimeMillis()))) {
                        logger.info("validation successfull");
                        return userdb.updatePassword(user);
                    } else {
                        logger.info("validation failed");
                    }
                } else {
                    logger.info("no email found");
                }
            }
            catch (SQLException e){
                logger.info(" sql validation failed");
            }
        }
        catch(Exception e){
            logger.severe("Error getting reset token " + e);
        }
        return false;
    }

    public boolean deleteFPwd(ForgetPassword fdb){
        String sql = "delete from forget_password where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, fdb.getEmail());
            return ps.executeUpdate() > 0;
        }
        catch(Exception e){
            logger.severe("Error deleting fpassword " + e);
        }
        return false;
    }

    public String fpwdInsert(String email){
        String sql = "insert into forget_password values (?,?,?)";
        String token = UUID.randomUUID().toString();
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + 15 * 60 * 1000);

        try(Connection con = DBConfig.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, email);
            ps.setString(2, token);
            ps.setTimestamp(3, expiry);
            logger.info("rows inserted" + ps.executeUpdate());
            logger.info("inserted");
            return token;
        }
        catch(Exception e){
            logger.severe("Error inserting foreget password " + e);
        }
        return "";
    }
}
