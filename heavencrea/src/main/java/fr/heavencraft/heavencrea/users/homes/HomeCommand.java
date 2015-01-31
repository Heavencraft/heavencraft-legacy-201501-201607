package fr.heavencraft.heavencrea.users.homes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencrea.HeavenCrea;

public final class HomeCommand extends AbstractCommandExecutor
{
	private final HeavenCrea plugin;

	public HomeCommand(HeavenCrea plugin)
	{
		super(plugin, "home");

		this.plugin = plugin;
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

		plugin.teleportPlayer(player, plugin.getUserProvider().getUserByUniqueId(player.getUniqueId()).getHome(nb));
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		plugin.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "Commandes des points d'habitation :");
		plugin.sendMessage(sender, "/{home} <numéro> : pour se téléporter à un home.");
		plugin.sendMessage(sender, "/{sethome} <numéro> : pour définir un home.");
		plugin.sendMessage(sender, "/{buyhome} : pour acheter un home.");
	}
}