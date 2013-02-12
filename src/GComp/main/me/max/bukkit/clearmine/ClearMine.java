package GComp.main.me.max.bukkit.clearmine;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class ClearMine {
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static String node;
	public static int maxradius;
	public static boolean isEnabled;
	
	public void enable(CompMain cm) {
		main = cm;
		setupConfig();

		if (!cm.getConfig().getBoolean("ClearMine.enable")) {
			return;
		}
		
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		
		log.log(Level.INFO, "[GummyComp] ClearMine Enabled");
		
		isEnabled      = true;
		node           = cm.getConfig().getString("ClearMine.permission");
		maxradius      = cm.getConfig().getInt("ClearMine.max-radius");
	}
	public void setupConfig() {
		if(!main.getConfig().isConfigurationSection("ClearMine")) {
			main.getConfig().set("ClearMine.enable", true);
			main.getConfig().set("ClearMine.permission", "ClearMine.use");
			main.getConfig().set("ClearMine.max-radius", 5);
		}
		
	}
	public void clearMine(Player clearer, int radius) {
		
		if (permission.hasPermission(clearer, node)) {
			if(radius > maxradius) {
				clearer.sendMessage(ChatColor.RED + "Radius is too large!");
				return;
			}
			for(Item onGround : clearer.getLocation().getWorld().getEntitiesByClass(Item.class)) {
				if(onGround.getItemStack().getType() == Material.COBBLESTONE) {
					if(onGround.getLocation().distance(clearer.getLocation()) <= radius) {
						onGround.remove();
					}
				}
			}
			clearer.sendMessage(ChatColor.GOLD + "Ground cleared!");
		}else {
			clearer.sendMessage(ChatColor.RED + "You do not have permission!");
		}
	}
}
