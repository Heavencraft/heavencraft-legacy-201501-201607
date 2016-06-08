package fr.heavencraft.heavenrp.questframework;

import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;

/**
 * A simple management framework to handle questing
 * 
 * @author Manuel
 *
 */
public class QuestFramework
{
	private static QuestFramework questManager = null;

	
	//TODO we shall inject in any way IFlagList for global Players when initializing.
	public static QuestFramework get()
	{
		if (questManager == null)
			questManager = new QuestFramework();
		return questManager;
	}

	// TODO UT
	/**
	 * Registers a quest and make it available
	 * 
	 * @param aQuest
	 * @param p
	 * @throws HeavenException
	 */
	public void RegisterQuest(AbstractQuest aQuest, Player p) throws HeavenException
	{
		// Do we have basic Quest functions?
		
		
		
		// TODO Reference instance of quest to ensure each quest is a singleton
	}

	// TODO UT
	public Collection<AbstractQuest> GetPlayerQuests(Player p)
	{
		return null;
		// return PlayerContextCache.getQuests(p.getUniqueId());
	}
	
	
	
	protected PlayerContext getPlayerContext(Player p) {
		return PlayerContextCache.getPlayerContext(p.getUniqueId());
	}
	
	/**
	 * Returns the context of a specified quest for a certain player
	 * @param p Player
	 * @param q Quest
	 * @return <code>null</code> if none found
	 */
	protected QuestContext getQuestContext(Player p, AbstractQuest q) {
		List<QuestContext> contexts = QuestContextListCache.get().getQuestContextList(p.getUniqueId());
		for(QuestContext qc : contexts)
			if(qc.getQuestId() == q.getQuestId())
				return qc;
		return null;
	}

}
