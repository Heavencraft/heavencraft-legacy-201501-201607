package fr.heavencraft.heavenvip.heads;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.options.Option;
import fr.heavencraft.heavenvip.MenuUtils;
import fr.heavencraft.heavenvip.menus.MainMenu;
import fr.heavencraft.heavenvip.menus.VipMenu;
import fr.heavencraft.heavenvip.querys.UpdateHPSBalanceQuery;

public class HeadMenu extends VipMenu
{
	private final static byte PLAYER = 3;
	public HeadMenu(Player p)
	{
		super(new Menu("§6Têtes", 3));
		// Variables for item coordinates inside inventory
		int x = 0;
		int y = 0;
		for(final Head hd : HeadProvider.getSoldHeads()) {
			// Generate representative item
			ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, PLAYER);
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			meta.setOwner(hd.getPlayerName());
			head.setItemMeta(meta);
			// Add option to menu
			super.m.addOption(x, y, new Option(head, 
					MenuUtils.generateShopItemLore(hd.getPlayerName(), hd.getPrice(), hd.getDescription())) {
				@Override
				public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current,
						ClickType type) throws HeavenException {
					QueriesHandler.addQuery(new UpdateHPSBalanceQuery(player.getName(), -hd.getPrice()) {
						@Override
						public void onSuccess() {
							player.getInventory().addItem(current);
						}
						@Override
						public void onHeavenException(HeavenException ex) {
							ChatUtil.sendMessage(player, ex.getMessage());
						}
					});
				};
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
