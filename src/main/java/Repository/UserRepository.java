package Repository;

import enums.AuthProvider;
import model.Users;
import util.AppLogger;
import util.DBConfig;
import java.sql.*;
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
}






