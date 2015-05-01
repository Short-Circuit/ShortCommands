package com.shortcircuit.shortcommands.exceptions;

import org.bukkit.ChatColor;

/**
 * @author ShortCircuit908
 */
public class TooFewArgumentsException extends Exception {
	private static final long serialVersionUID = -4091705821015041002L;

	public TooFewArgumentsException(String command) {
		super("Too few arguments\n" + ChatColor.RED + "Try /" + command + " help");
	}
}
