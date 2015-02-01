package fr.heavencraft.heavencrea.worlds;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import fr.heavencraft.heavencrea.generator.CreativeChunkGenerator;

public class WorldsManager
{
	public static final String WORLD_CREATIVE = "world_creative";
	public static final String WORLD_BIOME = "world_biome";

	public static void init()
	{
		if (!isLoaded(WORLD_BIOME))
		{
			final WorldCreator creator = new WorldCreator(WORLD_BIOME);
			creator.environment(Environment.NORMAL);
			creator.type(WorldType.NORMAL);
			creator.createWorld();
		}

		if (!isLoaded(WORLD_CREATIVE))
		{
			final WorldCreator creator = new WorldCreator("world_creative");
			creator.environment(Environment.NORMAL);
			creator.generator(new CreativeChunkGenerator());
			creator.createWorld();
		}
	}

	private static boolean isLoaded(String name)
	{
		return Bukkit.getWorld(name) != null;
	}

	public static World getWorldCreative()
	{
		return Bukkit.getWorld(WORLD_CREATIVE);
	}

	public static World getWorldBiome()
	{
		return Bukkit.getWorld(WORLD_BIOME);
	}
}