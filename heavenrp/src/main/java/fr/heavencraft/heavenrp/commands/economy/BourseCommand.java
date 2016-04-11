package fr.heavencraft.heavenrp.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class BourseCommand extends AbstractCommandExecutor
{
	private final static String PURSE_MESSAGE = "Vous comptez le nombre de pièces d'or dans votre bourse...";
	private final static String PURSE_EMPTY = "Malheureusement, elle est vide... :-(";
	private final static String PURSE_SUCCESS = "Fantastique ! Vous avez {%1$s} pièces d'or !";
	private final static String PURSE_FAIL = "Vous avez perdu le compte... Faites /bourse pour recompter.";

	public BourseCommand(HeavenRP plugin)
	{
		super(plugin, "bourse");
	}

	@Override
	protected void onPlayerCommand(final Player player, final String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(player, PURSE_MESSAGE);
		final int balance = UserProvider.getUserByName(player.getName()).getBalance();

		Bukkit.getScheduler().runTaskLaterAsynchronously(HeavenRP.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				if (balance == 0)
				{
					ChatUtil.sendMessage(player, PURSE_EMPTY);
				}
				else if (balance < 100)
				{
					if (HeavenRP.Random.nextInt(11) == 0)
						ChatUtil.sendMessage(player, PURSE_FAIL);
					else
						ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
				else if (balance < 200)
				{
					if (HeavenRP.Random.nextInt(9) == 0)
						ChatUtil.sendMessage(player, PURSE_FAIL);
					else
						ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
				else if (balance < 500)
				{
					if (HeavenRP.Random.nextInt(8) == 0)
						ChatUtil.sendMessage(player, PURSE_FAIL);
					else
						ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
				else if (balance < 700)
				{
					if (HeavenRP.Random.nextInt(7) == 0)
						ChatUtil.sendMessage(player, PURSE_FAIL);
					else
						ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
				else if (balance < 1000)
				{
					if (HeavenRP.Random.nextInt(5) == 0)
						ChatUtil.sendMessage(player, PURSE_FAIL);
					else
						ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
				else if (balance >= 1000)
				{
					if (HeavenRP.Random.nextInt(4) == 0)
						ChatUtil.sendMessage(player, PURSE_FAIL);
					else
						ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
				else
					ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
			}
		}, 40);
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