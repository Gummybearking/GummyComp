package GComp.main.me.max.bukkit.welcomer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import GComp.main.me.max.bukkit.CompMain;

public class Welcomer implements Listener{
	static CompMain main;
	static Logger log;
	
	public static boolean isEnabled;
	public static String lastJoined;
	public static String welcomeMessage;
	
	public void enable(CompMain cm){
		
		if(!cm.getConfig().getBoolean("Welcomer.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		welcomeMessage = cm.getConfig().getString("Welcomer.welcome-message");
		
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		log.log(Level.INFO, "[GummyComp] Welcomer Enabled");
		isEnabled = true;
	}
	
	public void welcome(Player welcomer) {
		if(lastJoined == null) {
			welcomer.sendMessage(ChatColor.RED + "Noone to be welcomed!");
		}
		if(welcomeMessage == null) {
			welcomeMessage = main.getConfig().getString("Welcomer.welcome-message");
			log.log(Level.INFO,  "\n\n\n\n" + welcomeMessage);
		}
		if(welcomer == null) {
			log.log(Level.INFO,  "\n\n\n\nDahell?");
		}
		welcomer.chat(welcomeMessage.replace("%player%", lastJoined));
	}
	
	
	
	
	/**
	 * Beginning of Events
	 */
	
	@EventHandler
		(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!event.getPlayer().hasPlayedBefore()) {
			lastJoined = event.getPlayer().getName();
		}
	}
}
