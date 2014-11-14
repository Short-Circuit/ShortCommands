package com.shortcircuit.shortcommands.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.shortcircuit.shortcommands.exceptions.BlockOnlyException;
import com.shortcircuit.shortcommands.exceptions.CommandExistsException;
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
public class ShortCommandHandler<T extends ShortCommand>{
	private final List<T> command_list = new ArrayList<>();
	/**
	 * Registers the provided ShortCommand
	 * @param command The command to register
	 * @see ShortCommand
	 */
	public void registerCommand(T command) throws CommandExistsException{
		List<String> conflicting_commands = new ArrayList<String>();
		for(String name : command.getCommandNames()) {
			if(hasCommand(name)) {
				conflicting_commands.add(name);
			}
		}
		if(conflicting_commands.size() > 0) {
			String message = "";
			for(String conflict : conflicting_commands) {
				message += ", " + getCommand(conflict).getOwningPlugin() + ":" + conflict;
			}
			message = message.replaceFirst(", ", "");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ShortCommands] Unable to register "
					+ "command: " + command.getOwningPlugin() + ":" + command.getCommandNames()[0]);
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ShortCommands] The command conflicts "
					+ "with the following commands: " + message);
			throw new CommandExistsException(conflicting_commands.toArray(new String[] {}));
		}
		command_list.add(command);
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[ShortCommands] Registered command: "
				+ command.getOwningPlugin() + ":" + command.getCommandNames()[0]);
	}
	/**
	 * Gets a list of all registered ShortCommands
	 * @return List of registered ShortCommands
	 * @see ShortCommand
	 */
	public List<T> getCommands(){
		return command_list;
	}
	/**
	 * Attempts to find and execute a ShortCommand based on the provided CommandWrapper
	 * <p>
	 * If a command with a matching name is found, the command is executed
	 * and the method returns true. If the command is not found, the method
	 * returns false.
	 *
	 * This class is strictly for internal command handling, and should not be used under any
	 * circumstances
	 * @param  command The CommandWrapper created from a server command
	 * @return  Whether or not the command ran successfully
	 * @see CommandWrapper
	 */
	public boolean exec(CommandWrapper command) {
		boolean success = false;
		try {
			check_loop : for(T command_check : command_list) {
				for(String command_name : command_check.getCommandNames()) {
					if(command_name.equalsIgnoreCase(command.getCommandLabel())) {
						String name = command.getSender().getName();
						if(name.equals("@")) {
							CommandBlock command_block = (CommandBlock)((BlockCommandSender)
									command.getSender()).getBlock().getState();
							name = "CommandBlock at " + command_block.getX() + "," + command_block.getY()
									+ "," + command_block.getZ();
						}
						Bukkit.getLogger().info(name + " issued ShortCommand: "
								+ command);
						success = true;
						if(command_check.getCommandType().equals(CommandType.CONSOLE)
								&& !(command.getSender() instanceof ConsoleCommandSender)) {
							throw new ConsoleOnlyException();
						}
						if(command_check.getCommandType().equals(CommandType.PLAYER)
								&& !(command.getSender() instanceof Player)) {
							throw new PlayerOnlyException();
						}
						if(command_check.getCommandType().equals(CommandType.BLOCK)
								&& !command.getSender().getName().equals("@")) {
							throw new BlockOnlyException();
						}
						if(command_check.getPermissions() == null
								|| command.getSender().hasPermission(command_check.getPermissions())) {
							if (command.getArgs().length > 0
									&& command.getArgs()[0].equalsIgnoreCase("help")) {
								for(String message : command_check.getHelp()) {
									command.getSender().sendMessage(message);
								}
								break check_loop;
							}
							for(String message : command_check.exec(command)) {
								command.getSender().sendMessage(message);
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
		catch (TooFewArgumentsException e) {
			command.getSender().sendMessage(ChatColor.RED + "Too few arguments.");
			command.getSender().sendMessage(ChatColor.RED + "Try /" + command.getCommandLabel() + " help");
		}
		catch (TooManyArgumentsException e) {
			command.getSender().sendMessage(ChatColor.RED + "Too many arguments.");
			command.getSender().sendMessage(ChatColor.RED + "Try /" + command.getCommandLabel() + " help");
		}
		catch (InvalidArgumentException e) {
			command.getSender().sendMessage(ChatColor.RED + "Invalid argument \"" + e.getArg());
			command.getSender().sendMessage(ChatColor.RED + "Try /" + command.getCommandLabel() + " help");
		}
		catch (NoPermissionException e) {
			command.getSender().sendMessage(ChatColor.RED + "Insufficient permissions");
		}
		catch (PlayerOnlyException e) {
			command.getSender().sendMessage(ChatColor.RED + "This command is player-only");
		}
		catch (ConsoleOnlyException e) {
			command.getSender().sendMessage(ChatColor.RED + "This command is console-only");
		}
		catch (BlockOnlyException e) {
			command.getSender().sendMessage(ChatColor.RED + "This command is block-only");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * Unregisters the provided ShortCommand
	 * @param command The command to unregister
	 * @see ShortCommand
	 */
	public T unregisterCommand(T command) {
		if(command_list.remove(command)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[ShortCommands] Unregistered command: "
					+ command.getOwningPlugin() + ":" + command.getCommandNames()[0]);
			return command;
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ShortCommands] Could not unregister command: "
				+ command.getOwningPlugin() + ":" + command.getCommandNames()[0]);
		return null;
	}
	/**
	 * Check whether a ShortCommand exists
	 * @param command_name One of the names of the ShortCommand
	 * @see ShortCommand
	 */
	public boolean hasCommand(String command_name) {
		for(ShortCommand command : command_list) {
			for(String name : command.getCommandNames()) {
				if(name.equalsIgnoreCase(command_name)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Gets a ShortCommand by its name
	 * @param command_name One of the names of the ShortCommand
	 * @see ShortCommand
	 */
	public ShortCommand getCommand(String command_name) {
		for(ShortCommand command : command_list) {
			for(String name : command.getCommandNames()) {
				if(name.equalsIgnoreCase(command_name)) {
					return command;
				}
			}
		}
		return null;
	}
}
