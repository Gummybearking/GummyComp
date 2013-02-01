package GComp.main.me.max.bukkit.utilities;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import GComp.main.me.max.bukkit.CompMain;

public class PermissionHandler {
	public static Permission permission = null;
	public static CompMain main;

	public PermissionHandler (CompMain cm) {
		main = cm;
		setupPermissions();
	}
	
	private static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = main.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if(permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	
	public boolean hasPermission(Player toCheck, String perm) {
		return toCheck.isOp() || permission.has(toCheck, perm);
	}
}
