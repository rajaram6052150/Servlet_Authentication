package util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class ResWriter {
    public static Logger logger = AppLogger.getLogger(ResWriter.class);

    public static void write(HttpServletResponse res , String str){
        try{
            res.getWriter().write(str);
        }
        catch(IOException e){
            logger.info("Response Writer error");
        }
    }
}
