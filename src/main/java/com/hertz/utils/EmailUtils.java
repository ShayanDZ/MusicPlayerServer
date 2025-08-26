package com.hertz.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtils {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "465";
    private static final String SMTP_USERNAME = "MusicAppShayan@gmail.com";
    private static final String SMTP_PASSWORD = "gppa pqgj iwlh oemr";

    public static void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.user", SMTP_USERNAME);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
        session.setDebug(true);
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("Error sending email to: " + to);
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
