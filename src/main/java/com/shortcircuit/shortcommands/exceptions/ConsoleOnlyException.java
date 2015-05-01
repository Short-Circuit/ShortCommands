package com.shortcircuit.shortcommands.exceptions;

/**
 * @author ShortCircuit908
 *
 */
public class ConsoleOnlyException extends Exception{
	private static final long serialVersionUID = 1803155064915494679L;
	public ConsoleOnlyException(){
		super("This command is console-only");
	}
}
