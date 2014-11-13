package com.shortcircuit.shortcommands.command;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.shortcircuit.shortcommands.ShortCommands;

/**
 * @author ShortCircuit908
 * 
 */
public class CommandListener implements Listener{
    private ShortCommands plugin;
    public CommandListener(ShortCommands plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        if(event.isCancelled()) {
            return;
        }
        String command = event.getMessage().replaceFirst("/", "");
        boolean success = plugin.getCommandHandler().exec(new CommandWrapper(event.getPlayer(),
                command.split(" ")[0], (String[])ArrayUtils.remove(command.split(" "), 0)));
        if(success) {
            event.setCancelled(true);
            event.setMessage("cmd-echo");
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(final ServerCommandEvent event) {
        boolean success = plugin.getCommandHandler().exec(new CommandWrapper(event.getSender(),
                event.getCommand().split(" ")[0], (String[])ArrayUtils.remove(event.getCommand().split(" "), 0)));
        if(success) {
            event.setCommand("cmd-echo");
        }
    }
}
