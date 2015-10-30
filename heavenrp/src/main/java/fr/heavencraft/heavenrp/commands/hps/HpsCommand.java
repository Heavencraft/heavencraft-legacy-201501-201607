package fr.heavencraft.heavenrp.commands.hps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.hps.UpdateHPSBalanceQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.hps.HpsManager;

public class HpsCommand extends AbstractCommandExecutor
{
	private final static byte PLAYER = 3;

	public HpsCommand(HeavenRP plugin)
	{
		super(plugin, "hps");
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		if (args[0].equalsIgnoreCase("tete"))
		{
			QueriesHandler.addQuery(new UpdateHPSBalanceQuery(player.getName(), -5)
			{
				@Override
				public void onSuccess()
				{
					ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, PLAYER);
					SkullMeta meta = (SkullMeta) head.getItemMeta();

					meta.setOwner(player.getName());

					head.setItemMeta(meta);
					player.getInventory().addItem(head);

					ChatUtil.sendMessage(player, "{%1$d} HPs ont été retirés de votre compte", 5);
					ChatUtil.sendMessage(player, "Vous venez de recevoir votre tête.");
				}

				@Override
				public void onHeavenException(HeavenException ex)
				{
					ChatUtil.sendMessage(player, ex.getMessage());
				}
			});
		}
		else
		{
			final int hps = DevUtil.toUint(args[0]);

			User user = UserProvider.getUserByName(player.getName());

			List<Query> queries = new ArrayList<Query>();
			queries.add(new UpdateHPSBalanceQuery(player.getName(), -hps));
			queries.add(new UpdateUserBalanceQuery(user, hps * 20));
			QueriesHandler.addQuery(new BatchQuery(queries)
			{
				@Override
				public void onSuccess()
				{
					ChatUtil.sendMessage(player, "{%1$d} HPs ont été retirés de votre compte", hps);
					ChatUtil.sendMessage(player, "Vous avez reçu {%1$d} pièces d'or.", hps * 20);
				}

				@Override
				public void onHeavenException(HeavenException ex)
				{
					ChatUtil.sendMessage(player, ex.getMessage());
				}
			});
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{hps} <nombre de HPs a convertir>");
		ChatUtil.sendMessage(sender, "/{hps} tete : acheter votre tête pour 5 HPs.");
		ChatUtil.sendMessage(sender, "Le taux est de {20} pièces d'or par HP.");
		try
		{
			ChatUtil.sendMessage(sender, "Vous avez {%1$d} HPs sur votre compte.",
					HpsManager.getBalance(sender.getName()));
		}
		catch (HeavenException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
