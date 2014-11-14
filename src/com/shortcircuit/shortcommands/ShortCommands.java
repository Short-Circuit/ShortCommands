package com.shortcircuit.shortcommands;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.shortcircuit.shortcommands.command.CommandListener;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.command.ShortCommandHandler;

/**
 * @author ShortCircuit908
 * 
 */
public class ShortCommands extends JavaPlugin{
	private ShortCommandHandler<ShortCommand> command_handler = new ShortCommandHandler<ShortCommand>();
	public void onEnable() {
		Bukkit.getLogger().info("[ShortCommands] ShortCommands by ShortCircuit908");
		getServer().getPluginManager().registerEvents(new CommandListener(this), this);
		Bukkit.getLogger().info("[ShortCommands] ShortCommands enabled");
	}
	public boolean onCommand(CommandSender command_sender, Command command, String command_label, String[] args){
		if(command_label.equalsIgnoreCase("cmd-echo")) {
			String message = "";
			for(String arg : args) {
				message += " " + arg;
			}
			if(!message.trim().isEmpty()) {
				Bukkit.getConsoleSender().sendMessage(message.trim());
			}
			return true;
		}
		else if(command_label.equalsIgnoreCase("cmd-run") && command_sender.getName().equals("@")) {
			String message = "";
			for(String arg : args) {
				message += " " + arg;
			}
			if(!message.trim().isEmpty()) {
				command_handler.exec(new CommandWrapper(command_sender, args[0],
						(String[])ArrayUtils.remove(args, 0)));
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
	public ShortCommandHandler<ShortCommand> getCommandHandler(){
		return command_handler;
	}
	/**
	 * Gets the current instance of this plugin
	 *
	 * @return The currently running instance
	 */
	public static ShortCommands getInstance() {
		return (ShortCommands)(Bukkit.getPluginManager().getPlugin("ShortCommands"));
	}
}
