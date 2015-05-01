package com.shortcircuit.shortcommands.command.bukkitwrapper;

import com.shortcircuit.shortcommands.ShortCommands;

import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ShortCircuit908
 */
public class WrapperTask implements Runnable {
	private final ShortCommands short_commands;
	private final BukkitCommandRegister command_register;
	private final Set<String> wrapped_plugins = new HashSet<>();

	public WrapperTask(ShortCommands short_commands) {
		this.short_commands = short_commands;
		command_register = short_commands.getCommandRegister();
		List<String> overrides = short_commands.getConfig().getStringList("CommandOverrides");
		if (overrides.contains("*")) {
			wrapped_plugins.add("Bukkit");
			for (Plugin plugin : short_commands.getServer().getPluginManager().getPlugins()) {
				wrapped_plugins.add(plugin.getName());
			}
		}
		else {
			for (String wrapped_plugin : overrides) {
				wrapped_plugins.add(wrapped_plugin);
			}
		}
		for(String override : overrides){
			if(override.startsWith("-")){
				wrapped_plugins.remove(override.substring(1).trim());
			}
		}
	}

	@Override
	public void run() {
		short_commands.getServer().getPluginManager().registerEvents(
				new PluginListener(short_commands, wrapped_plugins), short_commands);
		for (String wrapped_plugin : wrapped_plugins) {
			command_register.wrapPluginCommands(wrapped_plugin);
		}
	}
}
