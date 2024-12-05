package com.InternShip.Services;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
 class EmailService {

       private final String host = "smtp.gmail.com";
       private final String serviceEmail = "sanya.bero18@gmail.com";
       private final String password = "vvfl ybzg bpug icvl";
       private Session session;

        @PostConstruct
        public void init(){
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", this.host);
            properties.put("mail.smtp.port", "587");
    
            this.session = Session.getDefaultInstance(properties, new Authenticator(){
                protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(serviceEmail, password);
                }
            });
        }

    public boolean sendEmail(String to, String subject, String messageText){
        try{
            MimeMessage message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.serviceEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            return true;
        }
        catch(MessagingException e){
            e.printStackTrace();
            return false;
        }

    }
}