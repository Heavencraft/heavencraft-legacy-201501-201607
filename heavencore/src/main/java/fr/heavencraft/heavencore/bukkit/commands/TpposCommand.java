package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public class TpposCommand extends AbstractCommandExecutor
{
	public TpposCommand(HeavenPlugin plugin)
	{
		super(plugin, "tppos", CorePermissions.TPPOS_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 3)
		{
			sendUsage(player);
			return;
		}

		final int x = DevUtil.toInt(args[0]);
		final int y = DevUtil.toInt(args[1]);
		final int z = DevUtil.toInt(args[2]);

		PlayerUtil.teleportPlayer(player, new Location(player.getWorld(), x, y, z),
				"Vous avez été téléporté en x = {%1$d}, y = {%2$d}, z = {%3$d}.", x, y, z);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{tppos} <x> <y> <z>");
	}
}