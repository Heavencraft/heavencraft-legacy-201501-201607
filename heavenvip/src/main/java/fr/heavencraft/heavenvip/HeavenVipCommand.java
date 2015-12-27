package fr.heavencraft.heavenvip;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;

public class HeavenVipCommand extends AbstractCommandExecutor
{
	private MenuProvider menuProvider = new MenuProvider();
	public HeavenVipCommand(HeavenPlugin plugin)
	{
		super(plugin, "vip");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		MenuAPI.openMenu(player, menuProvider.getMainVIPMenu(player));
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}

}
