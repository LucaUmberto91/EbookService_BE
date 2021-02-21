package it.ebookservice.model;

public class EmailMessage {

	private String to;
	private String from;
	public static final String host = "127.0.0.1";
	private String subject;
	private String text;
	
	public EmailMessage(String to,String from,String subject,String text) {
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.text = text;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
