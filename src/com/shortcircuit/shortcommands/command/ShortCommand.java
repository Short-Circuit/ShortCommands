package com.shortcircuit.shortcommands.command;

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
public abstract class ShortCommand {
    /**
     * Gets the type of command
     *
     * @return The type of command
     */
    public abstract CommandType getCommandType();
    /**
     * Gets the names of the command
     * <p>
     * A command can have any number of names. They will all be checked for when the command is run
     *
     * @return An array containing the names of the command
     */
    public abstract String[] getCommandNames();
    /**
     * Gets the permissions required to run the command
     *
     * @return The required permissions
     */
    public abstract String getPermissions();
    /**
     * Gets the help topics of the command
     * <p>
     * Each element of the array is one line of the help message
     *
     * @return An array containing the help messages of the command
     */
    public abstract String[] getHelp();
    /**
     * Runs the command
     * <p>
     * This method is called whenever the command is run. All elements of the return value are sent
     * to the user who ran the command. This is the appropriate place to provide any user feedback
     * from the command
     * @throws TooFewArgumentsException, TooManyArgumentsException,
     * InvalidArgumentException, NoPermissionException,
     * PlayerOnlyException, ConsoleOnlyException,
     * BlockOnlyException
     *
     * @return An array containing user feedback
     */
    public abstract String[] exec(CommandWrapper command) throws
    TooFewArgumentsException, TooManyArgumentsException,
    InvalidArgumentException, NoPermissionException,
    PlayerOnlyException, ConsoleOnlyException,
    BlockOnlyException;
}
