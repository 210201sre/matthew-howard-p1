package com.revature.exceptions;

public class StockNotFoundException extends RuntimeException{
	public StockNotFoundException() {
	}

	public StockNotFoundException(String message) {
		super(message);
	}

	public StockNotFoundException(Throwable cause) {
		super(cause);
	}

	public StockNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public StockNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}