package fr.hc.quest.strategy;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.quest.animation.ChangeItemInHandsNPCAction;
import fr.hc.quest.animation.NPCActionTask;
import fr.hc.quest.animation.ShootArrowNPCAction;
import fr.hc.quest.npc.NPCUtil;
import net.citizensnpcs.api.ai.AttackStrategy;

public class DefenseStrategy implements AttackStrategy
{
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean handle(LivingEntity attacker, LivingEntity target)
	{
		if (!(attacker instanceof HumanEntity))
			return false;

		final HumanEntity npc = (HumanEntity) attacker;

		if (attacker.getLocation().distance(target.getLocation()) > 4)
		{
			// fireArrow(npc);
			log.info("Shooting arrow");

			NPCUtil.faceLocation(npc, target.getLocation());
			npc.launchProjectile(Arrow.class);
			return true;
		}
		else
		{
			log.info("default attack");
			return false;
		}

	}

	private void fireArrow(HumanEntity npc)
	{
		int tick = 0;

		if (!checkItemType(npc.getInventory().getItemInMainHand(), Material.BOW))
		{
			NPCActionTask.get().add(tick, new ChangeItemInHandsNPCAction(npc, Material.BOW, null));
			tick += 2;
		}

		NPCActionTask.get().add(tick, new ShootArrowNPCAction(npc));
	}

	private static boolean checkItemType(ItemStack item, Material type)
	{
		if (item == null)
			return false;

		return item.getType() == type;
	}
}