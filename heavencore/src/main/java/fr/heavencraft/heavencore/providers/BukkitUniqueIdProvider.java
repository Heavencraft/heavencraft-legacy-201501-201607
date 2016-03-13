package fr.heavencraft.heavencore.providers;

import java.util.UUID;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.sql.ConnectionProvider;

public class BukkitUniqueIdProvider extends DefaultUniqueIdProvider
{
	public BukkitUniqueIdProvider(ConnectionProvider connectionProvider)
	{
		super(connectionProvider);
	}

	@Override
	public String getNameFromUniqueId(UUID id) throws UserNotFoundException
	{
		final String name = Bukkit.getOfflinePlayer(id).getName();

		if (name != null)
		{
			log.info("Bukkit : %1$s => %2$s", id, name);
			// LogUtil.info(getClass(), "Bukkit : %1$s => %2$s", id, name);
			return name;
		}
		else
			return super.getNameFromUniqueId(id);
	}
}