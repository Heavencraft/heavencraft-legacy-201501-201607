package fr.heavencraft.rpg.artifacts;

import org.bukkit.entity.Player;

public interface Artifact
{
	/**
	 * Check if the player can use the artifact
	 * 
	 * @param player
	 *            The player to test
	 * @return true if the player is able to use the artifact, false elsewhere
	 */
	boolean canUse(Player player);

	void use(Player player);
}