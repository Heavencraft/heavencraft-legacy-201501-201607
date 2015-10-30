package fr.heavencraft.heavenrp.general;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import fr.heavencraft.commands.HeavenCommand;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.utils.ChatUtil;
import fr.heavencraft.utils.DevUtil;
import fr.heavencraft.utils.PlayerUtil;

public class HurtCommand extends HeavenCommand
{
	public HurtCommand()
	{
		super("hurt", RPPermissions.HURT);
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
		{
			sendUsage(sender);
			return;
		}

		final Player player = PlayerUtil.getPlayer(args[0]);
		final int amount = DevUtil.toUint(args[1]);

		if (WorldGuardPlugin.inst().getRegionManager(player.getWorld())
				.getApplicableRegions(player.getLocation()).allows(DefaultFlag.PVP))
		{
			player.damage(amount);
		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{hust} <player> <dégats>");
	}
}