package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

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
				ChatUtil.sendMessage(player, "Téléportation vers {%1$s}.", destination.getName());
				break;

			case 2:
				toTeleport = PlayerUtil.getPlayer(args[0]);
				destination = PlayerUtil.getPlayer(args[1]);
				ChatUtil.sendMessage(player, "Téléportation de {%1$s} vers {%2$s}.", toTeleport.getName(),
						destination.getName());
				break;

			default:
				sendUsage(player);
				return;
		}

		PlayerUtil.teleportPlayer(toTeleport, destination);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{tp} <joueur>");
		ChatUtil.sendMessage(sender, "/{tp} <joueur1> <joueur2>");
	}
}