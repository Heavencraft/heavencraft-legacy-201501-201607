package fr.heavencraft.heavenrp.general.users;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.bankaccounts.UpdateBankAccountNameQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserNameQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.utils.RPUtils;
import fr.heavencraft.utils.ChatUtil;

public class UserListener extends AbstractListener<HeavenPlugin>
{
	public UserListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerLogin(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();

		final String name = player.getName();
		final String uuid = RPUtils.getUUID(player);

		log.info("UsersListener.onPlayerLogin : %1$s = %2$s", uuid, name);

		try
		{
			User user = UserProvider.getUserByUUID(uuid);

			if (!name.equals(user.getName()))
			{
				new UpdateUserNameQuery(user, name).executeQuery();

				BankAccount bankAccount = BankAccountsManager.getBankAccount(user.getName(),
						BankAccountType.USER);
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