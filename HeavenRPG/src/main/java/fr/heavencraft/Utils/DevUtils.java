package fr.heavencraft.Utils;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DevUtils {

	public static String serializeLoc(Location l){
		return l.getWorld().getName()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ();
	}
	
	public static Location deserializeLoc(String s){
		String[] st = s.split(",");
		return new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3]));
	}
	
	public static void log(String message, Object... args)
	{
		Bukkit.getLogger().log(Level.INFO, String.format(message, args));
	}
	
}
