package fr.heavencraft.heavencrea;

import org.bukkit.generator.ChunkGenerator;

import fr.heavencraft.heavencore.BukkitHeavenPlugin;

public class CreativePlugin extends BukkitHeavenPlugin
{

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		getLogger().info("getDefaultWorldGenerator " + worldName);

		if ("world".equals(worldName))
		{
			return new CreativeChunkGenerator();
		}
		else
		{
			return super.getDefaultWorldGenerator(worldName, id);
		}
	}
}
