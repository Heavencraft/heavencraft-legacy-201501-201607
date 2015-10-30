package fr.lorgan17.heavenrp.commands.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.queries.BatchQuery;
import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.async.queries.Query;
import fr.heavencraft.commands.HeavenCommand;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.database.users.UpdateUserDealerLicenceQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.utils.ChatUtil;

public class LicenceCommand extends HeavenCommand
{
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public LicenceCommand()
	{
		super("licence");
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		final User user = UserProvider.getUserByName(player.getName());

		switch (args.length)
		{
			case 1:

				if (args[0].equalsIgnoreCase("marchand"))
				{
					if (user.hasDealerLicense())
					{
						ChatUtil.sendMessage(player, "Vous possédez déjà la licence de marchand.");
						ChatUtil.sendMessage(player, "Elle expirera le %1$s.",
								dateFormat.format(user.getLicenseExpireDate()));
						ChatUtil.sendMessage(player,
								"Faites {/licence marchand valider} pour acheter 1 mois supplémentaire. Il vous en coûtera 750 pièces d'or.");
					}
					else if (user.alreadyHasDealerLicense())
						ChatUtil.sendMessage(player,
								"La licence de marchand vous coûtera 750 pièces d'or. Faites {/licence marchand valider} pour valider");
					else
						ChatUtil.sendMessage(player,
								"La licence de marchand vous coûtera 400 pièces d'or. Faites {/licence marchand valider} pour valider");
				}
				break;

			case 2:
				if (args[0].equalsIgnoreCase("marchand") && args[1].equalsIgnoreCase("valider"))
				{
					List<Query> queries = new ArrayList<Query>();
					queries.add(new UpdateUserBalanceQuery(user, user.alreadyHasDealerLicense() ? -750 : -400));
					queries.add(new UpdateUserDealerLicenceQuery(user));
					QueriesHandler.addQuery(new BatchQuery(queries)
					{
						@Override
						public void onSuccess()
						{
							ChatUtil.sendMessage(player,
									"Vous venez d'acquérir la licence de marchand pour 1 mois.");
						}

						@Override
						public void onHeavenException(HeavenException ex)
						{
							ChatUtil.sendMessage(player, ex.getMessage());
						}
					});
				}

				break;

			default:
				sendUsage(player);
				return;
		}

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{licence} marchand : pour acheter la licence de marchand");
		// ChatUtil.sendMessage(sender,
		// "/{licence} ressources : pour acheter la licence d'accès au monde ressources");
	}
}
