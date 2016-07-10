package fr.hc.quest.npc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;

import fr.hc.quest.HeavenQuest;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;

public class SoldierListener extends AbstractListener<HeavenQuest>
{
	private final Map<NPC, Soldier> soldierByNPC = new HashMap<NPC, Soldier>();

	private SoldierListener()
	{
		super(HeavenQuest.get());
	}

	public void register(NPC npc, Soldier soldier)
	{
		soldierByNPC.put(npc, soldier);
	}

	@EventHandler
	private void onNPCDeath(NPCDeathEvent event)
	{
		final Soldier soldier = soldierByNPC.get(event.getNPC());
		if (soldier == null)
			return;

		soldier.onDeath();
	}

	/*
	 * Singleton pattern
	 */

	private static SoldierListener instance;

	public static SoldierListener get()
	{
		if (instance == null)
			instance = new SoldierListener();

		return instance;
	}
}