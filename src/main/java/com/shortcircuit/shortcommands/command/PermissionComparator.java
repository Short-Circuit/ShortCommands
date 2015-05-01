package com.shortcircuit.shortcommands.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @author ShortCircuit908
 */
public class PermissionComparator{
	public static boolean hasWildcardPermission(CommandSender user, String permission){
		if(permission == null || permission.trim().isEmpty() || user.hasPermission(permission)){
			return true;
		}
		String[] nodes = permission.split("\\.");
		for(String node : nodes){
			Bukkit.getLogger().info(node);
		}
		String appended_nodes = "";
		for(int i = 0; i < nodes.length - 1; i++){
			appended_nodes += nodes[i] + ".";
			if(user.hasPermission(appended_nodes + "*")){
				return true;
			}
		}
		return false;
	}
}
