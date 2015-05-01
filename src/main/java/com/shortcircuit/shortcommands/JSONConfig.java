package com.shortcircuit.shortcommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.command.ShortCommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/**
 * This class is strictly for internal command handling, and should not be used under any
 * circumstances
 *
 * @author ShortCircuit908
 */
public final class JSONConfig {
	private final Type set_type = new TypeToken<Set<String>>() {
	}.getType();
	private ShortCommands plugin;
	private ShortCommandHandler<ShortCommand> command_handler;
	private final File config_file;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	protected JSONConfig(ShortCommands plugin) {
		this.plugin = plugin;
		this.command_handler = plugin.getCommandHandler();
		config_file = new File(plugin.getDataFolder() + "/disabled_commands.json");
		try {
			plugin.getDataFolder().mkdirs();
			if (config_file.createNewFile()) {
				FileWriter writer = new FileWriter(config_file);
				writer.append("[]");
				writer.flush();
				writer.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadConfig() {
		try {
			String json_string = "";
			Scanner scanner = new Scanner(config_file);
			while (scanner.hasNextLine()) {
				json_string += scanner.nextLine();
			}
			scanner.close();
			Set<String> disabled_commands = gson.fromJson(json_string, set_type);
			for (String command : disabled_commands) {
				command_handler.addInitialDisabledCommand(command);
			}
			plugin.setSaveDisabledCommands(true);
		}
		catch (IOException e) {
			e.printStackTrace();
			plugin.setSaveDisabledCommands(false);
		}
		catch (JsonParseException e) {
			String message = "[ShortCommands] Error while parsing " + config_file.getName() + ": "
					+ e.getLocalizedMessage();
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + message);
			plugin.setSaveDisabledCommands(false);
		}
	}

	@SuppressWarnings("unchecked")
	public void saveConfig() {
		Set<String> disabled_commands = new HashSet<>();
		for (ShortCommand command : command_handler.getCommands()) {
			if (!command.isEnabled()) {
				disabled_commands.add(command.getOwningPlugin() + ":" + command.getClass().getSimpleName());
			}
		}
		try {
			FileWriter writer = new FileWriter(config_file);
			writer.append(gson.toJson(disabled_commands));
			writer.flush();
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
