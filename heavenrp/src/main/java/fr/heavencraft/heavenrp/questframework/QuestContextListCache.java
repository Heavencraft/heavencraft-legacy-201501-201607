package fr.heavencraft.heavenrp.questframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QuestContextListCache
{
	private static QuestContextListCache instance = null;
	/**
	 * Returns the instance of the object
	 * @return
	 */
	public static QuestContextListCache getInstance()
	{
		if(QuestContextListCache.instance == null)
			QuestContextListCache.instance = new QuestContextListCache();
		return QuestContextListCache.instance;
	}
	
	private Map<UUID, List<QuestContext>> questContextListStore = new HashMap<UUID, List<QuestContext>>();
	
	/**
	 * Adds a Quest context to the cache
	 * @param p
	 * @param context
	 */
	public void addQuestContext(UUID p, QuestContext context)
	{
		List<QuestContext> lst = getQuestContextList(p);
		if(lst == null)
			lst = new ArrayList<QuestContext>();
		lst.add(context);
		questContextListStore.put(p, lst);
	}
	
	/**
	 * Returns a list of quest contexts from the cache
	 * @param p
	 * @return a list of quest contexts
	 */
	public List<QuestContext> getQuestContextList(UUID p)
	{
		List<QuestContext> result = questContextListStore.get(p);
		if(result == null)
			result = new ArrayList<QuestContext>();
		return result;
	}
	
	/**
	 * Deletes a Quest context from the cache
	 * @param p
	 * @return
	 */
	public void removeQuestContext(UUID p, QuestContext context)
	{
		List<QuestContext> lst = getQuestContextList(p);
		if(lst == null)
			return;
		lst.remove(context);
		questContextListStore.put(p, lst);
	}
	
	/**
	 * Clear all the quest contexts of a player from the cache
	 * @param p
	 * @return
	 */
	public void removeQuestContextList(UUID p)
	{
		questContextListStore.remove(p);
	}
}
