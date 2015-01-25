package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.WorldEditUtil;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class RedefineSubCommand extends AbstractSubCommand
{
	public RedefineSubCommand(HeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.REDEFINE_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		if (args.length != 0)
		{
			sendUsage(sender);
			return;
		}

		if (!(sender instanceof Player))
			throw new HeavenException("Il faut être un joueur pour utiliser /region redefine.");

		redefine(sender, regionName, WorldEditUtil.getWESelection((Player) sender));
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/rg {redefine} <protection>");
	}

	private void redefine(CommandSender sender, String regionName, Selection selection) throws HeavenException
	{
		final Location min = selection.getMinimumPoint();
		final Location max = selection.getMaximumPoint();

		plugin.getRegionProvider().getRegionByName(regionName).redefine(selection.getWorld().getName(), //
				min.getBlockX(), min.getBlockY(), min.getBlockZ(), //
				max.getBlockX(), max.getBlockY(), max.getBlockZ());

		plugin.sendMessage(sender, "La protection {%1$s} a bien été redéfinie.", regionName);
	}
}