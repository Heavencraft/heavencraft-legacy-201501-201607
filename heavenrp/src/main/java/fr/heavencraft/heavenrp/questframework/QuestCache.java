package fr.heavencraft.heavenrp.questframework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class QuestCache
{
	private static Map<UUID, List<AbstractQuest>> playerQuests = new HashMap<UUID, List<AbstractQuest>>();

	/**
	 * Adds a quest to a player
	 * 
	 * @param p The players UUID
	 * @param quest
	 */
	public static void RegisterQuest(UUID p, AbstractQuest quest)
	{
		List<AbstractQuest> exitingQuests = playerQuests.get(p);
		if (exitingQuests == null)
		{
			List<AbstractQuest> coll = new ArrayList<AbstractQuest>();
			coll.add(quest);
			playerQuests.put(p, coll);
		}
	}

	/**
	 * Returns a collection of a players active quests for a player
	 * 
	 * @param p UUID of the player
	 * @return a collection of quests
	 */
	public static Collection<AbstractQuest> GetQuests(UUID p)
	{
		Collection<AbstractQuest> existingQuests = playerQuests.get(p);
		if (existingQuests == null)
			existingQuests = new ArrayList<AbstractQuest>();
		return existingQuests;
	}

}
