package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.PlayerUtil;

public class TpCommand extends AbstractCommandExecutor
{
	public TpCommand(HeavenPlugin plugin)
	{
		super(plugin, "tp", CorePermissions.TP_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		Player toTeleport;
		Player destination;

		switch (args.length)
		{
			case 1:
				toTeleport = player;
				destination = PlayerUtil.getPlayer(args[0]);
				plugin.sendMessage(player, "Téléportation vers {%1$s}.", destination.getName());
				break;

			case 2:
				toTeleport = PlayerUtil.getPlayer(args[0]);
				destination = PlayerUtil.getPlayer(args[1]);
				plugin.sendMessage(player, "Téléportation de {%1$s} vers {%2$s}.", toTeleport.getName(), destination.getName());
				break;

			default:
				sendUsage(player);
				return;
		}

		plugin.teleportPlayer(toTeleport, destination);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		plugin.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/{tp} <joueur>");
		plugin.sendMessage(sender, "/{tp} <joueur1> <joueur2>");
	}
}