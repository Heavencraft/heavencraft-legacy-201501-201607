package fr.heavencraft.heavenrp.questframework;

import java.util.Collection;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class QuestFramework
{
	private static QuestFramework questManager = null;

	public static QuestFramework getInstance()
	{
		if (questManager == null)
			questManager = new QuestFramework();
		return questManager;
	}

	/**
	 * Starts a quest for a player
	 * 
	 * @param quest
	 * @param p
	 * @throws HeavenException
	 */
	public void StartQuest(AbstractQuest quest, Player p) throws HeavenException
	{
		// Is the player able to start a quest?
		if (!quest.PlayerMeetStartRequirements(p))
			return;
		QuestCache.RegisterQuest(p.getUniqueId(), quest);
		quest.InitializeQuest(p, new QuestContext());
	}

	public Collection<AbstractQuest> GetPlayerQuests(Player p)
	{
		return QuestCache.GetQuests(p.getUniqueId());
	}

}
