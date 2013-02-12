package GComp.main.me.max.bukkit.worldkick;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class WorldKick implements Listener {
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static int time;
	public static World kickworld;
	
	public static boolean isEnabled;
	
	public static HashMap<String, Integer> kickmap = new HashMap<String, Integer>();
	
	public void enable(CompMain cm){
		if (cm.getConfig().getConfigurationSection("WorldKick") == null) {
			setupConfig(cm);
		}
		if(!cm.getConfig().getBoolean("WorldKick.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		log.log(Level.INFO, "[GummyComp] WorldKick Enabled");
		isEnabled = true;
		kickworld = Bukkit.getWorld(main.getConfig().getString("WorldKick.world"));
		time      = main.getConfig().getInt("WorldKick.world-timer");
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		timer();
	}
	
	public void setupConfig(CompMain cm) {
		cm.getConfig().set("WorldKick.enable",      true);
		cm.getConfig().set("WorldKick.world",       "Example_World");
		cm.getConfig().set("WorldKick.world-timer", 60);
		cm.saveConfig();
	}
	
	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(kickworld.getName())) {	
			kickmap.put(event.getPlayer().getName(), time);
		} else if (kickmap.containsKey(event.getPlayer().getName())) {
			kickmap.remove(event.getPlayer().getName());
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(kickworld.getName())) {	
			kickmap.put(event.getPlayer().getName(), time);
		}
	}
	public void timer() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				timer();
				for (Object o : kickmap.keySet().toArray()) {
					String playername = (String) o;
					int ptimer = kickmap.get(playername);
					if (ptimer <= 0) {
						kickmap.remove(playername);
						Player toKick = Bukkit.getPlayer(playername);
						if (toKick == null) {
							return;
						}
						toKick.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
						return;
					}
					kickmap.put(playername, ptimer - 1);
					
				}
			}
		}, 20);
		
	}
}
