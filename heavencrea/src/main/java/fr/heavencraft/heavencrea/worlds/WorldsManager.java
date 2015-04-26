package fr.heavencraft.heavencrea.worlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import fr.heavencraft.heavencrea.generator.CreativeChunkGenerator;
import fr.heavencraft.heavencrea.generator.FlatChunkGenerator;
import fr.heavencraft.heavencrea.generator.TalentChunkGenerator;

public class WorldsManager
{
	public static final String WORLD_CREATIVE = "world_creative";
	public static final String WORLD_BIOME = "world_biome";
	public static final String WORLD_TALENT = "world_talent";
	public static final String WORLD_ARCHITECT = "world_architect";

	private static Location biomeSpawnLocation;
	private static Location talentSpawnLocation;
	private static Location architectSpawnLocation;

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
			final WorldCreator creator = new WorldCreator(WORLD_CREATIVE);
			creator.environment(Environment.NORMAL);
			creator.generator(new CreativeChunkGenerator());
			creator.createWorld();
		}

		if (!isLoaded(WORLD_TALENT))
		{
			final WorldCreator creator = new WorldCreator(WORLD_TALENT);
			creator.environment(Environment.NORMAL);
			creator.generator(new TalentChunkGenerator());
			creator.createWorld();
		}

		if (!isLoaded(WORLD_ARCHITECT))
		{
			final WorldCreator creator = new WorldCreator(WORLD_ARCHITECT);
			creator.environment(Environment.NORMAL);
			creator.generator(new FlatChunkGenerator());
			creator.createWorld();
		}

		biomeSpawnLocation = new Location(getWorldBiome(), 120, 144, 652, 0, 0);
		talentSpawnLocation = new Location(getWorldTalent(), 0, 50, 0, 0, 0);
		architectSpawnLocation = new Location(getWorldArchitect(), 0, 50, 0, 0, 0);
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

	public static World getWorldTalent()
	{
		return Bukkit.getWorld(WORLD_TALENT);
	}

	public static World getWorldArchitect()
	{
		return Bukkit.getWorld(WORLD_ARCHITECT);
	}

	public static Location getBiomeSpawnLocation()
	{
		return biomeSpawnLocation;
	}

	public static Location getTalentSpawnLocation()
	{
		return talentSpawnLocation;
	}

	public static Location getArchitectSpawnLocation()
	{
		return architectSpawnLocation;
	}
}