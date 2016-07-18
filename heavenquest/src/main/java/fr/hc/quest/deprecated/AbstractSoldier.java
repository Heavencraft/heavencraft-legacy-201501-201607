package fr.hc.quest.deprecated;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.quest.npc.Team;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public abstract class AbstractSoldier implements Soldier
{
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private final String name;
	private final Location spawnLocation;
	private final Team team;

	protected final NPC npc;

	public AbstractSoldier(String name, Location spawnLocation, Team team)
	{
		this.name = name;
		this.spawnLocation = spawnLocation;
		this.team = team;

		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
		npc.setProtected(false);
		npc.spawn(spawnLocation);

		SoldierListener.get().register(npc, this);
	}

	@Override
	public void onDeath()
	{
		log.info("Soldier {} is dead, respawning NPC {}.", name, npc);
		npc.spawn(spawnLocation);
	}
}