package fr.heavencraft.heavensurvival.bukkit.teleport;

import org.bukkit.Location;

import fr.heavencraft.heavenguard.api.Region;

public class Selection
{
	private Location left;
	private Location right;

	public void setLeft(Location left)
	{
		this.left = left;
	}

	public void setRight(Location right)
	{
		this.right = right;
	}

	public boolean isValid()
	{
		return left != null && right != null;
	}

	public int getMinX()
	{
		return Math.min(left.getBlockX(), right.getBlockX());
	}

	public int getMaxX()
	{
		return Math.max(left.getBlockX(), right.getBlockX());
	}

	public int getMinZ()
	{
		return Math.min(left.getBlockZ(), right.getBlockZ());
	}

	public int getMaxZ()
	{
		return Math.max(left.getBlockZ(), right.getBlockZ());
	}

	public int getPrice()
	{
		return (Math.abs(left.getBlockX() - right.getBlockX()) + 1)
				* (Math.abs(left.getBlockZ() - right.getBlockZ()) + 1);
	}

	public boolean hasCollision(Region region)
	{
		if (region.contains(left.getWorld().getName(), left.getBlockX(), left.getBlockY(), left.getBlockZ()))
			return true;

		if (region.contains(right.getWorld().getName(), right.getBlockX(), right.getBlockY(), right.getBlockZ()))
			return true;

		if (contains(region.getMinX(), region.getMinZ()))
			return true;

		if (contains(region.getMaxX(), region.getMaxZ()))
			return true;

		return false;
	}

	private boolean contains(int x, int z)
	{
		return getMinX() <= x && x <= getMaxX() //
				&& getMinZ() <= z && z <= getMaxZ();
	}
}