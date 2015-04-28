package fr.heavencraft.heavencore.users;

import java.util.Date;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.utils.DateUtil;

public class DefaultUserListener<P extends HeavenPlugin & HasUserProvider<U>, U extends User> extends
		AbstractListener
{
	private final UserProvider<U> userProvider;

	public DefaultUserListener(P plugin)
	{
		super(plugin);
		userProvider = plugin.getUserProvider();
	}

	@EventHandler
	private void onPlayerLogin(PlayerLoginEvent event) throws HeavenException
	{
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();

		try
		{
			userProvider.getUserByUniqueId(uuid);
		}
		catch (final UserNotFoundException ex)
		{
			userProvider.createUser(uuid, player.getName());
		}
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		final U user = userProvider.getUserByUniqueId(event.getPlayer().getUniqueId());

		if (!DateUtil.isToday(user.getLastLogin()))
		{
			onFirstConnection(event.getPlayer(), user);
		}

		user.updateLastLogin(new Date());
	}

	// Première connection de la journée, peut être overridé
	protected void onFirstConnection(Player player, U user) throws HeavenException
	{
		// Do nothing
	}
}