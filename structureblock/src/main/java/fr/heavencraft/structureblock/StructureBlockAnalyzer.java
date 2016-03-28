package fr.heavencraft.structureblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class StructureBlockAnalyzer
{

	/**
	 * test if the structure IG correspond with the 3D list
	 * 
	 * @param player
	 * @param relative direction of the structure (WEST EST SOUTH NORTH)
	 * @param location
	 * @param relativeVector vector to first point if direction equals to NORTH
	 * @param smelteryLayers
	 * @param width (of the structure) (z)
	 * @param length (of the structure) (x)
	 * @param height (of the structure) (y)
	 * @return true for valid structure
	 *
	 */
	static boolean testStructure(final Player player, final BlockFace relative, Location location,
			final Vector relativeVector, final Vector sizeVector, Material[][][] smelteryLayers)
	{
		// defines some vectors
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

		// comparison of IG structure and 3D list
		for (int y = 0; y < sizeVector.getY(); y++)
		{
			for (int z = 0; z < sizeVector.getZ(); z++)
			{
				for (int x = 0; x < sizeVector.getX(); x++)
				{
					if (smelteryLayers[y][z][x] != null
							&& location.getBlock().getType() != smelteryLayers[y][z][x])
						return false;
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
