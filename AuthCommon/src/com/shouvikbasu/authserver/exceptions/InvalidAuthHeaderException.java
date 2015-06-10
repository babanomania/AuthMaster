package com.shouvikbasu.authserver.exceptions;

@SuppressWarnings("serial")
public class InvalidAuthHeaderException extends Exception {

	public InvalidAuthHeaderException(String message) {
		super(message);
	}

}
