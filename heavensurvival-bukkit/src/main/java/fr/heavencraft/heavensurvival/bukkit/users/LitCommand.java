package fr.heavencraft.heavensurvival.bukkit.users;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavensurvival.bukkit.BukkitHeavenSurvival;

public class LitCommand extends AbstractCommandExecutor
{
	public LitCommand(BukkitHeavenSurvival plugin)
	{
		super(plugin, "lit");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final Location bedLocation = player.getBedSpawnLocation();

		if (bedLocation == null)
			ChatUtil.sendMessage(player, "Votre lit a été détruit.");
		else
			player.teleport(bedLocation);
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