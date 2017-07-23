package fr.heavencraft.heavencore.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
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
			ConnectionHandlerFactory.getConnectionHandler(loadDatabase(getConfig(), "proxy.database")));

	public UniqueIdProvider getUniqueIdProvider()
	{
		return uniqueIdProvider;
	}

	public static Database loadDatabase(FileConfiguration config, String databaseProperty)
	{
		final String username = config.getString("mysql.username");
		final String password = config.getString("mysql.password");
		final String database = config.getString(databaseProperty);
		return new Database(database, username, password, 3);
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