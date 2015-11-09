package fr.heavencraft.heavencore.bukkit.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public class TphereCommand extends AbstractCommandExecutor
{
	public TphereCommand(HeavenPlugin plugin)
	{
		super(plugin, "tphere", CorePermissions.TPHERE_COMMAND, Arrays.asList("s"));
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		final Player toTeleport = PlayerUtil.getPlayer(args[0]);

		PlayerUtil.teleportPlayer(toTeleport, player, "Vous avez été téléporté par {%1$s}.", player.getName());
		ChatUtil.sendMessage(player, "Téléportation de {%1$s}.", toTeleport.getName());
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{tphere} <joueur>");
	}
}