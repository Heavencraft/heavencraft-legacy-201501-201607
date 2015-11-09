package fr.heavencraft.rpg.zones;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import fr.heavencraft.Utils.DevUtils;
import fr.heavencraft.rpg.RPGFiles;

public class ZoneManager {
	
		
	private static ArrayList<Zone> zones = new ArrayList<Zone>();
	
	public static void loadAllZones()
	{
		zones.clear();
		if(RPGFiles.getZones().getConfigurationSection("Zones") != null)
			for(String a : RPGFiles.getZones().getConfigurationSection("Zones").getKeys(false))
			{		
					Zone z = new Zone(a);
					z.set_zoneLevel(RPGFiles.getZones().getInt("Zones." + a + ".level"));
					z.set_name(RPGFiles.getZones().getString("Zones." + a + ".name"));			
					Location l1 = DevUtils.deserializeLoc(RPGFiles.getZones().getString("Zones." + a + ".l1"));
					Location l2 = DevUtils.deserializeLoc( RPGFiles.getZones().getString("Zones." + a + ".l2"));
					z.set_cubo(new CuboidSelection(l1.getWorld(), l1, l2));
					addZone(z);
					Bukkit.broadcastMessage(z.getName() + " a ete chargee.");
			}
	}
	
	/**
	 * Crée une nouvelle zone.
	 * @param name
	 * @param level
	 * @param s
	 */
	public static void createZone(String name, int level, CuboidSelection s)
	{
		Zone z = new Zone(name, level, s);
		if(zones.contains(z))
			return;
		zones.add(z);
		
	}
	/**
	 * Ajoute une zone.
	 * @param z
	 */
	public static void addZone(Zone z)
	{
		if(!zones.contains(z))
			zones.add(z);
	}
	
	/**
	 * Supprime une zone.
	 * @param z
	 */
	public static void removeZone(Zone z)
	{
		if(zones.contains(z))
		{
			RPGFiles.getZones().set("Zones." + z.get_name(), null);
			RPGFiles.saveZones();
			zones.remove(z);
		}		
	}
	
	/**
	 * Retourne la zone dans laquelle se trouve la position. (Peut etre null)
	 * Renvoie la zone au plus grand niveau.
	 * @param l
	 * @return
	 */
	public static Zone getZone(Location l)
	{
		ArrayList<Zone> foundZones = new ArrayList<Zone>();
		
		for(Zone z : zones)
			if(z.getCubo().contains(l))
				foundZones.add(z);
		
		Zone higestLevel = null;
		for(Zone z: foundZones)
			if(higestLevel == null)
				higestLevel = z;
			else if(higestLevel.get_zoneLevel() < z.getZoneLevel())
				higestLevel = z;
		return higestLevel;
	}
	
	/**
	 * Retourne une liste des zones.
	 * @param l
	 * @return
	 */
	public static ArrayList<Zone> getZones(Location l)
	{
		ArrayList<Zone> foundZones = new ArrayList<Zone>();
		for(Zone z : zones)
			if(z.getCubo().contains(l))
				foundZones.add(z);
		return foundZones;
	}
	
	
	/**
	 * Retourne la zone avec ce nom. (Peut etre null)
	 * @param name
	 * @return
	 */
	public static Zone getZoneByName(String name)
	{
		for(Zone z : zones)
		{
			if(z.getName().equalsIgnoreCase(name))
				return z;
		}
		return null;
	}
	
	
}
