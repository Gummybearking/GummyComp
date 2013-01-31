package GComp.main.me.max.bukkit.silentheal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.ItemUtilities;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class SilentHeal {
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static ItemUtilities itemutils;
	public static String node;
	public static boolean isEnabled;
	
	public void enable(CompMain cm) {
		
		if(!cm.getConfig().getBoolean("SilentHeal.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		itemutils      = new ItemUtilities(cm);
		log.log(Level.INFO, "[GummyComp] SilentHeal Enabled");
		isEnabled      = true;
		node           = cm.getConfig().getString("SilentHeal.permission");
		
	}
	public void sheal(Player healer, Player toHeal) {
		if(!healer.hasPermission(node)) {
			return;
		}
		toHeal.setHealth(20);
	}
}
