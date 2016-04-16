package fr.heavencraft.heavenrp.quests;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.questframework.AbstractQuest;
import fr.heavencraft.heavenrp.questframework.Quest;
import fr.heavencraft.heavenrp.questframework.QuestContext;

public class SampleQuest extends AbstractQuest implements Quest
{
	
	public SampleQuest()
	{
		super("My Sample Quest");
	}

	@Override
	public boolean PlayerMeetStartRequirements(Player p)
	{
		if(p.getName().equalsIgnoreCase("Manu67100"))
			return true;
		
		ChatUtil.sendMessage(p, "Tu ne t'appelles pas Manu toi...");
		return false;
	}

	@Override
	public void InitializeQuest(Player p, QuestContext context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SaveProgression(Player p)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RestoreProgression(Player p)
	{
		// TODO Auto-generated method stub
		
	}

}
