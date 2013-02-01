package GComp.main.me.max.bukkit;


import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import GComp.main.me.max.bukkit.EpicAbilities.EpicAbilities;
import GComp.main.me.max.bukkit.MobArena.MobArenaHook;
import GComp.main.me.max.bukkit.bountylister.BountyLister;
import GComp.main.me.max.bukkit.dyncompass.DynCompass;
import GComp.main.me.max.bukkit.enderdragonenhanced.EnderdragonEnhanced;
import GComp.main.me.max.bukkit.epicspawner.EpicSpawner;
import GComp.main.me.max.bukkit.silentheal.SilentHeal;
import GComp.main.me.max.bukkit.spellcontrol.SpellControl;
import GComp.main.me.max.bukkit.welcomer.Welcomer;

public class CompMain extends JavaPlugin {
	public static Logger logger = Logger.getLogger("Minecraft");
	public static Welcomer welcomer;
	public static DynCompass dyncompass;
	public static EpicSpawner epicspawner;
	public static MobArenaHook mobarenahook;
	public static BountyLister bountylister;
	public static EnderdragonEnhanced enderdragonenhanced;
	public static SilentHeal silentheal;
	public static EpicAbilities epicabilities; 
	public static SpellControl spellcontrol;
	
	public void onEnable() {
		PluginDescriptionFile pdffile = this.getDescription();
		CompMain.logger.info(pdffile.getName() + " Has Been Enabled!");
		

		File config = new File(this.getDataFolder(), "config.yml");
		if(!config.exists()){
			this.saveDefaultConfig();
			System.out.println("[GummyComp] No config.yml detected, config.yml created");

		}
		if(getConfig() == null) System.out.println("\nNULL CONFIG?\n");
		loadPlugins();
	}
	
	
	public void loadPlugins() {
		//Welcomer
		welcomer = new Welcomer();
		welcomer.enable(this);
		//DynCompass
		dyncompass = new DynCompass();
		dyncompass.enable(this);
		//EpicSpawner
		epicspawner = new EpicSpawner();
		epicspawner.enable(this);
		//MobArenaHooks
		mobarenahook = new MobArenaHook();
		mobarenahook.enable(this);
		//BountyLister
		bountylister = new BountyLister();
		bountylister.enable(this);
		//EnderdragonEnhanced
		enderdragonenhanced = new EnderdragonEnhanced();
		enderdragonenhanced.enable(this);
		//SilentHeal
		silentheal = new SilentHeal();
		silentheal.enable(this);
		//EpicAbilities
		epicabilities = new EpicAbilities();
		epicabilities.enable(this);
		//SpellControl
		spellcontrol = new SpellControl();
		spellcontrol.enable(this);
		
		getLog().log(Level.INFO, "[GummyComp] Plugins Enabled");
	}
	
    /**
     * Commands
     */
	public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {
		switch(label.toLowerCase()) {
			case "wel" :
			/* falls through */
			case "welcome" :
				if(Welcomer.isEnabled) {
					welcomer.welcome((Player)Sender);
				}
				break;
			case "sheal" :
				if(args.length == 1) {
					if(SilentHeal.isEnabled) {
						if(Bukkit.getPlayer(args[0]) != null) {
							silentheal.sheal((Player)Sender, Bukkit.getPlayer(args[0]));
							Sender.sendMessage("Healed");
						}else{
							Sender.sendMessage("Player not found");
						}
					}
				}
				break;
			case "boss" :
				if(EpicSpawner.isEnabled) {
					epicspawner.bossCommand((Player)Sender);
				}
				break;
			case "gummycomp" :
				if(args.length == 2 && Sender.isOp()) {
					if(args[0].equalsIgnoreCase("enable")) {
						switch(args[1].toLowerCase()) {
							case "welcomer":
								Sender.sendMessage("Welcomer enabled");
								Welcomer.isEnabled    = true;
								break;
							
							case "dyncompass" :
								Sender.sendMessage("DynCompass enabled");
								DynCompass.isEnabled  = true;
								break;
							
							case "epicspawner" :
								Sender.sendMessage("EpicSpawner enabled");
								EpicSpawner.isEnabled = true;
								break;

							case "all" :
								Sender.sendMessage("All plugins enabled");
								EpicSpawner.isEnabled = true;
								Welcomer.isEnabled    = true;
								DynCompass.isEnabled  = true;
								break;
							
							default:
								Sender.sendMessage("Plugin not found, use: All, welcomer, spicspawner or dyncompass");
								
						}
					}
					if(args[0].equalsIgnoreCase("disable")) {
						switch(args[1].toLowerCase()) {
							case "welcomer":
								Welcomer.isEnabled    = false;
								Sender.sendMessage("Welcomer disabled");
								break;
							
							case "dyncompass" :
								Sender.sendMessage("DynCompass disabled");
								DynCompass.isEnabled  = false;
								break;
							
							case "epicspawner" :
								Sender.sendMessage("EpicSpawner disabled");
								EpicSpawner.isEnabled = false;
								break;
							
							case "all" :
								Sender.sendMessage("All plugins disabled");
								EpicSpawner.isEnabled = false;
								Welcomer.isEnabled    = false;
								DynCompass.isEnabled  = false;
								break;
							
							default:
								Sender.sendMessage("Plugin not found, use: All, welcomer, spicspawner or dyncompass");
								
						}
					}
				} else {
					Sender.sendMessage("Usage: /GummyComp <enable/disable> <plugin>");
				}
		}
		
		return false;
	}
	
	public Logger getLog(){
		return logger;
	}
}
