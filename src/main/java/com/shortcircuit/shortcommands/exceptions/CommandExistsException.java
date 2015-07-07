package com.shortcircuit.shortcommands.exceptions;


/**
 * @author ShortCircuit908
 */
public class CommandExistsException extends Exception {
	private static final long serialVersionUID = 386465469301937099L;
	private final String[] conflicting_commands;

	public CommandExistsException(String[] conflicting_commands) {
		this.conflicting_commands = conflicting_commands.clone();
	}

	public String[] getConflictingCommands() {
		return conflicting_commands;
	}
}
