package com.shortcircuit.shortcommands.command;

/**
 * @author ShortCircuit908
 * 
 */
public enum CommandType {
    PLAYER(0),
    CONSOLE(1),
    BLOCK(2),
    ANY(3);
    
    private final int ordinal;
    
    private CommandType(int ordinal) {
        this.ordinal = ordinal;
    }
    
    public static int getOrdinal(CommandType type) {
        return type.ordinal;
    }
}
