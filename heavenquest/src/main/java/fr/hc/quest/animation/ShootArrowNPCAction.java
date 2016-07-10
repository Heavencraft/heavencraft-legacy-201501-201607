package fr.hc.quest.animation;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.HumanEntity;

public class ShootArrowNPCAction implements NPCAction
{
	private final HumanEntity npc;

	public ShootArrowNPCAction(HumanEntity npc)
	{
		this.npc = npc;
	}

	@Override
	public void perform()
	{
		npc.launchProjectile(Arrow.class);
	}
}