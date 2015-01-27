package fr.heavencraft.heavenguard.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class RegionManager
{
	private final RegionProvider regionProvider;

	public RegionManager(RegionProvider regionProvider)
	{
		this.regionProvider = regionProvider;
	}

	private Collection<Region> getRegionsAtLocationWithoutParents(String world, int x, int y, int z)
	{
		final Collection<Region> regions = regionProvider.getRegionsAtLocation(world, x, y, z);

		if (regions.size() > 1)
		{
			final Collection<Region> toRemove = new ArrayList<Region>();

			for (final Region region : regions)
			{
				for (final Region parent : getAllParents(region))
				{
					if (regions.contains(parent))
					{
						toRemove.add(parent);
					}
				}
			}

			regions.removeAll(toRemove);
		}

		return regions;
	}

	private Collection<Region> getAllParents(Region region)
	{
		final Region parent = region.getParent();

		if (parent == null)
			return new ArrayList<Region>();

		final Collection<Region> parents = getAllParents(parent);
		parents.add(parent);
		return parents;
	}

	public boolean canBuildAt(UUID player, String world, int x, int y, int z)
	{
		final Collection<Region> regions = getRegionsAtLocationWithoutParents(world, x, y, z);

		// If there are regions here
		if (regions.size() != 0)
		{
			// If a player can't build in one region, it can't build here
			for (final Region region : regions)
				if (!region.canBuilt(player))
					return false;

			return true;
		}

		// No regions here : the player can build if the world is public
		return regionProvider.getGlobalRegion(world).getBooleanFlag(Flag.PUBLIC);
	}

	public boolean isProtectedAgainstEnvironment(String world, int x, int y, int z)
	{
		final Collection<Region> regions = getRegionsAtLocationWithoutParents(world, x, y, z);

		// If there are regions here
		if (regions.size() != 0)
			return true;

		// No regions here : the block is protected if the world is not public
		return !regionProvider.getGlobalRegion(world).getBooleanFlag(Flag.PUBLIC);
	}

	public boolean areInSameRegion(String world, int x1, int y1, int z1, int x2, int y2, int z2)
	{
		final Collection<Region> regions1 = getRegionsAtLocationWithoutParents(world, x1, y1, z1);
		final Collection<Region> regions2 = getRegionsAtLocationWithoutParents(world, x2, y2, z2);

		return regions1.equals(regions2);
	}

	public boolean isPvp(String world, int x, int y, int z)
	{
		final Collection<Region> regions = getRegionsAtLocationWithoutParents(world, x, y, z);

		// If there are regions here
		if (regions.size() != 0)
		{
			boolean pvpEnabled = false;
			boolean pvpDisabled = false;

			for (final Region region : regions)
			{
				final Boolean pvp = region.getBooleanFlag(Flag.PVP);

				if (pvp == null)
					continue;

				if (pvp)
					pvpEnabled = true;
				else
					pvpDisabled = true;
			}

			return !pvpDisabled && pvpEnabled;
		}

		// No regions here : this block is pvp if the world is pvp
		return regionProvider.getGlobalRegion(world).getBooleanFlag(Flag.PVP);
	}

}