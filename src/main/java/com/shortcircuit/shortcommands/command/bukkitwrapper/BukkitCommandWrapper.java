package com.shortcircuit.shortcommands.command.bukkitwrapper;

import com.shortcircuit.shortcommands.ShortCommands;
import com.shortcircuit.shortcommands.command.CommandType;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.ShortCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShortCircuit908
 */
public class BukkitCommandWrapper extends ShortCommand {
	private final Command command;
	private final String[] command_names;
	private final String[] help;

	public BukkitCommandWrapper(Command command) {
		super(command.getName(), ShortCommands.getInstance());
		this.command = command;
		List<String> aliases = new ArrayList<>();
		for(String alias : command.getAliases()) {
			aliases.add(alias);
		}
		aliases.add(command.getLabel());
		aliases.add(command.getName());
		command_names = aliases.toArray(new String[0]);
		help = new String[] {
				ChatColor.AQUA + ChatColor.stripColor(command.getDescription()),
				ChatColor.AQUA + ChatColor.stripColor(command.getUsage())
		};
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.ANY;
	}

	@Override
	public String[] getCommandNames() {
		return command_names;
	}

	@Override
	public String getPermissions() {
		return command.getPermission();
	}

	@Override
	public String[] getHelp() {
		return help;
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}

	@Override
	public String getUniqueName(){
		return "Bukkit:" + command.getClass().getSimpleName();
	}

	@Override
	public String[] exec(CommandWrapper command_wrapper) {
		command.execute(command_wrapper.getSender(), command_wrapper.getCommandLabel(), command_wrapper.getArgs());
		return new String[] {};
	}
}
