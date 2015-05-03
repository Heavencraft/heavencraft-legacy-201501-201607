package fr.heavencraft.heavencore.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavencore.providers.BukkitUniqueIdProvider;
import fr.heavencraft.heavencore.providers.UniqueIdProvider;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.Database;

public abstract class HeavenPlugin extends JavaPlugin
{
	protected final HeavenLog log = HeavenLog.getLogger(getClass());
	protected final UniqueIdProvider uniqueIdProvider = new BukkitUniqueIdProvider(
			ConnectionHandlerFactory.getConnectionHandler(Database.PROXY));

	public UniqueIdProvider getUniqueIdProvider()
	{
		return uniqueIdProvider;
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		Bukkit.getScheduler().runTaskLater(this, new Runnable()
		{
			@Override
			public void run()
			{
				log.info("Running afterEnable...");
				afterEnable();
				log.info("Done afterEnable.");
			}
		}, 0);
	}

	// Sometimes, things must be initialized after Bukkit
	protected void afterEnable()
	{
	}

	/*
	 * Messages
	 */

	private static final String BEGIN = "{";
	private static final String END = "}";

	protected String textColor = ChatColor.GOLD.toString();
	protected String highlightColor = ChatColor.RED.toString();

	public void sendMessage(final CommandSender sender, final String format, final Object... args)
	{
		Bukkit.getScheduler().runTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				sender.sendMessage(new StringBuilder(textColor).append(
						String.format(format.replace(BEGIN, highlightColor).replace(END, textColor), args))
						.toString());
			}
		});
	}

	/*
	 * Teleport
	 */

	public void teleportPlayer(final Player player, final Entity entity)
	{
		teleportPlayer(player, entity.getLocation());
	}

	public void teleportPlayer(final Player player, final Location location)
	{
		// Bugfix for foodlevel changing after teleport on a different world
		if (!player.getWorld().equals(location.getWorld()))
		{
			final int foodLevel = player.getFoodLevel();
			final float saturation = player.getSaturation();

			Bukkit.getScheduler().runTaskLater(this, new Runnable()
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

			sendMessage(player, "Ton cheval a été téléporté avec toi. S'il n'est pas là, {déco reco}.");

			Bukkit.getScheduler().runTaskLater(this, new Runnable()
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