package com.shortcircuit.shortcommands.command;

import org.bukkit.command.CommandSender;

/**
 * @author ShortCircuit908
 * 
 */
public class CommandWrapper {
    private final CommandSender command_sender;
    private final String command_label;
    private final String[] args;
    protected CommandWrapper(CommandSender command_sender, String command_label, String[] args) {
        this.command_sender = command_sender;
        this.command_label = command_label;
        this.args = args;
    }
    /**
     * Gets the associated CommandSender
     *
     * @return The sender of the command
     */
    public CommandSender getSender() {
        return command_sender;
    }
    /**
     * Gets the name of the associated command
     *
     * @return The name of the command
     */
    public String getCommandLabel() {
        return command_label;
    }
    /**
     * Gets the arguments of the associated command
     *
     * @return The arguments of the command
     */
    public String[] getArgs() {
        return args;
    }
    @Override
    public String toString() {
        String as_string = "/" + command_label;
        for(String arg : args) {
            as_string += " " + arg;
        }
        return as_string;
    }
}
