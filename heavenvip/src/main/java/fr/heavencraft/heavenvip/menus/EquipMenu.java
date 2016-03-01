package fr.heavencraft.heavenvip.menus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.options.Option;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenvip.HeavenVIP;
import fr.heavencraft.heavenvip.MenuUtils;
import fr.heavencraft.heavenvip.vipeffects.EffectCache;

public class EquipMenu extends VipMenu
{
	public EquipMenu(final Player p, final String packName, final int packId)
	{
		super(new Menu("§7Equipement pour " + packName, 3));

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
				ChatUtil.broadcastMessage(rs.getString("representative_item"));
				final Material repMat = (Material.getMaterial(rs.getString("representative_item").toUpperCase()) == null) ? 
						Material.NETHER_STAR : Material.getMaterial(rs.getString("representative_item").toUpperCase());
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
						// Effect is active, green led. On click, remove from equiped.
						if(ledColor == 10)
						{
							MenuUtils.unequipEffect('p', p, effectId, new MenuUtils.IMenuUpdateCallback() {
								@Override
								public void success()
								{
									menu.getOption(fx, fy +1).setDamage((short)1);
									EquipMenu eMenu = new EquipMenu(player, packName, packId);
									eMenu.openMenu(player);
									EffectCache.updateCache(p);
								}
								@Override
								public void fail()
								{	
								}
							});

						}
						else
						{
							// Item is not in use.
							MenuUtils.equipEffect('p', player, effectId, new MenuUtils.IMenuUpdateCallback() {
								@Override
								public void success()
								{
									menu.getOption(fx, fy +1).setDamage((short)10);
									EquipMenu eMenu = new EquipMenu(player, packName, packId);
									eMenu.openMenu(player);
									EffectCache.updateCache(p);
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
				super.m.addOption(x, y, opt);

				Option optLed = new Option(Material.INK_SACK, ledColor, "") {

					@Override
					public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current,
							ClickType type) throws HeavenException
					{
					}
				};
				super.m.addOption(x, y+1, optLed);

				// Handle wrapping
				x+=2;
				// Update coordinates			
				if(++x >= 9) {
					x = 0;
					++y;
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void openMenu(Player p)
	{
		// Attach Navigation Bar
		MenuUtils.attachNavigationBar(this, new ParticlePackMenu(p), p, super.m.getHeight());
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
