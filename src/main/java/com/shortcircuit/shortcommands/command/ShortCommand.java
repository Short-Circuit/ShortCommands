package com.shortcircuit.shortcommands.command;

import com.shortcircuit.shortcommands.exceptions.TooFewArgumentsException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShortCircuit908
 */
public abstract class ShortCommand extends Command implements PluginIdentifiableCommand {
	private final String owning_plugin;
	private boolean enabled = true;
	private final ArrayList<String> aliases = new ArrayList<>();

	public ShortCommand(String name, String owning_plugin) {
		super(name);
		this.owning_plugin = owning_plugin;
	}

	public ShortCommand(String name, Plugin owning_plugin) {
		this(name, owning_plugin.getName());
	}

	protected void registerAliases() {
		for (String alias : getCommandNames()) {
			aliases.add(alias);
		}
	}

	/**
	 * Gets the name of the plugin that registered the command
	 *
	 * @return The name of the owning plugin
	 */
	public String getOwningPlugin() {
		return owning_plugin;
	}

	@Override
	public Plugin getPlugin() {
		return Bukkit.getServer().getPluginManager().getPlugin(owning_plugin);
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public Command setAliases(List<String> aliases) {
		this.aliases.clear();
		this.aliases.addAll(aliases);
		return this;
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
	 * <br>
	 * The very first line should be a short description of the command. Following the first line can be
	 * any number of lines containing more information, including a more detailed description, proper
	 * usage, etc. If included, the ${command} syntax will be replaced with the command name that was used
	 * in executing the command.
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

	public String getUniqueName() {
		return owning_plugin + ":" + this.getClass().getSimpleName();
	}

	public void tabComplete(final CommandTabCompleteEvent event) {

	}

	/**
	 * Runs the command
	 * <p>
	 * This method is called whenever the command is run. All elements of the return value are sent
	 * to the user who ran the command. This is the appropriate place to provide any user feedback
	 * from the command
	 *
	 * @param command The CommandWrapper associated with a given command
	 * @return An array containing user feedback
	 * @throws TooFewArgumentsException, TooManyArgumentsException,
	 *                                   InvalidArgumentException, NoPermissionException,
	 *                                   PlayerOnlyException, ConsoleOnlyException,
	 *                                   BlockOnlyException
	 */
	public abstract String[] exec(CommandWrapper command) throws Exception;

	@Override
	public boolean execute(CommandSender sender, String command_label, String[] args) {
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args)  throws IllegalArgumentException {
		return super.tabComplete(sender, alias, args);
	}
}