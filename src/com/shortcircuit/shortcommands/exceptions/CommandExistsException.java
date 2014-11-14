package com.shortcircuit.shortcommands.exceptions;


/**
 * @author ShortCircuit908
 * 
 */
public class CommandExistsException extends Exception{
	private static final long serialVersionUID = 386465469301937099L;
	//private final ShortCommand conflicting_command;
	private final String[] conflicting_commands;
	public CommandExistsException(String[] conflicting_commands) {
		this.conflicting_commands = conflicting_commands.clone();
	}
	public String[] getConflictingCommands() {
		return conflicting_commands;
	}
	/*
	public CommandExistsException(ShortCommand conflicting_command) {
		this.conflicting_command = conflicting_command;
	}
	public ShortCommand getConflictingCommand() {
		return conflicting_command;
	}
	*/
}
