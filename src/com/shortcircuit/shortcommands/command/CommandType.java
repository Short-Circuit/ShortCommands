package com.shortcircuit.shortcommands.command;

/**
 * @author ShortCircuit908
 * 
 */
public enum CommandType {
	/**
	 * Represents a player-only command
	 * <p>
	 * Player-only commands may only be run by players. Examples of player-only commands include those
	 * that require the command sender's location, inventory, health, etc.
	 * <p>
	 * <br>Ex:
	 * <br>  /sethome
	 * <br>  /tpa
	 * <br>  /warp
	 * <br>  /heal
	 */
	PLAYER(0),
	/**
	 * Represents a console-only command
	 * <p>
	 * Console-only commands may only be run from the server console. Examples of console-only commands
	 * include commands which are solely meant for debugging purposes or for obtaining server information.
	 */
	CONSOLE(1),
	/**
	 * Represents a block-only command
	 * <p>
	 * Block-only commands may only be run by command blocks. Examples of block-only commands include
	 * commands which solely provide world mechanics.
	 * <p>
	 * <br>Ex:
	 * <br>  /testfor
	 */
	BLOCK(2),
	/**
	 * Represents a universal command
	 * <p>
	 * Universal commands can be run by any user. These commands do not require specific aspects from the
	 * user.
	 * <p>
	 * <br>Ex:
	 * <br>  /time
	 * <br>  /weather
	 * <br>  /whisper
	 * <br>  /say
	 * <br>  /broadcast
	 */
	ANY(3);
	
	private final int ordinal;
	
	private CommandType(int ordinal) {
		this.ordinal = ordinal;
	}
	
	public static int getOrdinal(CommandType type) {
		return type.ordinal;
	}
}
