package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class SetparentSubCommand extends AbstractSubCommand
{
	public SetparentSubCommand(RegionProvider regionProvider)
	{
		super(regionProvider, HeavenGuardPermissions.SETPARENT_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			case 0: // Remove parent
				setparent(sender, regionName, null);
				break;

			case 1: // Set parent
				setparent(sender, regionName, args[0]);
				break;

			default:
				sendUsage(sender);
				return;
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		HeavenGuard.sendMessage(sender, "/rg {setparent} <protection> <protection parente>");
	}

	private void setparent(CommandSender sender, String regionName, String parentName) throws HeavenException
	{
		regionProvider.getRegionByName(regionName).setParent(parentName);
		HeavenGuard.sendMessage(sender, "La protection {%1$s} a désormais pour parent {%2$s}.", regionName, parentName);
	}
}