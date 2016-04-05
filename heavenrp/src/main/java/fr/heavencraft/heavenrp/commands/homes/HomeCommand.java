package fr.heavencraft.heavenrp.commands.homes;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPGLocks;
import fr.heavencraft.heavenrp.database.homes.HomeProvider;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class HomeCommand extends AbstractCommandExecutor
{
	public HomeCommand(HeavenRP plugin)
	{
		super(plugin, "home");
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
		
		RPGLocks.canTeleport(player);
		PlayerUtil.teleportPlayer(player, home);
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