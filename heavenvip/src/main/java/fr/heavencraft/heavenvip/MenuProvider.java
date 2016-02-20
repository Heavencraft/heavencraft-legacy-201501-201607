package fr.heavencraft.heavenvip;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;
import fr.heavencraft.heavencore.utils.menu.options.CountOption;
import fr.heavencraft.heavencore.utils.menu.options.EmptyOption;
import fr.heavencraft.heavencore.utils.menu.options.Option;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenvip.menus.MainMenu;
import fr.heavencraft.heavenvip.querys.UpdateHPSBalanceQuery;

public class MenuProvider
{
	
	/**
	 * Returns a menu to equip a player with a VIP effect
	 * @param p
	 * @param packName
	 * @param packId
	 * @param lastMenu
	 * @return
	 */
	//TODO convert this to new menu format
	public Menu getItemEquipMenu(final Player p, final String packName, final int packId, final Menu lastMenu)
	{
		Menu menu = new Menu("§7Equipement pour " + packName, 3);

		// Get a list of effects in this pack
		try (PreparedStatement ps = HeavenVIP.getMainConnection().getConnection()
				.prepareStatement("SELECT vip_effects.*, CASE WHEN EXISTS(" +
						"SELECT 1 FROM vip_equiped WHERE vip_equiped.uuid = ? AND vip_equiped.effect_id = vip_effects.vip_effect_id LIMIT 1)" +
						"THEN 1 " +
						"ELSE 0 " +
						"END as active " +
						"FROM vip_effects WHERE vip_effects.pack_id = ?"))
		{
			ps.setString(1, PlayerUtil.getUUID(p));
			ps.setInt(2, packId);

			final ResultSet rs = ps.executeQuery();
			int x = 0;
			int y = 0;
			// For each effect
			while (rs.next())
			{
				final int effectId = rs.getInt("vip_effect_id");
				final String displayName = ChatColor.translateAlternateColorCodes('§', rs.getString("name"));
				final boolean active = rs.getBoolean("active");
				final String desc = rs.getString("description");
				final Material repMat = (Material.getMaterial(rs.getString("representative_item")) == null) ? 
						Material.NETHER_STAR : Material.getMaterial(rs.getString("representative_item"));
				// Generate LED
				final short ledColor = (active ? (short)10 : (short)1);
				final int fx = x;
				final int fy = y;

				// Generate option
				Option opt = new Option(repMat, displayName, MenuUtils.generateEffectDescriptionLore(displayName, desc)) {
					@Override
					public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current,
							ClickType type) throws HeavenException
					{
						// Is active
						if(MenuAPI.getMenu(player).getOption(fx, fy).getDamage() == 10)
						{
							MenuUtils.unequipEffect('p', p, effectId, new MenuUtils.IMenuUpdateCallback() {
								@Override
								public void success()
								{
									MenuAPI.getMenu(player).getOption(fx, fy +1).setDamage((short)1);
									try
									{
										MenuAPI.getMenu(player).show(player);
										ChatUtil.broadcastMessage("Refresh");
									}
									catch (HeavenException e)
									{
										e.printStackTrace();
									}
								}
								@Override
								public void fail()
								{	
								}
							});

						}
						else
						{
							MenuUtils.equipEffect('p', player, effectId, new MenuUtils.IMenuUpdateCallback() {
								@Override
								public void success()
								{
									MenuAPI.getMenu(player).getOption(fx, fy +1).setDamage((short)10);
									try
									{
										MenuAPI.getMenu(player).show(player);
										ChatUtil.broadcastMessage("Refresh");
									}
									catch (HeavenException e)
									{
									}
								}
								@Override
								public void fail()
								{
								}
							});
						}
					}
				};
				// Representative item
				menu.addOption(x, y, opt);

				Option optLed = new Option(Material.INK_SACK, ledColor, "") {

					@Override
					public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current,
							ClickType type) throws HeavenException
					{
					}
				};
				menu.addOption(x, y+1, optLed);

				// Handle wrapping
				x+=2;
				// have we filled a line?
				if(x >= 9)
				{
					x = 0;
					++y;
				}
			}
			// Attach Navigation Bar
			MenuUtils.attachNavigationBar(menu, lastMenu, p, y + 2);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return menu;
	}

}
