package fr.heavencraft.heavenrp.commands.homes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.IncrementHomeNumberQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class BuyhomeCommand extends AbstractCommandExecutor
{

	public BuyhomeCommand(HeavenRP plugin)
	{
		super(plugin, "buyhome");
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		User user = UserProvider.getUserByName(player.getName());

		final int homeNumber = user.getHomeNumber() + 1;
		final int price = getPrice(homeNumber);

		switch (args.length)
		{
			case 0:
				ChatUtil.sendMessage(player,
						"Le {home %1$d} vous coûtera {%2$d} pièces d'or. Tapez /buyhome valider pour l'acheter.",
						homeNumber, price);
				break;
			case 1:
				if (!args[0].equalsIgnoreCase("valider"))
				{
					sendUsage(player);
					return;
				}

				List<Query> queries = new ArrayList<Query>();
				queries.add(new UpdateUserBalanceQuery(user, -price));
				queries.add(new IncrementHomeNumberQuery(user));
				QueriesHandler.addQuery(new BatchQuery(queries)
				{
					@Override
					public void onSuccess()
					{
						ChatUtil.sendMessage(player,
								"Votre {home %1$d} a bien été acheté pour {%2$d} pièces d'or.", homeNumber, price);
					}

					@Override
					public void onHeavenException(HeavenException ex)
					{
						ChatUtil.sendMessage(player, ex.getMessage());
					}
				});

				break;
			default:
				sendUsage(player);
				break;
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
		ChatUtil.sendMessage(sender, "/{buyhome}");
	}

	private static int getPrice(int homeNumber) throws HeavenException
	{
		switch (homeNumber)
		{
			case 3:
				return 1000;
			case 4:
				return 2000;
			case 5:
				return 3000;
			case 6:
				return 4000;
			case 7:
				return 5000;
			case 8:
				return 6000;
			case 9:
				return 7000;
			case 10:
				return 8000;
			default:
				throw new HeavenException("Vous avez déjà acheté tous les points d'habitation.");
		}
	}
}