package com.shortcircuit.shortcommands.command;

import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

/**
 * This class is strictly for internal command handling, and should not be used under any
 * circumstances
 * 
 * @author ShortCircuit908
 */
public class ShortHelpTopic extends HelpTopic{
	protected ShortHelpTopic(String command_name, ShortCommand command) {
		amendedPermission = command.getPermissions();
		name = command_name;
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
		fullText = fullText.replace("${command}", name).trim();
	}
	@Override
	public boolean canSee(CommandSender player) {
		return player.hasPermission(amendedPermission);
	}
}
