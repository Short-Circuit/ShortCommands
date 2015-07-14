package com.shortcircuit.shortcommands.command;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

/**
 * @author ShortCircuit908
 */
public class CommandTabCompleteEvent extends Event {
	protected static final HandlerList handlers = new HandlerList();
	private final ArrayList<String> suggestions = new ArrayList<>();
	private final CommandSender sender;
	private final String command_label;
	private final String[] args;

	public CommandTabCompleteEvent(CommandSender sender, String command_label, String[] args) {
		this.sender = sender;
		this.command_label = command_label;
		this.args = args;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public ArrayList<String> getSuggestions() {
		return suggestions;
	}

	public CommandSender getSender() {
		return sender;
	}

	public String getCommandLabel() {
		return command_label;
	}

	public String[] getArgs() {
		return args;
	}

	public String getArg(int i) {
		return args.length < i ? args[i] : null;
	}
}
