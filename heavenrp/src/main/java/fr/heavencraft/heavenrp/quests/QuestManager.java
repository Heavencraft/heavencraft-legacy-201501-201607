package fr.heavencraft.heavenrp.quests;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class QuestManager
{
	private static QuestManager questManager = null;

	public static QuestManager getInstance()
	{
		if (questManager == null)
			questManager = new QuestManager();
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
		QuestCache.RegisterQuest(p, quest);
		quest.InitializeQuest(p, new QuestContext());
	}

	public void RequestQuestLog()
	{

	}

}
