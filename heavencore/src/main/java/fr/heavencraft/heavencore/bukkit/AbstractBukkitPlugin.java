package fr.heavencraft.heavencore.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.heavencore.providers.BukkitUniqueIdProvider;
import fr.heavencraft.heavencore.providers.UniqueIdProvider;
import fr.heavencraft.heavencore.sql.ConnectionProvider.Database;
import fr.heavencraft.heavencore.sql.DefaultConnectionProvider;

public class AbstractBukkitPlugin extends JavaPlugin
{
	protected BukkitUniqueIdProvider uniqueIdProvider = new BukkitUniqueIdProvider(new DefaultConnectionProvider(Database.PROXY));

	public UniqueIdProvider getUniqueIdProvider()
	{
		return uniqueIdProvider;
	}
}