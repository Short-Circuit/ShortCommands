package com.shortcircuit.shortcommands.exceptions;


/**
 * @author ShortCircuit908
 */
public class InvalidArgumentException extends Exception {
	private static final long serialVersionUID = 5875650314991369677L;

	public InvalidArgumentException(){
		this("Invalid argument.");
	}

	public InvalidArgumentException(String message) {
		super(message);
	}
}
