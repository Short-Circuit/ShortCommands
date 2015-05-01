package com.shortcircuit.shortcommands.exceptions;


import org.bukkit.ChatColor;

/**
 * @author ShortCircuit908
 */
public class InvalidArgumentException extends Exception {
	private static final long serialVersionUID = 5875650314991369677L;
	private final String command;
	private final String arg;

	public InvalidArgumentException(String command, String arg) {
		super("Invalid argument \"" + arg + "\"\n" + ChatColor.RED + "Try /" + command + " help");
		this.command = command;
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

	public String getCommand() {
		return command;
	}
}
