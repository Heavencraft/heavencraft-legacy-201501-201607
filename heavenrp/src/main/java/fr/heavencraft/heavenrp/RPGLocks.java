package fr.heavencraft.heavenrp;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.dungeon.DungeonManager;

public class RPGLocks
{
	/**
	 * Throws an exception if a player is disallowed to teleport.
	 * @param p Player to test
	 * @throws HeavenException
	 */
	public static void canTeleport(Player p) throws HeavenException
	{
		if (DungeonManager.isPlayeing(p))
			throw new HeavenException("Vous êtes actuellement dans un dojnon.");
	}
}
