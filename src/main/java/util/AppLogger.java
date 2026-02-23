package util;

import java.util.logging.Logger;

public class AppLogger {
    public static Logger getLogger(Class<?> c){
        return Logger.getLogger(c.getName());
    }
}
