package fr.heavencraft.hellcraft.worlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldsManager
{
	public static final String WORLD_SPAWN = "spawn";
	public static final String WORLD_CITY = "CityWorld";

	private static Location spawnLocation;

	public static void init()
	{
		if (!isLoaded(WORLD_SPAWN))
		{
			final WorldCreator creator = new WorldCreator(WORLD_SPAWN);
			creator.environment(Environment.NORMAL);
			creator.type(WorldType.NORMAL);
			creator.createWorld();
		}

		if (!isLoaded(WORLD_CITY))
		{
			final WorldCreator creator = new WorldCreator(WORLD_CITY);
			creator.environment(Environment.NORMAL);
			creator.type(WorldType.NORMAL);
			creator.createWorld();
		}

		spawnLocation = new Location(getWorldSpawn(), 1.5, 67, 0.5, 0, 0);
	}

	private static boolean isLoaded(String name)
	{
		return Bukkit.getWorld(name) != null;
	}

	public static World getWorldSpawn()
	{
		return Bukkit.getWorld(WORLD_SPAWN);
	}

	public static World getWorldCity()
	{
		return Bukkit.getWorld(WORLD_CITY);
	}

	public static Location getSpawnLocation()
	{
		return spawnLocation;
	}
}