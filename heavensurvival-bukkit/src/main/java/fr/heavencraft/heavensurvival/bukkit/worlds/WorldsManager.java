package fr.heavencraft.heavensurvival.bukkit.worlds;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldsManager
{
	/*
	 * Singleton pattern
	 */

	private static WorldsManager instance;

	public static WorldsManager getInstance()
	{
		if (instance == null)
			instance = new WorldsManager();

		return instance;
	}

	private final World world;
	private final World world_nether;
	private final World world_the_end;

	public WorldsManager()
	{
		world = Bukkit.getWorld("world");
		world_nether = Bukkit.getWorld("world_nether");
		world_the_end = Bukkit.getWorld("world_the_end");
	}

	public World getWorld()
	{
		return world;
	}

	public World getNether()
	{
		return world_nether;
	}

	public World getTheEnd()
	{
		return world_the_end;
	}

	public Location getSpawnLocation()
	{
		return getWorld().getSpawnLocation();
	}

	public void setSpawnLocation(Location spawn)
	{
		getWorld().setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
	}

	public static void setDifficulty(Difficulty difficulty)
	{
		for (final World world : Bukkit.getWorlds())
			world.setDifficulty(difficulty);
	}
}