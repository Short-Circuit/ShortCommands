package com.shortcircuit.shortcommands;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.command.ShortCommandHandler;


/**
 * This class is strictly for internal command handling, and should not be used under any
 * circumstances
 * 
 * @author ShortCircuit908
 * 
 */
public class JSONConfig {
	private ShortCommands plugin;
	private ShortCommandHandler<ShortCommand> command_handler;
	private final File config_file;
	protected JSONConfig(ShortCommands plugin) {
		this.plugin = plugin;
		this.command_handler = plugin.getCommandHandler();
		config_file = new File(plugin.getDataFolder() + "/disabled_commands.json");
		try {
			plugin.getDataFolder().mkdirs();
			if(config_file.createNewFile()) {
				FileWriter writer = new FileWriter(config_file);
				writer.append("[]");
				writer.flush();
				writer.close();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void loadConfig() {
		try {
			FileReader reader = new FileReader(config_file);
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray)parser.parse(reader);
			for(Object object : array.toArray()) {
				command_handler.addInitialDisabledCommand((String)object);
			}
			reader.close();
			plugin.setSaveDisabledCommands(true);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ParseException e) {
			String message = "[ShortCommands] Error while parsing " + config_file.getName() + ": Unexpected ";
			switch(e.getErrorType()) {
			case ParseException.ERROR_UNEXPECTED_CHAR:
				message += "character";
				break;
			case ParseException.ERROR_UNEXPECTED_EXCEPTION:
				message += "exception";
				break;
			case ParseException.ERROR_UNEXPECTED_TOKEN:
				message += "token";
				break;
			}
			message += " at position " + e.getPosition() + ": " + e.getUnexpectedObject();
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + message);
			plugin.setSaveDisabledCommands(false);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveConfig() {
		Set<String> disabled_commands = new HashSet<String>();
		for(ShortCommand command : command_handler.getCommands()) {
			if(!command.isEnabled()) {
				disabled_commands.add(command.getOwningPlugin() + ":" + command.getClass().getSimpleName());
			}
		}
		JSONArray array = new JSONArray();
		array.addAll(disabled_commands);
		try {
			FileWriter writer = new FileWriter(config_file);
			writer.append(array.toString().replace(",", ",\n"));
			writer.flush();
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
