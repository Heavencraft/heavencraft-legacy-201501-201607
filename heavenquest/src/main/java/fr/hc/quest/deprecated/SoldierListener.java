package fr.hc.quest.deprecated;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.quest.HeavenQuest;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.npc.NPC;

public class SoldierListener extends AbstractListener<HeavenQuest>
{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Map<NPC, Soldier> soldierByNPC = new HashMap<NPC, Soldier>();

	private SoldierListener()
	{
		super(HeavenQuest.get());
	}

	public void register(NPC npc, Soldier soldier)
	{
		log.info("Registering NPC {} to Soldier {}", npc, soldier);

		soldierByNPC.put(npc, soldier);
	}

	@EventHandler
	private void onNPCDeath(NPCDeathEvent event)
	{
		// log.info("NPC death", event.getNPC());
		//
		// final Soldier soldier = soldierByNPC.get(event.getNPC());
		// if (soldier == null)
		// return;
		//
		// soldier.onDeath();
	}

	@EventHandler(ignoreCancelled = true)
	private void onNPCDespawn(NPCDespawnEvent event)
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