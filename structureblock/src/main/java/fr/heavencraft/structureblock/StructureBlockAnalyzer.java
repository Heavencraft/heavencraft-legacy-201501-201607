package fr.heavencraft.structureblock;

import org.bukkit.Location;
import org.bukkit.Material;
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
	 * @param sizeVector
	 * @param smelteryLayers
	 * @return
	 */
	static boolean checkStructure(Player player, BlockFace relative, Location location, Vector relativeVector,
			Vector sizeVector, Material[][][] smelteryLayers)
	{
		Vector addx = new Vector(0, 0, 0);
		Vector addz = new Vector(0, 0, 0);
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
		Vector subx = addx.clone().multiply(-sizeVector.getX());
		Vector subz = addz.clone().multiply(-sizeVector.getZ());
		for (int y = 0; y < sizeVector.getY(); y++)
		{
			for (int z = 0; z < sizeVector.getZ(); z++)
			{
				for (int x = 0; x < sizeVector.getX(); x++)
				{
					if ((smelteryLayers[y][z][x] != null)
							&& (location.getBlock().getType() != smelteryLayers[y][z][x]))
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
