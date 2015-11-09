package fr.heavencraft.heavencrea.users.homes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavencrea.CreaPermissions;
import fr.heavencraft.heavencrea.HeavenCrea;
import fr.heavencraft.heavencrea.users.CreativeUser;

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
		CreativeUser user;
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

		PlayerUtil.teleportPlayer(player, user.getHome(nb));
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