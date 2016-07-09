package fr.hc.scrolls;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.hc.quest.HeavenQuest;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public class ScrollListener extends AbstractListener<HeavenPlugin>
{

	public ScrollListener(HeavenQuest plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onTeleportScrollUse(PlayerInteractEvent event) throws HeavenException
	{
		Player p = event.getPlayer();
		ItemStack itemInHand = null;

		if (p.getInventory().getItemInMainHand().getType() != Material.PAPER)
			return;
		itemInHand = p.getInventory().getItemInMainHand();

		if (itemInHand == null || itemInHand.getType() == Material.AIR)
			return;

		try
		{
			Scroll scr = ScrollProvider.getInstance().getScroll(itemInHand.getItemMeta().getDisplayName());
			if (scr == null)
				return;
			scr.executeScroll(p);
		}
		catch (HeavenException ex)
		{
			ChatUtil.sendMessage(p, ex.getMessage());
			event.setCancelled(true);
			return;
		}

		// Remove 1 scroll
		int amount = p.getInventory().getItemInMainHand().getAmount();
		if(amount == 1)
			p.getInventory().setItemInMainHand(null);
		else
			p.getInventory().getItemInMainHand().setAmount(amount - 1);
		event.setCancelled(true);
	}

	// @EventHandler
	public void onSupportScrollUse(PlayerInteractEvent event)
	{
		// if (event.getPlayer().getInventory().getItemInOffHand().getType() != Material.PAPER)
		// return;
	}
}
