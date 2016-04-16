package fr.heavencraft.heavenrp.questframework;

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

}
