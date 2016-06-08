package fr.heavencraft.heavenrp.questframework;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

/**
 * @author Manuel
 *
 */
public abstract class AbstractQuest
{
	private final int questId;

	private String questName = "My Quest Name";

	/**
	 * This constructor should only be called when loading an already existing quest
	 * @param questId
	 * @param questName
	 */
	protected AbstractQuest(int questId, String questName)
	{
		this.questId = questId;
		this.questName = questName;
	}
	
	/**
	 * Creates a new quest.
	 * @param questName The name of the quest
	 */
	public AbstractQuest(String questName)
	{
		this.questId = HeavenRP.Random.nextInt(255);
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
	 * Returns the quest's context
	 * @param p The player
	 * @return
	 */
	protected QuestContext getQuestContext(Player p)
	{
		return QuestFramework.get().getQuestContext(p, this);
	}
	
	/**
	 * Returns the player's context
	 * @param p The player
	 * @return
	 */
	protected PlayerContext getPlayerContext(Player p)
	{
		return QuestFramework.get().getPlayerContext(p);
	}
	
	/**
	 * Returns if the player can start the quest
	 * 
	 * @param p
	 * @return
	 * @throws HeavenException
	 */
	abstract protected boolean PlayerMeetStartRequirements(Player p);
	
	/**
	 * Called when the quest should start.
	 * 
	 * @param p
	 *            The player
	 * @param context
	 *            The quest context
	 */
	abstract protected void InitializeQuest(Player p, QuestContext context);
	
}
