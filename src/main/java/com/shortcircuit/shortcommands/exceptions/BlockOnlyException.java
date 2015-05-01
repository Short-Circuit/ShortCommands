package com.shortcircuit.shortcommands.exceptions;

/**
 * @author ShortCircuit908
 *
 */
public class BlockOnlyException extends Exception{
	private static final long serialVersionUID = -7540534873192048118L;
	public BlockOnlyException(){
		super("This command is block-only");
	}
}
