package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.utils.ChatUtil;

public class RoucoupsCommand extends AbstractCommandExecutor
{
	public RoucoupsCommand(HeavenPlugin plugin)
	{
		super(plugin, "roucoups", CorePermissions.ROUCOUPS_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (player.getAllowFlight())
		{
			player.setAllowFlight(false);
			ChatUtil.sendMessage(player, "Vous venez de descendre de {Roucoups}.");
		}
		else
		{
			player.setAllowFlight(true);
			ChatUtil.sendMessage(player, "{Roucoups} utilise vol.");
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}