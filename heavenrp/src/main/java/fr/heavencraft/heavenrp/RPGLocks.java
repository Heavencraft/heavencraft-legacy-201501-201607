package fr.heavencraft.heavenrp;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.dungeon.DungeonManager;
import fr.lorgan17.heavenrp.listeners.PVP4Manager;
import fr.lorgan17.heavenrp.listeners.PVPManager;

public class RPGLocks
{
	private static RPGLocks rpgLocksInstance = null;
	public static RPGLocks getInstance()
	{
		if (rpgLocksInstance == null)
		{
			rpgLocksInstance = new RPGLocks();
		}
		return rpgLocksInstance;
	}
	
	/**
	 * Throws an exception if a player is disallowed to teleport.
	 * @param p Player to test
	 * @throws HeavenException if not allowed, containing the cause of fail.
	 */
	public void canTeleport(Player p) throws HeavenException
	{
		if (DungeonManager.isPlaying(p))
			throw new HeavenException("Vous êtes actuellement dans un donjon.");
	}
	
	/**
	 * Throws an exception if a player is disallowed to obtain his province's effects.
	 * @param p
	 * @throws HeavenException
	 */
	public void canObtainProvinceEffect(Player p) throws HeavenException
	{
		if (PVP4Manager.isPlaying(p))
			throw new HeavenException("Vous êtes actuellement dans un évènement PVP4.");
		
		if (PVPManager.isPlaying(p))
			throw new HeavenException("Vous êtes actuellement dans un évènement PVP4.");
	}
}
