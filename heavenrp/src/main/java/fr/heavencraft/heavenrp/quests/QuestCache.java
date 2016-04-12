package fr.heavencraft.heavenrp.quests;

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
	 * @param p
	 * @param quest
	 */
	public static void RegisterQuest(Player p, AbstractQuest quest)
	{
		List<AbstractQuest> exitingQuests = playerQuests.get(p.getUniqueId());
		if (exitingQuests == null)
		{
			List<AbstractQuest> coll = new ArrayList<AbstractQuest>();
			coll.add(quest);
			playerQuests.put(p.getUniqueId(), coll);
		}
	}

	/**
	 * Returns a collection of a players active quests for a player
	 * 
	 * @param p
	 * @return a collection of quests
	 */
	public static Collection<AbstractQuest> GetQuests(Player p)
	{
		Collection<AbstractQuest> existingQuests = playerQuests.get(p.getUniqueId());
		if (existingQuests == null)
			existingQuests = new ArrayList<AbstractQuest>();
		return existingQuests;
	}

}
