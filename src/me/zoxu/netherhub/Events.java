package me.zoxu.netherhub;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;
import org.bukkit.plugin.Plugin;

public class Events implements Listener{
	
	private static Advancement netherAdvancement;
	
	private static Plugin plugin;
	
	public static void startupMethod(Plugin p) {
		plugin = p;
		
		Iterator<Advancement> i = plugin.getServer().advancementIterator();
		
		while(i.hasNext()) {
			Advancement holder = i.next();
			if(holder.getKey().getKey().equalsIgnoreCase("story/enter_the_nether")) {
				netherAdvancement = holder;
				break;
			}
		}
		
	}
	
	private Location getNetherHub() {
		Location location = new Location(null, 0, 0, 0);
		location.setWorld(plugin.getServer().getWorld(plugin.getConfig().getString("NetherHub.location.world")));
		location.setX(plugin.getConfig().getDouble("NetherHub.location.x"));
		location.setY(plugin.getConfig().getDouble("NetherHub.location.y"));
		location.setZ(plugin.getConfig().getDouble("NetherHub.location.z"));
		location.setYaw((float) plugin.getConfig().getDouble("NetherHub.location.yaw"));
		location.setPitch((float) plugin.getConfig().getDouble("NetherHub.location.pitch"));
		
		return location;
	}
	
	@EventHandler
	public void onPortalEnter(PlayerPortalEvent e) {
		Player p = e.getPlayer();
		boolean advancement = false;
		
		if(p.getAdvancementProgress(netherAdvancement).isDone()) {
			advancement = true;
		}
		
		if(e.getCause().compareTo(TeleportCause.NETHER_PORTAL) == 0) {
		
			if(e.getPlayer().getUniqueId().toString().equals(Utils.getOwnerPortalConfig(plugin, e.getFrom()))){
				//If they are in the overworld when triggering this event
				if(e.getFrom().getWorld().equals(plugin.getServer().getWorld(plugin.getConfig().getString("Overworld.name")))) {
					e.setCanCreatePortal(false);
					//Check if player has entered the Nether
					if(!advancement) {
						e.setTo(this.getNetherHub());
						Utils.setPlayerLocConfig(plugin, true, p, e.getFrom());
						
						return;
					}
					
					//Checks if they have a location set for the Netherworld otherwise will allow default portal teleportation
					if(Utils.getPlayerLocConfig(plugin, false, p) == null) {
						return;
					}
					
					//Sends them to last nether location
					Utils.setPlayerLocConfig(plugin, true, p, e.getFrom());
					e.setTo(Utils.getPlayerLocConfig(plugin, false, p));
					
					return;
				}
				
				
				//If they are in the nether world when triggering this event
				if(e.getFrom().getWorld().equals(plugin.getServer().getWorld(plugin.getConfig().getString("NetherWorld.name")))) {
					e.setCanCreatePortal(false);
					
					//Checks if they have a location set for the Overworld otherwise will allow default portal teleportation
					if(Utils.getPlayerLocConfig(plugin, true, p) == null) {
						return;
					}
					
					//Sends them to last overworld location
					Utils.setPlayerLocConfig(plugin, false, p, e.getFrom());
					e.setTo(Utils.getPlayerLocConfig(plugin, true, p));
					
					return;
				}
			}else {
				
				//Gets which player this portal belongs too
				String uuid = Utils.getOwnerPortalConfig(plugin, e.getFrom());
				boolean overworld = true;
				
				//Checks what world the portal is in
				if(e.getFrom().getWorld().equals(plugin.getServer().getWorld(plugin.getConfig().getString("NetherWorld.name")))){
					overworld = false;
				}else if(e.getFrom().getWorld().equals(plugin.getServer().getWorld(plugin.getConfig().getString("Overworld.name")))){
					
				}else {
					return;
				}
				
				if(uuid == null) return;
				
				e.setTo(Utils.getPlayerLocConfig(plugin, overworld, Bukkit.getPlayer(UUID.fromString(uuid))));
				return;
			}
		}
	}
	
	@EventHandler
	public void onPortalCreate(PortalCreateEvent e) {
		if(e.getReason() == CreateReason.FIRE || e.getReason() == CreateReason.NETHER_PAIR) {
			
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				
				if(!plugin.getConfig().getString("Overworld.name").equalsIgnoreCase(e.getWorld().getName()) || !plugin.getConfig().getString("NetherWorld.name").equalsIgnoreCase(e.getWorld().getName())) {
					return;
				}
				ArrayList<BlockState> blocks = (ArrayList<BlockState>) e.getBlocks();
				
				Utils.setNewPortalConfig(plugin, p, e.getWorld(), blocks);
			}
			
		}
		
		
	}
	
}
