package fr.heavencraft.heavenrp.questframework;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;

/**
 * Basic interface for a quest. Needed for all kind of quests
 * @author Manuel
 *
 */
public interface Quest
{
	int getQuestId();
	/**
	 * Returns if the player can start the quest
	 * 
	 * @param p
	 * @return
	 * @throws HeavenException
	 */
	boolean PlayerMeetStartRequirements(Player p);

	/**
	 * Called when the quest should start.
	 * 
	 * @param p
	 *            The player
	 * @param context
	 *            The quest context
	 */
	void InitializeQuest(Player p, QuestContext context);

	/**
	 * Should save state of the state of the quest
	 * 
	 * @param p
	 */
	 void SaveProgression(Player p);

	/**
	 * Should load the quest at from last save
	 * 
	 * @param p
	 */
	 void RestoreProgression(Player p);
}
