package GComp.main.me.max.bukkit.epicspawner;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;
import GComp.main.me.max.bukkit.utilities.YAMLUtils;

public class EpicSpawner {
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static YAMLUtils yamlutils;
	public static long time;
	
	public static boolean isEnabled;
	
	public void enable(CompMain cm){
		
		if(!cm.getConfig().getBoolean("EpicSpawner.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		yamlutils      = new YAMLUtils(main);
		log.log(Level.INFO, "[GummyComp] EpicSpawner Enabled");
		isEnabled = true;
		yamlutils.getEpicSpawnerTimer().options().copyDefaults();

		if(yamlutils.getEpicSpawnerTimer().getString("timer") == null) {
			time = cm.getConfig().getLong("EpicSpawner.timer");
		} else {
			time = yamlutils.getEpicSpawnerTimer().getLong("timer");
		}
		
		commandTimer();

	}
	public void bossCommand(Player player) {
		String message = main.getConfig().getString("EpicSpawner.command-message");
		message = ChatColor.translateAlternateColorCodes('&', message);
		
		//seperate into time units
		long minutes  = time / 60 + (time%60);
		
		message = message.replace("%time%", String.valueOf(minutes));
		player.sendMessage(message);
	}
	public void commandTimer() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				try {
					if(!isEnabled) {		
						Bukkit.getServer().getScheduler().cancelTasks(main);
						return;
					}
					time = time - 1;
					if(time <= 0) {
						runACommand();
						time = main.getConfig().getLong("EpicSpawner.timer");
						return;
					}
					yamlutils.getEpicSpawnerTimer().set("timer", time);
					yamlutils.saveEpicSpawnerTimer();
					
					//messaging
					
					if(time%main.getConfig().getLong("EpicSpawner.broadcast-interval") == 0) {
						String message = main.getConfig().getString("EpicSpawner.broadcast-message");
						message = ChatColor.translateAlternateColorCodes('&', message);
						
						//seperate into time units
						long hours   = time / 3600;
						long minutes = (time % 3600)/60;
						long seconds = (time % 3600)%60;
						String timer = "";
						if (hours != 0 && minutes != 0 && seconds != 0) {
							timer = hours + " hours, " + 
									minutes + " minutes, and " + 
									seconds + " seconds";
						}
						if (hours != 0 && minutes != 0 && seconds == 0) {
							timer = hours + " hours and " + 
									minutes + " minutes";
						}
						if (hours != 0 && minutes == 0 && seconds != 0) {
							timer = hours + " hours and " + 
									seconds + " seconds";
						}
						if (hours == 0 && minutes != 0 && seconds != 0) {
							timer = minutes + " minutes and " + 
									seconds + " seconds";
						}
						if (hours != 0 && minutes == 0 && seconds == 0) {
							timer = hours + " hours";  
						}
						if (hours == 0 && minutes == 0 && seconds != 0) {
							timer = seconds + " seconds";
						}
						if (hours == 0 && minutes != 0 && seconds == 0) {
							timer = minutes + " minutes";
						}
						message = message.replace("%time%", String.valueOf(timer));
						Bukkit.getServer().broadcastMessage(message);
					}
					commandTimer();
				} catch(Exception e) {
					e.printStackTrace();
					return;
				}
				
			}
		},20);
	}
	
	public void runACommand() {
		Random r = new Random();
		if(main == null) {
			log.log(Level.SEVERE, "\n\n\n\n\nMain is null");
			
		}
		Set<String> commands = main.getConfig().getConfigurationSection("EpicSpawner.commands").getKeys(false);
		int random = r.nextInt(commands.size());
		int key    = random + 1;
		List<String> toRun   = main.getConfig().getStringList("EpicSpawner.commands." + key);
		if(toRun == null || toRun.isEmpty()) {
			log.log(Level.SEVERE,  "\n\n\n\n" + commands.toString());
		}
		for(String s : toRun) {
			Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(),s);
		}
	}
}
