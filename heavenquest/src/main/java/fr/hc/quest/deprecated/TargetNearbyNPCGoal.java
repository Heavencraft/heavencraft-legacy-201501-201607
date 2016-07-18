package fr.hc.quest.deprecated;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.hc.quest.HeavenQuest;
import fr.hc.quest.npc.NPCUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;

public class TargetNearbyNPCGoal implements Goal, Listener
{
	private static final int RADIUS = 16;

	private final NPC npc;
	private boolean npcDead = false;
	private NPC targetNPC;
	private boolean targetNPCDead = false;

	public TargetNearbyNPCGoal(NPC npc)
	{
		this.npc = npc;

		Bukkit.getPluginManager().registerEvents(this, HeavenQuest.get());
	}

	@EventHandler
	private void onNPCDeath(NPCDeathEvent event)
	{
		if (event.getNPC() == targetNPC)
			targetNPCDead = true;
		else if (event.getNPC() == npc)
			npcDead = true;
	}

	@Override
	public void reset()
	{
	}

	@Override
	public void run(GoalSelector selector)
	{
		if (targetNPCDead)
			selector.finish();
		else if (npcDead)
			selector.finish();
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{
		if (!npc.isSpawned())
			return false;

		final NPC target = getTargetNPC();
		if (target == null)
			return false;

		targetNPC = target;
		// npc.getNavigator().setTarget(target.getEntity(), true);

		return true;
	}

	private NPC getTargetNPC()
	{
		for (final Entity entity : npc.getEntity().getNearbyEntities(RADIUS, RADIUS, RADIUS))
		{
			if (!CitizensAPI.getNPCRegistry().isNPC(entity))
				continue;

			final NPC otherNPC = CitizensAPI.getNPCRegistry().getNPC(entity);

			// Same team
			if (NPCUtil.checkSameTeam(npc, otherNPC))
				continue;

			return otherNPC;
		}

		return null;
	}

}