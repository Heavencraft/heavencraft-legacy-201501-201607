package fr.heavencraft.heavenrp.questframework;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import fr.heavencraft.heavencore.utils.BookBuilder;

public class QuestBookHandler
{
	private static QuestBookHandler questBookHandler = null;

	public static QuestBookHandler getInstance()
	{
		if (questBookHandler == null)
			questBookHandler = new QuestBookHandler();
		return questBookHandler;
	}

	public BookMeta GenerateMeta(Player p, BookMeta meta)
	{
		
		// Clear the old book
		meta.setPages(new ArrayList<String>());

		meta.addPage("\n\n\n\n       Journal des Quêtes");

		// Quest index
		StringBuilder pageText = new StringBuilder();

		for (int qIndex = 0; qIndex < QuestFramework.getInstance().GetPlayerQuests(p).size(); qIndex++)
		{

			AbstractQuest q = ((List<AbstractQuest>) QuestFramework.getInstance().GetPlayerQuests(p)).get(qIndex);
			String questName = q.getQuestName();
			if (questName.length() >= 21)
			{
				questName = q.getQuestName().substring(0, 15) + "...";
			}

			pageText.append(qIndex + 1);
			pageText.append(": ");
			pageText.append(questName);
			pageText.append("\n");

			// page wrap
			if (qIndex == 12 || qIndex == (QuestFramework.getInstance().GetPlayerQuests(p).size() - 1))
			{
				meta.addPage(pageText.toString());
				pageText = new StringBuilder();
			}

		}

		meta.setAuthor("LaLicorne");
		meta.setTitle("Quêtes");

		return meta;
	}

}
