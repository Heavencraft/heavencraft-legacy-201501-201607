package fr.heavencraft.heavenrp.horses;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.HorseInventory;

import fr.heavencraft.utils.DevUtil;

public class HorsesListener implements Listener
{
	public HorsesListener()
	{
		DevUtil.registerListener(this);
	}

	@EventHandler(ignoreCancelled = true)
	private void onVehicleEnter(VehicleEnterEvent event)
	{
		if (event.getVehicle().getType() != EntityType.HORSE || event.getEntered().getType() != EntityType.PLAYER)
			return;

		Horse horse = (Horse) event.getVehicle();
		Player player = (Player) event.getEntered();

		if (!HorsesManager.canUse(horse, player))
		{
			HorsesManager.sendWarning(horse, player);
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onInventoryOpen(InventoryOpenEvent event)
	{
		if (!(event.getInventory() instanceof HorseInventory))
			return;

		Horse horse = (Horse) event.getInventory().getHolder();
		Player player = (Player) event.getPlayer();

		if (!HorsesManager.canUse(horse, player))
		{
			HorsesManager.sendWarning(horse, player);
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if (event.getEntityType() != EntityType.HORSE)
			return;

		Horse horse = (Horse) event.getEntity();
		Entity damager = event.getDamager();
		Player player;

		if (damager instanceof Player)
			player = (Player) damager;
		else if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player)
			player = (Player) ((Projectile) damager).getShooter();
		else
			return;

		if (!HorsesManager.canUse(horse, player))
		{
			if (horse.getOwner() != null)
				DevUtil.logInfo("%1$s tried to damage %2$s's horse", player.getName(), horse.getOwner().getName());

			event.setCancelled(true);
		}
	}
}