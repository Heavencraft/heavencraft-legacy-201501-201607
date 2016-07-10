package fr.hc.quest.npc;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public abstract class AbstractSoldier implements Soldier
{
	private final NPC npc;
	private final Location spawnLocation;

	public AbstractSoldier(String name, Location spawnLocation)
	{
		this.spawnLocation = spawnLocation;

		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
		npc.setProtected(false);
		npc.spawn(spawnLocation);

		SoldierListener.get().register(npc, this);
	}

	@Override
	public void onDeath()
	{
		npc.spawn(spawnLocation);
	}
}