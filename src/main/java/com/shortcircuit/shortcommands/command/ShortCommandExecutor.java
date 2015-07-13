package com.shortcircuit.shortcommands.command;

import com.shortcircuit.shortcommands.ShortCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author ShortCircuit908
 */
public class ShortCommandExecutor implements CommandExecutor {
	private final ShortCommandHandler<ShortCommand> command_handler = ShortCommands.getInstance().getCommandHandler();

	public ShortCommandExecutor() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return command_handler.exec(new CommandWrapper(sender, label, args));
	}
}
