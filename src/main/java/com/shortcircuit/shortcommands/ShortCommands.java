package com.shortcircuit.shortcommands;

import com.shortcircuit.shortcommands.command.CommandListener;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.command.ShortCommandHandler;
import com.shortcircuit.shortcommands.command.bukkitwrapper.BukkitCommandRegister;
import com.shortcircuit.shortcommands.command.bukkitwrapper.WrapperTask;
import com.shortcircuit.shortcommands.commands.DisableCommand;
import com.shortcircuit.shortcommands.commands.EnableCommand;
import com.shortcircuit.shortcommands.commands.InfoCommand;
import com.shortcircuit.shortcommands.commands.ListCommand;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * @author ShortCircuit908
 */
public final class ShortCommands extends JavaPlugin {
	private final ShortCommandHandler<ShortCommand> command_handler = new ShortCommandHandler<>();
	private BukkitCommandRegister command_register;
	private JSONConfig config;
	private boolean save_disabled_commands = true;

	public void onEnable() {
		getLogger().info("ShortCommands by ShortCircuit908");
		saveDefaultConfig();
		config = new JSONConfig(this);
		config.loadConfig();
		command_register = new BukkitCommandRegister(this);
		getServer().getPluginManager().registerEvents(new CommandListener(this), this);
		Set<ShortCommand> unregistered_commands = command_handler.registerCommands(
				new DisableCommand(this),
				new EnableCommand(this),
				new InfoCommand(this),
				new ListCommand(this));
		if (unregistered_commands.size() > 0) {
			getLogger().severe("Could not register core commands");
			getLogger().severe("Disabling...");
			setEnabled(false);
			return;
		}
		command_handler.registerHelpTopics(this);
		getLogger().info("ShortCommands enabled");
		getServer().getScheduler().scheduleSyncDelayedTask(this, new WrapperTask(this), 0);
	}

	public void onDisable() {
		for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
			command_register.unwrapPluginCommands(plugin.getName());
		}
		HandlerList.unregisterAll(this);
		if (save_disabled_commands) {
			config.saveConfig();
		}
		getLogger().info("ShortCommands disabled");
	}

	public boolean onCommand(CommandSender command_sender, Command command, String command_label, String[] args) {
		if (command_label.equalsIgnoreCase("cmd-echo")) {
			String message = "";
			for (String arg : args) {
				message += " " + arg;
			}
			if (!message.trim().isEmpty()) {
				getServer().getConsoleSender().sendMessage(message.trim());
			}
			return true;
		}
		else if (command_label.equalsIgnoreCase("cmd-run") && command_sender instanceof BlockCommandSender) {
			String message = "";
			for (String arg : args) {
				message += " " + arg;
			}
			if (!message.trim().isEmpty()) {
				command_handler.exec(new CommandWrapper(command_sender, args[0], ArrayUtils.remove(args, 0)));
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets the ShortCommand command handler
	 *
	 * @return The command handler
	 * @see ShortCommandHandler
	 */
	public ShortCommandHandler<ShortCommand> getCommandHandler() {
		return command_handler;
	}

	/**
	 * Gets the current instance of this plugin
	 *
	 * @return The currently running instance
	 */
	public static ShortCommands getInstance() {
		return (ShortCommands) (Bukkit.getPluginManager().getPlugin("ShortCommands"));
	}

	/**
	 * Sets whether or not the disabled command list should be saved
	 * <p>
	 * This method is strictly for internal command handling, and should not be used under any
	 * circumstances
	 *
	 * @param save Whether or not to save the list of disabled commands to the config file
	 */
	protected void setSaveDisabledCommands(boolean save) {
		this.save_disabled_commands = save;
	}

	/**
	 * Gets the BukkitCommandRegister used to wrap plugin commands
	 *
	 * @return The command register
	 */
	public BukkitCommandRegister getCommandRegister() {
		return command_register;
	}
}
