package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

public class DBConfig{
    private static DBConfig instance = null;
    private Logger logger = AppLogger.getLogger(DBConfig.class);
    private String url = "jdbc:mysql://localhost:3306/zoho";

    private DBConfig(){}

    public static DBConfig getInstance(){
        if(instance == null){
            instance = new DBConfig();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String user = System.getenv("user");
            String pwd = System.getenv("db_password");
            return DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            logger.info("DbConfig error");
        }
        return null;
    }
}







