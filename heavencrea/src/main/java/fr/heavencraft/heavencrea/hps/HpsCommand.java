package fr.heavencraft.heavencrea.hps;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencrea.HeavenCrea;

public class HpsCommand extends AbstractCommandExecutor
{
	private final HeavenCrea plugin;

	public HpsCommand(HeavenCrea plugin)
	{
		super(plugin, "hps");

		this.plugin = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		final int hps = DevUtil.toUint(args[0]);

		if (hps < 0)
			throw new HeavenException("Le nombre est incorrect.");

		plugin.getHpsManager().removeBalance(player.getName(), hps);
		plugin.getUserProvider().getUserByUniqueId(player.getUniqueId()).updateBalance(hps * HpsManager.TAUX_JETON);

		plugin.sendMessage(player, "%1$s HPs ont été retirés de votre compte", hps);
		plugin.sendMessage(player, "Vous avez reçu {%1$s} Jetons.", hps * HpsManager.TAUX_JETON);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/{hps} <nombre de HPs a convertir>");
		plugin.sendMessage(sender, "Le taux est de {" + HpsManager.TAUX_JETON + "} Jetons par HP.");

		try
		{
			plugin.sendMessage(sender, "Vous avez {%1$s} HPs sur votre compte.",
					plugin.getHpsManager().getBalance(sender.getName()));
		}
		catch (final HeavenException e)
		{
			e.printStackTrace();
		}
	}
}