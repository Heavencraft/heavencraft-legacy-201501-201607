package fr.hc.worlds;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldsManager
{
	public static final int RESOURCES_SIZE = 5000;

	private static Location _spawn;

	private static Location _tuto;
	static Random rnd = new Random();

	public static void init()
	{
		// Force difficulty of SRP to HARD
		getWorld().setDifficulty(Difficulty.HARD);
		_spawn = new Location(getWorld(), 145.5D, 107D, 130.5D, 270F, 0F);
	}

	public static Location getSpawn()
	{
		return _spawn;
	}

	public static Location getTutoLocation()
	{
		return _tuto;
	}

	public static World getWorld()
	{
		return Bukkit.getWorld("world");
	}

	public static World getNether()
	{
		return Bukkit.getWorld("world_nether");
	}

	public static World getTheEnd()
	{
		return Bukkit.getWorld("world_the_end");
	}

	public static World getResources()
	{
		return Bukkit.getWorld("world_resources");
	}
}