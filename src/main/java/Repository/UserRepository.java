package Repository;

import enums.AuthProvider;
import model.Users;
import util.AppLogger;
import util.DBConfig;
import java.sql.*;
import java.util.logging.Logger;
import Exception.ServerException;

public class UserRepository{

    public Logger logger = AppLogger.getLogger(UserRepository.class);

    public boolean insert(Users user , AuthProvider p){

        String sql = "insert into users (email , hashed_password , auth_provider) values(?,?,?)";

        try (Connection con = DBConfig.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1 , user.getEmail());
            ps.setString(2 , user.getPassword());
            ps.setString(3 , p.name());
            return ps.executeUpdate() > 0;
        }
        catch(SQLException e){
            logger.severe("Error inserting " + e);
            throw new ServerException("Database error" + e);
        }
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
        catch(SQLException e){
            logger.severe("Error getting password " + e);
            throw new ServerException("Database error" + e);
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
        catch (SQLException e){
            logger.severe("Error getting provider " + e);
            throw new ServerException("Database error" + e);
        }
        return null;
    }

    public boolean isEmailExist(String email){

        String sql = "select email from users where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
        catch (SQLException e){
            logger.severe("Error getting provider " + e);
            throw new ServerException("Database error" + e);
        }
    }

    public boolean changeProvider(String email){
        String sql = "update users set auth_provider = ? where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, AuthProvider.BOTH.name());
            ps.setString(2 , email);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e){
            logger.severe("Error getting provider " + e);
            throw new ServerException("Database error" + e);
        }
    }

    public boolean updatePassword(Users user){
        String sql = "update users set hashed_password = ? where email = ?";
        try (Connection con = DBConfig.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, user.getPassword());
            ps.setString(2 , user.getEmail());
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e){
            logger.severe("Error getting provider " + e);
            throw new ServerException("Database error" + e);
        }
    }
}





