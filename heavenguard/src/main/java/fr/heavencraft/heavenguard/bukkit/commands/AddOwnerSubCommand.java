package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class AddOwnerSubCommand extends AbstractSubCommand
{
	public AddOwnerSubCommand(RegionProvider regionProvider)
	{
		super(regionProvider, HeavenGuardPermissions.ADDOWNER_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Region region = regionProvider.getRegionByName(regionName);

		for (final String arg : args)
		{
			@SuppressWarnings("deprecation")
			final OfflinePlayer player = Bukkit.getOfflinePlayer(arg);

			region.addMember(player.getUniqueId(), false);
			HeavenGuard.sendMessage(sender, "{%1$s} est maintenant propriétaire de la protection {%2$s}.", player.getName(),
					regionName);
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		HeavenGuard.sendMessage(sender, "/rg {addowner} <protection> <propriétaire(s)>");
	}
}