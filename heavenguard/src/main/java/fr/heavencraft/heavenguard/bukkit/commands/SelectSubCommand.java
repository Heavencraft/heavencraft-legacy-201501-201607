package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.WorldEditUtil;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class SelectSubCommand extends AbstractSubCommand
{
	public SelectSubCommand(HeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.SELECT_COMMAND);
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
			throw new HeavenException("Il faut être un joueur pour utiliser /region select.");

		final Region region = plugin.getRegionProvider().getRegionByName(regionName);
		final World world = Bukkit.getWorld(region.getWorld());

		final Selection selection = new CuboidSelection(world, //
				new Location(world, region.getMinX(), region.getMinY(), region.getMinZ()), //
				new Location(world, region.getMaxX(), region.getMaxY(), region.getMaxZ()));

		WorldEditUtil.getWorldEdit().setSelection((Player) sender, selection);
		plugin.sendMessage(sender, "La protection {%1$s} a (peut-être) été selectionnée.", regionName);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/rg {select} <protection>");
	}
}