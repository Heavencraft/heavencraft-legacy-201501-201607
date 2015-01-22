package fr.heavencraft.heavenguard.bukkit.commands.owner;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class RemoveMemberSubCommand extends AbstractOwnerSubCommand
{
	public RemoveMemberSubCommand(RegionProvider regionProvider)
	{
		super(regionProvider, HeavenGuardPermissions.REMOVEMEMBER_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Region region = regionProvider.getRegionByName(regionName);

		for (final String arg : args)
		{
			@SuppressWarnings("deprecation")
			final OfflinePlayer player = Bukkit.getOfflinePlayer(arg);

			region.removeMember(player.getUniqueId(), false);
			HeavenGuard.sendMessage(sender, "{%1$s} n'est plus membre de la protection {%2$s}.", player.getName(), regionName);
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		HeavenGuard.sendMessage(sender, "/rg {removemember} <protection> <membre(s)>");
	}
}