package fr.heavencraft.heavencrea.users.homes;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencrea.HeavenCrea;

public class SethomeCommand extends AbstractCommandExecutor
{
	private final HeavenCrea plugin;

	public SethomeCommand(HeavenCrea plugin)
	{
		super(plugin, "sethome");

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

		plugin.getUserProvider().getUserByUniqueId(player.getUniqueId()).setHome(nb, player.getLocation());

		ChatUtil.sendMessage(player, "Votre {home %1$d} a bien été configuré.", nb);
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
}