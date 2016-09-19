package minas.test;

import java.io.Serializable;

public class PhoneDto implements Serializable{

	private static final long serialVersionUID = -751997082351462343L;

	private String sendCode;
	
	private String receiveCode;
	
	private String message;

	public String getSendCode() {
		return sendCode;
	}

	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
