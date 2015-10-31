package fr.heavencraft.heavencore.bukkit.commands;

import static fr.heavencraft.utils.ChatUtil.sendMessage;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.utils.ChatUtil;

public class HeadCommand extends AbstractCommandExecutor
{
	private final static byte PLAYER = 3;

	public HeadCommand(HeavenPlugin plugin)
	{
		super(plugin, "head", CorePermissions.HEAD_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, PLAYER);
		SkullMeta meta = (SkullMeta) head.getItemMeta();

		meta.setOwner(args[0]);

		head.setItemMeta(meta);
		player.getInventory().addItem(head);

		ChatUtil.sendMessage(player, "Vous venez de recevoir la tête de {" + args[0] + "}.");
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		sendMessage(sender, "/{head} <joueur>");
	}
}