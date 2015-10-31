package fr.heavencraft.generators;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class EmptyChunkGenerator extends ChunkGenerator
{
	@Override
	public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
	{
		return new byte[world.getMaxHeight() / 16][];
	}
}