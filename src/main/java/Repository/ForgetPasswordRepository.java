package Repository;

import model.ForgetPassword;
import util.AppLogger;
import util.DBConfig;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;
import Exception.RequestFormatException;
import Exception.ServerException;

public class ForgetPasswordRepository {

    public Logger logger = AppLogger.getLogger(ForgetPassword.class);

    public String getResetToken(ForgetPassword fdb){

        String sql = "select token , expiry from forget_password where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, fdb.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Timestamp expiry = rs.getTimestamp("expiry");
                if (expiry.after(new Timestamp(System.currentTimeMillis()))) {
                    logger.info("Not expired");
                    return rs.getString("token");
                } else throw new RequestFormatException("Token Validation Expired");
            }
            else throw new RequestFormatException("Email Not Found");
        }
        catch(SQLException e){
            logger.severe("Database Connection failed " + e);
            throw new ServerException("Database connection failed" + e);
        }
    }

    public boolean deleteFPwd(ForgetPassword fdb) {
        String sql = "delete from forget_password where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, fdb.getEmail());
            return ps.executeUpdate() > 0;
        }
        catch(SQLException e){
            logger.severe("Error inserting foreget password " + e);
            throw new ServerException("Database Connection failed" + e);
        }
    }

    public String fpwdInsert(String email) {
        String sql = "insert into forget_password values (?,?,?)";
        String token = UUID.randomUUID().toString();
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + 15 * 60 * 1000);

        try(Connection con = DBConfig.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, email);
            ps.setString(2, token);
            ps.setTimestamp(3, expiry);
            logger.info("rows inserted " + ps.executeUpdate());
            return token;
        }
        catch(SQLException e){
            logger.severe("Database Connection failed " + e);
            throw new ServerException("Database connection failed");
        }
    }
}
