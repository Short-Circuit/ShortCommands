package com.shortcircuit.shortcommands.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
public class ShortCommandHandler<T extends ShortCommand>{
    private final List<T> command_list = new ArrayList<>();
    /**
     * Registers the provided ShortCommand
     * @param command The command to register
     * @see ShortCommand
     */
    public void registerCommand(T command) {
        command_list.add(command);
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[ShortCommands] Registered command \""
                + command.getCommandNames()[0] + "\"");
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
     * @param  command The CommandWrapper created from a server command
     * @return  Whether or not the command ran successfully
     * @see CommandWrapper
     */
    protected boolean exec(CommandWrapper command) {
        boolean success = false;
        try {
            check_loop : for(T command_check : command_list) {
                for(String command_name : command_check.getCommandNames()) {
                    if(command_name.equalsIgnoreCase(command.getCommandLabel())) {
                        Bukkit.getLogger().info(command.getSender().getName() + " issued ShortCommand: "
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
                                && !(command.getSender() instanceof BlockCommandSender)) {
                            throw new BlockOnlyException();
                        }
                        if(command_check.getPermissions() == null
                                || command.getSender().hasPermission(command_check.getPermissions())) {
                            if (command.getArgs().length > 0 && command.getArgs()[0].equalsIgnoreCase("help")) {
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
                    + command.getCommandNames()[0]);
            return command;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ShortCommands] Could not unregister command: "
                + command.getCommandNames()[0]);
        return null;
    }
}
