package fr.heavencraft.heavenrp.commands.homes;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.async.actions.TeleportPlayerAction;
import fr.heavencraft.commands.HeavenCommand;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.database.homes.HomeProvider;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.utils.ChatUtil;
import fr.heavencraft.utils.DevUtil;

public class HomeCommand extends HeavenCommand
{
	public HomeCommand()
	{
		super("home");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		int nb;

		if (args.length == 0)
			nb = 1;
		else if (args.length == 1)
			nb = DevUtil.toUint(args[0]);
		else
		{
			sendUsage(player);
			return;
		}

		Location home = HomeProvider.getHome(UserProvider.getUserByName(player.getName()), nb);

		ActionsHandler.addAction(new TeleportPlayerAction(player, home));
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
		ChatUtil.sendMessage(sender, "/{home}");
		ChatUtil.sendMessage(sender, "/{home} <numéro>");
		ChatUtil.sendMessage(sender, "/{sethome}");
		ChatUtil.sendMessage(sender, "/{sethome} <nombre>");
		ChatUtil.sendMessage(sender, "/{buyhome} <nombre>");
	}
}