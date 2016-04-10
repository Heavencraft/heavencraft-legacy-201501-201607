package fr.heavencraft.heavenrp.scrolls;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class ScrollListener extends AbstractListener<HeavenPlugin>
{

	public ScrollListener(HeavenRP plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onTeleportScrollUse(PlayerInteractEvent event) throws HeavenException
	{
		// TODO remove legacy support when passing on 1.9
		Player p = event.getPlayer();
		ItemStack itemInHand = null;
		try
		{
			if (p.getInventory().getItemInMainHand().getType() != Material.PAPER)
				return;
			itemInHand = p.getInventory().getItemInMainHand();
		}
		catch (NoSuchMethodError err)
		{
			// Minecraft 1.8 mode
			if (p.getInventory().getItemInHand().getType() != Material.PAPER)
				return;
			itemInHand = p.getInventory().getItemInHand();
		}

		if (itemInHand == null || itemInHand.getType() == Material.AIR)
			return;

		Scroll scr = ScrollProvider.getInstance().getScroll(itemInHand.getItemMeta().getDisplayName());
		if (scr == null)
			return;
		scr.executeScroll(p);
		// Remove 1 scroll
		try
		{
			p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
		}
		catch (NoSuchMethodError err)
		{
			p.getInventory().getItemInHand().setAmount(p.getInventory().getItemInHand().getAmount() - 1);
		}
		event.setCancelled(true);
	}

	// @EventHandler
	public void onSupportScrollUse(PlayerInteractEvent event)
	{
		// if (event.getPlayer().getInventory().getItemInOffHand().getType() != Material.PAPER)
		// return;
	}
}
