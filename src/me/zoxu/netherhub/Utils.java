package me.zoxu.netherhub;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Utils {
	
	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static String strip(String s) {
		return ChatColor.stripColor(s);
	}
	
	public static String colorTheme(String s) {
		return Utils.color("&5&lNetherHub &d" + s);
	}
	
	public static void helpMenu(Player p) {
		String[] menu = {"&d-------------","&5Help Menu", "&d/NetherHub location", "&d&oSets location of NetherHub.", "&d/NetherHub overworld <name>",
				"&d&oSets the overworld this plugin will use.", "&d/NetherHub netherworld <name>", "&d&oSets the nether world this plugin will use.", "&d-------------"};
		
		for(String message : menu) {
			p.sendMessage(Utils.color(message));
		}
		
		return;
	}
	
	public static void setPlayerLocConfig(Plugin plugin, boolean overworld, Player p, Location l) {
		String path = "";
		
		if(overworld) {
			path = "Player." + p.getUniqueId().toString() + ".location.overworld"; 
		}else {
			path = "Player." + p.getUniqueId().toString() + ".location.netherworld"; 
		}
		
		plugin.getConfig().set(path + ".world", l.getWorld().getName());
		plugin.getConfig().set(path + ".x", l.getX());
		plugin.getConfig().set(path + ".y", l.getY());
		plugin.getConfig().set(path + ".z", l.getZ());
		plugin.getConfig().set(path + ".yaw", l.getYaw());
		plugin.getConfig().set(path + ".pitch", l.getPitch());
		
	}
	
	public static Location getPlayerLocConfig(Plugin plugin, boolean overworld, Player p) {
		Location newLoc = new Location(null, 0, 0, 0, 0, 0);
		String path = "";
		
		if(overworld) {
			path = "Player." + p.getUniqueId().toString() + ".location.overworld"; 
		}else {
			path = "Player." + p.getUniqueId().toString() + ".location.netherworld"; 
		}
		
		if(!plugin.getConfig().contains(path)) {
			return null;
		}
		
		newLoc.setWorld(Bukkit.getWorld(plugin.getConfig().getString(path + ".world")));
		newLoc.setX(plugin.getConfig().getDouble(path + ".x"));
		newLoc.setY(plugin.getConfig().getDouble(path + ".y"));
		newLoc.setZ(plugin.getConfig().getDouble(path + ".z"));
		newLoc.setYaw((float) plugin.getConfig().getDouble(path + ".yaw"));
		newLoc.setPitch((float) plugin.getConfig().getDouble(path + ".pitch"));
		
		return newLoc;
	}
	
	public static void setNewPortalConfig(Plugin plugin, Player p, World w, ArrayList<BlockState> blocks) {
		
		//plugin.getConfig().set("Portal." + p.getUniqueId().toString() , "");
		for(BlockState b : blocks) {
			plugin.getConfig().set("Portal." + w.getName() + "." + b.getX() + b.getY() + b.getZ(), p.getUniqueId().toString());
		}
		
	}
	
	public static String getOwnerPortalConfig(Plugin plugin, Location l) {
		if(plugin.getConfig().contains("Portal." + l.getWorld().getName() + "." + l.getBlockX() + l.getBlockY() + l.getBlockZ())) {
			return plugin.getConfig().getString("Portal." + l.getWorld().getName() + "." + l.getBlockX() + l.getBlockY() + l.getBlockZ());
		}
		return null;
	}
}
