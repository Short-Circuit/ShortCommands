package com.shortcircuit.shortcommands.command;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.shortcircuit.shortcommands.ShortCommands;

/** 
 * This class is strictly for internal command handling, and should not be used under any
 * circumstances
 * 
 * @author ShortCircuit908
 */
public class CommandListener implements Listener{
	private ShortCommands plugin;
	public CommandListener(ShortCommands plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(final PlayerCommandPreprocessEvent event) {
		if(event.isCancelled() || event.getMessage().split(" ")[0].equalsIgnoreCase("/stop")
				|| event.getMessage().split(" ")[0].equalsIgnoreCase("/reload")) {
			return;
		}
		String command = event.getMessage().replaceFirst("/", "");
		boolean success = plugin.getCommandHandler().exec(new CommandWrapper(event.getPlayer(),
				command.split(" ")[0], (String[])ArrayUtils.remove(command.split(" "), 0)));
		if(success) {
			event.setMessage("/cmd-echo");
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(final ServerCommandEvent event) {
		if(event.getCommand().split(" ")[0].equalsIgnoreCase("stop")
				|| event.getCommand().split(" ")[0].equalsIgnoreCase("reload")) {
			return;
		}
		boolean success = plugin.getCommandHandler().exec(new CommandWrapper(event.getSender(),
				event.getCommand().split(" ")[0], (String[])ArrayUtils.remove(event.getCommand().split(" "), 0)));
		if(success) {
			event.setCommand("cmd-echo");
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommandBlock(final BlockRedstoneEvent event) {
		if(event.getNewCurrent() == event.getOldCurrent()) {
			return;
		}
		Block block = event.getBlock();
		if(block.getType().equals(Material.COMMAND)) {
			CommandBlock command_block = (CommandBlock)block.getState();
			if(!command_block.getCommand().trim().isEmpty()
					&& plugin.getCommandHandler().hasCommand(command_block.getCommand().split(" ")[0])) {
				command_block.setCommand("cmd-run " + command_block.getCommand());
				command_block.update();
			}
		}
	}
}
