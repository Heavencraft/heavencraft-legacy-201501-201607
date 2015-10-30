package fr.heavencraft.async.actions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePlugin;
import fr.heavencraft.utils.ChatUtil;

public class TeleportPlayerAction extends AbstractAction
{
	private final Player player;
	private final Location location;

	public TeleportPlayerAction(Player player, Location location)
	{
		this.player = player;
		this.location = location;

		if (!location.getChunk().isLoaded())
			location.getChunk().load();
	}

	public TeleportPlayerAction(Player player, Entity destination)
	{
		this(player, destination.getLocation());
	}

	@Override
	public void executeAction()
	{
		// Bugfix for foodlevel changing after teleport on a different world
		if (!player.getWorld().equals(location.getWorld()))
		{
			final int foodLevel = player.getFoodLevel();
			final float saturation = player.getSaturation();

			Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), new Runnable()
			{
				@Override
				public void run()
				{
					player.setFoodLevel(foodLevel);
					player.setSaturation(saturation);
				}
			}, 20);
		}

		if (player.isInsideVehicle() && player.getVehicle() instanceof Horse)
		{
			final Horse horse = (Horse) player.getVehicle();

			player.teleport(location);
			horse.teleport(player);

			ChatUtil.sendMessage(player, "Ton cheval a été téléporté avec toi. S'il n'est pas là, {déco reco}.");

			Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), new Runnable()
			{
				@Override
				public void run()
				{
					horse.setHealth(horse.getMaxHealth());
					horse.setPassenger(player);
				}
			}, 20);
		}

		else
			player.teleport(location);
	}
}