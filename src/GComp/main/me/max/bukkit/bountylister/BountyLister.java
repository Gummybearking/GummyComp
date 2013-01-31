package GComp.main.me.max.bukkit.bountylister;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.herocraftonline.dthielke.herobounty.HeroBounty;
import com.herocraftonline.dthielke.herobounty.bounties.Bounty;
import com.herocraftonline.dthielke.herobounty.bounties.BountyManager;


import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class BountyLister {
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static BountyManager bounty;
	
	public static boolean isEnabled;
	
	public void enable(CompMain cm){
		
		if(!cm.getConfig().getBoolean("BountyLister.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		log.log(Level.INFO, "[GummyComp] BountyLister Enabled");
		isEnabled      = true;
		Plugin grab    = main.getServer().getPluginManager().getPlugin("HeroBounty");
		if(grab != null) {
			bounty         = ((HeroBounty) grab).getBountyManager();
		}else{
			for(Plugin plug : main.getServer().getPluginManager().getPlugins()) {
				if(plug.getName().equals("HeroBounty")) {
					
					bounty = ((HeroBounty) plug).getBountyManager();
				}
			}
		}
		main.getServer().getScheduler().scheduleSyncRepeatingTask(main, 
			new Runnable(){
				public void run(){
					String message = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("BountyLister.message"));
					int size = 0;
					for(Bounty bount : bounty.getBounties()) {
						Player target = Bukkit.getPlayer(bount.getTarget());
						if(target == null) continue;
						if(target.isOnline()){
							size++;
						}
					}
					message = message.replace("%amount%", String.valueOf(size));
					if(size > 0) {
						main.getServer().broadcastMessage(message);
					}
				}
			}, 0, main.getConfig().getLong("BountyLister.timer") * 20
		);
	}

}
