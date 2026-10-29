package it.unibo.msrehab;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.util.Properties;

public class MailTest
{
    static String appPassword = "eioazsyyhtmtgwjo";

    public static void main(String[] args) throws Exception
    {
        useSimpleMail();
    }

    public static void useSimpleMail() throws Exception
    {
        Email email = EmailBuilder.startingBlank()
                .from("msrehabmail@gmail.com")
                .to("msrehabmail@gmail.com")
                .withSubject("Email with Plain Text!")
                .withPlainText("This is a test email sent using SJM.")
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "msrehabmail@gmail.com", appPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(120000)
                .buildMailer();

        mailer.sendMail(email).thenRun(() ->
        {
            try
            {
                mailer.close();
            }
            catch (Exception ignored){}
        });

    }


    public static void useJakarta() {

        final String username = "msrehabmail@gmail.com";
        final String password = appPassword;
        final String host = "smtp.gmail.com";
        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.starttls.required", "true");
        prop.put("mail.smtp.socketFactory.port", "587");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("msrehabmail@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("msrehabmail@gmail.com")
            );
            message.setSubject("Testing Gmail SSL");
            message.setText("Dear Mail Crawler,"
                    + "\n\n Please do not spam my email!");


            Transport transport = session.getTransport("smtps");
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

