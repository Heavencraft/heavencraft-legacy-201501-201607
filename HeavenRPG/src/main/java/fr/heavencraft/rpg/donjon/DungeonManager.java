package fr.heavencraft.rpg.donjon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.heavencraft.Utils.ChatUtil;
import fr.heavencraft.Utils.DevUtils;
import fr.heavencraft.rpg.RPGFiles;
import fr.heavencraft.rpg.donjon.Dungeon.DungeonRoom;

public class DungeonManager implements Listener{
	private static List<Dungeon> _dungeons = new ArrayList<Dungeon>();
	private static boolean _debug = false;

	public static List<Dungeon> get_dungeons() {
		return _dungeons;
	}	

	public static void loadDungeons()
	{
		_dungeons.clear();
		if(RPGFiles.getDungeons().getConfigurationSection("Dungeons") != null)
		{
			for(String dname : RPGFiles.getDungeons().getConfigurationSection("Dungeons").getKeys(false))
			{
				try
				{
					Dungeon dg = new Dungeon(dname,
							RPGFiles.getDungeons().getInt("Dungeons." + dname + ".requiredPlayers"),
							DevUtils.deserializeLoc(RPGFiles.getDungeons().getString("Dungeons." + dname + ".lobby")));

					// Load all dungeons rooms
					for(String roomidx : RPGFiles.getDungeons().getConfigurationSection("Dungeons." + dname + ".rooms").getKeys(false))
					{
						Location lsp = DevUtils.deserializeLoc(RPGFiles.getDungeons().getString("Dungeons." + dname + ".rooms." + roomidx + ".spawn"));
						Location l1 = DevUtils.deserializeLoc(RPGFiles.getDungeons().getString("Dungeons." + dname + ".rooms." + roomidx + ".l1"));
						Location l2 = DevUtils.deserializeLoc(RPGFiles.getDungeons().getString("Dungeons." + dname + ".rooms." + roomidx + ".l2"));
						Location trig = null;
						if(RPGFiles.getDungeons().getString("Dungeons." + dname + ".rooms." + roomidx + ".trigger") != null)		
							trig = DevUtils.deserializeLoc(RPGFiles.getDungeons().getString("Dungeons." + dname + ".rooms." + roomidx + ".trigger"));

						DungeonRoom dgr = new DungeonRoom(Integer.parseInt(roomidx), lsp, new CuboidSelection(l1.getWorld(), l1, l2), trig);
						dg.addDungeonRoom(dgr);	
					}
					DungeonManager.addDungeon(dg);
				}
				catch(Exception ex)
				{
					ChatUtil.sendMessage(Bukkit.getConsoleSender(), "[ERROR] Le donjon " + dname + " est corrompu! vérifiez la config!");
				}
				
			}
		}
	}


	/**
	 * Retourne un donjon en fonction de son nom.
	 * @param name
	 * @return
	 */
	public static Dungeon getDungeonByName(String name)
	{
		for(Dungeon dg : get_dungeons())
			if(dg.get_name().equalsIgnoreCase(name))
				return dg;
		return null;
	}

	public static Dungeon getDungeonByUser(Player p)
	{
		for(Dungeon dg : get_dungeons())
			if(dg.isPlayerInside(p))
				return dg;
		return null;
	}

	private static void addDungeon(Dungeon dg)
	{
		get_dungeons().add(dg);
	}

	public static void createDungeon(String name, int reqPlayer)
	{
		Dungeon dg = new Dungeon(name, reqPlayer);
		addDungeon(dg);
	}

	public static void deleteDungeon(Dungeon dg)
	{
		dg.get_rooms().clear();
		get_dungeons().remove(dg);
		RPGFiles.getDungeons().set("Dungeons." + dg.get_name(), null);
		RPGFiles.saveDungeons();
	}

	public static void setLobby(Dungeon dg, Location loc)
	{
		dg.set_lobby(loc);
		RPGFiles.getDungeons().set("Dungeons." + dg.get_name() + ".lobby", DevUtils.serializeLoc(loc));
		RPGFiles.saveDungeons();
	}

	public static void createDungeonRoom(Dungeon dg,int idx, Location spawn, Selection cubo)
	{
		//inscrire dans le fichier cfg
		RPGFiles.getDungeons().set("Dungeons." + dg.get_name() + ".rooms." + idx + ".spawn", DevUtils.serializeLoc(spawn));
		RPGFiles.getDungeons().set("Dungeons." + dg.get_name() + ".rooms." + idx + ".l1", DevUtils.serializeLoc(cubo.getMinimumPoint()));
		RPGFiles.getDungeons().set("Dungeons." + dg.get_name() + ".rooms." + idx + ".l2", DevUtils.serializeLoc(cubo.getMaximumPoint()));
		RPGFiles.saveDungeons();
		DungeonRoom dgr = new DungeonRoom(idx, spawn, new CuboidSelection(cubo.getWorld(), cubo.getMinimumPoint(), cubo.getMaximumPoint()));
		dg.addDungeonRoom(dgr);
	}

	public static void deleteDungeonRoom(Dungeon dg, DungeonRoom dgr)
	{	
		int idx = dgr.get_index();
		RPGFiles.getDungeons().set("Dungeons." + dg.get_name() + ".rooms." + idx , null);
		RPGFiles.saveDungeons();		
		dg.removeDungeonRoom(dgr);
	}

	public static void updateDungeonRoomTrigger(Dungeon dg, DungeonRoom dgr, Location triggerBlock)
	{	
		int idx = dgr.get_index();
		RPGFiles.getDungeons().set("Dungeons." + dg.get_name() + ".rooms." + idx + ".trigger", DevUtils.serializeLoc(triggerBlock));
		RPGFiles.saveDungeons();	
		dgr.set_trigger(triggerBlock);
	}

	public static void updateDungeonRoomSpawn(Dungeon dg, DungeonRoom dgr, Location spawn)
	{	
		int idx = dgr.get_index();
		RPGFiles.getDungeons().set("Dungeons." + dg.get_name() + ".rooms." + idx + ".spawn", DevUtils.serializeLoc(spawn));
		RPGFiles.saveDungeons();		
		dg.updateDungeonRoomSpawn(dgr, spawn);
	}

	public static DungeonRoom getRoomByLocation(Location loc)
	{
		for(Dungeon dg: get_dungeons())
			for(DungeonRoom dgr : dg.get_rooms())
				if(dgr.get_region().contains(loc))
					return dgr;
		return null;
	}

	public static DungeonRoom getRoomByEntity(Entity monster)
	{
		for(Dungeon dg: get_dungeons())
			for(DungeonRoom dgr : dg.get_rooms())
				if(dgr.get_mobs().contains(monster))
					return dgr;
		return null;
	}
	
	
	public static void evacutateDungeon()
	{
		for(Dungeon dg : get_dungeons())
			if(dg.is_Running())
				dg.stopDungeon();
	}

	public static boolean is_debug() {
		return _debug;
	}



	public static void set_debug(boolean _debug) {
		DungeonManager._debug = _debug;
	}
	

}
