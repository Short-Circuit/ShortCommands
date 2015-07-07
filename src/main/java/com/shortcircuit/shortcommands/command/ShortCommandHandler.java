package com.shortcircuit.shortcommands.command;

import com.google.common.collect.ImmutableSet;
import com.shortcircuit.shortcommands.command.bukkitwrapper.BukkitCommandWrapper;
import com.shortcircuit.shortcommands.exceptions.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author ShortCircuit908
 */
@SuppressWarnings("unused")
public final class ShortCommandHandler<T extends ShortCommand> {
	private final Set<T> command_list = new HashSet<>();
	private final Set<String> disabled_commands = new HashSet<>();
	private final Class<T> command_type = (Class<T>)ShortCommand.class;

	public ShortCommandHandler(){

	}

	/**
	 * Registers the provided {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 *
	 * @param command The command to register
	 */
	public void registerCommand(T command) throws CommandExistsException {
		String command_name = command.getUniqueName();
		List<String> conflicting_commands = new ArrayList<>();
		for (String name : command.getCommandNames()) {
			T match_command = getCommand(name);
			if (match_command != null) {
				if (match_command instanceof BukkitCommandWrapper) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ShortCommands] Replacing wrapped command with " + command_name);
					unregisterCommand(match_command);
				}
				else {
					conflicting_commands.add(name);
				}
			}
		}
		if (conflicting_commands.size() > 0) {
			String message = "";
			for (String conflict : conflicting_commands) {
				message += ", " + command_name + ":" + conflict;
			}
			message = message.replaceFirst(", ", "");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ShortCommands] Unable to register "
			                                      + "command: " + command_name);
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ShortCommands] The command conflicts "
			                                      + "with the following commands: " + message);
			throw new CommandExistsException(conflicting_commands.toArray(new String[]{}));
		}
		command_list.add(command);
		if (disabled_commands.contains(command_name) && command.canBeDisabled()) {
			command.setEnabled(false);
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[ShortCommands] Registered command: "
		                                      + command_name);
	}

	/**
	 * Wrap a Bukkit command for handling by ShortCommands
	 */
	@SuppressWarnings("unchecked")
	public void wrapPluginCommand(BukkitCommandWrapper wrapper) {
		command_list.add((T) wrapper);
	}

	/**
	 * Allow a wrapped command to be handled by Bukkit
	 */
	@SuppressWarnings("SuspiciousMethodCalls")
	public void unwrapPluginCommand(BukkitCommandWrapper wrapper) {
		command_list.remove(wrapper);
	}

	/**
	 * Attempts to register a collection of ShortCommands
	 * <p>
	 * The command manager will attempt to register each command individually. If it cannot be registered,
	 * the command is added to a set of other ShortCommands that could not be registered, which is then
	 * returned.
	 *
	 * @param commands The ShortCommands to add
	 * @return A set containing all commands that could not be added
	 */
	@SuppressWarnings("unchecked")
	public Set<T> registerCommands(T... commands) {
		Set<T> unregistered_commands = new HashSet<>();
		for (T command : commands) {
			try {
				registerCommand(command);
			}
			catch (CommandExistsException e) {
				unregistered_commands.add(command);
			}
		}
		return ImmutableSet.copyOf(unregistered_commands);
	}

	/**
	 * Attempts to register a collection of ShortCommands
	 * <p>
	 * The command manager will attempt to register each command individually. If it cannot be registered,
	 * the command is added to a set of other ShortCommands that could not be registered, which is then
	 * returned.
	 *
	 * @param commands The ShortCommands to add
	 * @return A set containing all commands that could not be added
	 */
	public Set<T> registerCommands(Iterable<T> commands) {
		Set<T> unregistered_commands = new HashSet<>();
		for (T command : commands) {
			try {
				registerCommand(command);
			}
			catch (CommandExistsException e) {
				unregistered_commands.add(command);
			}
		}
		return ImmutableSet.copyOf(unregistered_commands);
	}

	public Set<T> registerCommands(Plugin plugin, String package_name) {
		Reflections reflections = new Reflections(package_name, plugin.getClass().getClassLoader());
		Set<Class<? extends T>> command_classes = reflections.getSubTypesOf(command_type);
		Set<T> unregistered_commands = new HashSet<>();
		for (Class<? extends T> command_class : command_classes) {
			T command = null;
			try {
				try {
					Constructor<? extends T> constructor = command_class.getDeclaredConstructor(plugin.getClass());
					command = constructor.newInstance(plugin);
				}
				catch (NoSuchMethodException e) {
					Constructor<? extends T> constructor = command_class.getDeclaredConstructor();
					command = constructor.newInstance();
				}
				registerCommand(command);
			}
			catch(ReflectiveOperationException e){
				e.printStackTrace();
			}
			catch (CommandExistsException e) {
				unregistered_commands.add(command);
			}
		}
		return ImmutableSet.copyOf(unregistered_commands);
	}

	/**
	 * Gets a list of all registered {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}s
	 *
	 * @return A set of registered {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}s
	 */
	public Set<T> getCommands() {
		return ImmutableSet.copyOf(command_list);
	}

	/**
	 * Gets a list of registered {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}s belonging to a plugin
	 *
	 * @param plugin The plugin that registered the commands
	 * @return A set of registered {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}s belonging to a plugin
	 */
	public Set<T> getCommands(Plugin plugin) {
		Set<T> commands = new HashSet<>();
		for (T command : command_list) {
			if (command.getOwningPlugin().equals(plugin.getName())) {
				commands.add(command);
			}
		}
		return ImmutableSet.copyOf(commands);
	}

	/**
	 * Gets a list of registered {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}s belonging to a plugin
	 *
	 * @param plugin The name of the plugin that registered the commands
	 * @return A set of registered {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}s belonging to a plugin
	 */
	public Set<T> getCommands(String plugin) {
		Set<T> commands = new HashSet<>();
		for (T command : command_list) {
			if (command.getOwningPlugin().equalsIgnoreCase(plugin)) {
				commands.add(command);
			}
		}
		return ImmutableSet.copyOf(commands);
	}

	/**
	 * Attempts to find and execute a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 * based on the provided {@link com.shortcircuit.shortcommands.command.CommandWrapper CommandWrapper}
	 * <p>
	 * If a command with a matching name is found, the command is executed
	 * and the method returns true. If the command is not found, the method
	 * returns false.
	 * <p>
	 * This method is strictly for internal command handling, and should not be used under any
	 * circumstances
	 *
	 * @param command The {@link com.shortcircuit.shortcommands.command.CommandWrapper CommandWrapper} created from a server command
	 * @return Whether or not the command ran successfully
	 */
	public boolean exec(CommandWrapper command) {
		boolean success = false;
		try {
			check_loop:
			for (T command_check : command_list) {
				for (String command_name : command_check.getCommandNames()) {
					if (command_name.equalsIgnoreCase(command.getCommandLabel())) {
						if (!command_check.isEnabled()) {
							return false;
						}
						String name = command.getSender().getName();
						if (name.equals("@")) {
							CommandBlock command_block = (CommandBlock) ((BlockCommandSender)
									command.getSender()).getBlock().getState();
							name = "CommandBlock at " + command_block.getX() + "," + command_block.getY()
							       + "," + command_block.getZ();
						}
						Bukkit.getLogger().info(name + " issued ShortCommand: "
						                        + command);
						success = true;
						if (command_check.getCommandType().equals(CommandType.CONSOLE)
						    && !(command.getSender() instanceof ConsoleCommandSender)) {
							throw new ConsoleOnlyException();
						}
						if (command_check.getCommandType().equals(CommandType.PLAYER)
						    && !(command.getSender() instanceof Player)) {
							throw new PlayerOnlyException();
						}
						if (command_check.getCommandType().equals(CommandType.BLOCK)
						    && !(command.getSender() instanceof BlockCommandSender)) {
							throw new BlockOnlyException();
						}

						if (PermissionComparator.hasWildcardPermission(command.getSender(),
								command_check.getPermissions())) {
							if (command.getArgs().length > 0
							    && command.getArgs()[0].equalsIgnoreCase("help")) {
								for (String message : command_check.getHelp()) {
									command.getSender().sendMessage(message.replace("${command}",
											command.getCommandLabel()));
								}
								break check_loop;
							}
							for (String message : command_check.exec(command)) {
								command.getSender().sendMessage(ChatColor.AQUA + message);
							}
							break check_loop;
						}
						else {
							throw new NoPermissionException(command_check.getPermissions());
						}
					}
				}
			}
		}
		catch (TooFewArgumentsException | TooManyArgumentsException | InvalidArgumentException
				| NoPermissionException | PlayerOnlyException | ConsoleOnlyException | BlockOnlyException e) {
			command.getSender().sendMessage(ChatColor.RED + e.getMessage());
		}
		catch (Exception e) {
			command.getSender().sendMessage(ChatColor.RED + "An internal error has occurred\n" + ChatColor.RED + "Please check the console");
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * Unregisters the provided {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 *
	 * @param command The command to unregister
	 * @return The command that was unregistered
	 */
	public T unregisterCommand(T command) {
		if (command_list.remove(command)) {
			if (!(command instanceof BukkitCommandWrapper)) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[ShortCommands] Unregistered command: "
				                                      + command.getUniqueName());
			}
			return command;
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ShortCommands] Could not unregister command: "
		                                      + command.getUniqueName());
		return null;
	}

	/**
	 * Check whether a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand} exists
	 *
	 * @param command_name One of the names of the {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}, or the name in the format
	 *                     "OwningPlugin:ClassName"
	 * @return Whether or not the command exists
	 */
	public boolean hasCommand(String command_name) {
		for (T command : command_list) {
			if (command_name.contains(":") && command_name.equalsIgnoreCase(command.getUniqueName())) {
				return true;
			}
			for (String name : command.getCommandNames()) {
				if (name.equalsIgnoreCase(command_name)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand} by its name
	 *
	 * @param command_name One of the names of the {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}, or the name in the format
	 *                     "OwningPlugin:ClassName"
	 * @return The matching {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 */
	public T getCommand(String command_name) {
		for (T command : command_list) {
			if (command_name.contains(":") && command_name.equalsIgnoreCase(command.getUniqueName())) {
				return command;
			}
			for (String name : command.getCommandNames()) {
				if (name.equalsIgnoreCase(command_name)) {
					return command;
				}
			}
		}
		return null;
	}

	/**
	 * Gets a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand} by its class
	 *
	 * @param clazz The class of the {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 * @return The matching {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 */
	public T getCommand(Class<T> clazz) {
		for (T command : command_list) {
			if (command.getClass().equals(clazz)) {
				return command;
			}
		}
		return null;
	}

	/**
	 * Disables a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 * <p>
	 * When a command is disabled, any calls to the command will be entirely ignored, and will be passed
	 * along to Bukkit's default command handling. The command itself is not unregistered, to prevent name
	 * conflicts should another plugin attempt to register a command with a matching name.
	 *
	 * @return Whether or not the command was successfully disabled
	 */
	public boolean disableCommand(T command) throws PersistentCommandException {
		if (command == null) {
			return false;
		}
		if (!command.canBeDisabled()) {
			throw new PersistentCommandException();
		}
		if (command_list.contains(command)) {
			command.setEnabled(false);
			return true;
		}
		return false;
	}

	/**
	 * Disables a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 * <p>
	 * When a command is disabled, any calls to the command will be entirely ignored, and will be passed
	 * along to Bukkit's default command handling. The command itself is not unregistered, to prevent name
	 * conflicts should another plugin attempt to register a command with a matching name.
	 *
	 * @return Whether or not the command was successfully disabled
	 */
	public boolean disableCommand(String command_name) throws PersistentCommandException {
		T command = getCommand(command_name);
		if (command == null) {
			return false;
		}
		if (!command.canBeDisabled()) {
			throw new PersistentCommandException();
		}
		command.setEnabled(false);
		return true;
	}

	/**
	 * Disables a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 * <p>
	 * When a command is disabled, any calls to the command will be entirely ignored, and will be passed
	 * along to Bukkit's default command handling. The command itself is not unregistered, to prevent name
	 * conflicts should another plugin attempt to register a command with a matching name.
	 *
	 * @return Whether or not the command was successfully disabled
	 */
	public boolean disableCommand(Class<T> clazz) throws PersistentCommandException {
		T command = getCommand(clazz);
		if (command == null) {
			return false;
		}
		if (!command.canBeDisabled()) {
			throw new PersistentCommandException();
		}
		command.setEnabled(false);
		return true;
	}

	/**
	 * Enables a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 * <p>
	 * When a command is disabled, any calls to the command will be entirely ignored, and will be passed
	 * along to Bukkit's default command handling. The command itself is not unregistered, to prevent name
	 * conflicts should another plugin attempt to register a command with a matching name.
	 *
	 * @return Whether or not the command was successfully enabled
	 */
	public boolean enableCommand(T command) {
		if (command == null) {
			return false;
		}
		if (command_list.contains(command)) {
			command.setEnabled(true);
			return true;
		}
		return false;
	}

	/**
	 * Enables a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 * <p>
	 * When a command is disabled, any calls to the command will be entirely ignored, and will be passed
	 * along to Bukkit's default command handling. The command itself is not unregistered, to prevent name
	 * conflicts should another plugin attempt to register a command with a matching name.
	 *
	 * @return Whether or not the command was successfully enabled
	 */
	public boolean enableCommand(String command_name) {
		T command = getCommand(command_name);
		if (command == null) {
			return false;
		}
		command.setEnabled(true);
		return true;
	}

	/**
	 * Enables a {@link com.shortcircuit.shortcommands.command.ShortCommand ShortCommand}
	 * <p>
	 * When a command is disabled, any calls to the command will be entirely ignored, and will be passed
	 * along to Bukkit's default command handling. The command itself is not unregistered, to prevent name
	 * conflicts should another plugin attempt to register a command with a matching name.
	 *
	 * @return Whether or not the command was successfully enabled
	 */
	public boolean enableCommand(Class<T> clazz) {
		T command = getCommand(clazz);
		if (command == null) {
			return false;
		}
		command.setEnabled(true);
		return true;
	}

	/**
	 * Adds a command to be disabled when registered
	 * <p>
	 * This method is strictly for internal command handling, and should not be used under any
	 * circumstances
	 *
	 * @param command_name The given name of the command to be disabled
	 */
	public void addInitialDisabledCommand(String command_name) {
		disabled_commands.add(command_name);
	}

	/**
	 * Registers help topics for a plugin and all its commands
	 * <p>
	 * This method should be called in a plugin's onEnable() method only AFTER all commands have been
	 * registered. Help topics will only be generated for commands that have already been registered at
	 * the time this method is called.
	 *
	 * @param plugin The plugin to register help topics for
	 */
	@SuppressWarnings("unchecked")
	public void registerHelpTopics(Plugin plugin) {
		Set<T> commands = getCommands(plugin);
		for (T command : commands) {
			for (String name : command.getCommandNames()) {
				Bukkit.getServer().getHelpMap().addTopic(new ShortHelpTopic(name, command));
			}
		}
		Bukkit.getServer().getHelpMap().addTopic(new ShortHelpTopic(plugin.getName(),
				(Set<ShortCommand>) commands));
	}
}
