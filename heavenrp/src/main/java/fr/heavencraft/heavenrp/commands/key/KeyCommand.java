package fr.heavencraft.heavenrp.commands.key;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.key.KeyManager;

public class KeyCommand extends AbstractCommandExecutor
{

	public KeyCommand(HeavenRP plugin)
	{
		super(plugin, "key", RPPermissions.KEY);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 2)
			return;

		Player player = PlayerUtil.getPlayer(args[0]);
		String key = args[1];

		KeyManager.giveKey(player, key);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}