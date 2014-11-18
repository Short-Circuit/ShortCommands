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
	private boolean enabled = true;
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
	 * Gets whether or not the command can be disabled
	 * <p>
	 * For most commands, this should return true. However, if your command provides core functionality,
	 * you may wish to have your command not be disabled.
	 * 
	 * @return Whether or not the command can be disabled
	 */
	public abstract boolean canBeDisabled();
	/**
	 * Gets whether or not the command is enabled
	 * <p>
	 * When a command is disabled, any calls to the command will be entirely ignored, and will be passed
	 * along to Bukkit's default command handling. The command itself is not unregistered, to prevent name
	 * conflicts should another plugin attempt to register a command with a matching name.
	 * 
	 * @return Whether or not the command is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * Sets whether or not the command is enabled
	 * <p>
	 * When a command is disabled, any calls to the command will be entirely ignored, and will be passed
	 * along to Bukkit's default command handling. The command itself is not unregistered, to prevent name
	 * conflicts should another plugin attempt to register a command with a matching name.
	 * 
	 * @param enabled The enabled state of the command
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
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
	 * @param command The CommandWrapper associated with a given command
	 * @return An array containing user feedback
	 */
	public abstract String[] exec(CommandWrapper command) throws
	TooFewArgumentsException, TooManyArgumentsException,
	InvalidArgumentException, NoPermissionException,
	PlayerOnlyException, ConsoleOnlyException,
	BlockOnlyException;
}
