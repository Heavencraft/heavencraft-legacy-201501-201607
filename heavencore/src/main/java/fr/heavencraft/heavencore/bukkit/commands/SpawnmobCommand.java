package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public class SpawnmobCommand extends AbstractCommandExecutor
{
	private String availableMobs = "";

	public SpawnmobCommand(HeavenPlugin plugin)
	{
		super(plugin, "spawnmob", CorePermissions.SPAWNMOB_COMMAND);

		for (EntityType entity : EntityType.values())
			if (entity.isSpawnable())
				availableMobs += (availableMobs != "" ? ", " : "") + entity.getName();
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(player);
			return;
		}

		int nb = 1;

		if (args.length > 1)
			nb = DevUtil.toUint(args[1]);

		EntityType entity = EntityType.fromName(args[0]);

		if (entity == null || !entity.isSpawnable())
		{
			sendUsage(player);
			return;
		}

		for (int i = 0; i != nb; i++)
			player.getWorld().spawnEntity(player.getLocation(), entity);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{spawnmob} nom [quantité]");
		ChatUtil.sendMessage(sender, availableMobs);
	}
}