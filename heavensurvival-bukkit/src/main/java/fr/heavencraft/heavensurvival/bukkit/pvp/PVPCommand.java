package fr.heavencraft.heavensurvival.bukkit.pvp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavensurvival.common.pvp.PVPManager;
import fr.heavencraft.heavensurvival.common.users.User;
import fr.heavencraft.heavensurvival.common.users.UserProvider;

public class PVPCommand extends AbstractCommandExecutor
{
	public PVPCommand(HeavenPlugin plugin)
	{
		super(plugin, "pvp");
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		final User user = UserProvider.getInstance().getUserByUniqueId(player.getUniqueId());

		if (user.isPvp())
		{
			PVPManager.get().setEnabled(user, false);
			ChatUtil.sendMessage(player, "Le pvp a été désactivé");
		}
		else
		{
			PVPManager.get().setEnabled(user, true);
			ChatUtil.sendMessage(player, "Le pvp a été activé");
		}
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