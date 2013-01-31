package GComp.main.me.max.bukkit.dyncompass;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class DynCompass implements Listener{
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static DynmapAPI api;
	
	public static boolean isEnabled;
	
	public void enable(CompMain cm){
		
		if(!cm.getConfig().getBoolean("DynCompass.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		Plugin grab    = main.getServer().getPluginManager().getPlugin("dynmap");
		api            = (DynmapAPI)grab;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		log.log(Level.INFO, "[GummyComp] DynCompass Enabled");
		isEnabled = true;
	}
	
	public void dynHide(Player player) {
		if(!api.getPlayerVisbility(player.getName())){
			//if invisible
			return;
		}
		api.setPlayerVisiblity(player,false);
		player.sendMessage("You are no longer on the dynmap!");
		log.log(Level.INFO, "[DynCompass] " +  player.getName() + " should be not the Dynmap");

	}
	public void dynShow(Player player) {
		if(api.getPlayerVisbility(player.getName())){
			//if visible
			return;
		}
		api.setPlayerVisiblity(player,true);
		player.sendMessage(ChatColor.RED + "You are now on the dynmap!");
		log.log(Level.INFO, "[DynCompass] " +  player.getName() + " should be on the Dynmap");

	}
	
	/**
	 * Beginning of events
	 */
	
	@EventHandler
	(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent event) {
		if(!isEnabled) {
			return;
		}
		if(event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.COMPASS) {
			
			HumanEntity human = event.getWhoClicked();
			if(!(human instanceof Player)){
				return;
			}
			Player holder = (Player) human;
			if(!permission.hasPermission(holder, main.getConfig().getString("DynCompass.permission"))) {
				return;
			}
			
			dynShow(holder);

			
		} else {
			HumanEntity human = event.getWhoClicked();
			if(!(human instanceof Player)){
				return;
			}
			Player holder = (Player) human;				
			
			if(holder.getName() == null) {
				log.log(Level.SEVERE, "\n\n\n\n\n[DynCompass] Player is null");
			}
				
			if(!permission.hasPermission(holder, main.getConfig().getString("DynCompass.permission"))) {
				return;
			}
				
			if(holder.getInventory().contains(Material.COMPASS)) {
				return;
			}
				
			dynHide(holder);

		}
	}
	@EventHandler
	(priority = EventPriority.LOWEST)
	public void onPlayerDrop(PlayerDropItemEvent event) {
		if(!isEnabled) {
			return;
		}
		if(event.getItemDrop().getItemStack().getType() == Material.COMPASS) {
			if(event.getPlayer() == null) {
				log.log(Level.SEVERE, "[DynCompass] Player is null");
			}
			if(!event.getPlayer().getInventory().contains(Material.COMPASS)) {
				dynHide(event.getPlayer());

			}
		}
	}
	@EventHandler
	(priority = EventPriority.LOWEST)
	public void onPlayerPickup(PlayerPickupItemEvent event) {
		if(!isEnabled) {
			return;
		}
		if(event.getItem().getItemStack().getType() == Material.COMPASS) {
			if(event.getPlayer() == null) {
				log.log(Level.SEVERE, "[DynCompass] Player is null");
			}
			dynShow(event.getPlayer());
			
		}
	}
	@EventHandler
	(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if(!isEnabled) {
			return;
		}
		if(event.getPlayer().getInventory().contains(Material.COMPASS)) {
			dynShow(event.getPlayer());
			return;
		}
		dynHide(event.getPlayer());
	}
}
