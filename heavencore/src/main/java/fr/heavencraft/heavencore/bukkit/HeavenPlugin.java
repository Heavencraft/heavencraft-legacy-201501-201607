package fr.heavencraft.heavencore.bukkit;

import org.bukkit.Bukkit;
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
}