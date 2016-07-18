package fr.hc.quest.deprecated;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.quest.npc.NPCUtil;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.npc.NPC;
import net.jafama.FastMath;

/*
 * Goal to test NPCUtil.lookAt
 */
public class LookAtGoal implements Goal
{
	private static int RADIUS = 10;

	private static Location NPC_LOCATION = new Location(null, 0, 0, 0);
	private static Location TARGET_LOCATION = new Location(null, 0, 0, 0);

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final NPC npc;
	private Player target;

	public LookAtGoal(NPC npc)
	{
		this.npc = npc;
	}

	@Override
	public void reset()
	{
		target = null;
	}

	@Override
	public void run(GoalSelector selector)
	{
		final Entity entity = npc.getEntity();
		final Location npcLocation = entity.getLocation(NPC_LOCATION);
		final Location targetLocation = target.getLocation(TARGET_LOCATION);

		NPCUtil.faceLocation(entity, targetLocation);
		log.info("Direction: {}", npcLocation.getDirection());
		log.info("Direction2: {}", NPCUtil.getDirectionToLookAt(entity, targetLocation));

		// Target out of zone
		if (FastMath.abs(npcLocation.getX() - targetLocation.getX()) > RADIUS
				|| FastMath.abs(npcLocation.getZ() - targetLocation.getZ()) > RADIUS
				|| FastMath.abs(npcLocation.getY() - targetLocation.getY()) > RADIUS)
		{
			selector.finish();
		}
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{
		if (!npc.isSpawned())
			return false;

		for (final Entity entity : npc.getEntity().getNearbyEntities(RADIUS, RADIUS, RADIUS))
			if (entity instanceof Player)
			{
				target = (Player) entity;
				log.info("looking at {}", entity.getName());
				return true;
			}

		return false;
	}
}