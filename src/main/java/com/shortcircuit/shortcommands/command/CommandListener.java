package com.shortcircuit.shortcommands.command;

import com.shortcircuit.shortcommands.ShortCommands;
import com.shortcircuit.shortcommands.command.bukkitwrapper.BukkitCommandRegister;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * This class is strictly for internal command handling, and should not be used under any
 * circumstances
 *
 * @author ShortCircuit908
 */
@SuppressWarnings("unused")
public final class CommandListener implements Listener {
	private final ShortCommandHandler<ShortCommand> command_handler;
	private final BukkitCommandRegister command_register;

	public CommandListener(ShortCommands plugin) {
		command_handler = plugin.getCommandHandler();
		command_register = plugin.getCommandRegister();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(final PlayerCommandPreprocessEvent event) {
		if (event.isCancelled() || event.getMessage().split(" ")[0].equalsIgnoreCase("/stop")
				|| event.getMessage().split(" ")[0].equalsIgnoreCase("/reload")) {
			return;
		}
		String command = event.getMessage().replaceFirst("/", "");
		String[] args = command.split(" ");
		String[] new_args = new String[0];
		if (args.length > 1) {
			new_args = new String[args.length - 1];
			System.arraycopy(args, 1, new_args, 0, new_args.length);
		}
		boolean success = command_handler.exec(new CommandWrapper(event.getPlayer(), command.split(" ")[0], new_args));
		if (success) {
			event.setMessage("/cmd-echo");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(final ServerCommandEvent event) {
		if (event.getCommand().split(" ")[0].equalsIgnoreCase("stop")
				|| event.getCommand().split(" ")[0].equalsIgnoreCase("reload")) {
			return;
		}
		String command = event.getCommand();
		String[] args = command.split(" ");
		String[] new_args = new String[0];
		if (args.length > 1) {
			new_args = new String[args.length - 1];
			System.arraycopy(args, 1, new_args, 0, new_args.length);
		}
		boolean success = command_handler.exec(new CommandWrapper(event.getSender(), command.split(" ")[0], new_args));
		if (success) {
			event.setCommand("cmd-echo");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommandBlock(final BlockRedstoneEvent event) {
		if (event.getNewCurrent() == event.getOldCurrent()) {
			return;
		}
		Block block = event.getBlock();
		if (block.getType().equals(Material.COMMAND)) {
			CommandBlock command_block = (CommandBlock) block.getState();
			if (!command_block.getCommand().trim().isEmpty()
					&& command_handler.hasCommand(command_block.getCommand().split(" ")[0])) {
				command_block.setCommand("cmd-run " + command_block.getCommand());
				command_block.update();
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(final PluginDisableEvent event) {
		for (ShortCommand command : command_handler.getCommands(event.getPlugin())) {
			command_handler.unregisterCommand(command);
		}
		command_register.unwrapPluginCommands(event.getPlugin().getName());
	}
}
