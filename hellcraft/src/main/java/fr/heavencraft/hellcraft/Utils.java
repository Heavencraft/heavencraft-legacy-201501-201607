package fr.heavencraft.hellcraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Utils {

	private static boolean isBlockAboveAir(World world, int x, int y, int z)
	{
		return world.getBlockAt(x, y - 1, z).getType() == Material.AIR;
	}

	private static boolean isBlockUnsafe(World world, int x, int y, int z)
	{
		final Block below = world.getBlockAt(x, y - 1, z);
		if ((below.getType() == Material.LAVA) || (below.getType() == Material.STATIONARY_LAVA))
		{
			return true;
		}

		if (below.getType() == Material.FIRE)
		{
			return true;
		}

		if ((world.getBlockAt(x, y, z).getType() != Material.AIR) || (world.getBlockAt(x, y + 1, z).getType() != Material.AIR))
		{
			return true;
		}
		return isBlockAboveAir(world, x, y, z);
	}

	public static Location getSafeDestination(Location loc)
	{
		final World world = loc.getWorld();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		final int z = loc.getBlockZ();

		while (isBlockAboveAir(world, x, y, z))
		{
			y--;
			if (y < 0)
			{
				return null;
			}
		}
		while (isBlockUnsafe(world, x, y, z))
		{
			y++;
			if (y >= 255)
			{
				x++;
			}
		}
		while (isBlockUnsafe(world, x, y, z))
		{
			y--;
			if (y <= 1)
			{
				y = 255;
				x++;
			}
		}
		return new Location(world, x + 0.5D, y, z + 0.5D, loc.getYaw(), loc.getPitch());
	}
	
}
