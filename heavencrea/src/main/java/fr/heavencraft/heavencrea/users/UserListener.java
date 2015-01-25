package fr.heavencraft.heavencrea.users;

import java.util.Date;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.utils.DateUtil;
import fr.heavencraft.heavencrea.HeavenCrea;

public class UserListener extends AbstractListener
{
	private final UserProvider userProvider;

	public UserListener(HeavenCrea plugin)
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
	public void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		final User user = userProvider.getUserByUniqueId(event.getPlayer().getUniqueId());

		if (!DateUtil.isToday(user.getLastLogin()))
		{
			user.updateBalance(100);
			plugin.sendMessage(event.getPlayer(), "Vous venez d'obtenir {100} jetons en vous connectant !");
		}

		user.updateLastLogin(new Date());
	}
}