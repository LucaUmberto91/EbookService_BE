package it.ebookservice.service;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.ebookservice.model.EmailMessage;
import it.ebookservice.model.Users;

@Service
public class SendEmailImpl implements SendEmail{
	
	@Value("${email.authentication.username}")
	private String usernameAdmin;
	@Value("${email.authentication.password}")
	private String pswdAdmin;

	@Override
	public void sendEmail(EmailMessage mail) {
    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    // Get a Properties object
       Properties props = System.getProperties();
       props.setProperty("mail.smtp.host", "smtp.gmail.com");
       props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
       props.setProperty("mail.smtp.socketFactory.fallback", "false");
       props.setProperty("mail.smtp.port", "465");
       props.setProperty("mail.smtp.socketFactory.port", "465");
       props.put("mail.smtp.auth", "true");
       props.put("mail.debug", "true");
       props.put("mail.store.protocol", "pop3");
       props.put("mail.transport.protocol", "smtp");

    try {
    	Session session = Session.getDefaultInstance(props, 
                new Authenticator(){
                   protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication(usernameAdmin,pswdAdmin);
                   }});
	       MimeMessage message = new MimeMessage(session);
	       message.setFrom(new InternetAddress(mail.getFrom()));
	       message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
	       message.setSubject(mail.getSubject());
	       message.setText(mail.getText());
	       Transport.send(message);
	       System.out.println("Sent email successfully....");
    } catch (MessagingException mex) {
        mex.printStackTrace();
    }    
    }
	
	@Override
	public String generatePassayPassword() {
		  // ASCII range - alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Integer len = 8;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
 
        // each iteration of loop choose a character randomly from the given ASCII range
        // and append it to StringBuilder instance
 
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
 
        return sb.toString();
    }
	
	
	@Override
	public void sendEmailRegister(Users utente) {
		String pswd = generatePassayPassword();
		utente.setPassword(pswd);
		String txt = "Congratulations "+utente.getName()+"! You are registered correctly!\n Your Password is : "+pswd+"\nNow you can access the site with the following password and change it.";
		EmailMessage email = new EmailMessage(utente.getEmail(),"noreply@books.com","Registration Confirmation",txt);
		sendEmail(email);
	}
}
    
