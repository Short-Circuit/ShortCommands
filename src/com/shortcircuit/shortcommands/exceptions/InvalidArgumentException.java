package com.shortcircuit.shortcommands.exceptions;


/**
 * @author ShortCircuit908
 * 
 */
public class InvalidArgumentException extends Exception {
	private static final long serialVersionUID = 5875650314991369677L;
	private final String arg;
	public InvalidArgumentException(String arg) {
		this.arg = arg;
	}
	/**
	 * Gets the argument which caused this exception
	 *
	 * @return The invalid argument
	 */
	public String getArg() {
		return arg;
	}
}
