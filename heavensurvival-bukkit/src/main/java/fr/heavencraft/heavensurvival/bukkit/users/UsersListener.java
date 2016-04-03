package fr.heavencraft.heavensurvival.bukkit.users;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavensurvival.bukkit.BukkitHeavenSurvival;
import fr.heavencraft.heavensurvival.common.users.UpdateUserNameQuery;
import fr.heavencraft.heavensurvival.common.users.User;
import fr.heavencraft.heavensurvival.common.users.UserProvider;

// Listener used to update users table
public class UsersListener extends AbstractListener<HeavenPlugin>
{
	public UsersListener(BukkitHeavenSurvival plugin)
	{
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerLogin(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		final UUID uniqueId = player.getUniqueId();
		final String name = player.getName();

		log.info("UsersListener.onPlayerLogin : %1$s = %2$s", uniqueId, name);

		try
		{
			final User user = UserProvider.getInstance().getUserByUniqueId(uniqueId);

			if (!name.equals(user.getName()))
			{
				new UpdateUserNameQuery(user, name).executeQuery();
			}
		}
		catch (final UserNotFoundException ex)
		{
			UserProvider.getInstance().createUser(uniqueId, name);
		}
		catch (final HeavenException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}