package GComp.main.me.max.bukkit.spellcontrol;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.events.SpellCastEvent;


import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.ItemUtilities;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class SpellControl implements Listener{
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static ItemUtilities itemutils;
	public static MagicSpells magicspells;
	public static boolean isEnabled;
	public static HashMap<String,List<String>> whitelist;
	public void enable(CompMain cm) {
		
		if(!cm.getConfig().getBoolean("SpellControl.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		itemutils      = new ItemUtilities(cm);
		log.log(Level.INFO, "[GummyComp] SpellControl Enabled");
		isEnabled      = true;
		Plugin grab    = main.getServer().getPluginManager().getPlugin("MagicSpells");
		magicspells    = (MagicSpells) grab;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		addConfigValues();
		
		
		ConfigurationSection spellSec          = main.getConfig().getConfigurationSection("SpellControl.whitelist");
		whitelist = new HashMap<String,List<String>>();
		
		for(Object o : spellSec.getKeys(false)) {
			String world = (String) o;
			whitelist.put(world, spellSec.getStringList(world));
		}
	}
	public void addConfigValues() {
		if(main.getConfig().getString("SpellControl.enable") == null) {
			main.getConfig().set("SpellControl.enable", true);
			String[] list = {"DerpSpell", "HerpSpell"};
			main.getConfig().set("SpellControl.whitelist.Example_World", list);
		}
	}
	@EventHandler
	public void SpellEvent(SpellCastEvent event) {
		for(String world : whitelist.keySet()) {
			if(!whitelist.get(world).contains(event.getSpell().getName())
					&& whitelist.containsKey(event.getCaster().getWorld().getName())) {
				event.setCancelled(true);
			}
		}
		
	}
}
