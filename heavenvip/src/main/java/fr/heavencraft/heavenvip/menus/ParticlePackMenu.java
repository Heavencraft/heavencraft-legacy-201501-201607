package fr.heavencraft.heavenvip.menus;

import java.util.Calendar;
import java.util.Date;

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
import fr.heavencraft.heavenvip.HpsManager;
import fr.heavencraft.heavenvip.MenuUtils;
import fr.heavencraft.heavenvip.querys.UpdateHPSBalanceQuery;
import fr.heavencraft.heavenvip.vippacks.PackType;
import fr.heavencraft.heavenvip.vippacks.VipPack;
import fr.heavencraft.heavenvip.vippacks.VipPackProvider;

public class ParticlePackMenu extends VipMenu
{
	private final static String ITEM_NOT_FOR_SALE = "Cet objet n'est pas a vendre en ce moment.";
	private final static String NOT_ENOUGH_HPS = "Vous n'avez pas assez d'HPS, plus d'informations: www.heavencraft.fr";
	private final static String ITEM_BOUGHT = "Merci pour votre achat! Vous pouvez désormez vous en équiper.";
	
	public ParticlePackMenu(Player p)
	{
		super(new Menu("Packs Particles", 4));
		// Variables for item coordinates inside inventory
		int x = 0;
		int y = 0;
		for(final VipPack pack : VipPackProvider.getPacks(p, PackType.PARTICLE)) {
			// Can we buy this item? Do we own it? -> set durability for item color.
			Date date = new Date(System.currentTimeMillis());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int actualMonth = cal.get(Calendar.MONTH) + 1;
			short packStateColor;
			if(pack.isOwned())
				packStateColor = (short)5;
			else if(pack.getMonthOfAvailability() == actualMonth || pack.getMonthOfAvailability() <= 0)
				packStateColor = (short)14;
			else
				packStateColor = (short)7;
			final short itemColor = packStateColor;

			super.m.addOption(x, y, new Option(Material.WOOL, itemColor, pack.getPackName(), 
					MenuUtils.generateShopItemLore(pack.getPackName(), pack.getPrice(), pack.getDescription())) {
				@Override
				public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current,
						ClickType type) throws HeavenException
				{
					if(itemColor == 5) {
						// We own this pack, open equip menu.
						EquipMenu eMenu = new EquipMenu(player, pack.getPackName(), pack.getVipPackId());
						eMenu.openMenu(player);
					}
					else if(itemColor == 7) {
						// Item is not for sale this month.
						ChatUtil.sendMessage(player, ITEM_NOT_FOR_SALE);
					}
					else if(itemColor == 14) {
						// Item is for sale, check if player has enough to buy.
						int hpsCount = HpsManager.getBalance(p.getName());
						if(pack.getPrice() > hpsCount){
							// Player is too poor
							ChatUtil.sendMessage(player, NOT_ENOUGH_HPS);
							return;
						}
						// Update balance
						QueriesHandler.addQuery(new UpdateHPSBalanceQuery(player.getName(), -pack.getPrice()) {
							@Override
							public void onSuccess() {
								// Add pack
								VipPackProvider.addPack(player, pack.getVipPackId());
								ChatUtil.sendMessage(player, ITEM_BOUGHT);
								//TODO open equip menu
								try
								{
									MenuAPI.openMenu(player, new MainMenu().GetMenu());
								}
								catch (HeavenException e)
								{
									e.printStackTrace();
								}
							}
							@Override
							public void onHeavenException(HeavenException ex) {
								// Error
								ChatUtil.sendMessage(player, ex.getMessage());
							}
						});
					}
				}
			});
			// Update coordinates			
			if(++x >= 9) {
				x = 0;
				++y;
			}
		}
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
