package com.shortcircuit.shortcommands.exceptions;

import org.bukkit.ChatColor;

/**
 * @author ShortCircuit908
 */
public class TooManyArgumentsException extends Exception {
	private static final long serialVersionUID = -3684596901032869405L;

	public TooManyArgumentsException(String command) {
		super("Too few arguments\n" + ChatColor.RED + "Try /" + command + " help");
	}
}
