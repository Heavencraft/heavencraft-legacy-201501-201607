package fr.hc.cinematics.components;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CinematicsTeleporter
{
	private final static Map<String, Location> cinematicsCoordinates = new HashMap<String, Location>();
	private final static String MAP_SPLIT = "_";
	private static Location backUpPoint;

	public CinematicsTeleporter()
	{
		// Cinematic_Timer, Location.
		World world = Bukkit.getServer().getWorld("world");
		cinematicsCoordinates.put("0" + MAP_SPLIT + "1", new Location(world, -975.7, 75.5, -64.5, -90, 0));
	}

	public static void teleportWithoutBackUpPoint(Player player, Integer timer, Integer cinematic)
	{
		String index = cinematic.toString() + MAP_SPLIT + timer.toString();
		Location location = cinematicsCoordinates.get(index);
		player.teleport(location);

	}

	public static void teleportWithBackUpPoint(Player player, Integer timer, Integer cinematic)
	{
		String index = cinematic.toString() + MAP_SPLIT + timer.toString();
		Location location = cinematicsCoordinates.get(index);
		backUpPoint = player.getLocation();
		player.teleport(location);

	}

	public static void teleportToBackUpPoint(Player player, Integer timer, Integer cinematic)
	{
		player.teleport(backUpPoint);
	}

}
