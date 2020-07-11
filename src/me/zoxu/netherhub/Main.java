package me.zoxu.netherhub;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	
	@Override
	public void onEnable() {
		
		System.out.println("Starting up NetherHub by Zoxu...");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		Events.startupMethod(this);
		
		this.saveDefaultConfig();
		
	}
	
	@Override
	public void onDisable() {
		this.saveConfig();
		System.out.println("Shutting down NetherHub by Zoxu...");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("NetherHub")) {
			
			//Checks if a player is running this command
			if(!(sender instanceof Player)) {
				sender.sendMessage("This command is for players only!");
				return true;
			}
			
			//Checking for correct permissions to use this command
			if(!sender.hasPermission("netherhub.use")) {
				sender.sendMessage(Utils.colorTheme("You do not have permission to use this command!"));
				return true;
			}
			
			Player p = (Player) sender;
			
			if(args.length == 0) {
				Utils.helpMenu(p);
				return true;
			}
			
			//Set the location of the Nether Hub
			if(args[0].equalsIgnoreCase("location")) {
				
				this.getConfig().set("NetherHub.location.world", p.getWorld().getName());
				this.getConfig().set("NetherHub.location.x", p.getLocation().getX());
				this.getConfig().set("NetherHub.location.y", p.getLocation().getY());
				this.getConfig().set("NetherHub.location.z", p.getLocation().getZ());
				this.getConfig().set("NetherHub.location.yaw", p.getLocation().getYaw());
				this.getConfig().set("NetherHub.location.pitch", p.getLocation().getPitch());
				this.saveConfig();
				
				p.sendMessage(Utils.colorTheme("Location has been set!"));
				return true;
			}
			
			//Set the name of the overworld World
			if(args[0].equalsIgnoreCase("overworld")){
				this.getConfig().set("Overworld.name", args[1]);
				this.saveConfig();
				
				p.sendMessage(Utils.colorTheme("Overworld has been set!"));
				return true;
			}
			
			//Set the name of the nether world 
			if(args[0].equalsIgnoreCase("netherworld")) {
				this.getConfig().set("NetherWorld.name", args[1]);
				this.saveConfig();
				
				p.sendMessage(Utils.colorTheme("Nether world has been set!"));
				return true;
			}
			
		}
		
		return true;
	}
	
}
