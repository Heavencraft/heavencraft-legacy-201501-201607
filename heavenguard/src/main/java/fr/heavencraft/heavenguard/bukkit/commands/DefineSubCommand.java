package fr.heavencraft.heavenguard.bukkit.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.WorldEditUtil;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class DefineSubCommand extends AbstractSubCommand
{
	public DefineSubCommand(RegionProvider regionProvider)
	{
		super(regionProvider, HeavenGuardPermissions.DEFINE_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Selection selection = WorldEditUtil.getWESelection((Player) sender);
		final Collection<OfflinePlayer> owners = new ArrayList<OfflinePlayer>();

		if (args.length != 0)
		{
			for (final String ownerName : args)
				owners.add(Bukkit.getOfflinePlayer(ownerName));
		}

		define(sender, regionName, selection, owners);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		HeavenGuard.sendMessage(sender, "/rg {define} <protection>");
	}

	private void define(CommandSender sender, String name, Selection selection, Collection<OfflinePlayer> owners)
			throws HeavenException
	{
		final Location min = selection.getMinimumPoint();
		final Location max = selection.getMaximumPoint();

		// Create the region
		regionProvider.createRegion(name, selection.getWorld().getName(), //
				min.getBlockX(), min.getBlockY(), min.getBlockZ(), //
				max.getBlockX(), max.getBlockY(), max.getBlockZ());

		HeavenGuard.sendMessage(sender, "La région {%1$s} a bien été créée.", name);

		// Add the initial owners
		if (!owners.isEmpty())
		{
			final Region region = regionProvider.getRegionByName(name);

			for (final OfflinePlayer owner : owners)
			{
				region.addMember(owner.getUniqueId(), true);
				HeavenGuard.sendMessage(sender, "{%1$s} est maintenant propriétaire de la protection {%2$s}.", owner.getName(),
						name);
			}
		}
	}
}