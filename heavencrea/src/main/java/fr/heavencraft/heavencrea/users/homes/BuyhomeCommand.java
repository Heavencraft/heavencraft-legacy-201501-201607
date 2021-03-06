package fr.heavencraft.heavencrea.users.homes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencrea.HeavenCrea;
import fr.heavencraft.heavencrea.users.CreativeUser;

public class BuyhomeCommand extends AbstractCommandExecutor
{
	private final HeavenCrea plugin;

	public BuyhomeCommand(HeavenCrea plugin)
	{
		super(plugin, "buyhome");

		this.plugin = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final CreativeUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());

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

				user.updateBalance(-price);
				user.incrementHomeNumber();

				ChatUtil.sendMessage(player, "Votre {home %1$d} a bien été acheté pour {%2$d} pièces d'or.",
						homeNumber, price);
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
		ChatUtil.sendMessage(sender, "Commandes des points d'habitation :");
		ChatUtil.sendMessage(sender, "/{home} <numéro> : pour se téléporter à un home.");
		ChatUtil.sendMessage(sender, "/{sethome} <numéro> : pour définir un home.");
		ChatUtil.sendMessage(sender, "/{buyhome} : pour acheter un home.");
	}

	private static int getPrice(int homeNumber) throws HeavenException
	{
		switch (homeNumber)
		{
			case 2:
				return 100;
			case 3:
				return 200;
			case 4:
				return 400;
			case 5:
				return 800;
			case 6:
				return 1600;
			case 7:
				return 3200;
			case 8:
				return 6400;
			case 9:
				return 12800;
			case 10:
				return 25600;
			default:
				throw new HeavenException("Vous avez déjà acheté tous les points d'habitation.");
		}
	}
}