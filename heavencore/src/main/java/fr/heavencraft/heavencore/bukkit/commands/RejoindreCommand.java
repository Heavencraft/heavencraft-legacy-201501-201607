package fr.heavencraft.heavencore.bukkit.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.PlayerUtil;

public class RejoindreCommand extends AbstractCommandExecutor
{
	private static Map<String, String> requests = new HashMap<String, String>();

	public RejoindreCommand(HeavenPlugin plugin)
	{
		super(plugin, "rejoindre", CorePermissions.REJOINDRE_COMMAND, null);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		final Player destination = PlayerUtil.getPlayer(args[0]);

		if (!player.hasPermission(CorePermissions.REJOINDRE_COMMAND))
			throw new HeavenException("Vous n'êtes pas actuellement dans le monde ressources.");

		if (!destination.hasPermission(CorePermissions.REJOINDRE_COMMAND))
			throw new HeavenException("{%1$s} n'est pas actuellement dans le monde ressources.", destination.getName());

		addRequest(player.getName(), destination.getName());

		plugin.sendMessage(destination, "{%1$s} souhaite vous rejoindre. Tapez /accepter {%1$s} pour accepter.", player.getName());

		plugin.sendMessage(player, "Votre demande de téléportation a été transmise à {%1$s}", destination.getName());
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		plugin.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/{rejoindre} <joueur>");
		plugin.sendMessage(sender, "/{accepter} <joueur>");
	}

	public static void addRequest(String toTeleport, String destination)
	{
		requests.put(toTeleport, destination);
	}

	public static boolean acceptRequest(String toTeleport, String destination)
	{
		final String destination2 = requests.get(toTeleport);

		if (destination.equalsIgnoreCase(destination2))
		{
			requests.remove(toTeleport);
			return true;
		}

		return false;
	}
}