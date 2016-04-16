package fr.heavencraft.heavenrp.questframework;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.heavencraft.heavenrp.exceptions.QuestFlagCollisionException;
import fr.heavencraft.heavenrp.exceptions.QuestFlagTypeException;
import fr.heavencraft.heavenrp.exceptions.UnknownQuestFlagException;

/**
 * This class offers a way to store "flags" as context of the quest to handle save and state
 * 
 * @author Manuel
 *
 */
public class PlayerContext
{
	private Map<String, Object> flagStore = new HashMap<String, Object>();
	private Map<UUID, List<AbstractQuest>> playerActiveQuests = new HashMap<UUID, List<AbstractQuest>>();

	/**
	 * Returns if the context contains this flag
	 * @param questFlag
	 * @return
	 */
	public boolean hasFlag(QfFlag questFlag)
	{
		if (this.flagStore.containsKey(questFlag.getKey()))
			return true;
		return false;
	}

	/**
	 * Adds a flag to a quest context
	 * 
	 * @param flag
	 * @param value
	 */
	public void addFlag(QfFlag flag, int value) throws QuestFlagCollisionException
	{
		if(this.flagStore.containsKey(flag.getKey()))
				throw new QuestFlagCollisionException(flag);
		this.flagStore.put(flag.getKey(), value);
	}
	/**
	 * Adds a flag to a quest context
	 * 
	 * @param flag
	 * @param value
	 */
	public void addFlag(QfFlag flag, boolean value) throws QuestFlagCollisionException
	{
		if(this.flagStore.containsKey(flag.getKey()))
			throw new QuestFlagCollisionException(flag);
		this.flagStore.put(flag.getKey(), value);
	}
	/**
	 * Adds a flag to a quest context
	 * 
	 * @param flag
	 * @param value
	 */
	public void addFlag(QfFlag flag, String value) throws QuestFlagCollisionException
	{
		if(this.flagStore.containsKey(flag.getKey()))
			throw new QuestFlagCollisionException(flag);
		this.flagStore.put(flag.getKey(), value);
	}
	
	/**
	 * Updates a flag's value
	 * @param flag
	 * @param value
	 * @throws UnknownQuestFlagException
	 * @throws QuestFlagTypeException 
	 */
	public void updateFlag(QfFlag flag, String value) throws UnknownQuestFlagException, QuestFlagTypeException
	{
		Object v = this.flagStore.get(flag.getKey());
		if(v == null)
			throw new UnknownQuestFlagException(flag);
		if(!(v instanceof String))
			throw new QuestFlagTypeException(flag);
		
		this.flagStore.put(flag.getKey(), value);
	}
	/**
	 * Updates a flag's value
	 * @param flag
	 * @param value
	 * @throws UnknownQuestFlagException
	 * @throws QuestFlagTypeException 
	 */
	public void updateFlag(QfFlag flag, int value) throws UnknownQuestFlagException, QuestFlagTypeException
	{
		Object v = this.flagStore.get(flag.getKey());
		if(v == null)
			throw new UnknownQuestFlagException(flag);
		if(!(v instanceof Integer))
			throw new QuestFlagTypeException(flag);
		
		this.flagStore.put(flag.getKey(), value);
	}
	
	/**
	 * Updates a flag's value
	 * @param flag
	 * @param value
	 * @throws UnknownQuestFlagException
	 * @throws QuestFlagTypeException 
	 */
	public void updateFlag(QfFlag flag, boolean value) throws UnknownQuestFlagException, QuestFlagTypeException
	{
		Object v = this.flagStore.get(flag.getKey());
		if(v == null)
			throw new UnknownQuestFlagException(flag);
		if(!(v instanceof Boolean))
			throw new QuestFlagTypeException(flag);
		
		this.flagStore.put(flag.getKey(), value);
	}
	
	/**
	 * Returns a flag's value
	 * @param flag
	 * @return
	 */
	public Object getValue(QfFlag flag)
	{
		return this.flagStore.get(flag.getKey());
	}
	
	
	
	//TODO UT
	/**
	 * Adds a quest to a player
	 * 
	 * @param p The player's UUID
	 * @param quest
	 */
	public static void registerQuest(UUID p, AbstractQuest quest)
	{
//		List<AbstractQuest> exitingQuests = playerQuests.get(p);
//		if (exitingQuests == null)
//		{
//			List<AbstractQuest> coll = new ArrayList<AbstractQuest>();
//			coll.add(quest);
//			//playerQuests.put(p, coll);
//		}
	}

	//TODO UT
	/**
	 * Returns a collection of a players active quests for a player
	 * 
	 * @param p UUID of the player
	 * @return a collection of quests
	 */
	public static Collection<AbstractQuest> getQuests(UUID p)
	{
//		Collection<AbstractQuest> existingQuests = playerQuests.get(p);
//		if (existingQuests == null)
//			existingQuests = new ArrayList<AbstractQuest>();
//		return existingQuests;
		return null;
	}
}
