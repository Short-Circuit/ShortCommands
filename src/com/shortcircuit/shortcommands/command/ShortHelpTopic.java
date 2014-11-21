package com.shortcircuit.shortcommands.command;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

/**
 * This class is strictly for internal command handling, and should not be used under any
 * circumstances
 * 
 * @author ShortCircuit908
 */
public class ShortHelpTopic extends HelpTopic {
	protected ShortHelpTopic(String plugin_name, Set<ShortCommand> commands) {
		amendedPermission = "";
		name = plugin_name;
		shortText = "All commands for " + plugin_name;
		fullText = ChatColor.GRAY + "Below is a list of all " + plugin_name + " commands:";
		for(ShortCommand command : commands) {
			String truncated = "\n" + ChatColor.GOLD + "/" + command.getCommandNames()[0] + ": " + ChatColor.WHITE
					+ ChatColor.stripColor(command.getHelp()[0]);
			if(truncated.length() > 55) {
				truncated = StringUtils.abbreviate(truncated, 55);
			}
			fullText += truncated;
		}
	}
	protected ShortHelpTopic(String command_name, ShortCommand command) {
		amendedPermission = command.getPermissions();
		name = "/" + command_name;
		shortText = command.getHelp()[0];
		fullText = "";
		for(String line : command.getHelp()) {
			fullText += line + "\n";
		}
		if(command.getCommandNames().length > 1) {
			fullText += "Aliases: ";
			String aliases = "";
			for(String alias : command.getCommandNames()) {
				aliases += ", " + alias;
			}
			fullText += aliases.replaceFirst(", ", "");
		}
		fullText = fullText.replace("${command}", command_name).trim();
	}
	@Override
	public boolean canSee(CommandSender player) {
		return player.hasPermission(amendedPermission);
	}
}
