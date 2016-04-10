package fr.heavencraft.heavensurvival.bukkit.worlds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavensurvival.bukkit.BukkitHeavenSurvival;

public class SpawnCommand extends AbstractCommandExecutor
{
	public SpawnCommand(BukkitHeavenSurvival plugin)
	{
		super(plugin, "spawn");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		PlayerUtil.teleportPlayer(player, WorldsManager.get().getSpawnLocation());
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