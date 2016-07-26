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
		World end = Bukkit.getServer().getWorld("end_void");
		cinematicsCoordinates.put("0" + MAP_SPLIT + "0", new Location(end, 750, 20, 0, 90, 0));
		cinematicsCoordinates.put("0" + MAP_SPLIT + "10", new Location(end, 750, 0, 0));
		cinematicsCoordinates.put("0" + MAP_SPLIT + "20", new Location(end, 750, 0, 0));
		cinematicsCoordinates.put("0" + MAP_SPLIT + "30", new Location(end, 750, 0, 0));
		cinematicsCoordinates.put("0" + MAP_SPLIT + "40", new Location(end, 750, 0, 0));
		World world = Bukkit.getServer().getWorld("final_map");
		cinematicsCoordinates.put("0" + MAP_SPLIT + "50", new Location(world, 17.5, 196.5, -77.5, -180, 0));
	}

	public static void teleportWithoutBackUpPoint(Player player, Integer timer, Integer cinematic)
	{
		player.sendMessage("" + cinematicsCoordinates.get("0_0"));
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

	public static void teleportWithRelativeCoordinate(Player player, Integer timer, Integer cinematic)
	{
		String index = cinematic.toString() + MAP_SPLIT + timer.toString();
		Location location = player.getLocation();
		location.add(cinematicsCoordinates.get(index));
		player.teleport(location);
	}

}
