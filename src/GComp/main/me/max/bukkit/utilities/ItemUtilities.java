package GComp.main.me.max.bukkit.utilities;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import GComp.main.me.max.bukkit.CompMain;

public class ItemUtilities {
	public static CompMain main;
	public static PermissionHandler permission;
	
	public static boolean isEnabled;
	
	public ItemUtilities(CompMain cm){
		
		if(!cm.getConfig().getBoolean("BountyLister.enable")) {
			return;
		}
		
		main           = cm;
		permission     = new PermissionHandler(cm);
		isEnabled      = true;
	}
	public ItemStack ItemFromConfig(String toConvert) {
		  
			  
		ItemStack stack;
		String parts[] = toConvert.split(" ");
		stack = new ItemStack(Material.getMaterial(parts[0].replace("Type:", "")), 1);
			  
		for(String part : parts) {

			String split[] = part.split(":");
			String key     = split[0];
			String value   = split[1];
			switch(key.trim()) {
				case "Type":
					break;
				case "Amount":
				  	stack.setAmount(Integer.valueOf(value));
				  	break;
				case "Enchants":
				  	for(String ewl : value.split(":")) {
				  		if(!ewl.equals(key)) {
				  			Enchantment ench = Enchantment.getByName(ewl.split("-")[0]);
				  			if(ench == null) {
				  				stack.addUnsafeEnchantment(getEnchantmentByCommonName(ewl.split("-")[0]), Integer.valueOf(ewl.split("-")[1]));
				  			}else{
				  				stack.addUnsafeEnchantment(Enchantment.getByName(ewl.split("-")[0]), Integer.valueOf(ewl.split("-")[1]));
				  			}
				  		}
				  	}
				  	break;
				case "Durability":
				  	stack.setDurability(Short.valueOf(value));
				  	break;
				case "Name":
					ItemMeta im = stack.getItemMeta();
				  	im.setDisplayName(color(value.replace("_"," ")));
				  	stack.setItemMeta(im);
				  	break;
				case "Lore":
		  			List<String> list = new LinkedList<String>();
		  			ItemMeta meta = stack.getItemMeta();
				  	for(String Lore : value.split(":")) {
				  		if(!Lore.equals(key)) {
				  			list.add(color(Lore));
				  		}
				  	}
				  	meta.setLore(list);
				  	stack.setItemMeta(meta);
				  	break;
				case "Color":
				  	int r = Integer.valueOf(value.split("-")[0]);
				  	int g = Integer.valueOf(value.split("-")[1]);
				  	int b = Integer.valueOf(value.split("-")[2]);
				  	LeatherArmorMeta lam = (LeatherArmorMeta) stack.getItemMeta();
				  	lam.setColor(Color.fromRGB(r, g, b));
				  	stack.setItemMeta(lam);
				  	break;
				case "MetaEnch":
				  	for(String ewl : value.split(":")) {
				  		if(!ewl.equals(key)) {
				  			ItemMeta bookmeta = stack.getItemMeta();
				  			bookmeta.addEnchant(Enchantment.getByName(ewl.split("-")[0]), Integer.valueOf(ewl.split("-")[1]), true);
				  			stack.setItemMeta(bookmeta);
				  		}
				  	}
				  	break;	
				case "Data":
					stack.setData((new MaterialData(Integer.valueOf(value))));
			}
		}
		return stack;
	}
	public static String getChance(String s) {
		String parts[] = s.split(" ");
			  
		for(String part : parts) {
			String split[] = part.split(":");
			String key     = split[0];
			String value   = split[1];
			if(key.trim().equals("Chance")) {
				return value;
			}
		}
		return "";
	}
	public static String getName(String s) {
		String parts[] = s.split(" ");
		String ret = null;
		for(String part : parts) {
			String split[] = part.split(":");
			String key     = split[0];
			String value   = split[1];
			if(key.trim().equals("Name")) {
				ret = color(value);
			}
			if(key.trim().equals("Type") && ret == null) {
				ret = capFirst(value.replace("_", ""));
			}
		}
		
		return ret;
	}
	public static Enchantment getEnchantmentByCommonName(String name){
		name = name.toLowerCase();
		if(name.contains("fire") && name.contains("prot")) return Enchantment.PROTECTION_FIRE;
		if((name.contains("exp") || name.contains("blast")) && name.contains("prot")) return Enchantment.PROTECTION_EXPLOSIONS;
		if((name.contains("arrow") || name.contains("proj")) && name.contains("prot")) return Enchantment.PROTECTION_PROJECTILE;
		if(name.contains("prot")) return Enchantment.PROTECTION_ENVIRONMENTAL;
		if(name.contains("fall")) return Enchantment.PROTECTION_FALL;
		if(name.contains("respiration")) return Enchantment.OXYGEN;
		if(name.contains("aqua")) return Enchantment.WATER_WORKER;
		if(name.contains("sharp")) return Enchantment.DAMAGE_ALL;
		if(name.contains("smite")) return Enchantment.DAMAGE_UNDEAD;
		if(name.contains("arth")) return Enchantment.DAMAGE_ARTHROPODS;
		if(name.contains("knockback")) return Enchantment.KNOCKBACK;
		if(name.contains("fire")) return Enchantment.FIRE_ASPECT;
		if(name.contains("loot")) return Enchantment.LOOT_BONUS_MOBS;
		if(name.contains("power")) return Enchantment.ARROW_DAMAGE;
		if(name.contains("punch")) return Enchantment.ARROW_KNOCKBACK;
		if(name.contains("flame")) return Enchantment.ARROW_FIRE;
		if(name.contains("infin")) return Enchantment.ARROW_INFINITE; 
		if(name.contains("dig") || name.contains("eff")) return Enchantment.DIG_SPEED;
		if(name.contains("dura") || name.contains("unbreaking")) return Enchantment.DURABILITY;
		if(name.contains("silk")) return Enchantment.SILK_TOUCH;
		if(name.contains("fort")) return Enchantment.LOOT_BONUS_BLOCKS;
		if(name.contains("thorn")) return Enchantment.THORNS;
		
		return null;

	}
	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	public static String capFirst(String s){
		String firstLetter = s.substring(0,1);
		String rest = s.substring(1);
		s = firstLetter.toUpperCase() + rest.toLowerCase();
		return s;
	}
	
	public boolean convertChanceToPercent(String chancestring) {
		double chance     = Double.valueOf(chancestring);
		Random random     = new Random();
		double randomized = 1;
		
		int denom         = 100;
		int numer         = 0;
		
		//100%
		if(chance == 100) {
			randomized = 0;
		} 
		
		
		//50%
		if(chance == 50) {
			randomized = random.nextInt(1);
		} else {
			//If its not a pretty number (Not 50 or 100)
			String temp = chancestring;
			
			//If it has a decimal point
			if(temp.contains(".")) {
				
				//remove the whole numbers
				temp  = temp.substring(temp.indexOf("."));
				
				//SANITY CHECK(Removing any left-over decimals)
				temp  = temp.replace(".", "");
				
				//Power of ten for each decimal place
				denom = (int) (100 * Math.pow(10d, temp.length()));
				
				//turn chance into an int by mutpliply the decimals away
				numer = (int) (chance * Math.pow(10d, temp.length()));
				
				
			} else {
				//if it has no decimals
				numer = Integer.valueOf(chancestring);
			}
			
		}
		
		if(chance > 50 && chance < 50) {
			double outcome = random.nextInt(denom);
			//If the outcome falls within the numer/denom
			if(outcome <= numer) {
				randomized = 0;
			}
		}
		
		return randomized == 0;
	}
}
