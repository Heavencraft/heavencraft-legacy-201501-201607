package fr.heavencraft.heavenguard.datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.FlagHandler;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;

public class SQLRegion implements Region
{
	// SQL queries
	private static final String SETPARENT = "UPDATE regions SET parent_name = LOWER(?) WHERE name = LOWER(?) LIMIT 1";
	private static final String REDEFINE = "UPDATE regions SET world = LOWER(?), min_x = ?, min_y = ?, min_z = ?, max_x = ?, max_y = ?, max_z = ? WHERE name = LOWER(?) LIMIT 1";
	private static final String LOAD_MEMBERS = "SELECT uuid, owner FROM regions_members WHERE region_name = LOWER(?);";
	private static final String ADD_MEMBER = "INSERT INTO regions_members (region_name, uuid, owner) VALUES (LOWER(?), ?, ?);";
	private static final String REMOVE_MEMBER = "DELETE FROM regions_members WHERE region_name = LOWER(?) AND uuid = ? AND owner = ? LIMIT 1;";

	private final ConnectionProvider connectionProvider;
	private final RegionProvider regionProvider;

	private final String name;
	private String parentName;

	private String world;
	private int minX;
	private int minY;
	private int minZ;
	private int maxX;
	private int maxY;
	private int maxZ;

	private final Collection<UUID> members = new HashSet<UUID>();
	private final Collection<UUID> owners = new HashSet<UUID>();
	private final FlagHandler flagHandler;

	SQLRegion(ConnectionProvider connectionHandler, ResultSet rs, RegionProvider regionProvider) throws SQLException
	{
		this.connectionProvider = connectionHandler;
		this.regionProvider = regionProvider;

		name = rs.getString("name");
		parentName = rs.getString("parent_name");

		world = rs.getString("world");
		minX = rs.getInt("min_x");
		minY = rs.getInt("min_y");
		minZ = rs.getInt("min_z");
		maxX = rs.getInt("max_x");
		maxY = rs.getInt("max_y");
		maxZ = rs.getInt("max_z");

		loadMembers();

		flagHandler = new SQLFlagHandler(connectionHandler, rs, this);
	}

	@Override
	public String toString()
	{
		return name;
	}

	private void loadMembers() throws SQLException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(LOAD_MEMBERS))
		{
			ps.setString(1, name);

			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final UUID player = UUID.fromString(rs.getString("uuid"));

					if (rs.getBoolean("owner"))
						owners.add(player);
					else
						members.add(player);
				}
			}
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean canBuilt(UUID player)
	{
		// If this region is public
		if (getFlagHandler().getBooleanFlag(Flag.PUBLIC) == Boolean.TRUE)
			return true;

		// Members/Owners of this region can build there
		if (isMember(player, false))
			return true;

		// Players that can build in the parent region can also build there
		final Region parent = getParent();
		if (parent != null)
			return parent.canBuilt(player);

		return false;
	}

	/*
	 * Parent
	 */

	@Override
	public Region getParent()
	{
		if (parentName == null)
			return null;

		try
		{
			return regionProvider.getRegionByName(parentName);
		}
		catch (final HeavenException ex)
		{
			return null;
		}
	}

	@Override
	public void setParent(String parentName) throws HeavenException
	{
		// Update database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SETPARENT))
		{
			ps.setString(1, parentName);
			ps.setString(2, name);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}

		// Update cache
		this.parentName = parentName;
	}

	/*
	 * Coordonnées
	 */

	@Override
	public boolean contains(String world, int x, int y, int z)
	{
		return this.world.equals(world) //
				&& containsSameWorld(x, y, z);
	}

	@Override
	public boolean containsSameWorld(int x, int y, int z)
	{
		return minX <= x && x <= maxX //
				&& minY <= y && y <= maxY //
				&& minZ <= z && z <= maxZ;
	}

	@Override
	public void redefine(String world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
			throws HeavenException
	{
		// Update database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(REDEFINE))
		{
			ps.setString(1, world);
			ps.setInt(2, minX);
			ps.setInt(3, minY);
			ps.setInt(4, minZ);
			ps.setInt(5, maxX);
			ps.setInt(6, maxY);
			ps.setInt(7, maxZ);
			ps.setString(8, name);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}

		// Update cache
		this.world = world;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	@Override
	public String getWorld()
	{
		return world;
	}

	@Override
	public int getMinX()
	{
		return minX;
	}

	@Override
	public int getMinY()
	{
		return minY;
	}

	@Override
	public int getMinZ()
	{
		return minZ;
	}

	@Override
	public int getMaxX()
	{
		return maxX;
	}

	@Override
	public int getMaxY()
	{
		return maxY;
	}

	@Override
	public int getMaxZ()
	{
		return maxZ;
	}

	/*
	 * Members
	 */

	@Override
	public void addMember(UUID player, boolean owner) throws HeavenException
	{
		if (isMember(player, false))
			throw new HeavenException("Le joueur {%1$s} est déjà membre de la protection {%2$s}.", player.toString(),
					name);

		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(ADD_MEMBER))
		{
			ps.setString(1, name);
			ps.setString(2, player.toString());
			ps.setBoolean(3, owner);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");

			if (owner)
				owners.add(player);
			else
				members.add(player);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			new SQLErrorException();
		}
	}

	@Override
	public boolean isMember(UUID player, boolean owner)
	{
		if (owner)
			return owners.contains(player);
		else
			return owners.contains(player) || members.contains(player);
	}

	@Override
	public void removeMember(UUID player, boolean owner) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(REMOVE_MEMBER))
		{
			ps.setString(1, name);
			ps.setString(2, player.toString());
			ps.setBoolean(3, owner);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");

			owners.remove(player);
			members.remove(player);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			new SQLErrorException();
		}
	}

	@Override
	public Collection<UUID> getMembers(boolean owner)
	{
		// Never return the original collection, because plugin could modify it.
		return new HashSet<UUID>(owner ? owners : members);
	}

	/*
	 * Flags
	 */

	@Override
	public FlagHandler getFlagHandler()
	{
		return flagHandler;
	}
}