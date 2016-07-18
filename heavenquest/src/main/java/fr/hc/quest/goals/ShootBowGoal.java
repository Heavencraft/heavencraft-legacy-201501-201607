package fr.hc.quest.goals;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import fr.hc.quest.npc.HeavenNPC;
import fr.hc.quest.npc.NPCUtil;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.util.PlayerAnimation;

public class ShootBowGoal extends AbstractAttackGoal
{
	public ShootBowGoal(HeavenNPC npc, int radius, TargetAcceptor targetAcceptor)
	{
		super(npc, radius, targetAcceptor);
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
				NPCUtil.getDirectionToLookAt(npcEntity, targetLocation), npcEntity.getEyeHeight(), radius);

		while (it.hasNext())
			if (it.next().getType() != Material.AIR)
				return false;

		return true;
	}

	@Override
	public void reset()
	{
		log.info("reset: frame=0");
		super.reset();
	}

	@Override
	protected void run(GoalSelector selector, int frame, Entity target)
	{
		final Player npcPlayer = (Player) npc.getEntity();
		//
		switch (frame)
		{
			case 0:
				NPCUtil.faceLocation(npcPlayer, target.getLocation(TARGET_LOCATION));
				npcPlayer.getInventory().setItemInMainHand(new ItemStack(Material.BOW));
				npcPlayer.getInventory().setItemInOffHand(new ItemStack(Material.ARROW));
				PlayerAnimation.START_USE_MAINHAND_ITEM.play(npcPlayer);
				break;
			//
			case 10:
				NPCUtil.faceLocation(npcPlayer, target.getLocation(TARGET_LOCATION));
				// PlayerAnimation.STOP_USE_ITEM.play(npcPlayer);
				final Arrow launchProjectile = npcPlayer.launchProjectile(Arrow.class);
				log.info("Launched: {}", launchProjectile);
				selector.finish();
				break;
		}
	}
}