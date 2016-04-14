package fr.heavencraft.heavenrp.questframework;

import java.util.UUID;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;

/**
 * @author Manuel
 *
 */
public abstract class AbstractQuest
{
	private final UUID questId;

	private String questName = "My Quest Name";

	protected QuestContext questContext;

	/**
	 * This constructor should only be called when loading an already existing quest
	 * @param questId
	 * @param questName
	 */
	protected AbstractQuest(UUID questId, String questName)
	{
		this.questId = questId;
		this.questName = questName;
	}
	
	/**
	 * Creates a new quest.
	 * @param questName
	 */
	public AbstractQuest(String questName)
	{
		this.questId = UUID.randomUUID();
		this.questName = questName;
	}

	/**
	 * Returns the UID of the quest
	 * 
	 * @return
	 */
	public UUID getQuestId()
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
