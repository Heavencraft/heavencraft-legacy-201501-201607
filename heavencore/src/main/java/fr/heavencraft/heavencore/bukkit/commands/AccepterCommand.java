package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.PlayerUtil;

public class AccepterCommand extends AbstractCommandExecutor
{
	public AccepterCommand(HeavenPlugin plugin)
	{
		super(plugin, "accepter", CorePermissions.ACCEPTER_COMMAND, null);
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

		if (!player.hasPermission(CorePermissions.ACCEPTER_COMMAND))
			throw new HeavenException("Vous n'avez actuellement pas la permission de faire /accepter.");

		if (!toTeleport.hasPermission(CorePermissions.REJOINDRE_COMMAND))
			throw new HeavenException("{%1$s} n'a actuellement pas la permission de faire /rejoindre.",
					toTeleport.getName());

		if (!RejoindreCommand.acceptRequest(toTeleport.getName(), player.getName()))
			throw new HeavenException("{%1$s} n'a pas demandé à vous rejoindre.", toTeleport.getName());

		// toTeleport.teleport(player);
		plugin.teleportPlayer(toTeleport, player);
		plugin.sendMessage(toTeleport, " Vous avez été téléporté à {%1$s}.", player.getName());
		plugin.sendMessage(player, "{%1$s} a été téléporté.", toTeleport.getName());
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
}