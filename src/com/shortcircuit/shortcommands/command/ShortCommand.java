package com.shortcircuit.shortcommands.command;

import org.bukkit.plugin.Plugin;

import com.shortcircuit.shortcommands.exceptions.BlockOnlyException;
import com.shortcircuit.shortcommands.exceptions.ConsoleOnlyException;
import com.shortcircuit.shortcommands.exceptions.InvalidArgumentException;
import com.shortcircuit.shortcommands.exceptions.NoPermissionException;
import com.shortcircuit.shortcommands.exceptions.PlayerOnlyException;
import com.shortcircuit.shortcommands.exceptions.TooFewArgumentsException;
import com.shortcircuit.shortcommands.exceptions.TooManyArgumentsException;

/**
 * @author ShortCircuit908
 * 
 */
public abstract class ShortCommand {
	private final String owning_plugin;
	public ShortCommand(String owning_plugin) {
		this.owning_plugin = owning_plugin;
	}
	public ShortCommand(Plugin owning_plugin) {
		this.owning_plugin = owning_plugin.getName();
	}
	/**
	 * Gets the name of the plugin that registered the command
	 * 
	 * @return The name of the owning plugin
	 */
	public String getOwningPlugin() {
		return owning_plugin;
	}
	/**
	 * Gets the type of command
	 *
	 * @return The type of command
	 */
	public abstract CommandType getCommandType();
	/**
	 * Gets the names of the command
	 * <p>
	 * A command can have any number of names. They will all be checked for when the command is run
	 *
	 * @return An array containing the names of the command
	 */
	public abstract String[] getCommandNames();
	/**
	 * Gets the permissions required to run the command
	 *
	 * @return The required permissions
	 */
	public abstract String getPermissions();
	/**
	 * Gets the help topics of the command
	 * <p>
	 * Each element of the array is one line of the help message
	 *
	 * @return An array containing the help messages of the command
	 */
	public abstract String[] getHelp();
	/**
	 * Runs the command
	 * <p>
	 * This method is called whenever the command is run. All elements of the return value are sent
	 * to the user who ran the command. This is the appropriate place to provide any user feedback
	 * from the command
	 * @throws TooFewArgumentsException, TooManyArgumentsException,
	 * InvalidArgumentException, NoPermissionException,
	 * PlayerOnlyException, ConsoleOnlyException,
	 * BlockOnlyException
	 *
	 * @return An array containing user feedback
	 */
	public abstract String[] exec(CommandWrapper command) throws
	TooFewArgumentsException, TooManyArgumentsException,
	InvalidArgumentException, NoPermissionException,
	PlayerOnlyException, ConsoleOnlyException,
	BlockOnlyException;
}
