package fr.hc.quest.animation;

import org.bukkit.entity.HumanEntity;

public class BowNPCAction implements NPCAction
{
	private final HumanEntity npc;

	public BowNPCAction(HumanEntity npc)
	{
		this.npc = npc;
	}

	@Override
	public void perform()
	{
	}

}
