package fr.heavencraft.heavencrea;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

public class WorldsManager
{
	public WorldsManager()
	{
		if (!isLoaded("world"))
		{
			final WorldCreator creator = new WorldCreator("world");
			creator.environment(Environment.NORMAL);
			creator.generator(new CreativeChunkGenerator());
			creator.createWorld();
		}
		else
		{
		}
	}

	private static boolean isLoaded(String name)
	{
		return Bukkit.getWorld(name) != null;
	}
}