package fr.heavencraft.heavenrp.dungeon;

import org.bukkit.Location;

public class DungeonRoom
{
	public enum DungeonRoomType
	{
		LOBBY,
		ROOM
	}
	
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
