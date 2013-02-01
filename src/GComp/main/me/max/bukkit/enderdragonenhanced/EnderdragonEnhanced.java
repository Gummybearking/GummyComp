package GComp.main.me.max.bukkit.enderdragonenhanced;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.ItemUtilities;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class EnderdragonEnhanced implements Listener {
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static ItemUtilities itemutils;

	public static boolean isEnabled;
	
	public void enable(CompMain cm) {
		
		if(!cm.getConfig().getBoolean("EnderdragonEnhanced.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		itemutils      = new ItemUtilities(cm);
		log.log(Level.INFO, "[GummyComp] EnderdragonEnhanced Enabled");
		isEnabled      = true;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);

		
	}
	@EventHandler
	public void onEnderDeath(EntityDeathEvent event) {
		if(!(event.getEntity() instanceof EnderDragon) && !main.getConfig().getBoolean("EnderdragonEnhanced.testing")) {
			return;
		}
		if(!event.getEntity().getLocation().getWorld().getName().toLowerCase().contains("end")) {
			return;
		}
		if(!isEnabled) {
			return;
		}
		event.getDrops().clear();
		List<ItemStack> drops = getDrops();
		String message;
		if(event.getEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
			EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
			if(ede.getEntity() instanceof Player) {
				message = main.getConfig().getString("EnderdragonEnhanced.reward-list.top-line-player").replace("%player%", ((Player)ede.getDamager()).getName());
			}else {
				message = main.getConfig().getString("EnderdragonEnhanced.reward-list.top-line-envi");
			}
		}else{
			message = main.getConfig().getString("EnderdragonEnhanced.reward-list.top-line-envi");
		}
		
		main.getServer().broadcastMessage(message);
		for(ItemStack stack : drops) {
			log.log(Level.SEVERE, "Dropping a(n) " + stack.getType());
			event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), stack);
			main.getServer().broadcastMessage(main.getConfig().getString("EnderdragonEnhanced.reward-list.list-prefix"));
		}
		
	}
	
	
	
	
	public List<ItemStack> getDrops() {		
		List<String> passedFirst = new LinkedList<String>();
		for(String s : getAllPossible()){
			Random firstParse = new Random();
			int itemChance =  2;
			if(Integer.valueOf( ItemUtilities.getChance( s ) ) == 100){
				itemChance = 1;
				passedFirst.add(s);
			}else if(Integer.valueOf( ItemUtilities.getChance( s ) ) > 50)  {
				itemChance =  Math.round(100 / (100-Integer.valueOf( ItemUtilities.getChance( s ) ) ) );
				int passFirst = firstParse.nextInt(itemChance - 1);
				if(passFirst != 0){
					passedFirst.add(s);
				}
			}else if(Integer.valueOf( ItemUtilities.getChance( s ) ) < 50) {
				itemChance =  Math.round( 100 / Integer.valueOf( ItemUtilities.getChance( s ) ) );
				int passFirst = firstParse.nextInt(itemChance - 1);
				if(passFirst == 0) {
					passedFirst.add(s);
				}
			}else{
				int passFirst = firstParse.nextInt(1);
				if(passFirst == 0) passedFirst.add(s);
			}
		}
		Collections.shuffle(passedFirst);
		List<ItemStack> ret = new LinkedList<ItemStack>();
		for(String itemS : passedFirst){
			log.log(Level.SEVERE, "Dropping: " + itemS);
			ret.add(itemutils.ItemFromConfig(itemS));
		}
		
		String s;
		Random ran = new Random();
		int choice = ran.nextInt(getAllRares().size() - 1);
		s = getAllRares().get(choice);
		Random firstParse = new Random();
		int itemChance =  2;
		if(Integer.valueOf( ItemUtilities.getChance( s ) ) == 100){
				itemChance = 1;
				passedFirst.add(s);
			
			
			}else if(Integer.valueOf( ItemUtilities.getChance( s ) ) > 50) 
			{
				itemChance =  Math.round(100 / (100-Integer.valueOf( ItemUtilities.getChance( s ) ) ) );
				int passFirst = firstParse.nextInt(itemChance - 1);
				if(passFirst != 0)
				{
					passedFirst.add(s);
				
				}
			}else if(Integer.valueOf( ItemUtilities.getChance( s ) ) < 50)
			{
				itemChance =  Math.round( 100 / Integer.valueOf( ItemUtilities.getChance( s ) ) );
				int passFirst = firstParse.nextInt(itemChance - 1);
				if(passFirst == 0)
				{
					passedFirst.add(s);
				}

		}else{
			int passFirst = firstParse.nextInt(1);
			if(passFirst == 0) passedFirst.add(s);
		}
		ret.add(itemutils.ItemFromConfig(s));
		log.log(Level.SEVERE, "Dropping: " + s);
		return ret;
	}
	
		
		
	public static List<String> getAllPossible() {
		return main.getConfig().getStringList("EnderdragonEnhanced.drops");
	}
	public static List<String> getAllRares() {
		return main.getConfig().getStringList("EnderdragonEnhanced.rare-drops");
	}
	public String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
}
