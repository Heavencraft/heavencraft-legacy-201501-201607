package fr.heavencraft.heavenrp.dungeon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.mysql.jdbc.Statement;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
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
	
	
	// Hashmap association playerUUID --> DungeonID
	private static HashMap<UUID, Integer> playerDungeon = new HashMap<UUID, Integer>();
	// Hashmap association DungeonID --> Dungeon
	public static HashMap<Integer, Dungeon> Dungeons = new HashMap<Integer, Dungeon>();

	/**
	 * Constructor, loads dungeons from database
	 * @throws Exception 
	 */
	public DungeonManager() throws Exception
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"SELECT * FROM dungeons"))
		{
			final ResultSet dungeonResultSet = ps.executeQuery();

			// For each dungeon in DB, create dungeon object and store it
			while (dungeonResultSet.next()){
				// Create a new dungeon object
				Dungeon dg = new Dungeon(dungeonResultSet.getInt("dungeon_id"), dungeonResultSet.getString("name"), dungeonResultSet.getInt("requiredPlayers"));
				// Lobby spawn location
				
				// Trigger block location
				World spawnWorld = Bukkit.getWorld(dungeonResultSet.getString("world"));
				if(spawnWorld != null)
				{
					Location lobbySpawn = new Location(
							spawnWorld,
							dungeonResultSet.getDouble("spawnX"),
							dungeonResultSet.getDouble("spawnY"),
							dungeonResultSet.getDouble("spawnZ"),
							dungeonResultSet.getFloat("spawnYaw"),
							dungeonResultSet.getFloat("spawnPitch"));
					dg.Lobby = lobbySpawn;
				}			
				
				// Store Dungeon
				Dungeons.put(dg.getId(), dg);
				System.out.println("Loaded dungeon " + dg.getName());
				// For each room, add into dungeonRoom
				//TODO Load rooms and register them
				
			}
		}
		catch (final SQLException ex)
		{
			System.out.println(ex.toString());
		}
	}

	/* ~~ Generic Section ~~ */

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
		if(playerDungeon.containsKey(p.getUniqueId())){
			throw new HeavenException(ALREADY_IN_DUNGEON);
		}
		// Dungeon exists?
		Dungeon dungeon = null;
		for(final Dungeon dg : Dungeons.values())
			if(dg.getName().equalsIgnoreCase(dungeonName))
				dungeon = dg;
		if(dungeon == null)
			throw new HeavenException(NAME_UNKNOWN_DUNGEON);
		// Dungeon has lobby?
		if(dungeon.Lobby == null)
			throw new HeavenException(NO_LOBBY_DUNGEON);
		// Dungeon has free slots?
		if(dungeon.PlayerCount >= dungeon.requiredPlayer)
			throw new HeavenException(DUNGEON_FULL);
		
		// Add player to list
		playerDungeon.put(p.getUniqueId(), dungeon.getId());
		// Teleport player to lobby
		p.teleport(dungeon.getRooms().get(0).getSpawn());
		++dungeon.PlayerCount;
		
		// Do we meet requirements to play?
		if(dungeon.PlayerCount == dungeon.requiredPlayer){
			//TODO trigger start
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
	 * @param p
	 * @param dungeonName
	 * @param requiredPlayers
	 * @throws HeavenException
	 */
	public static void CreateDungeon(final Player p, final String dungeonName, final int requiredPlayers) throws HeavenException
	{
		
		if (getDungeon(dungeonName) != null)
			throw new HeavenException("Ce donjon existe déjà.");
		
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"INSERT INTO dungeons (name, requiredPlayers) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, dungeonName);
			ps.setInt(2, requiredPlayers);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
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
	 * @param u
	 * @param dungeonName
	 * @return
	 * @throws HeavenException 
	 */
	public static void DeleteDungeon(Player u, String dungeonName) throws HeavenException
	{
		Dungeon dg = getDungeon(dungeonName);
		if (dg == null)
			throw new HeavenException("Ce donjon n'existe pas.");
		
		// Delete rooms
		deleteDungeonRooms(dg);
		return;
	}
	
	private static void deleteDungeonRooms(Dungeon dungeonName) {
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"INSERT INTO dungeons (name, requiredPlayers) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, dungeonName);
			ps.setInt(2, requiredPlayers);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				final Dungeon dg = new Dungeon(id, dungeonName, requiredPlayers);
				Dungeons.put(id, dg);
				ChatUtil.sendMessage(p, DUNGEON_CREATED, dg.getName());
			}
			rs.close();
			ps.close();
			
		}
	}

	public static boolean AddDungeonRoom(Player u)
	{
		// TODO self generated function
		return true;
	}

	public static boolean RemoveDungeonRoom(Player u)
	{
		// TODO self generated function
		return true;
	}

	/* ~~ Utilities Section ~~ */
	
	/**
	 * Returns a dungeon object. 
	 * NULL if no dungeon found.
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



	
	
	
	/* ~~ Object Classes ~~ */
	public static class Dungeon
	{
		public Dungeon(int id, String name, int requiredPlayer)
		{
			super();
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
		public HashMap<Integer, DungeonRoom> getRooms()
		{
			return rooms;
		}
		
		private int id;
		private String name;
		private int requiredPlayer = 1;
		public int PlayerCount = 0;
		public Location Lobby = null;

		private HashMap<Integer, DungeonRoom> rooms = new HashMap<Integer, DungeonRoom>();


	}

	public enum DungeonRoomType
	{
		LOBBY,
		ROOM
	}

	public static class DungeonRoom
	{


		private int roomId;

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

		private DungeonRoomType roomType;

		public DungeonRoomType getRoomType()
		{
			return roomType;
		}

		public DungeonRoom(int roomId, Location spawn, Location triggerBlock, DungeonRoomType roomType)
		{
			this.roomId = roomId;
			this.spawn = spawn;
			this.triggerBlock = triggerBlock;
			this.roomType = roomType;
		}
	}

}
