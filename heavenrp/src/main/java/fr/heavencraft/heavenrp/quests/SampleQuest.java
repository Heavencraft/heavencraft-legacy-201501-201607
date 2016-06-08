package fr.heavencraft.heavenrp.quests;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.questframework.AbstractQuest;
import fr.heavencraft.heavenrp.questframework.QfFlag;
import fr.heavencraft.heavenrp.questframework.QuestContext;

public class SampleQuest extends AbstractQuest
{
	
	public SampleQuest()
	{
		super("My Sample Quest");
	}

	private QfFlag qflag1 = new QfFlag("MyFlag1");
	
	@Override
	protected boolean PlayerMeetStartRequirements(Player p)
	{
		if(p.getName().equalsIgnoreCase("Manu67100") ||
				p.getName().equalsIgnoreCase("lorgan17"))
			return true;
		
		ChatUtil.sendMessage(p, "Tu ne t'appelles pas Manu toi...");
		return false;
	}

	@Override
	protected void InitializeQuest(Player p, QuestContext context)
	{
		// Accessing Quest related flags
		System.out.println(super.getQuestContext(p).hasFlag(qflag1));
		System.out.println(super.getQuestContext(p).getValue(qflag1));
		// Accessing Player related flags
		System.out.println(super.getPlayerContext(p).hasFlag(PlayerFlags.FIRST_TEST_FLAG.getFlag()));
	}
}
