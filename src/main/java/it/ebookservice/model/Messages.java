package it.ebookservice.model;

public class Messages {

	private Integer code;
	private String message;
	
	public Messages(Integer codice,String message) {
		this.code = codice;
		this.message = message;
	}
	public Messages() {
		
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer codice) {
		this.code = codice;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
