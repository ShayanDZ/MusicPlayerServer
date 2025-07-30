package com.hertz.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtils {

    // TODO
    private static final String SMTP_HOST = "your_smtp_host"; // e.g., "smtp.gmail.com"
    private static final String SMTP_PORT = "your_smtp_port";   // e.g., "587" (TLS) or "465" (SSL)
    private static final String SMTP_USERNAME = "your_email_address";  // e.g., "your_email@gmail.com"
    private static final String SMTP_PASSWORD = "your_email_password";  // Your email password or app password

    public static void sendEmail(String to, String subject, String body) {
        // Set up properties for the SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        // Enable STARTTLS if your server requires it (e.g., Gmail)
        props.put("mail.smtp.starttls.enable", "true");

        // Enable authentication
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.user", SMTP_USERNAME);

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            // Create a new MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set the subject and body of the email
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("Error sending email to: " + to);
            e.printStackTrace();  // Log the full exception for debugging
            throw new RuntimeException("Failed to send email", e); //Re-throw as runtime exception
        }
    }
}
