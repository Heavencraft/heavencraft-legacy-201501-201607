package fr.heavencraft.heavenguard.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.bukkit.AbstractBukkitPlugin;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavencore.sql.ConnectionProvider.Database;
import fr.heavencraft.heavencore.sql.DefaultConnectionProvider;
import fr.heavencraft.heavenguard.api.RegionManager;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.listeners.PlayerListener;
import fr.heavencraft.heavenguard.bukkit.listeners.ProtectionEnvironmentListener;
import fr.heavencraft.heavenguard.bukkit.listeners.ProtectionPlayerListener;
import fr.heavencraft.heavenguard.datamodel.SQLRegionProvider;

/*
 * Database looks like :
 * regions (
 *   id, name, parent_id,
 *   world, min_x, min_y, min_z, max_x, max_y, max_z,
 *   ... +flags
 * )
 * 
 * hg_regions_members (region_id, user_id, owner)
 * 
 * hg_uuid (id, uuid, last_name)
 * 
 */
public class HeavenGuard extends AbstractBukkitPlugin
{
	public static final String PLUGIN_NAME = "HeavenGuard";

	private static HeavenGuard instance;

	public static HeavenGuard getInstance()
	{
		return instance;
	}

	private static RegionProvider regionProvider;
	private static RegionManager regionManager;

	private ConnectionProvider connectionProvider;

	@Override
	public void onEnable()
	{
		instance = this;

		super.onEnable();

		new PlayerListener();

		new ProtectionPlayerListener();
		new ProtectionEnvironmentListener();

		connectionProvider = new DefaultConnectionProvider(Database.TEST);

		regionProvider = new SQLRegionProvider(connectionProvider);
		regionManager = new RegionManager(regionProvider);

		new RegionCommand(this, regionProvider);
	}

	public static RegionProvider getRegionProvider()
	{
		return regionProvider;
	}

	public static RegionManager getRegionManager()
	{
		return regionManager;
	}

	/*
	 * 
	 */

	/*
	 * SendMessage Utility
	 */

	private static final String BEGIN = "{";
	private static final String END = "}";
	private static final String COLOR = ChatColor.AQUA.toString();
	private static final String COLOR_H = ChatColor.GREEN.toString();

	public static void sendMessage(final CommandSender sender, final String format, final Object... args)
	{
		Bukkit.getScheduler().runTask(getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				String message = String.format(format, args);
				message = COLOR + message.replace(BEGIN, COLOR_H).replace(END, COLOR);
				sender.sendMessage(message);
			}
		});
	}
}