package fr.heavencraft.heavenrp.questframework;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class offers a way to store "flags" as context of the quest to handle save and state
 * 
 * @author Manuel
 *
 */
public class QuestContext
{
	private Map<UUID, Object> textualFlags = new HashMap<UUID, Object>();

	public boolean hasFlag(QuestFlag questFlag)
	{
		if (this.textualFlags.containsKey(questFlag))
			return true;
		return false;
	}

	/**
	 * Adds a flag to a quest context
	 * 
	 * @param flag
	 * @param value
	 */
	public void addFlag(QuestFlag flag, int value)
	{
		this.textualFlags.put(flag.getFlagId(), value);
	}

	public void addFlag(QuestFlag flag, boolean value)
	{
		this.textualFlags.put(flag.getFlagId(), value);
	}
	
	public void addFlag(QuestFlag flag, String value)
	{
		this.textualFlags.put(flag.getFlagId(), value);
	}
	
	/**
	 * Returns a flag's value
	 * @param flag
	 * @return
	 */
	public Object getValue(QuestFlag flag)
	{
		return this.textualFlags.get(flag);
	}
}
