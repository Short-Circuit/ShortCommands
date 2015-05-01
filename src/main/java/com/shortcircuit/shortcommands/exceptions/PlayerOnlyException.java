package com.shortcircuit.shortcommands.exceptions;

/**
 * @author ShortCircuit908
 *
 */
public class PlayerOnlyException extends Exception{
	private static final long serialVersionUID = -7540534873192048118L;
	public PlayerOnlyException(){
		super("This command is player-only");
	}
}
