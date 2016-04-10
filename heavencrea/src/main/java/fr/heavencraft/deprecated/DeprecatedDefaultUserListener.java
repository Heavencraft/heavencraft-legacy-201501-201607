package fr.heavencraft.deprecated;

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

public class DeprecatedDefaultUserListener<P extends HeavenPlugin & DeprecatedHasUserProvider<U>, U extends DeprecatedUser> extends
		AbstractListener<P>
{
	private final DeprecatedUserProvider<U> userProvider;

	public DeprecatedDefaultUserListener(P plugin)
	{
		super(plugin);
		userProvider = plugin.getUserProvider();
	}

	@EventHandler
	protected void onPlayerLogin(PlayerLoginEvent event) throws HeavenException
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
	protected void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
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