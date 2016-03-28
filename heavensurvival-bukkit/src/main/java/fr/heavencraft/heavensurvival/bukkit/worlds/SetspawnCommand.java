package fr.heavencraft.heavensurvival.bukkit.worlds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavensurvival.bukkit.BukkitHeavenSurvival;
import fr.heavencraft.heavensurvival.common.SurvivalPermissions;

public class SetspawnCommand extends AbstractCommandExecutor
{
	public SetspawnCommand(BukkitHeavenSurvival plugin)
	{
		super(plugin, "setspawn", SurvivalPermissions.SET_SPAWN);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		WorldsManager.getInstance().setSpawnLocation(player.getLocation());
		ChatUtil.sendMessage(player, "L'emplacement du spawn a bien été changé.");
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}