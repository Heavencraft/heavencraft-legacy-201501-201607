package fr.heavencraft.heavenrp.structureblock;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class StructureBlockAnalyzer
{
	/**
	 * Check if the IG structure correspond with the theoretical one
	 * 
	 * @param player
	 * @param relative
	 * @param location
	 * @param relativeVector
	 * @return
	 */
	static boolean checkStructure(Player player, BlockFace relative, Location location, Vector relativeVector)
	{
		// initialize vector
		final Vector sizeVector = StructureBlock.smelterySize;
		final Vector addx = new Vector(0, 0, 0);
		final Vector addz = new Vector(0, 0, 0);

		// set vector and location values according to BlockFace
		switch (relative)
		{
			case NORTH:
				location.subtract(relativeVector);
				addx.setX(-1);
				addz.setZ(1);
				break;
			case EAST:
				location.subtract(-relativeVector.getZ(), relativeVector.getY(), relativeVector.getX());
				addx.setZ(-1);
				addz.setX(-1);
				break;
			case SOUTH:
				location.add(relativeVector.getX(), -relativeVector.getY(), relativeVector.getZ());
				addx.setX(1);
				addz.setZ(-1);
				break;
			case WEST:
				location.add(-relativeVector.getZ(), -relativeVector.getY(), relativeVector.getX());
				addx.setZ(1);
				addz.setX(1);
				break;
			default:
				return false;
		}

		// set vector value according to structure size
		final Vector subx = addx.clone().multiply(-sizeVector.getX());
		final Vector subz = addz.clone().multiply(-sizeVector.getZ());

		// test if the structure IG correspond with the configuration file
		for (int y = 0; y < sizeVector.getY(); y++)
		{
			for (int z = 0; z < sizeVector.getZ(); z++)
			{
				for (int x = 0; x < sizeVector.getX(); x++)
				{
					if ((StructureBlock.smelteryLayers[y][z][x] != null)
							&& (location.getBlock().getType() != StructureBlock.smelteryLayers[y][z][x]))
					{
						return false;
					}
					location.add(addx);
				}
				location.add(subx);
				location.add(addz);
			}
			location.add(subz);
			location.add(0, 1, 0);
		}
		return true;
	}
}
