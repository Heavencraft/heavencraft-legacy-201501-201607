package fr.hc.quest.goals;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.hc.quest.npc.HeavenNPC;
import net.citizensnpcs.api.ai.GoalSelector;

public class SwordAttackGoal extends AbstractAttackGoal
{
	public SwordAttackGoal(HeavenNPC npc, int radius, TargetAcceptor targetAcceptor)
	{
		super(npc, radius, targetAcceptor);
	}

	@Override
	protected boolean canAttack(Entity target)
	{
		((Player) npc.getEntity()).getInventory().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
		((Player) npc.getEntity()).getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
		npc.getNavigator().setTarget(target, true);
		return true;
	}

	@Override
	protected void run(GoalSelector selector, int frame, Entity target)
	{

		if (!npc.getNavigator().isNavigating())
			selector.finish();
	}
}