package fr.heavencraft.heavenfun.commands.spawn;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.commands.HeavenCommand;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenfun.BukkitHeavenFun;
import fr.heavencraft.utils.ChatUtil;

public class SpawnCommand extends HeavenCommand
{
	public SpawnCommand()
	{
		super("spawn");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		player.teleport(BukkitHeavenFun.getSpawn());
		ChatUtil.sendMessage(player, "Vous avez été téléporté au spawn.");
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}