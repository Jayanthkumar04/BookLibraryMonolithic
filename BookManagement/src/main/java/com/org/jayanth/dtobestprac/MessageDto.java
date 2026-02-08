package com.org.jayanth.dtobestprac;

public class MessageDto {
private String message;

public MessageDto(String message) {
	super();
	this.message = message;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

@Override
public String toString() {
	return "MessageDto [message=" + message + "]";
}



}
