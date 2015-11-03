package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public class CreacheatCommand extends AbstractCommandExecutor
{
	public CreacheatCommand(HeavenPlugin plugin)
	{
		super(plugin, "creacheat", CorePermissions.CREACHEAT_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{

		if (player.getGameMode() == GameMode.CREATIVE)
			player.setGameMode(GameMode.SURVIVAL);
		else
			player.setGameMode(GameMode.CREATIVE);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 1)
			return;

		Player player = PlayerUtil.getPlayer(args[0]);

		ChatUtil.sendMessage(sender, "{%1$s} est en mode {%2$s}.", player.getName(), player.getGameMode().name());
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}