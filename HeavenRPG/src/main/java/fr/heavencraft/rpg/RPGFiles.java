package fr.heavencraft.rpg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RPGFiles {

	// Set up all the needed things for files
		public static YamlConfiguration Zones = null;
		public static YamlConfiguration Dungeons = null;
		public static File zonesFile = null;
		public static File dungeonFile = null;

		public static void saveAll() {
			saveZones();
			saveDungeons();
		}

		public static void reloadAll() {
			reloadZones();
			reloadDungeons();
		}
		
		
		// Reload Abilities File
		public static void reloadZones() {
			if (zonesFile == null)
				zonesFile = new File(Bukkit.getPluginManager().getPlugin("HeavenRPG").getDataFolder(), "Zones.yml");
			Zones = YamlConfiguration.loadConfiguration(zonesFile);		
			// Look for defaults in the jar
			InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("HeavenRPG").getResource("Zones.yml");
			if (defConfigStream != null)
			{
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				if (!zonesFile.exists() || zonesFile.length() == 0)
					Zones.setDefaults(defConfig);
			}
		}
		
		public static void reloadDungeons() {		
			if (dungeonFile == null)
				dungeonFile = new File(Bukkit.getPluginManager().getPlugin("HeavenRPG").getDataFolder(), "Dungeons.yml");
			Dungeons = YamlConfiguration.loadConfiguration(dungeonFile);		
			// Look for defaults in the jar
			InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("HeavenRPG").getResource("Dungeons.yml");
			if (defConfigStream != null)
			{
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				if (!dungeonFile.exists() || dungeonFile.length() == 0)
					Dungeons.setDefaults(defConfig);
			}
		}

		public static FileConfiguration getZones() {
			if (Zones == null)
				reloadZones();
			return Zones;
		}
		
		public static FileConfiguration getDungeons() {
			if (Dungeons == null)
				reloadDungeons();
			return Dungeons;
		}

		public static void saveZones() {
			if (Zones == null || zonesFile == null)
				return;
			try
			{
				getZones().save(zonesFile);
			} catch (IOException ex)
			{
				Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + zonesFile, ex);
			}
		}
		
		public static void saveDungeons() {
			if (Dungeons == null || dungeonFile == null)
				return;
			try
			{
				getDungeons().save(dungeonFile);
			} catch (IOException ex)
			{
				Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + dungeonFile, ex);
			}
		}
}
