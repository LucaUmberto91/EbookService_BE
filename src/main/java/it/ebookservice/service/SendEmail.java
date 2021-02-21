package it.ebookservice.service;


import it.ebookservice.model.EmailMessage;
import it.ebookservice.model.Users;


public interface SendEmail {

	public void sendEmail(EmailMessage mail);
	public void sendEmailRegister(Users utente);
	public String generatePassayPassword();
}
