package fr.heavencraft.heavenrp.commands.homes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.commands.HeavenCommand;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.database.homes.SetHomeQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.utils.ChatUtil;
import fr.heavencraft.utils.DevUtil;

public class SethomeCommand extends HeavenCommand
{
	public SethomeCommand()
	{
		super("sethome");
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		final int homeNumber;

		if (args.length == 0)
			homeNumber = 1;

		else if (args.length == 1)
			homeNumber = DevUtil.toUint(args[0]);

		else
		{
			sendUsage(player);
			return;
		}

		User user = UserProvider.getUserByName(player.getName());

		if (homeNumber < 1 || homeNumber > user.getHomeNumber())
			throw new HeavenException("Vous n'avez pas acheté le {home %1$d}.", homeNumber);

		QueriesHandler.addQuery(new SetHomeQuery(user, homeNumber, player.getLocation())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "Votre {home %1$d} a bien été configuré.", homeNumber);
			}

			@Override
			public void onHeavenException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		});
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{home}");
		ChatUtil.sendMessage(sender, "/{home} <nombre>");
		ChatUtil.sendMessage(sender, "/{sethome}");
		ChatUtil.sendMessage(sender, "/{sethome} <nombre>");
	}
}