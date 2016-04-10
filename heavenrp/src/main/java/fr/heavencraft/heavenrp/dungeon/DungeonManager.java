package fr.heavencraft.heavenrp.dungeon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mysql.jdbc.Statement;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;

public class DungeonManager
{
	// Exceptions
	private static String ALREADY_IN_DUNGEON = "Vous êtes déjà dans un donjon.";
	private static String NOT_IN_DUNGEON = "Vous n'êtes pas dans un donjon.";
	private static String NAME_UNKNOWN_DUNGEON = "Le nom du donjon est invalide.";
	private static String UNKNOWN_ROOM = "Salle inconnue dans ce donjon.";
	private static String NO_ROOMS_IN_DUNGEON = "Ce donjon n'a pas de salles.";
	private static String DUNGEON_FULL = "Il y a déjà quelqu'un dans ce donjon...";
	private static String NOT_IDLE = "Je ne peux pas entrer pour l'instant, il y a déjà quelqu'un...";
	// Messages
	private static String DUNGEON_WAITING_FOR_PLAYERS = "Je dois attendre encore %1$d camarade(s).";
	private static String DUNGEON_CREATED = "Le donjon %1$s a été crée avec succès.";
	private static String DUNGEON_FIRST_ROOM_SET = "Vous avez changé la première salle du dojnon %1$s avec succès.";
	private static String DUNGEON_DELETED = "Le donjon %1$s a été supprimé avec succes.";
	private static String DUNGEON_ROOM_CREATED = "La salle {%1$d} a été ajouté avec succès.";
	private static String DUNGEON_ROOM_DELETED = "La salle {%1$d} a été supprimée avec succès.";
	private static String LOADING_DUNGEON = "Je me prépare a enter dans le donjon!";
	private static String NOT_ALL_MOBS_DEAD = "Il y a encore {%1$d} monstre(s)!";
	private static String WON_DUNGEON = "{Victoire}!";
	private static String LOST_DUNGEON = "{Défaite}!";
	private static String YOU_LEFT_DUNGEON = "Vous avez pris la {fuite}!";
	private static String SOMEONE_OF_YOUR_TEAM_FLEED = "Un de mes amis a pris la fuite!";

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
			throw new HeavenException("Impossible de recharger les donjons. Des joueurs sont en train de l'utiliser.");

		DungeonManager.playerDungeon.clear();
		DungeonManager.Dungeons.clear();

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement("SELECT * FROM dungeons"))
		{
			final ResultSet dungeonResultSet = ps.executeQuery();

			// For each dungeon in DB, create dungeon object and store it
			while (dungeonResultSet.next())
			{
				// Create a new dungeon object
				Dungeon dg = new Dungeon(dungeonResultSet.getInt("dungeon_id"), dungeonResultSet.getString("name"),
						dungeonResultSet.getInt("requiredPlayers"));

				dg.setFirstRoomId(dungeonResultSet.getInt("firstRoom"));

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
				dg.DungeonState = DungeonStates.IDLE;
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
			throw new HeavenException("Erreur lors d'une tentative de chargement de salles d'un donjon inexistant.");

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

				Location corner1 = new Location(spawnWorld, rs.getInt("minX"), rs.getInt("minY"), rs.getInt("minZ"));
				Location corner2 = new Location(spawnWorld, rs.getInt("maxX"), rs.getInt("maxY"), rs.getInt("maxZ"));

				DungeonRoom dgr = new DungeonRoom(dungeonId, rs.getInt("dungeon_room_id"), spawn, trigger, corner1,
						corner2);

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
		Dungeon dungeon = getDungeon(dungeonName);
		if (dungeon == null)
			throw new HeavenException(NAME_UNKNOWN_DUNGEON);

		// Dungeon idle?
		if (dungeon.DungeonState != DungeonStates.IDLE)
			throw new HeavenException(NOT_IDLE);
		// Dungeon has lobby?
		if (dungeon.Lobby == null)
			throw new HeavenException(NO_ROOMS_IN_DUNGEON);
		// Dungeon has free slots?
		if (dungeon.PlayerCount >= dungeon.requiredPlayer)
			throw new HeavenException(DUNGEON_FULL);

		// Add player to list
		playerDungeon.put(p.getUniqueId(), dungeon.getId());
		// Teleport player to lobby
		p.teleport(dungeon.Lobby);
		++dungeon.PlayerCount;

		// Do we meet requirements to play?
		if (dungeon.PlayerCount == dungeon.requiredPlayer)
		{
			dungeon.DungeonState = DungeonStates.BOOTING;
			AttempNextRoom(p, -1);
		}
		else
		{
			ChatUtil.sendMessage(p, DUNGEON_WAITING_FOR_PLAYERS, dungeon.requiredPlayer - dungeon.PlayerCount);
		}
	}

	/**
	 * Makes a Player leave a dungeon, when it is not started.
	 * 
	 * @param invoker
	 * @throws HeavenException
	 */
	public static void PlayerLeave(Player invoker, boolean disconnecting) throws HeavenException
	{
		// Currently playing ?
		if (!DungeonManager.playerDungeon.containsKey(invoker.getUniqueId()))
			throw new HeavenException(NOT_IN_DUNGEON);
		Dungeon dg = Dungeons.get(playerDungeon.get(invoker.getUniqueId()));
		// Dungeon idle --> player in lobby ?
		if (dg.DungeonState == DungeonStates.IDLE)
		{
			ChatUtil.sendMessage(invoker, YOU_LEFT_DUNGEON);
			invoker.teleport(dg.ExitPoint);
			playerDungeon.remove(invoker.getUniqueId());
			--dg.PlayerCount;
			return;
		}
		// Dungeon running?
		else
		{
			// is the invoker disconnecting?
			if (disconnecting)
				invoker.teleport(dg.ExitPoint);
			EndDungeon(dg, DungeonEndingCauses.FLEED);
		}
	}

	/**
	 * A player attempt to change room
	 * 
	 * @param invoker
	 * @param nextRoomId
	 *            next room id (-1 if you should be teleported from lobby to room 1)
	 * @return
	 * @throws HeavenException
	 */
	public static void AttempNextRoom(final Player invoker, int nextRoomId) throws HeavenException
	{
		// Currently playing?
		if (!DungeonManager.playerDungeon.containsKey(invoker.getUniqueId()))
			throw new HeavenException(NOT_IN_DUNGEON);
		Dungeon dg = Dungeons.get(playerDungeon.get(invoker.getUniqueId()));

		boolean firstRoom = (nextRoomId < 0) ? true : false;
		// Delay before teleport.
		final int teleportWarmup;
		if (firstRoom)
		{
			// First room, 3s delay
			teleportWarmup = 30;
			firstRoom = true;
			nextRoomId = dg.getFirstRoomId();
			dg.CurrentRoomId = dg.getFirstRoomId();
		}
		else
		{
			// Normal attempt
			teleportWarmup = 0;
			// Check if all mobs are dead
			DungeonRoom dgr = dg.Rooms.get(dg.CurrentRoomId);
			if (dgr == null || dgr.Mobs.size() > 0)
			{
				ChatUtil.sendMessage(invoker, NOT_ALL_MOBS_DEAD, dgr.Mobs.size());
				// Trigger a check for corruption
				Bukkit.getServer().getScheduler()
				.scheduleSyncDelayedTask(HeavenRP.getInstance(),
						new DungeonMobCacheCorruptionCheckTask());
				return;
			}
		}

		// Does the dungeon contains the room?
		if (!dg.Rooms.containsKey(nextRoomId))
			throw new HeavenException(UNKNOWN_ROOM);

		DungeonRoom nextRoom = dg.Rooms.get(nextRoomId);
		dg.CurrentRoomId = nextRoom.getRoomId();

		// Apply effects
		if (firstRoom)
		{
			for (Player p : getPlayersByDungeon(dg.getId()))
			{
				ChatUtil.sendMessage(p, LOADING_DUNGEON);
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, teleportWarmup * 2, 10));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, teleportWarmup * 2, 10));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, teleportWarmup * 2, 10));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, teleportWarmup * 2, 10));
			}
		}

		// Prepare reset of the trigger block
		Block resdstoneBlock = nextRoom.triggerBlock.getBlock();
		Bukkit.getServer().getScheduler()
				.scheduleSyncDelayedTask(HeavenRP.getInstance(),
						new RestoreBlockTask(resdstoneBlock.getWorld().getName(), resdstoneBlock.getX(),
								resdstoneBlock.getY(), resdstoneBlock.getZ(), resdstoneBlock.getType()),
						40 + teleportWarmup);

		// Teleport players
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HeavenRP.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				for (Player p : getPlayersByDungeon(dg.getId()))
				{
					// Restore inventory of dead players
					if (DeadDungeonPlayerInventoryStore.hasStored(p))
						DeadDungeonPlayerInventoryStore.RestoreInventory(p);
					p.teleport(nextRoom.spawn);
				}
				// Player are no longer dead
				dg.DeadPlayerCount = 0;
				// Update Dungeon State to Running
				dg.DungeonState = DungeonStates.RUNNING;
				// Run trigger
				resdstoneBlock.setType(Material.REDSTONE_BLOCK);
			}
		}, teleportWarmup);
	}

	/**
	 * A player attempt to end the current dungeon
	 * 
	 * @param invoker
	 * @throws HeavenException
	 */
	public static void AttempEndDungeon(Player invoker) throws HeavenException
	{
		// Currently playing?
		if (!DungeonManager.playerDungeon.containsKey(invoker.getUniqueId()))
			throw new HeavenException(NOT_IN_DUNGEON);
		Dungeon dg = Dungeons.get(playerDungeon.get(invoker.getUniqueId()));
		// We must be running before ending.
		if (dg.DungeonState != DungeonStates.RUNNING)
			return;
		// Check if all mobs are dead
		DungeonRoom dgr = dg.Rooms.get(dg.CurrentRoomId);
		if (dgr == null)
			return;

		if (dgr.Mobs.size() > 0)
		{
			ChatUtil.sendMessage(invoker, NOT_ALL_MOBS_DEAD, dgr.Mobs.size());
			return;
		}
		EndDungeon(dg, DungeonEndingCauses.VICTORY);
	}

	/**
	 * Handles the player when he should die
	 * 
	 * @param p
	 * @throws HeavenException
	 */
	public static void PlayerDies(Player p, final String dungeonName) throws HeavenException
	{
		if (getDungeon(dungeonName) == null)
			throw new HeavenException("Vous n'êtes pas dans un dojon.");
		// Store player inventory
		DeadDungeonPlayerInventoryStore.StoreInventory(p);
		p.setLevel(0);
		p.setTotalExperience(0);
		p.getActivePotionEffects().clear();
		p.setHealth(p.getMaxHealth());
		p.setFireTicks(0);
		p.setFoodLevel(20);
		Dungeon dg = getDungeon(dungeonName);
		// Teleport to lobby
		p.teleport(dg.Lobby);
		// Increment dead player count
		++dg.DeadPlayerCount;
		// Are all players dead?
		if (dg.DeadPlayerCount >= dg.getRequiredPlayer())
			EndDungeon(dg, DungeonEndingCauses.FAIL);
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
						+ "outX, outY, outZ, outYaw, outPitch, firstRoom) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
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
			ps.setInt(14, 0);

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
			{
				int id = rs.getInt(1);
				Dungeon dg = new Dungeon(id, dungeonName, requiredPlayers);
				dg.Lobby = spawn;
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
			throw new HeavenException("Erreur fatale lors de la suppression du donjon. Informez en un administrateur.");
		}
		ChatUtil.sendMessage(player, DUNGEON_DELETED, dg.getName());
		return;
	}

	/**
	 * Updates the first dungeonRoom of a given dungeon.
	 * 
	 * @param invoker
	 * @param dungeonName
	 * @param roomId
	 * @throws HeavenException
	 */
	public static void UpdateFirstRoom(final Player invoker, final String dungeonName, final int roomId)
			throws HeavenException
	{
		Dungeon dg = getDungeon(dungeonName);
		if (dg == null)
			throw new HeavenException("Ce donjon n'existe pas.");
		if (!dg.Rooms.containsKey(roomId))
			throw new HeavenException("ID de la salle inconnue.");

		try (PreparedStatement ps = HeavenRP.getConnection()
				.prepareStatement("UPDATE dungeons SET firstRoom = ? WHERE dungeon_id = ?"))
		{
			ps.setInt(1, roomId);
			ps.setInt(2, dg.getId());
			ps.executeUpdate();
			// Update first room
			Dungeons.get(dg.getId()).firstRoomId = roomId;
			ps.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new HeavenException("Erreur fatale lors de la mise a jour du donjon. Informez en un administrateur.");
		}
		ChatUtil.sendMessage(invoker, DUNGEON_FIRST_ROOM_SET, dg.getName());
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

		try (PreparedStatement ps = HeavenRP.getConnection()
				.prepareStatement(
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
				final DungeonRoom dgr = new DungeonRoom(dg.getId(), roomUuniqueId, spawn, trigger, corner1, corner2);
				Dungeons.get(dg.getId()).Rooms.put(roomUuniqueId, dgr);
				ChatUtil.sendMessage(p, DUNGEON_ROOM_CREATED, roomUuniqueId);
				if (dg.firstRoomId == 0)
				{
					DungeonManager.UpdateFirstRoom(p, dungeonName, roomUuniqueId);
				}
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

	/**
	 * Deletes a given room from the dungeon.
	 * 
	 * @param player
	 * @param roomId
	 * @throws HeavenException
	 */
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
	 * Ends a dungeon and resets
	 * 
	 * @param dg
	 */
	private static void EndDungeon(final Dungeon dg, DungeonEndingCauses cause)
	{
		final int teleportWarmup = 30;
		// Update Dungeon State to Ending
		dg.DungeonState = DungeonStates.ENDING;
		// List of all in dungeon players UUID
		ArrayList<UUID> playersUUID = new ArrayList<UUID>();

		// Clear all mobs from the dungeon
		for (int rmId : dg.Rooms.keySet())
		{
			DungeonRoom dgr = dg.Rooms.get(rmId);
			for (LivingEntity lety : dgr.getSpawn().getWorld().getLivingEntities())
			{
				if (dgr.Mobs.containsKey(lety.getUniqueId()))
				{
					lety.remove();
				}
			}
			dgr.Mobs.clear();
		}

		// Teleport player out
		for (Player p : getPlayersByDungeon(dg.getId()))
		{
			playersUUID.add(p.getUniqueId());
			// Announce why dungeon is ending
			if (cause == DungeonEndingCauses.VICTORY)
				ChatUtil.sendMessage(p, WON_DUNGEON);
			else if (cause == DungeonEndingCauses.FAIL)
			{
				DeadDungeonPlayerInventoryStore.DiscardInventory(p.getUniqueId());
				ChatUtil.sendMessage(p, LOST_DUNGEON);
			}
			else if (cause == DungeonEndingCauses.FLEED)
				ChatUtil.sendMessage(p, SOMEONE_OF_YOUR_TEAM_FLEED);
			// Apply effects
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, teleportWarmup * 2, 10));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, teleportWarmup * 2, 10));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, teleportWarmup * 2, 10));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, teleportWarmup * 2, 10));
		}

		// Teleport players & Reset dungeon
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HeavenRP.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				for (UUID uid : playersUUID)
				{
					// Get player
					Player p = Bukkit.getPlayer(uid);
					// we are in a delayed event, player could have disconnected
					if (p == null)
					{
						DeadDungeonPlayerInventoryStore.DiscardInventory(uid);
					}
					else
					{
						if (DeadDungeonPlayerInventoryStore.hasStored(p))
							DeadDungeonPlayerInventoryStore.RestoreInventory(p);
						p.teleport(dg.ExitPoint);
					}
					// Remove player from playing list
					playerDungeon.remove(uid);
				}

				// Reset dungeon state
				dg.PlayerCount = 0;
				dg.DeadPlayerCount = 0;
				// Update Dungeon State to Ending
				dg.DungeonState = DungeonStates.IDLE;
			}
		}, teleportWarmup);
	}

	/**
	 * Returns the dungeon where the player is.
	 * 
	 * @param player
	 * @return Dungeon or null
	 */
	public static Dungeon getDungeonByPlayer(final Player player)
	{
		if (!playerDungeon.containsKey(player.getUniqueId()))
			return null;
		int dungeonId = playerDungeon.get(player.getUniqueId());
		return Dungeons.get(dungeonId);
	}

	/**
	 * Returns the room where the given location is.
	 * 
	 * @param loc
	 * @return DungeonRoom or null if none
	 */
	public static DungeonRoom getRoomByLocation(final Location loc)
	{
		for (Dungeon dg : Dungeons.values())
		{
			for (DungeonRoom dgr : dg.Rooms.values())
			{
				// Are we in the same world?
				if (loc.getWorld().getUID() != dgr.spawn.getWorld().getUID())
					break; // Will not be this dungeon
				// Are we at the right height
				int lowY = Math.min(dgr.corner1.getBlockY(), dgr.corner2.getBlockY());
				int highY = Math.max(dgr.corner1.getBlockY(), dgr.corner2.getBlockY());
				if (loc.getBlockY() < lowY || loc.getBlockY() > highY)
					continue; // Nope, next dungeon
				int lowX = Math.min(dgr.corner1.getBlockX(), dgr.corner2.getBlockX());
				int highX = Math.max(dgr.corner1.getBlockX(), dgr.corner2.getBlockX());
				if (loc.getBlockX() < lowX || loc.getBlockX() > highX)
					continue; // Nope, next dungeon
				int lowZ = Math.min(dgr.corner1.getBlockZ(), dgr.corner2.getBlockZ());
				int highZ = Math.max(dgr.corner1.getBlockZ(), dgr.corner2.getBlockZ());
				if (loc.getBlockZ() < lowZ || loc.getBlockZ() > highZ)
					continue; // Nope, next dungeon
				return dgr;
			}
		}
		return null;
	}

	/**
	 * Handles when a mob disappears
	 * 
	 * @param uid
	 */
	public static void HandleMobKill(final LivingEntity e)
	{
		for (Dungeon dg : Dungeons.values())
		{
			for (DungeonRoom dgr : dg.Rooms.values())
			{
				if (dgr.Mobs.containsKey(e.getUniqueId()))
				{
					dgr.Mobs.remove(e.getUniqueId());
					System.out.println("MOB DEAD, left: " + dgr.Mobs.size());
					return;
				}
			}
		}
	}

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
	 * Returns a dungeon by it'S id
	 * 
	 * @param dungeonId
	 * @return
	 */
	public static Dungeon getDungeon(int dungeonId)
	{
		return Dungeons.get(dungeonId);
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
	 * Returns a list of players inside a dungeon
	 * 
	 * @param dungeonId
	 * @return
	 */
	private static ArrayList<Player> getPlayersByDungeon(int dungeonId)
	{
		ArrayList<Player> list = new ArrayList<Player>();

		for (UUID uid : playerDungeon.keySet())
			if (playerDungeon.get(uid) == dungeonId)
				list.add(Bukkit.getPlayer(uid));

		return list;
	}

	/**
	 * Returns if a player is inside a dungeon
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isPlayeing(Player p)
	{
		return playerDungeon.containsKey(p.getUniqueId());
	}

	/**
	 * Displays a list of available dungeons and their state
	 * 
	 * @param p
	 */
	public static void PrintDungeonList(final Player p)
	{
		ChatUtil.sendMessage(p, "");
		ChatUtil.sendMessage(p, "╔═════════Donjons═════════╗");
		int i = 1; // Counter
		for (DungeonManager.Dungeon dg : DungeonManager.Dungeons.values())
		{
			char linePrefix = (i >= DungeonManager.Dungeons.values().size()) ? '└' : '├';
			ChatUtil.sendMessage(p, "%1$c ({%2$d}) %3$s | Joueurs: {%4$d}/{%5$d}", linePrefix, dg.getId(), dg.getName(),
					dg.PlayerCount, dg.getRequiredPlayer());
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
				blockType = dgr.getTriggerBlock().getBlock().getState().getType().toString();

			ChatUtil.sendMessage(p,
					"%1$c {ID}: %2$d | {Spawn}: %3$.2f %4$.2f %5$.2f %6$.2f %7$.2f | {Trigger}: %8$d %9$d %10$d (%11$s) "
							+ "{Mobs}: %12$d",
					linePrefix, dgr.getRoomId(), dgr.getSpawn().getX(), dgr.getSpawn().getY(), dgr.getSpawn().getZ(),
					dgr.getSpawn().getYaw(), dgr.getSpawn().getPitch(), dgr.getTriggerBlock().getBlockX(),
					dgr.getTriggerBlock().getBlockY(), dgr.getTriggerBlock().getBlockZ(), blockType, dgr.Mobs.size());
		}

	}

	/**
	 * Displays detailed information about a mobs in a Dungeon
	 * 
	 * @param p
	 * @param dungeon
	 * @throws HeavenException
	 */
	public static void PrintDungeonMobInfo(final Player p, final String dungeon) throws HeavenException
	{
		final Dungeon dg = DungeonManager.getDungeon(dungeon);
		if (dg == null)
			throw new HeavenException("Dojnon recherché inconnu.");
		for (DungeonRoom dgr : dg.Rooms.values())
		{
			if (dgr.Mobs.size() <= 0)
				continue;

			for (LivingEntity lety : dgr.getSpawn().getWorld().getLivingEntities())
			{
				if (dgr.Mobs.containsKey(lety.getUniqueId()))
				{
					ChatUtil.sendMessage(p, "- %1$s %2$.2f %3$.2f %4$.2f", lety.getType().toString(),
							lety.getLocation().getX(), lety.getLocation().getY(), lety.getLocation().getZ());
				}
			}
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

		public int getFirstRoomId()
		{
			return firstRoomId;
		}

		public void setFirstRoomId(int firstRoomId)
		{
			this.firstRoomId = firstRoomId;
		}

		private int id;
		private String name;
		private int requiredPlayer = 1;
		private int firstRoomId = 0;
		public int PlayerCount = 0;
		public int DeadPlayerCount = 0;
		public int CurrentRoomId = 0;
		public DungeonStates DungeonState = DungeonStates.IDLE;

		public Location Lobby = null;
		public Location ExitPoint = null;

		public HashMap<Integer, DungeonRoom> Rooms = new HashMap<Integer, DungeonRoom>();

	}

	public static class DungeonRoom
	{
		private int dungeonId;
		private int roomId;
		private final Location corner1;
		private final Location corner2;
		public HashMap<UUID, LivingEntity> Mobs = new HashMap<UUID, LivingEntity>();

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

		public DungeonRoom(int dungeonId, int roomId, Location spawn, Location triggerBlock, Location corner1,
				Location corner2)
		{
			this.dungeonId = dungeonId;
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

		public int getDungeonId()
		{
			return dungeonId;
		}
	}

	public enum DungeonStates
	{
		IDLE, BOOTING, RUNNING, ENDING;
	}

	public enum DungeonEndingCauses
	{
		FAIL, VICTORY, FLEED;
	}

	static class RestoreBlockTask implements Runnable
	{
		String _world;
		int _x;
		int _y;
		int _z;
		Material _type;

		public RestoreBlockTask(String world, int x, int y, int z, Material type)
		{
			_world = world;
			_x = x;
			_y = y;
			_z = z;
			_type = type;
		}

		@Override
		public void run()
		{
			Bukkit.getServer().getWorld(_world).getBlockAt(_x, _y, _z).setType(_type);
		}
	}

	static class DungeonMobCacheCorruptionCheckTask implements Runnable
	{

		@Override
		public void run()
		{
			for (Dungeon dg : Dungeons.values())
			{
				if (dg.DungeonState != DungeonManager.DungeonStates.RUNNING)
					continue;

				for (DungeonRoom dgr : dg.Rooms.values())
				{
					if (dgr.Mobs.size() <= 0)
						continue;

					for (UUID uid : dgr.Mobs.keySet())
					{
						boolean found = false;
						for (LivingEntity lety : dgr.getSpawn().getWorld().getLivingEntities())
						{
							if(lety.getUniqueId() == uid)
							{
								found = true;
								break;
							}
						}
						// Mob not found, it is a bugged one.
						if(!found)
							dgr.Mobs.remove(uid);
					}
				}
			}
		}
	}
}
