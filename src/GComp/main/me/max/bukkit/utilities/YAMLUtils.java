package GComp.main.me.max.bukkit.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import GComp.main.me.max.bukkit.CompMain;

public class YAMLUtils {
	public static FileConfiguration EpicSpawnerTimerConfiguration;
	public static File              EpicSpawnerTimerFile;
	public static CompMain          main;
	public YAMLUtils (CompMain cm) {
		main = cm;
		reloadEpicSpawnerTimer();
		
	}
	public void saveEpicSpawnerTimer() {
	    if (EpicSpawnerTimerConfiguration == null || EpicSpawnerTimerFile == null) {
	    return;
	    }
	    try {
	        getEpicSpawnerTimer().save(EpicSpawnerTimerFile);
	    } catch (IOException ex) {
	        main.getLogger().log(Level.SEVERE, "Could not save config to " + EpicSpawnerTimerFile, ex);
	    }
	}
	public FileConfiguration getEpicSpawnerTimer() {
	    if (EpicSpawnerTimerConfiguration == null) {
	        this.reloadEpicSpawnerTimer();
	    }
	    return EpicSpawnerTimerConfiguration;
	}
	public void reloadEpicSpawnerTimer() {
	    if (EpicSpawnerTimerFile == null) {
	    	EpicSpawnerTimerFile = new File(main.getDataFolder(), "EpicSpawnerTimer.yml");
	    }
	    EpicSpawnerTimerConfiguration = YamlConfiguration.loadConfiguration(EpicSpawnerTimerFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = main.getResource("golems.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        EpicSpawnerTimerConfiguration.setDefaults(defConfig);
	    }
	    
	}

}
