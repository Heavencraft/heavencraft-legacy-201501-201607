package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;
import fr.heavencraft.heavenguard.bukkit.RegionUtil;

public class SavestateSubCommand extends AbstractSubCommand
{
	public SavestateSubCommand(HeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.SAVESTATE_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Region region = plugin.getRegionProvider().getRegionByName(regionName);

		plugin.sendMessage(sender, "Sauvegarde de la protection {%1$s}...", region.getName());
		RegionUtil.loadState(region);
		plugin.sendMessage(sender, "La protection {%1$s} a bien été sauvegardée.", region.getName());
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/rg {savestate} <protection>");
	}
}