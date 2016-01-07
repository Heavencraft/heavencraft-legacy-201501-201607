package fr.heavencraft.heavenvip;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;
import fr.heavencraft.heavenvip.menus.MainMenu;

public class HeavenVipCommand extends AbstractCommandExecutor
{
	public HeavenVipCommand(HeavenPlugin plugin)
	{
		super(plugin, "vip");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		//MenuAPI.openMenu(player, menuProvider.getMainVIPMenu(player));
		// Open main menu
		MainMenu mmenu = new MainMenu();
		mmenu.openNewMenu(player);
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
