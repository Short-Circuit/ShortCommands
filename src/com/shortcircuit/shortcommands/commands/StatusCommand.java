package com.shortcircuit.shortcommands.commands;

import org.bukkit.ChatColor;

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

/**
 * @author ShortCircuit908
 * 
 */
public class StatusCommand extends ShortCommand{
	private ShortCommandHandler<ShortCommand> command_handler;
	public StatusCommand(ShortCommands owning_plugin) {
		super(owning_plugin);
		this.command_handler = owning_plugin.getCommandHandler();
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.CONSOLE;
	}
	
	@Override
	public String[] getCommandNames() {
		return new String[] {"cmd-list"};
	}
	
	@Override
	public String getPermissions() {
		return "*";
	}
	
	@Override
	public String[] getHelp() {
		return new String[] {ChatColor.AQUA + "Display a list of registered ShortCommands"
				+ ChatColor.AQUA + "/${command}"};
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
		String message = "";
		for(ShortCommand short_command : command_handler.getCommands()) {
			message += ", " + (short_command.isEnabled() ? ChatColor.GREEN : ChatColor.RED)
					+ short_command.getOwningPlugin() + ":" + short_command.getClass().getSimpleName();
		}
		return new String[] {message.replaceFirst(",", "")};
	}
	
}
