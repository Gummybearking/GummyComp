package GComp.main.me.max.bukkit.EpicAbilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.ThaH3lper.com.EpicBoss;
import me.ThaH3lper.com.Api.BossDeathEvent;
import me.ThaH3lper.com.Api.BossSkillEvent;
import me.ThaH3lper.com.Boss.Boss;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import GComp.main.me.max.bukkit.CompMain;
import GComp.main.me.max.bukkit.utilities.ItemUtilities;
import GComp.main.me.max.bukkit.utilities.PermissionHandler;

public class EpicAbilities implements Listener{
	public static CompMain main;
	public static Logger log;
	public static PermissionHandler permission;
	public static ItemUtilities itemutils;
	public static EpicBoss epicboss;
	public static boolean isEnabled;
	public static HashMap<Block, Material> unbreakable = new HashMap<Block,Material>();
	public static List<String> blacklist = new LinkedList<String>();
	public static List<String> bossblacklist = new LinkedList<String>();

	public void enable(CompMain cm) {
		
		if(!cm.getConfig().getBoolean("EpicAbilities.enable")) {
			return;
		}
		
		main           = cm;
		log            = cm.getLog();
		permission     = new PermissionHandler(cm);
		itemutils      = new ItemUtilities(cm);
		log.log(Level.INFO, "[GummyComp] EpicAbilities Enabled");
		isEnabled      = true;
		Plugin grab    = main.getServer().getPluginManager().getPlugin("EpicBossRecoded");
		epicboss       = (EpicBoss) grab;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		if (epicboss == null) {
			for(Plugin plugin : main.getServer().getPluginManager().getPlugins()) {
				if(plugin.getName().equalsIgnoreCase("epicboss")) {
					epicboss = (EpicBoss) plugin;
				}
			}
		}
		
		
		setupConfig();
		
		blacklist = cm.getConfig().getStringList("EpicAbilities.blacklist");
		bossblacklist = cm.getConfig().getStringList("EpicAbilities.boss-blacklist");

	}
	public void setupConfig() {
		if (main.getConfig().getString("EpicAbilities.enable") == null) {
			main.getConfig().set("EpicAbilities.enable", true);		
		}
		if (main.getConfig().getString("EpicAbilities.drops") == null) {
			main.getConfig().set("EpicAbilities.drops", "[]");		
		}
		if (main.getConfig().getString("EpicAbilities.blacklist") == null) {
			main.getConfig().set("EpicAbilities.blacklist", "[]");	
		}
		if (main.getConfig().getString("EpicAbilities.boss-blacklist") == null) {
			main.getConfig().set("EpicAbilities.boss-blacklist", "[]");	
		}
		main.saveConfig();
	}
	@EventHandler
	public void onBossDeath(BossDeathEvent event) {
		//Get our nasty feller
		Boss boss = null;
		for (Object bossobj : epicboss.BossList.toArray()) {
			if (((Boss) bossobj).getName().equals(event.getBossName())){
				boss = (Boss) bossobj;
			}
		}
		//Drop static loot
		dropStaticLoot(event);
		//Make Sure the config contains the Boss-Name
		if (!main.getConfig().contains("EpicAbilities." + event.getBossName())) {
			log.log(Level.SEVERE, "[EpicAbilities] A Boss Has No Drops! Boss: " + event.getBossName());
			return;
		}
		
		for (Player toGive : boss.getLocation().getWorld().getPlayers()) {
			if (blacklist.contains(toGive.getName())) {
				return;
			}
			//Drop common loot
			giveCommonLoot(event, toGive);
		}
		
		giveRareLoot(event, boss.getLocation().getWorld());
	}
	public void giveCommonLoot(BossDeathEvent event, Player toGive) {
		List<ItemStack> items = new LinkedList<ItemStack>();
		
		for(String toConvert : main.getConfig().getStringList("EpicAbilities." + event.getBossName() + ".common-loot")) {
			if(itemutils.convertChanceToPercent(ItemUtilities.getChance(toConvert))) {
					items.add(itemutils.ItemFromConfig(toConvert));
			}
		}
		//Check to see if its list is empty
		if(items.isEmpty()) {
			log.log(Level.SEVERE, "[EpicAbilities] A Boss Has No Common-Loot! Boss: " + event.getBossName());
			return;
		}
		for(ItemStack item : items) {
			toGive.getInventory().addItem(item);
		}
	}
	public void giveRareLoot(BossDeathEvent event, World world) {
		HashMap<String, Integer> rolls = new HashMap<String,Integer>();
		Random random = new Random();
		List<ItemStack> items = new LinkedList<ItemStack>();
		
		for (String toConvert : main.getConfig().getStringList("EpicAbilities." + event.getBossName() + ".rare-loot")) {
			if (itemutils.convertChanceToPercent(ItemUtilities.getChance(toConvert))) {
					items.add(itemutils.ItemFromConfig(toConvert));
			}
		}
		//Check to see if its list is empty
		if(items.isEmpty()) {
			log.log(Level.SEVERE, "[EpicAbilities] A Boss Has No Rare-Loot! Boss: " + event.getBossName());
			return;
		}
		
		for (Player playerInWorld : world.getPlayers()) {
			int roll = 0;
			
			do {
				roll = random.nextInt(99) + 1;
			} while (rolls.containsValue(roll));
			
			rolls.put(playerInWorld.getName(), roll);
		}
		HashMap<String, Integer> round = new HashMap<String,Integer>();
		for (ItemStack item : items) {
			if(round.isEmpty()) {
				round.putAll(rolls);
			}
			String next = "";
			int    nextValue = 0;
			for(String toGive : round.keySet()) {
				int v = rolls.get(toGive);
				if (v > nextValue && !blacklist.contains(toGive)) {
					next = toGive;
					nextValue = v;
				}
			}
			Bukkit.getPlayer(next).getInventory().addItem(item);
			Bukkit.getPlayer(next).sendMessage(ChatColor.GOLD + "Rolling for Boss Loot...");
			Bukkit.getPlayer(next).sendMessage(ChatColor.GOLD + "You Rolled " + nextValue + "!");

			round.remove(next);
		}
	}
	public void dropStaticLoot(BossDeathEvent event) {
		List<ItemStack> items = new LinkedList<ItemStack>();
		Boss boss = null;
		if (bossblacklist.contains(event.getBossName())) {
			return;
		}
		for (String toConvert : main.getConfig().getStringList("EpicAbilities.drops")) {
			items.add(itemutils.ItemFromConfig(toConvert));
		}
		for (Object bossobj : epicboss.BossList.toArray()) {
			if (((Boss) bossobj).getName().equals(event.getBossName())){
				boss = (Boss) bossobj;
			}
		}
		for (ItemStack item : items) {
			for (Player toGive : boss.getLocation().getWorld().getPlayers()){
				if (!blacklist.contains(toGive.getName())) {
					toGive.getInventory().addItem(item);
				}
				
			}
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(unbreakable.containsKey(event.getBlock())){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onBossAbility(BossSkillEvent event) {
		epicboss.api.addNewSkill("return");
		epicboss.api.addNewSkill("web");
		if(event.getSkillName().equals("return")) {
			for(Object bossobj : epicboss.BossList.toArray()) {
				Boss boss = (Boss) bossobj;
				if(boss.getName().equals(event.getBossName())){
					event.getBoss().teleport(boss.getSpawnLocation());
					
				}
			}
		}
		if(event.getSkillName().equals("web")) {
			Block term = event.getBoss().getLocation().getBlock();
            int radius = 2;
            final List<Block> blocks = new LinkedList<Block>();
            for (int x = term.getX() - radius; x < term.getX() + radius; x++)
            {
            	for (int z = term.getZ() - radius; z < term.getZ() + radius; z++)
                {
                	if(term.getWorld().getBlockAt(x,term.getY(),z).getType() != Material.AIR) {
                		continue;
                	}
            		unbreakable.put(term.getWorld().getBlockAt(x,term.getY(),z), term.getWorld().getBlockAt(x,term.getY(),z).getType());
            		blocks.add(term.getWorld().getBlockAt(x,term.getY(),z));
                	term.getWorld().getBlockAt(x,term.getY(),z).setType(Material.WEB);
                }
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
    			public void run() {
    				for(Block b : (Block[]) blocks.toArray()) {
    					b.setType(Material.AIR);
    					unbreakable.remove(b);
    					blocks.remove(b);
    				}
    			}
    		},60 * 20);
    	
		}
		
	}
	
}
