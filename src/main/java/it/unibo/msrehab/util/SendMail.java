/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.util;


import it.unibo.msrehab.model.entities.MSRUser;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.LoggerFactory;

/**
 *
 * @author floriano
 */
public class SendMail
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SendMail.class);

    /*
    Credenziali gmail
     */
    private static final String gmailUsername = "msrehabmail@gmail.com";
    private static final String gmailPassword = "msrehab123";

    private static final String gmailAppPassword = "eioazsyyhtmtgwjo"; // Google app password



    private static final String from = "msrehabmail@gmail.com";

    /**
     * Sends a mail asynchronously using the gmail account msrehabmail@gmail.com.
     *
     * @param to
     * @param subject
     * @param mex
     */
    public static void sendMail(String to, String subject, String mex)
    {
        Email email = EmailBuilder.startingBlank()
                .from(from)
                .to(to)
                .withSubject(subject)
                .withPlainText(mex)
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, gmailUsername, gmailAppPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(200000)
                .buildMailer();


        Thread th = new Thread(() ->
        {
            logger.info("Sending email: {}", to);
            mailer.sendMail(email).thenRun(() ->
            {
                logger.info("Email sent to: {}", to);
                try
                {
                    mailer.close();
                } catch (Exception ignored)
                {
                }
            });
        });
        th.start();
    }
        
    public static void sendCredentials(MSRUser user)
    {
        String message = "Le tue credenziali di accesso:\n"
                + "\nUsername:\t" + user.getUsername()
                + "\nPassword:\t" + user.getPassword();

        String to = user.getEmail();

        sendMail(to, "Recupero credenziali MS-Rehab", message);
    }


    /**
     * Sends a mail asynchronously with an attachment using the gmail account msrehabmail@gmail.com.
     *
     * @param to
     * @param subject
     * @param mex
     * @param fileName
     */
    public static void sendMail(
            String to,
            String subject,
            String mex,
            String fileName)
    {
        DataSource source = new FileDataSource(fileName);

        Email email = EmailBuilder.startingBlank()
                .from(from)
                .to(to)
                .withSubject(subject)
                .withPlainText(mex)
                .withAttachment(fileName, source)
                .buildEmail();


        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, gmailUsername, gmailAppPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(100000)
                .buildMailer();
        Thread th = new Thread(() ->
        {
            mailer.sendMail(email).thenRun(() ->
            {
                try
                {
                    mailer.close();
                } catch (Exception ignored){}
            });
        });
        th.start();

    }
}