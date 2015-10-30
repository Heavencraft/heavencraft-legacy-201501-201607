package fr.heavencraft.heavenrp.general.users;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.exceptions.UserNotFoundException;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.bankaccounts.UpdateBankAccountNameQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserNameQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.utils.ChatUtil;
import fr.heavencraft.utils.DevUtil;
import fr.heavencraft.utils.PlayerUtil;

public class UserListener implements Listener
{
	public UserListener()
	{
		DevUtil.registerListener(this);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerLogin(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();

		final String name = player.getName();
		final String uuid = PlayerUtil.getUUID(player);

		DevUtil.logInfo("UsersListener.onPlayerLogin : %1$s = %2$s", uuid, name);

		try
		{
			User user = UserProvider.getUserByUUID(uuid);

			if (!name.equals(user.getName()))
			{
				new UpdateUserNameQuery(user, name).executeQuery();

				BankAccount bankAccount = BankAccountsManager
						.getBankAccount(user.getName(), BankAccountType.USER);
				new UpdateBankAccountNameQuery(bankAccount, name).executeQuery();
			}
		}
		catch (UserNotFoundException ex)
		{
			UserProvider.createUser(uuid, name);
		}
		catch (HeavenException ex)
		{
			ChatUtil.sendMessage(player, ex.getMessage());
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			ChatUtil.sendMessage(player, "{Un problème est survenu lors du changement de pseudo.");
		}
	}
}