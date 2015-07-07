package com.shortcircuit.shortcommands.command.bukkitwrapper;

import com.shortcircuit.shortcommands.ShortCommands;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.command.ShortCommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author ShortCircuit908
 */
public class BukkitCommandRegister {
	private final ShortCommands short_commands;
	private final ShortCommandHandler<ShortCommand> command_handler;
	private Map<String, Command> known_commands;

	public BukkitCommandRegister(ShortCommands short_commands) {
		this.short_commands = short_commands;
		this.command_handler = short_commands.getCommandHandler();
		hookComands();
	}

	@SuppressWarnings("unchecked")
	private void hookComands() {
		try {
			SimplePluginManager plugin_manager = (SimplePluginManager)short_commands.getServer().getPluginManager();
			Field command_map_field = plugin_manager.getClass().getDeclaredField("commandMap");
			command_map_field.setAccessible(true);
			SimpleCommandMap command_map = (SimpleCommandMap)command_map_field.get(plugin_manager);
			command_map.setFallbackCommands();
			Field known_commands_field = command_map.getClass().getDeclaredField("knownCommands");
			known_commands_field.setAccessible(true);
			known_commands = (Map<String, Command>)known_commands_field.get(command_map);
		}
		catch(NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void wrapPluginCommands(String plugin) {
		if(plugin.equalsIgnoreCase("shortcommands")) {
			return;
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ShortCommands] Wrapping commands from " + plugin + "...");
		for(String fallback_prefix : known_commands.keySet()) {
			String owner = fallback_prefix.split(":")[0];
			if(plugin.equalsIgnoreCase(owner)) {
				command_handler.wrapPluginCommand(new BukkitCommandWrapper(known_commands.get(fallback_prefix)));
			}
		}
	}

	public void unwrapPluginCommands(String plugin) {
		if(plugin.equalsIgnoreCase("shortcommands")) {
			return;
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ShortCommands] Unwrapping commands from " + plugin + "...");
		for(ShortCommand command : command_handler.getCommands(plugin)) {
			if(command instanceof BukkitCommandWrapper) {
				command_handler.unwrapPluginCommand((BukkitCommandWrapper)command);
			}
		}
	}
}
