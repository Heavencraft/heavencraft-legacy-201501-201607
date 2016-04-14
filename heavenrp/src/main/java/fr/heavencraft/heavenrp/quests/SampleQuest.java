package fr.heavencraft.heavenrp.quests;

import org.bukkit.entity.Player;

import fr.heavencraft.heavenrp.questframework.AbstractQuest;
import fr.heavencraft.heavenrp.questframework.QuestContext;

public class SampleQuest extends AbstractQuest
{
	
	public SampleQuest()
	{
		super("My Sample Quest");
	}

	@Override
	protected boolean PlayerMeetStartRequirements(Player p)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void InitializeQuest(Player p, QuestContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void SaveProgression(Player p)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void RestoreProgression(Player p)
	{
		// TODO Auto-generated method stub
		
	}

}
