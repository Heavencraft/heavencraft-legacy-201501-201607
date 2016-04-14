package fr.heavencraft.heavenrp;

import fr.heavencraft.heavenrp.quests.SampleQuest;

/**
 * This class allows registering of Quests into the framework
 * @author Manue
 *
 */
public class QuestInitialisation
{
	public static void init()
	{
		new SampleQuest();
	}
}
