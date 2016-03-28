package fr.heavencraft.heavenguard.bukkit.commands.owner;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.BukkitHeavenGuard;

public class RemoveMemberSubCommand extends AbstractOwnerSubCommand
{
	public RemoveMemberSubCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.REMOVEMEMBER_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Region region = plugin.getRegionProvider().getRegionByName(regionName);

		for (final String arg : args)
		{
			@SuppressWarnings("deprecation")
			final OfflinePlayer player = Bukkit.getOfflinePlayer(arg);

			region.removeMember(player.getUniqueId(), false);
			ChatUtil.sendMessage(sender, "{%1$s} n'est plus membre de la protection {%2$s}.", player.getName(),
					regionName);
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {removemember} <protection> <membre(s)>");
	}
}