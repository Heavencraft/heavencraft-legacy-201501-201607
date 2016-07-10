package fr.hc.quest.animation;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public class ChangeItemInHandsNPCAction implements NPCAction
{
	private final HumanEntity human;
	private final Material firstHand;
	private final Material secondHand;

	public ChangeItemInHandsNPCAction(HumanEntity human, Material firstHand, Material secondHand)
	{
		this.human = human;
		this.firstHand = firstHand;
		this.secondHand = secondHand;
	}

	@Override
	public void perform()
	{
		if (firstHand != null)
			human.getInventory().setItemInMainHand(new ItemStack(firstHand));
		if (secondHand != null)
			human.getInventory().setItemInOffHand(new ItemStack(secondHand));
	}
}