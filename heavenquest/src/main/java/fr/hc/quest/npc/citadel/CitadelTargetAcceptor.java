package fr.hc.quest.npc.citadel;

import org.bukkit.entity.Entity;

import fr.hc.quest.goals.TargetAcceptor;
import fr.hc.quest.npc.HeavenNPC;
import fr.hc.quest.npc.HeavenNPCRegistry;
import fr.hc.quest.npc.Team;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class CitadelTargetAcceptor implements TargetAcceptor
{
	public static final TargetAcceptor INSTANCE = new CitadelTargetAcceptor();

	@Override
	public boolean accept(Entity target)
	{
		if (target == null)
			return false;

		final NPC targetCitizen = CitizensAPI.getNPCRegistry().getNPC(target);
		if (targetCitizen == null)
			return false;

		final HeavenNPC targetNPC = HeavenNPCRegistry.get().getNPC(targetCitizen);
		if (targetNPC == null)
			return false;

		return targetNPC.getTeam() == Team.EMPIRE;
	}
}