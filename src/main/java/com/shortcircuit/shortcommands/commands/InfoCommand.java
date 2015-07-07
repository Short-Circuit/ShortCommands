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

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.ChatColor;

/**
 * @author ShortCircuit908
 */
public class InfoCommand extends ShortCommand {
	private ShortCommandHandler<ShortCommand> command_handler;

	public InfoCommand(ShortCommands owning_plugin) {
		super(owning_plugin);
		this.command_handler = owning_plugin.getCommandHandler();
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CONSOLE;
	}

	@Override
	public String[] getCommandNames() {
		return new String[]{"cmd-info"};
	}

	@Override
	public String getPermissions() {
		return "*";
	}

	@Override
	public String[] getHelp() {
		return new String[]{
				ChatColor.AQUA + "View a command's pertinent information",
				ChatColor.AQUA + "Ender the command name as shown with /cmd-list",
				ChatColor.AQUA + "/${command} <command>",
				ChatColor.AQUA + "Ex: /${command} ShortCommands:InfoCommand"};
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
			throw new TooFewArgumentsException(command.getCommandLabel());
		}
		if (command_handler.hasCommand(command.getArg(0))) {
			ShortCommand short_command = command_handler.getCommand(command.getArg(0));
			assert short_command != null;
			return new String[]{ChatColor.GRAY + "Information for " + ChatColor.WHITE
					+ short_command.getUniqueName() + ChatColor.GRAY + ":",
					ChatColor.GOLD + "User type: " + ChatColor.WHITE + short_command.getCommandType(),
					ChatColor.GOLD + "Required permissions: " + ChatColor.WHITE
							+ short_command.getPermissions(),
					ChatColor.GOLD + "Aliases: " + ChatColor.WHITE
							+ ArrayUtils.toString(short_command.getCommandNames()),
					ChatColor.GOLD + "Enabled: " + boolToString(short_command.isEnabled()),
					ChatColor.GOLD + "Can be disabled: " + boolToString(short_command.canBeDisabled()),
			};
		}
		return new String[]{ChatColor.RED + "[ShortCommands] Could not find command with name: "
				+ command.getArg(0)};
	}

	private String boolToString(boolean bool) {
		return bool ? ChatColor.GREEN + "" + bool : ChatColor.RED + "" + bool;
	}
}
