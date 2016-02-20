package fr.heavencraft.heavenvip.menus.prompts;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;
import fr.heavencraft.heavencore.utils.menu.options.Option;
import fr.heavencraft.heavenvip.MenuUtils;
import fr.heavencraft.heavenvip.menus.MainMenu;
import fr.heavencraft.heavenvip.menus.VipMenu;
import fr.heavencraft.heavenvip.querys.UpdateHPSBalanceQuery;
import fr.heavencraft.heavenvip.vippacks.VipPackProvider;

public class BuyPrompt extends VipMenu
{
	private final static String ITEM_BOUGHT = "Félécitaions! Vous venez d'acheter un nouvel objet! Vous pouvez dorénavant vous en équiper!";

	public BuyPrompt(final Player p, final int price, final int productId , final Menu lastMenu)
	{
		super(new Menu("§7Valider l'achat pour " + price + " HPS", 1));

		// Buy Option
		super.m.addOption(3, 0, new Option(Material.INK_SACK, (short)10, "Valider")
		{
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				// Update balance
				QueriesHandler.addQuery(new UpdateHPSBalanceQuery(player.getName(), -price)
				{
					@Override
					public void onSuccess()
					{
						VipPackProvider.addPack(player, productId);
						
						try
						{
							MenuAPI.closeMenu(player);
						}
						catch (HeavenException e)
						{
							e.printStackTrace();
						}
						ChatUtil.sendMessage(player, ITEM_BOUGHT);
					}
					@Override
					public void onHeavenException(HeavenException ex)
					{
						ChatUtil.sendMessage(player, ex.getMessage());
					}
				});
			}
		});

		// Cancel Option
		super.m.addOption(5, 0, new Option(Material.INK_SACK, (short)1, "Annuler") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				MenuAPI.closeMenu(player);
				MenuAPI.openMenu(player, lastMenu);
			}
		});
	}

	@Override
	public void openMenu(Player p)
	{
		// Attach Navigation Bar
		MenuUtils.attachNavigationBar(this, new MainMenu(), p, super.m.getHeight());
		try
		{
			super.OpenMenu(p);
		}
		catch (HeavenException e)
		{
			e.printStackTrace();
		}
	}

}
