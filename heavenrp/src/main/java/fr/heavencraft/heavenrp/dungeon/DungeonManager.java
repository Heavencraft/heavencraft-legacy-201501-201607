package fr.heavencraft.heavenrp.dungeon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.mysql.jdbc.Statement;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;

public class DungeonManager
{
	// Exceptions
	private static String ALREADY_IN_DUNGEON = "Vous ètes déjà dans un donjon.";
	private static String NAME_UNKNOWN_DUNGEON = "Le nom du donjon est invalide.";
	private static String NO_LOBBY_DUNGEON = "Ce donjon n'a pas de salon.";
	private static String DUNGEON_FULL = "Il y a déjà quelqu'un dans ce donjon...";
	// Messages
	private static String DUNGEON_WAITING_FOR_PLAYERS = "Je dois attendre encore %1$d camarade(s).";
	private static String DUNGEON_CREATED = "Le dojnon %1$s a été crée avec succes.";
	private static String DUNGEON_DELETED = "Le dojnon %1$s a été supprimé avec succes.";
	private static String DUNGEON_ROOM_CREATED = "La salle {%1$d} a été ajouté avec succes.";
	private static String DUNGEON_ROOM_DELETED = "La salle {%1$d} a été supprimée avec succes.";

	// Hashmap association playerUUID --> DungeonID
	private static HashMap<UUID, Integer> playerDungeon = new HashMap<UUID, Integer>();
	// Hashmap association DungeonID --> Dungeon
	public static HashMap<Integer, Dungeon> Dungeons = new HashMap<Integer, Dungeon>();

	/**
	 * Constructor, loads dungeons from database
	 * 
	 * @throws Exception
	 */
	public DungeonManager()
	{
		try
		{
			LoadDungeons();
		}
		catch (HeavenException e)
		{
			e.printStackTrace();
		}
	}

	/* ~~ Generic Section ~~ */

	/**
	 * Loads dungeons from database
	 * 
	 * @throws HeavenException
	 */
	public static void LoadDungeons() throws HeavenException
	{
		// Is someone playing in dungeons?
		if (playerDungeon.values().size() > 0)
			throw new HeavenException(
					"Impossible de recharger les donjons. Des joueurs sont en train de l'utiliser.");

		DungeonManager.playerDungeon.clear();
		DungeonManager.Dungeons.clear();

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement("SELECT * FROM dungeons"))
		{
			final ResultSet dungeonResultSet = ps.executeQuery();

			// For each dungeon in DB, create dungeon object and store it
			while (dungeonResultSet.next())
			{
				// Create a new dungeon object
				Dungeon dg = new Dungeon(dungeonResultSet.getInt("dungeon_id"),
						dungeonResultSet.getString("name"), dungeonResultSet.getInt("requiredPlayers"));

				// Trigger block location
				World spawnWorld = Bukkit.getWorld(dungeonResultSet.getString("world"));
				Location lobbySpawn = new Location(spawnWorld, dungeonResultSet.getDouble("spawnX"),
						dungeonResultSet.getDouble("spawnY"), dungeonResultSet.getDouble("spawnZ"),
						dungeonResultSet.getFloat("spawnYaw"), dungeonResultSet.getFloat("spawnPitch"));

				Location outSpawn = new Location(spawnWorld, dungeonResultSet.getDouble("outX"),
						dungeonResultSet.getDouble("outY"), dungeonResultSet.getDouble("outZ"),
						dungeonResultSet.getFloat("outYaw"), dungeonResultSet.getFloat("outPitch"));

				dg.Lobby = lobbySpawn;
				dg.ExitPoint = outSpawn;

				// Store Dungeon
				Dungeons.put(dg.getId(), dg);
				// Load rooms
				LoadDungeonRooms(dg.getId());
				System.out.println("Loaded dungeon " + dg.getName());
			}
		}
		catch (final SQLException ex)
		{
			System.out.println(ex.toString());
		}
	}

	/**
	 * Loads from the database a dungeons rooms
	 * 
	 * @param dungeonId
	 * @throws HeavenException
	 */
	private static void LoadDungeonRooms(final int dungeonId) throws HeavenException
	{
		if (!Dungeons.containsKey(dungeonId))
			throw new HeavenException(
					"Erreur lors d'une tentative de chargement de salles d'un donjon inexistant.");

		try (PreparedStatement ps = HeavenRP.getConnection()
				.prepareStatement("SELECT * FROM dungeon_rooms WHERE dungeon_id = ?"))
		{
			ps.setInt(1, dungeonId);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				World spawnWorld = Bukkit.getWorld(rs.getString("world"));

				Location spawn = new Location(spawnWorld, rs.getDouble("spawnX"), rs.getDouble("spawnY"),
						rs.getDouble("spawnZ"), rs.getFloat("spawnYaw"), rs.getFloat("spawnPitch"));

				Location trigger = new Location(spawnWorld, rs.getInt("triggerX"), rs.getInt("triggerY"),
						rs.getInt("triggerZ"));

				Location corner1 = new Location(spawnWorld, rs.getInt("minX"), rs.getInt("minY"),
						rs.getInt("minZ"));
				Location corner2 = new Location(spawnWorld, rs.getInt("maxX"), rs.getInt("maxY"),
						rs.getInt("maxZ"));

				DungeonRoom dgr = new DungeonRoom(rs.getInt("dungeon_room_id"), spawn, trigger, corner1, corner2);

				Dungeons.get(dungeonId).Rooms.put(dgr.getRoomId(), dgr);
			}

		}
		catch (final SQLException ex)
		{
			System.out.println(ex.toString());
		}

	}

	/**
	 * Makes a Player enter a dungeon.
	 * 
	 * @param p
	 * @param dungeonName
	 * @throws HeavenException
	 */
	public static void PlayerJoin(Player p, String dungeonName) throws HeavenException
	{
		// Already in dungeon?
		if (playerDungeon.containsKey(p.getUniqueId()))
		{
			throw new HeavenException(ALREADY_IN_DUNGEON);
		}
		// Dungeon exists?
		Dungeon dungeon = null;
		for (final Dungeon dg : Dungeons.values())
			if (dg.getName().equalsIgnoreCase(dungeonName))
				dungeon = dg;
		if (dungeon == null)
			throw new HeavenException(NAME_UNKNOWN_DUNGEON);
		// Dungeon has lobby?
		if (dungeon.Lobby == null)
			throw new HeavenException(NO_LOBBY_DUNGEON);
		// Dungeon has free slots?
		if (dungeon.PlayerCount >= dungeon.requiredPlayer)
			throw new HeavenException(DUNGEON_FULL);

		// Add player to list
		playerDungeon.put(p.getUniqueId(), dungeon.getId());
		// Teleport player to lobby
		p.teleport(dungeon.Rooms.get(0).getSpawn());
		++dungeon.PlayerCount;

		// Do we meet requirements to play?
		if (dungeon.PlayerCount == dungeon.requiredPlayer)
		{
			// TODO trigger start
		}
		else
		{
			ChatUtil.sendMessage(p, DUNGEON_WAITING_FOR_PLAYERS, dungeon.requiredPlayer - dungeon.PlayerCount);
		}
	}

	/**
	 * Makes a Player leave a dungeon
	 * 
	 * @param u
	 * @return
	 */
	public static boolean PlayerLeave(Player u)
	{
		// TODO self generated function
		return true;
	}

	/**
	 * A player attempt to change room
	 * 
	 * @param u
	 * @param nextRoomId next room id
	 * @return
	 */
	public static boolean AttempNextRoom(Player u, int nextRoomId)
	{
		// TODO self generated function
		return true;
	}

	public static boolean PlayerDies(Player u)
	{
		// TODO self generated function
		return true;
	}

	public static boolean RestoreInventory(Player u)
	{
		// TODO self generated function
		return true;
	}

	/* ~~ Edit Section ~~ */

	/**
	 * Creates a new Dungeon.
	 * 
	 * @param p
	 * @param dungeonName
	 * @param requiredPlayers
	 * @param spawn
	 * @param out
	 * @throws HeavenException
	 */
	public static void CreateDungeon(final Player p, final String dungeonName, final int requiredPlayers,
			Location spawn, Location out) throws HeavenException
	{

		if (getDungeon(dungeonName) != null)
			throw new HeavenException("Ce donjon existe déjà.");

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"INSERT INTO dungeons (name, requiredPlayers, world, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch, "
						+ "outX, outY, outZ, outYaw, outPitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
						Statement.RETURN_GENERATED_KEYS))
		{
			// General
			ps.setString(1, dungeonName);
			ps.setInt(2, requiredPlayers);
			ps.setString(3, spawn.getWorld().getName());
			// spawn
			ps.setDouble(4, spawn.getX());
			ps.setDouble(5, spawn.getY());
			ps.setDouble(6, spawn.getZ());
			ps.setFloat(7, spawn.getYaw());
			ps.setFloat(8, spawn.getPitch());
			// out
			ps.setDouble(9, out.getX());
			ps.setDouble(10, out.getY());
			ps.setDouble(11, out.getZ());
			ps.setFloat(12, out.getYaw());
			ps.setFloat(13, out.getPitch());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
			{
				int id = rs.getInt(1);
				final Dungeon dg = new Dungeon(id, dungeonName, requiredPlayers);
				Dungeons.put(id, dg);
				ChatUtil.sendMessage(p, DUNGEON_CREATED, dg.getName());
			}
			rs.close();
			ps.close();

		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new HeavenException("Erreur inconnue.");
		}
	}

	/**
	 * Deletes a dungeon
	 * 
	 * @param player
	 * @param dungeonName
	 * @return
	 * @throws HeavenException
	 */
	public static void DeleteDungeon(final Player player, final String dungeonName) throws HeavenException
	{
		Dungeon dg = getDungeon(dungeonName);
		if (dg == null)
			throw new HeavenException("Ce donjon n'existe pas.");

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"DELETE dungeons, dungeon_rooms FROM dungeons INNER JOIN dungeon_rooms WHERE dungeon_rooms.dungeon_id = dungeons.dungeon_id AND dungeons.dungeon_id = ?"))
		{
			ps.setInt(1, dg.getId());
			ps.executeUpdate();
			// Remove from cache
			Dungeons.remove(dg.getId());
			ps.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new HeavenException(
					"Erreur fatale lors de la suppression du donjon. Informez en un administrateur.");
		}
		ChatUtil.sendMessage(player, DUNGEON_DELETED, dg.getName());
		return;
	}

	/**
	 * Creates a new Dungeon Room. It will be added to Database and to cache
	 * 
	 * @param p
	 * @param dungeonName
	 * @param spawn
	 * @param trigger
	 * @param corner1
	 * @param corner2
	 * @throws HeavenException
	 */
	public static void AddDungeonRoom(final Player p, final String dungeonName, final Location spawn,
			final Location trigger, final Location corner1, final Location corner2) throws HeavenException
	{
		Dungeon dg = getDungeon(dungeonName);

		if (dg == null)
			throw new HeavenException("Ce donjon n'existe pas.");

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"INSERT INTO `dungeon_rooms`(`dungeon_id`, `world`, `spawnX`, `spawnY`, `spawnZ`, `spawnYaw`, `spawnPitch`, "
						+ "`triggerX`, `triggerY`, `triggerZ`, `minX`, `minY`, `minZ`, `maxX`, `maxY`, `maxZ`) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
						Statement.RETURN_GENERATED_KEYS))
		{
			// Dungeon
			ps.setInt(1, dg.getId());
			ps.setString(2, spawn.getWorld().getName());
			// Spawn
			ps.setDouble(3, spawn.getX());
			ps.setDouble(4, spawn.getY());
			ps.setDouble(5, spawn.getZ());
			ps.setFloat(6, spawn.getYaw());
			ps.setFloat(7, spawn.getPitch());
			// Trigger
			ps.setInt(8, trigger.getBlockX());
			ps.setInt(9, trigger.getBlockY());
			ps.setInt(10, trigger.getBlockZ());
			// Min Corner
			ps.setInt(11, corner1.getBlockX());
			ps.setInt(12, corner1.getBlockY());
			ps.setInt(13, corner1.getBlockZ());
			// Max Corner
			ps.setInt(14, corner2.getBlockX());
			ps.setInt(15, corner2.getBlockY());
			ps.setInt(16, corner2.getBlockZ());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
			{
				int roomUuniqueId = rs.getInt(1);
				DungeonRoom dgr = new DungeonRoom(roomUuniqueId, spawn, trigger, corner1, corner2);
				dg.Rooms.put(roomUuniqueId, dgr);
				ChatUtil.sendMessage(p, DUNGEON_ROOM_CREATED, roomUuniqueId);
				return;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new HeavenException(
					"Erreur fatale lors de la création de la salle de donjon. Informez-en un administrateur.");
		}
	}

	public static void RemoveDungeonRoom(final Player player, final int roomId) throws HeavenException
	{
		Dungeon dg = getDungeonByRoom(roomId);
		if (dg == null)
			throw new HeavenException("Il n'existe pas de dojon comportant cette salle.");

		if (!dg.Rooms.containsKey(roomId))
			throw new HeavenException("Cette salle n'existe pas.");

		try (PreparedStatement ps = HeavenRP.getConnection()
				.prepareStatement("DELETE FROM dungeon_rooms WHERE dungeon_room_id = ?"))
		{
			ps.setInt(1, roomId);
			ps.executeUpdate();
			// Remove from cache
			dg.Rooms.remove(roomId);
			ps.close();
			ChatUtil.sendMessage(player, DUNGEON_ROOM_DELETED, roomId);
			return;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new HeavenException(
					"Erreur fatale lors de la suppression de la salle du donjon. Informez en un administrateur.");
		}

	}

	/* ~~ Utilities Section ~~ */

	/**
	 * Returns a dungeon object. NULL if no dungeon found.
	 * 
	 * @param dungeonName
	 * @return
	 */
	private static Dungeon getDungeon(String dungeonName)
	{
		for (Dungeon dg : Dungeons.values())
			if (dg.getName().equalsIgnoreCase(dungeonName))
				return dg;
		return null;
	}

	/**
	 * Returns the dungeon containing the room with given ID
	 * 
	 * @param roomId
	 * @return
	 */
	private static Dungeon getDungeonByRoom(int roomId)
	{
		for (Dungeon dg : Dungeons.values())
			if (dg.Rooms.containsKey(roomId))
				return dg;
		return null;
	}

	/**
	 * Displays a list of available dungeons and their state
	 * 
	 * @param p
	 */
	public static void PrintDungeonList(final Player p)
	{
		ChatUtil.sendMessage(p, "╔═════════════════════════╗");
		ChatUtil.sendMessage(p, "║         Donjons         ║");
		ChatUtil.sendMessage(p, "╚═════════════════════════╝");
		int i = 1; // Counter
		for (DungeonManager.Dungeon dg : DungeonManager.Dungeons.values())
		{
			char linePrefix = (i >= DungeonManager.Dungeons.values().size()) ? '└' : '├';
			ChatUtil.sendMessage(p, "%1$c ({%2$d}) %3$s | Joueurs: {%4$d}/{%5$d}", linePrefix, dg.getId(),
					dg.getName(), dg.PlayerCount, dg.getRequiredPlayer());
			++i;
		}
	}

	/**
	 * Displays detailed information about a Dungeon
	 * 
	 * @param p
	 * @param dungeon
	 * @throws HeavenException
	 */
	public static void PrintDungeonInfo(final Player p, final String dungeon) throws HeavenException
	{
		final Dungeon dg = DungeonManager.getDungeon(dungeon);
		if (dg == null)
			throw new HeavenException("Dojnon recherché inconnu.");

		// Header
		ChatUtil.sendMessage(p, "╔═════════════════════════╗");
		ChatUtil.sendMessage(p, "║%1$s║", StringUtils.center(dg.getName(), 25));
		ChatUtil.sendMessage(p, "╚═════════════════════════╝");
		ChatUtil.sendMessage(p, "ID: %1$d | Joueurs: %2$d/%3$d | Salles: %4$d", dg.getId(), dg.PlayerCount,
				dg.getRequiredPlayer(), dg.Rooms.size());
		ChatUtil.sendMessage(p, "Salles: ");

		// Rooms
		int i = 1; // Counter
		for (DungeonRoom dgr : dg.Rooms.values())
		{
			char linePrefix = (i >= dg.Rooms.values().size()) ? '└' : '├';
			// Get Trigger block type
			String blockType = "";
			if (dgr.getTriggerBlock().getBlock() != null)
				dgr.getTriggerBlock().getBlock().getState().getType().toString();

			ChatUtil.sendMessage(p,
					"%1$c {ID}: %2$d | {Spawn}: %3$.2f %4$.2f %5$.2f %6$.2f %7$.2f | {Trigger}: %8$d %9$d %10$d (%11$s)",
					linePrefix, dgr.getRoomId(), dgr.getSpawn().getX(), dgr.getSpawn().getY(),
					dgr.getSpawn().getZ(), dgr.getSpawn().getYaw(), dgr.getSpawn().getPitch(),
					dgr.getTriggerBlock().getBlockX(), dgr.getTriggerBlock().getBlockY(),
					dgr.getTriggerBlock().getBlockZ(), blockType);
		}

	}

	/* ~~ Object Classes ~~ */
	public static class Dungeon
	{
		public Dungeon(int id, String name, int requiredPlayer)
		{
			this.id = id;
			this.name = name;
			this.requiredPlayer = requiredPlayer;
		}

		public int getId()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

		public int getRequiredPlayer()
		{
			return requiredPlayer;
		}

		private int id;
		private String name;
		private int requiredPlayer = 1;
		public int PlayerCount = 0;

		public Location Lobby = null;
		public Location ExitPoint = null;

		public HashMap<Integer, DungeonRoom> Rooms = new HashMap<Integer, DungeonRoom>();

	}

	public static class DungeonRoom
	{

		private int roomId;
		private final Location corner1;
		private final Location corner2;

		public int getRoomId()
		{
			return roomId;
		}

		private Location spawn;

		public Location getSpawn()
		{
			return spawn;
		}

		private Location triggerBlock;

		public Location getTriggerBlock()
		{
			return triggerBlock;
		}

		public DungeonRoom(int roomId, Location spawn, Location triggerBlock, Location corner1, Location corner2)
		{
			this.roomId = roomId;
			this.spawn = spawn;
			this.triggerBlock = triggerBlock;
			this.corner1 = corner1;
			this.corner2 = corner2;
		}

		public Location getCorner1()
		{
			return corner1;
		}

		public Location getCorner2()
		{
			return corner2;
		}
	}

}
