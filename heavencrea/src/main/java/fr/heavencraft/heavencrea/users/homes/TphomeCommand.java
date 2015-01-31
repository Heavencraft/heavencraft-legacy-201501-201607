package fr.heavencraft.heavencrea.users.homes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencrea.CreaPermissions;
import fr.heavencraft.heavencrea.HeavenCrea;
import fr.heavencraft.heavencrea.users.User;

public final class TphomeCommand extends AbstractCommandExecutor
{
	private final HeavenCrea plugin;

	public TphomeCommand(HeavenCrea plugin)
	{
		super(plugin, "tphome", CreaPermissions.TPHOME_COMMAND);

		this.plugin = plugin;
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
				user = plugin.getUserProvider().getUserByName(args[0]);
				break;
			default:
				sendUsage(player);
				return;
		}

		plugin.teleportPlayer(player, user.getHome(nb));
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		plugin.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/{tphome} <joueur>");
		plugin.sendMessage(sender, "/{tphome} <joueur> <numéro>");
	}
}