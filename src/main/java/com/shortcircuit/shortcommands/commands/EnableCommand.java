package com.shortcircuit.shortcommands.commands;

import com.shortcircuit.shortcommands.ShortCommands;
import com.shortcircuit.shortcommands.command.CommandType;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.command.ShortCommandHandler;
import com.shortcircuit.shortcommands.exceptions.BlockOnlyException;
import com.shortcircuit.shortcommands.exceptions.ConsoleOnlyException;
import com.shortcircuit.shortcommands.exceptions.InvalidArgumentException;
import com.shortcircuit.shortcommands.exceptions.NoPermissionException;
import com.shortcircuit.shortcommands.exceptions.PlayerOnlyException;
import com.shortcircuit.shortcommands.exceptions.TooFewArgumentsException;
import com.shortcircuit.shortcommands.exceptions.TooManyArgumentsException;

import org.bukkit.ChatColor;

/**
 * @author ShortCircuit908
 */
public class EnableCommand extends ShortCommand {
	private ShortCommandHandler<ShortCommand> command_handler;

	public EnableCommand(ShortCommands owning_plugin) {
		super(owning_plugin);
		this.command_handler = owning_plugin.getCommandHandler();
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CONSOLE;
	}

	@Override
	public String[] getCommandNames() {
		return new String[]{"cmd-enable"};
	}

	@Override
	public String getPermissions() {
		return "*";
	}

	@Override
	public String[] getHelp() {
		return new String[]{
				ChatColor.AQUA + "Enable a ShortCommand",
				ChatColor.AQUA + "Enter commands to be enabled in a comma-separated list",
				ChatColor.AQUA + "/${command} <commands...>",
				ChatColor.AQUA + "Ex: /${command} command1,command2,command3"};
	}

	@Override
	public boolean canBeDisabled() {
		return false;
	}

	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		if (command.getArgs().length < 1) {
			throw new TooFewArgumentsException();
		}
		if (command.getArgs().length > 1) {
			throw new TooManyArgumentsException();
		}
		String[] commands = command.getArg(0).split(",");
		for (String command_name : commands) {
			if (command_handler.hasCommand(command_name)) {
				ShortCommand short_command = command_handler.getCommand(command_name);
				assert short_command != null;
				command_handler.enableCommand(short_command);
				command.getSender().sendMessage(ChatColor.AQUA + "[ShortCommands] Enabled command: "
						+ short_command.getUniqueName());
			}
			else {
				command.getSender().sendMessage(ChatColor.RED + "[ShortCommands] Could not find command "
						+ "with name: " + command_name);
			}
		}
		return new String[]{};
	}

}
