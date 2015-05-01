package com.shortcircuit.shortcommands.command;

import org.bukkit.ChatColor;

/**
 * @author ShortCircuit908
 */
public enum CommandType{
	/**
	 * Represents a player-only command
	 * <p/>
	 * Player-only commands may only be run by players. Examples of player-only commands include those
	 * that require the command sender's location, inventory, health, etc.
	 * <p/>
	 * <br>Ex:
	 * <br>  /sethome
	 * <br>  /tpa
	 * <br>  /warp
	 * <br>  /heal
	 */
	PLAYER(ChatColor.LIGHT_PURPLE),
	/**
	 * Represents a console-only command
	 * <p/>
	 * Console-only commands may only be run from the server console. Examples of console-only commands
	 * include commands which are solely meant for debugging purposes or for obtaining server information.
	 */
	CONSOLE(ChatColor.AQUA),
	/**
	 * Represents a block-only command
	 * <p/>
	 * Block-only commands may only be run by command blocks. Examples of block-only commands include
	 * commands which solely provide world mechanics.
	 * <p/>
	 * <br>Ex:
	 * <br>  /testfor
	 */
	BLOCK(ChatColor.YELLOW),
	/**
	 * Represents a universal command
	 * <p/>
	 * Universal commands can be run by any user. These commands do not require specific aspects from the
	 * user.
	 * <p/>
	 * <br>Ex:
	 * <br>  /time
	 * <br>  /weather
	 * <br>  /whisper
	 * <br>  /say
	 * <br>  /broadcast
	 */
	ANY(ChatColor.BLUE);

	private final ChatColor color;

	private CommandType(ChatColor color){
		this.color = color;
	}

	public static ChatColor getColor(CommandType type){
		return type.color;
	}

	public String toString(){
		return color + name();
	}
}
