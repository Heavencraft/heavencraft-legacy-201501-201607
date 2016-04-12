package fr.heavencraft.heavenrp.quests;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;

/**
 * @author Manuel
 *
 */
public abstract class AbstractQuest
{
	private final int questId;

	private final String questName;

	protected QuestContext questContext;

	public AbstractQuest(int questId, String questName)
	{
		this.questId = questId;
		this.questName = questName;
	}

	/**
	 * Returns the UID of the quest
	 * 
	 * @return
	 */
	public int getQuestId()
	{
		return questId;
	}

	/**
	 * Returns the quest's name
	 * 
	 * @return
	 */
	public String getQuestName()
	{
		return questName;
	}

	/**
	 * Returns if the player can start the quest
	 * 
	 * @param p
	 * @return
	 * @throws HeavenException
	 */
	protected abstract boolean PlayerMeetStartRequirements(Player p);

	/**
	 * Called when the quest should start.
	 * 
	 * @param p
	 *            The player
	 * @param context
	 *            The quest context
	 */
	protected abstract void InitializeQuest(Player p, QuestContext context);

	/**
	 * Should save state of the state of the quest
	 * 
	 * @param p
	 */
	protected abstract void SaveProgression(Player p);

	/**
	 * Should load the quest at from last save
	 * 
	 * @param p
	 */
	protected abstract void RestoreProgression(Player p);

}
