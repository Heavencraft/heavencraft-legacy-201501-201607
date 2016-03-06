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
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;

public class DungeonManager
{
	// Exceptions
	private static String ALREADY_IN_DUNGEON = "Vous ètes déjà dans un donjon.";
	private static String NAME_UNKNOWN_DUNGEON = "Le nom du donjon est invalide.";
	private static String DUNGEON_FULL = "Il y a déjà quelqu'un dans ce donjon...";
	private static String DUNGEON_WAITING_FOR_PLAYERS = "Je dois attendre encore %1$d camarade(s).";
	
	
	// Hashmap association playerUUID --> DungeonID
	private static HashMap<UUID, Integer> playerDungeon = new HashMap<UUID, Integer>();
	// Hashmap association DungeonID --> Dungeon
	private static HashMap<Integer, Dungeon> dungeons = new HashMap<Integer, Dungeon>();

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
				Location spawn = new Location(
						Bukkit.getWorld(dungeonResultSet.getString("world")),
						dungeonResultSet.getDouble("spawnX"),
						dungeonResultSet.getDouble("spawnY"),
						dungeonResultSet.getDouble("spawnZ"),
						dungeonResultSet.getFloat("spawnYaw"),
						dungeonResultSet.getFloat("spawnPitch"));
				// Trigger block location
				Location trigger = new Location(
						Bukkit.getWorld(dungeonResultSet.getString("world")),
						dungeonResultSet.getDouble("triggerX"),
						dungeonResultSet.getDouble("triggerY"),
						dungeonResultSet.getDouble("triggerZ"));
				// Generate a lobby room
				DungeonRoom lobby = new DungeonRoom(0, spawn, trigger, DungeonRoomType.LOBBY);
				// Store room
				dg.getRooms().put(0, lobby);
				// Store Dungeon
				dungeons.put(dg.getId(), dg);
				
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
		for(final Dungeon dg : dungeons.values())
			if(dg.getName().equalsIgnoreCase(dungeonName))
				dungeon = dg;
		if(dungeon == null)
			throw new HeavenException(NAME_UNKNOWN_DUNGEON);
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
	 * A player attemp to change room
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

	public static void CreateDungeon(Player p, String dungeonName) throws HeavenException
	{
		if (getDungeon(dungeonName) != null)
			throw new HeavenException("Ce donjon existe déjà.");

		//TODO Finish this one
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"INSERT INTO dungeons (name) VALUES (?);"))
		{
			ps.setString(1, dungeonName);
			ps.executeUpdate();

		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	public static boolean DeleteDungeon(Player u)
	{
		// TODO self generated function
		return true;
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
		for (Dungeon dg : dungeons.values())
			if (dg.getName().equalsIgnoreCase(dungeonName))
				return dg;
		return null;
	}



	
	
	
	/* ~~ Object Classes ~~ */
	public class Dungeon
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

		private HashMap<Integer, DungeonRoom> rooms = new HashMap<Integer, DungeonRoom>();


	}

	public enum DungeonRoomType
	{
		LOBBY,
		ROOM
	}

	public class DungeonRoom
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
