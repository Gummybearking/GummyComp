package GComp.main.me.max.bukkit.MobArena;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;


import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class MobArenaHook implements Listener{
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	
	public static boolean isEnabled;
	
	public void enable(CompMain cm){
		
		if(!cm.getConfig().getBoolean("MobArenaHooks.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		log.log(Level.INFO, "[GummyComp] MobArenaHooks Enabled");
		isEnabled = true;
	}
	
	//JoinMessage
	@EventHandler
	public void onArenaJoin(ArenaPlayerJoinEvent event) {
		if(!isEnabled) return;
		
		String name  = event.getPlayer().getName();
		String arena = event.getArena().arenaName();
		
		String message = main.getConfig().getString("MobArenaHooks.join-message");
			   message = ChatColor.translateAlternateColorCodes('&', message);
			   message = message.replace("%player%", name);
			   message = message.replace("%arena%",  arena);

		main.getServer().broadcastMessage(message);

	}
}
