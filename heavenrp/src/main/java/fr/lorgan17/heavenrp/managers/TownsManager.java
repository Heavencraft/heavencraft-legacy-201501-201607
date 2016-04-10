package fr.lorgan17.heavenrp.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.utils.RPUtils;
import fr.heavencraft.heavenrp.worlds.WorldsManager;

public class TownsManager
{
	public static boolean regionExists(String name)
	{
		return RPUtils.getWorldGuard().getRegionManager(WorldsManager.getWorld()).getRegion(name) != null;
	}

	public static List<String> getMayors(String name)
	{
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(
						"SELECT u.name FROM users u, mayors m WHERE m.region_name = ? AND m.user_id = u.id"))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			final List<String> result = new ArrayList<String>();

			while (rs.next())
			{
				result.add(rs.getString("name"));
			}
			return result;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public static void addMayor(String town, User mayor) throws HeavenException
	{
		town = town.toLowerCase();
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("INSERT INTO mayors (user_id, region_name) VALUES (?, ?);"))
		{
			ps.setInt(1, mayor.getId());
			ps.setString(2, town);

			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			throw new HeavenException("Le joueur {%1$s} est déjà maire de la ville {%2$s}.",
					new Object[] { mayor.getName(), town });
		}
	}

	public static void removeMayor(String town, User mayor) throws HeavenException
	{
		town = town.toLowerCase();
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("DELETE FROM mayors WHERE user_id = ? AND region_name = ?;"))
		{
			ps.setInt(1, mayor.getId());
			ps.setString(2, town);

			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new HeavenException("Ceci n'est pas sensé se produire :O");
		}
	}

	public static void createSubRegion(String townName, User owner, Selection selection, int up, int down)
			throws HeavenException
	{
		final Vector[] expand = new Vector[2];
		expand[0] = new Vector(0, -down, 0);
		expand[1] = new Vector(0, up, 0);
		try
		{
			selection.getRegionSelector().getRegion().expand(expand);
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}

		final World world = selection.getWorld();
		final BlockVector pt1 = BukkitUtil.toVector(selection.getMinimumPoint()).toBlockVector();
		final BlockVector pt2 = BukkitUtil.toVector(selection.getMaximumPoint()).toBlockVector();

		final RegionManager rm = RPUtils.getWorldGuard().getRegionManager(world);

		final ProtectedRegion town = rm.getRegionExact(townName);

		if (town == null)
		{
			throw new HeavenException("La ville {%1$s} n'existe pas.", new Object[] { townName });
		}
		if ((!town.contains(pt1)) || (!town.contains(pt2)))
		{
			throw new HeavenException("La parcelle doit être dans ta ville.");
		}
		final String id = createRegionName(rm, townName, owner.getName());

		final ProtectedRegion region = new ProtectedCuboidRegion(id, pt1, pt2);

		rm.addRegion(region);
		region.getOwners().addPlayer(owner.getName());

		try
		{
			region.setParent(town);
		}
		catch (final CircularInheritanceException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			rm.save();
		}
		catch (final ProtectionDatabaseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void removeSubRegion(String townName, String regionName) throws HeavenException
	{
		final RegionManager rm = RPUtils.getWorldGuard().getRegionManager(WorldsManager.getWorld());

		final ProtectedRegion region = rm.getRegionExact(regionName);

		if (region == null)
		{
			throw new HeavenException("Cette parcelle n'existe pas.");
		}
		if (!region.getParent().getId().equalsIgnoreCase(townName))
		{
			throw new HeavenException("Cette parcelle ne fait pas partie de ta ville.");
		}
		rm.removeRegion(regionName);
	}

	private static String createRegionName(RegionManager rm, String townName, String userName)
	{
		int i = 1;
		String name;
		do
		{
			name = townName + "_" + userName + "_" + i;
			i++;
		}
		while (rm.getRegionExact(name) != null);

		return name;
	}

	public static boolean isMayor(User mayor, String townName)
	{
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("SELECT m.user_id FROM mayors m WHERE m.user_id = ? AND m.region_name = ?"))
		{
			ps.setInt(1, mayor.getId());
			ps.setString(2, townName);

			final ResultSet rs = ps.executeQuery();
			return rs.next();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
}