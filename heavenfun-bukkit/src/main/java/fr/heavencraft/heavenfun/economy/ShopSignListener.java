package fr.heavencraft.heavenfun.economy;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.users.balance.UpdateUserBalanceQuery;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenfun.common.users.FunUser;
import fr.heavencraft.heavenfun.common.users.FunUserProvider;

public class ShopSignListener extends AbstractSignListener
{
	private static final String CURRENCY = " FP";

	public ShopSignListener(HeavenPlugin plugin)
	{
		super(plugin, "Magasin", "");
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		final Material material = Material.matchMaterial(event.getLine(1));
		if (material == null)
			return false;

		final int price = DevUtil.toUint(event.getLine(2));

		event.setLine(1, material.name());
		event.setLine(2, price + CURRENCY);
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		final Material material = Material.matchMaterial(sign.getLine(1));
		final int price = DevUtil.toUint(sign.getLine(2).substring(0, sign.getLine(2).length() - 3));

		final FunUser user = FunUserProvider.get().getUserByUniqueId(player.getUniqueId());
		QueriesHandler.addQuery(new UpdateUserBalanceQuery(user, -price, FunUserProvider.get())
		{
			@Override
			public void onSuccess()
			{
				player.getInventory().addItem(new ItemStack(material));
				ChatUtil.sendMessage(player, "Vous avez acheté un %1$s.", material.name());
			}

			@Override
			public void onHeavenException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		});
	}
}