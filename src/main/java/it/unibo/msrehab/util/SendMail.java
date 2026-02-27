/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.util;

import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.LoggerFactory;

/**
 *
 * @author floriano
 */
public class SendMail {
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SendMail.class);
    

    private static final String from = "floriano.zini@unibo.it";

    // Assuming you are sending email from localhost
    private static final String host = "smtp.cs.unibo.it";
    //private static final String host = "mail.unibo.it";

    public static void sendPassword(String to, String pwd) {

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);        
        //properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.auth", "false");
        //properties.setProperty("mail.debug", "false");
        properties.setProperty("mail.smtp.port", "25");
        //properties.setProperty("mail.smtp.starttls.enable", "false");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

//        Session session = Session.getDefaultInstance(properties,
//                new javax.mail.Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("floriano.zini@unibo.it", "333%Peace$");
//                    }
//                });
        
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Riabilitazione cognitiva");

            // Now set the actual message
            message.setText("Le tue credenziali di accesso:\n"
                    + "\nEmail:\t\t" + to
                    + "\nPassword:\t" + pwd);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException ex) {
            logger.error("... Error while sending mail: "+ex.getMessage());        }
    }
        
    public static void sendCredentials(String to, Integer id, String pwd) {

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);        
        //properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.auth", "false");
        //properties.setProperty("mail.debug", "false");
        properties.setProperty("mail.smtp.port", "25");
        //properties.setProperty("mail.smtp.starttls.enable", "false");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Riabilitazione cognitiva - credenziali di accesso");

            // Now set the actual message
            message.setText("Le tue credenziali di accesso:\n"
                    + "\nIdentificativo:\t" + id
                    + "\nPassword:\t" + pwd);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException ex) {
            logger.error("... Error while sending mail: "+ex.getMessage());        }
    }

    public static void sendMail(String to,String subject, String mex) {

        // Get system properties
        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", host);        
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.port", "25");
        Session session = Session.getDefaultInstance(properties);
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(mex);
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException ex) {
            logger.error("... Error while sending mail: "+ex.getMessage());        }
    }
    
    

    public static void sendMail(
            String to,
            String subject,
            String mex,
            String fileName) {

        // Get system properties
        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", host);        
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.port", "25");
        Session session = Session.getDefaultInstance(properties);
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mex);
            Multipart multipart = new MimeMultipart();
            messageBodyPart = new MimeBodyPart();
            if(fileName!=null) {
                DataSource source = new FileDataSource(fileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(source.getName());
            }
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException ex) {
            logger.error("... Error while sending mail: "+ex.getMessage());        }
    }
}