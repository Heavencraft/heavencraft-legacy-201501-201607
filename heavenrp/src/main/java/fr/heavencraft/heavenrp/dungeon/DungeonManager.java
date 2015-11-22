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

import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.User;

public class DungeonManager
{
	// Hashmap assiociation between player and the dungeon he is inside
	private static HashMap<UUID, Dungeon> playerDungeon = new HashMap<UUID, Dungeon>();
	// List containing all Dungeons
	private static List<Dungeon> dungeons = new ArrayList<Dungeon>();
	// Hashmap assiociation between dungeon ID and the rooms
	private static HashMap<Integer, DungeonRoom> dungeonRooms = new HashMap<Integer, DungeonRoom>();
	
	// TODO load dungeons
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
				// Store Dungeon
				dungeons.add(dg);
				// Store room
				dungeonRooms.put(dg.getId(), lobby);
			}
			
			// For each room, add into dungeonRoom
			//TODO Load rooms and register them
			
		}
		catch (final SQLException ex)
		{
			
		}
	}

	/* ~~ Generic Section ~~ */

	/**
	 * Makes a user enter a dungeon.
	 * 
	 * @param u
	 * @return
	 */
	public static boolean UserJoin(User u)
	{
		// TODO self generated function
		return true;
	}

	/**
	 * Makes a user leave a dungeon
	 * 
	 * @param u
	 * @return
	 */
	public static boolean UserLeave(User u)
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
	public static boolean AttempNextRoom(User u, int nextRoomId)
	{
		// TODO self generated function
		return true;
	}

	public static boolean UserDies(User u)
	{
		// TODO self generated function
		return true;
	}

	public static boolean RestoreInventory(User u)
	{
		// TODO self generated function
		return true;
	}

	/* ~~ Edit Section ~~ */

	public static void CreateDungeon(Player p, String dungeonName) throws SQLErrorException
	{
		if (getDungeon(dungeonName) != null)
		{
			//throw new HeavenException("Ce donjon existe déjà.");
		}
		
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
			throw new SQLErrorException();
		}
	}

	public static boolean DeleteDungeon(User u)
	{
		// TODO self generated function
		return true;
	}

	public static boolean AddDungeonRoom(User u)
	{
		// TODO self generated function
		return true;
	}

	public static boolean RemoveDungeonRoom(User u)
	{
		// TODO self generated function
		return true;
	}

	/* ~~ Utils Section ~~ */

	private static Dungeon getDungeon(String dungeonName)
	{
		for (Dungeon dg : dungeons)
			if (dg.getName().equalsIgnoreCase(dungeonName))
				return dg;
		return null;
	}
}
