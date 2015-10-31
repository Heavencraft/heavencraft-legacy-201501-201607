package fr.heavencraft.heavencore.bukkit.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.utils.ChatUtil;

public class NoentitiesCommand extends AbstractCommandExecutor
{
	public NoentitiesCommand(HeavenPlugin plugin)
	{
		super(plugin, "noentities", "heavencraft.commands.noentities");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			case 0:
				for (final World world : Bukkit.getWorlds())
				{
					for (final Chunk chunk : world.getLoadedChunks())
					{
						if (chunk.getEntities().length > 200)
						{
							printWarning(chunk, sender);
						}
					}
				}
				ChatUtil.sendMessage(sender, "Entités comptées");
				break;

			case 4:
				final World world = Bukkit.getWorld(args[0]);
				final int x = DevUtil.toInt(args[1]);
				final int z = DevUtil.toInt(args[2]);
				final EntityType type = EntityType.fromName(args[3]);

				if (type == null)
					throw new HeavenException("Invalid entity type : %1$s", args[3]);

				int i = 0;

				for (final Entity entity : world.getChunkAt(x, z).getEntities())
				{
					if (entity.getType() == type)
					{
						entity.remove();
						i++;
					}
				}

				ChatUtil.sendMessage(sender, "%1$s entités retirées.", i);
				break;

			default:
				sendUsage(sender);
				break;
		}

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{noentities}");
		ChatUtil.sendMessage(sender, "/{noentities} <world> <x> <y> <type>");
	}

	private static void printWarning(Chunk chunk, CommandSender sender)
	{
		final Map<EntityType, Integer> entities = new HashMap<EntityType, Integer>();

		for (final Entity entity : chunk.getEntities())
		{
			final EntityType type = entity.getType();

			if (entities.containsKey(type))
				entities.put(type, entities.get(type) + 1);
			else
				entities.put(type, 1);
		}

		ChatUtil.sendMessage(sender, "Chunk %1$s %2$s %3$s", chunk.getWorld().getName(), chunk.getX(),
				chunk.getZ());

		for (final Entry<EntityType, Integer> entity : entities.entrySet())
			ChatUtil.sendMessage(sender, "  %1$s : %2$s", entity.getKey().getName(), entity.getValue());
	}
}