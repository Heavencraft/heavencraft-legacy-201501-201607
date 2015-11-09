package fr.heavencraft.heavencore.bukkit.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

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
			throw new HeavenException("Vous n'avez pas actuellement la permission d'utiliser /rejoindre.");

		if (!destination.hasPermission(CorePermissions.ACCEPTER_COMMAND))
			throw new HeavenException("{%1$s} n'a pas actuellement la permission de faire /accepter.",
					destination.getName());

		addRequest(player.getName(), destination.getName());

		ChatUtil.sendMessage(destination, "{%1$s} souhaite vous rejoindre. Tapez /accepter {%1$s} pour accepter.",
				player.getName());

		ChatUtil.sendMessage(player, "Votre demande de téléportation a été transmise à {%1$s}",
				destination.getName());
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{rejoindre} <joueur>");
		ChatUtil.sendMessage(sender, "/{accepter} <joueur>");
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