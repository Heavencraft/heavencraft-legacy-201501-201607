package fr.hc.quest.npc;

import java.util.HashMap;
import java.util.Map;

import net.citizensnpcs.api.npc.NPC;

public class HeavenNPCRegistry
{
	private final Map<NPC, HeavenNPC> npcByCitizen = new HashMap<NPC, HeavenNPC>();

	public void register(HeavenNPC npc, NPC citizen)
	{
		npcByCitizen.put(citizen, npc);
	}

	public HeavenNPC getNPC(NPC citizen)
	{
		return npcByCitizen.get(citizen);
	}

	/*
	 * Singleton pattern
	 */

	private static final HeavenNPCRegistry instance = new HeavenNPCRegistry();

	public static HeavenNPCRegistry get()
	{
		return instance;
	}
}