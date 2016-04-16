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

	public static QuestFramework getInstance()
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
		if (!(aQuest instanceof Quest))
			throw new HeavenException("The quest %1$s does not implement the necessary interface Quest",
					aQuest.getQuestName());
		
		
		// TODO Reference instance of quest to ensure each quest is a singleton
	}

	// TODO UT
	public Collection<AbstractQuest> GetPlayerQuests(Player p)
	{
		return null;
		// return PlayerContextCache.getQuests(p.getUniqueId());
	}
	
	
	
	public PlayerContext getPlayerContext(Player p) {
		return PlayerContextCache.getPlayerContext(p.getUniqueId());
	}
	
	/**
	 * Returns the context of a specified quest for a certain player
	 * @param p Player
	 * @param q Quest
	 * @return <code>null</code> if none found
	 */
	public QuestContext getQuestContext(Player p, AbstractQuest q) {
		List<QuestContext> contexts = QuestContextListCache.getInstance().getQuestContextList(p.getUniqueId());
		for(QuestContext qc : contexts)
			if(qc.getQuestId() == q.getQuestId())
				return qc;
		return null;
	}

}
