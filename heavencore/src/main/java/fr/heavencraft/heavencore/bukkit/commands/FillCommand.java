package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.utils.ChatUtil;

public class FillCommand extends AbstractCommandExecutor
{
	public FillCommand(HeavenPlugin plugin)
	{
		super(plugin, "fill", CorePermissions.FILL_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		boolean isReplace;

		if (args.length == 8)
			isReplace = false;
		else if (args.length == 10)
			isReplace = true;
		else
		{
			sendUsage(sender);
			return;
		}

		World world;

		if (sender instanceof Player)
			world = ((Player) sender).getWorld();
		else if (sender instanceof BlockCommandSender)
			world = ((BlockCommandSender) sender).getBlock().getWorld();
		else
			return;

		int x1 = DevUtil.toInt(args[0]);
		int y1 = DevUtil.toInt(args[1]);
		int z1 = DevUtil.toInt(args[2]);
		int x2 = DevUtil.toInt(args[3]);
		int y2 = DevUtil.toInt(args[4]);
		int z2 = DevUtil.toInt(args[5]);
		Material tile = Material.matchMaterial(args[6]);

		int tmp;

		if (x2 < x1)
		{
			tmp = x1;
			x1 = x2;
			x2 = tmp;
		}

		if (y2 < y1)
		{
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}

		if (z2 < z1)
		{
			tmp = z1;
			z1 = z2;
			z2 = tmp;
		}

		if (!isReplace)
			set(world, x1, y1, z1, x2, y2, z2, tile);
		else
		{
			Material newTile = Material.matchMaterial(args[9]);

			replace(world, x1, y1, z1, x2, y2, z2, tile, newTile);
		}
	}

	private void set(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material tile)
	{
		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++)
				for (int z = z1; z <= z2; z++)
					world.getBlockAt(x, y, z).setType(tile);
	}

	private void replace(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material tile,
			Material newTile)
	{
		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++)
				for (int z = z1; z <= z2; z++)
					if (world.getBlockAt(x, y, z).getType() == tile)
						world.getBlockAt(x, y, z).setType(newTile);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{fill} <x1> <y1> <z1> <x2> <y2> <z2> <TileName> replace : //set");
		ChatUtil.sendMessage(sender,
				"/{fill} <x1> <y1> <z1> <x2> <y2> <z2> <TileName> 0 replace [replaceTileName] : //replace");
	}
}