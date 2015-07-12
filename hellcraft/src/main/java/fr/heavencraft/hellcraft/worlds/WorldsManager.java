package fr.heavencraft.hellcraft.worlds;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;

import fr.heavencraft.hellcraft.Utils;

public class WorldsManager
{
	public static final String WORLD_SPAWN = "spawn";
	public static final String WORLD_CITY = "city";
	private static final String GENERATOR_CITY = "CityWorld";

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
			creator.generator(GENERATOR_CITY);
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

	static Random rnd = new Random();

	public static Location getCitySpawn()
	{
		int x;
		int z;
		do
		{
			x = rnd.nextInt(400) - 200;
			z = rnd.nextInt(400) - 200;
		}
		while ((getWorldCity().getBiome(x, z) == Biome.OCEAN)
				|| (getWorldCity().getBiome(x, z) == Biome.DEEP_OCEAN));

		return Utils.getSafeDestination(new Location(getWorldCity(), x, 215.0D, z));
	}
}