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
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.homes.HomeProvider;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class TphomeCommand extends AbstractCommandExecutor
{
	public TphomeCommand(HeavenRP plugin)
	{
		super(plugin, "tphome", RPPermissions.TPHOME);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		User user;
		int nb = 1;

		switch (args.length)
		{
			case 2:
				nb = DevUtil.toUint(args[1]);
			case 1:
				user = UserProvider.getUserByName(args[0]);
				break;
			default:
				sendUsage(player);
				return;
		}

		Location home = HomeProvider.getHome(user, nb);
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
		ChatUtil.sendMessage(sender, "/{tphome} <joueur>");
		ChatUtil.sendMessage(sender, "/{tphome} <joueur> <numéro>");
	}
}