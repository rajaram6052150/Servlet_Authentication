package Repository;

import enums.AuthProvider;
import model.ForgetPassword;
import model.Users;
import util.AppLogger;
import util.DBConfig;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

public class UserRepository{

    public Logger logger = AppLogger.getLogger(UserRepository.class);

    public boolean insert(Users user , AuthProvider p){

        String sql = "insert into users (email , hashed_password , auth_provider) values(?,?,?)";

        try (Connection con = DBConfig.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1 , user.getEmail());
            ps.setString(2 , user.getPassword());
            ps.setString(3 , p.name());
            ps.executeUpdate();
        }
        catch(Exception e){
            logger.severe("Error inserting " + e);
            return false;
        }
        return true;
    }

    public String getPassword(String email){

        String sql = "select * from users where email = ?";

        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1 , email);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("hashed_password");
            }
        }
        catch(Exception e){
            logger.severe("Error getting password " + e);
        }
        return null;
    }

    public AuthProvider getProvider(String email){

        String sql = "select * from users where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return AuthProvider.valueOf(rs.getString("Auth_provider"));
            }
        }
        catch (Exception e){
            logger.severe("Error getting provider " + e);
        }
        return null;
    }

    public boolean isEmailExist(String email){

        String sql = "select * from users where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
        catch (Exception e){
            logger.severe("Error getting email " + e);
        }
        return false;
    }

    public boolean changeProvider(String email){
        String sql = "update users set auth_provider = ? where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, AuthProvider.BOTH.name());
            ps.setString(2 , email);
            return ps.executeUpdate() > 0;
        }
        catch(Exception e){
            logger.severe("Error changing provider " + e);
        }
        return false;
    }

    public boolean updatePassword(Users user){
        String sql = "update users set hashed_password = ? where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, user.getPassword());
            ps.setString(2 , user.getEmail());
            ps.executeUpdate();
        }
        catch(Exception e){
            logger.severe("Error changing password " + e);
            return false;
        }
        return true;
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
                        return updatePassword(user);
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
}


