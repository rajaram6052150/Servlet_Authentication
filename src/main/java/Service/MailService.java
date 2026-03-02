package Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import util.AppLogger;
import java.util.logging.Logger;
import java.util.Properties;

public class MailService {
    Logger logger = AppLogger.getLogger(MailService.class);

    public void sentMail(String recipient, String subject, String body){
        if (recipient != null && !recipient.trim().isEmpty()) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("rajasandy2431@gmail.com", System.getenv("rajasandyAppCode"));
                }
            });
            Message message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress("rajasandy2431@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(subject);
                message.setContent("<h1>Hello!</h1><p>" + body + "</p>", "text/html");
                Transport.send(message);
                logger.info("Mail successfully sent");
            } catch (Exception e) {
                logger.severe("AddressException: " + e.getMessage());
            }
        }
    }
}

