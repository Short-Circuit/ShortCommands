package com.shortcircuit.shortcommands.command.bukkitwrapper;

import com.shortcircuit.shortcommands.ShortCommands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.Set;

/**
 * @author ShortCircuit908
 */
@SuppressWarnings("unused")
public class PluginListener implements Listener {
	private final BukkitCommandRegister command_register;
	private final Set<String> wrapped_plugins;

	public PluginListener(ShortCommands plugin, Set<String> wrapped_plugins) {
		this.wrapped_plugins = wrapped_plugins;
		command_register = plugin.getCommandRegister();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event) {
		String plugin_name = event.getPlugin().getName();
		if(wrapped_plugins.contains("*") || wrapped_plugins.contains(plugin_name.toLowerCase())) {
			command_register.wrapPluginCommands(plugin_name);
		}
	}
}
