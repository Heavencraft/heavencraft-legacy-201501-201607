package fr.hc.quest.goals;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;

import fr.hc.quest.npc.NPCUtil;
import net.citizensnpcs.api.npc.NPC;

public class ShootBowGoal extends AttackGoal
{
	public ShootBowGoal(NPC npc)
	{
		super(npc);
	}

	@Override
	protected boolean canAttack(Entity target)
	{
		if (target.getType() != EntityType.PLAYER)
			return false;

		final LivingEntity npcEntity = (LivingEntity) npc.getEntity();

		final Location npcLocation = npcEntity.getLocation(NPC_LOCATION);
		final Location targetLocation = target.getLocation(TARGET_LOCATION);

		final BlockIterator it = new BlockIterator(npcLocation.getWorld(), npcLocation.toVector(),
				NPCUtil.getDirectionToLookAt(npcEntity, targetLocation), npcEntity.getEyeHeight(), RADIUS);

		while (it.hasNext())
			if (it.next().getType() != Material.AIR)
				return false;

		return true;
	}

	@Override
	protected void attack(Entity target)
	{
		final LivingEntity npcEntity = (LivingEntity) npc.getEntity();
		NPCUtil.faceLocation(npcEntity, target.getLocation(TARGET_LOCATION));
		npcEntity.launchProjectile(Arrow.class);
	}
}