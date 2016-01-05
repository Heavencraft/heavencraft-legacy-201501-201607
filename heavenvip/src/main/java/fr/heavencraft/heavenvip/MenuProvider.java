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
import fr.heavencraft.heavenvip.querys.UpdateHPSBalanceQuery;

public class MenuProvider
{
	private final static String ITEM_NOT_FOR_SALE = "Cet objet n'est pas a vendre en ce moment.";
	private final static String ITEM_BOUGHT = "Félécitaions! Vous venez d'acheter un nouvel objet! Vous pouvez dorénavant vous en équiper!";
	private final static String NO_HPS_ACCOUNT = "Vous n'avez pas de HPS, plus d'informations: www.heavencraft.fr";

	private final static byte PLAYER = 3;

	public Menu getMainVIPMenu(Player p) {
		Menu menu = new Menu("§6Aventages VIP -- HPS", 1);

		// Particles
		menu.addOption(3, 0, new Option(Material.NETHER_STAR, ChatColor.GOLD + "Particules") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
			{
				// Open the particle sub-menu
				MenuAPI.openMenu(player, getParticleMenu(p));
			}
		});

		// Heads
		menu.addOption(5, 0, new Option(Material.SKULL_ITEM, (short)3, 1, ChatColor.GOLD + "Tetes du Staff") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
			{
				// Open the particle sub-menu
				MenuAPI.openMenu(player, getPlayerHeadMenu(p));
			}
		});

		//TODO remove this

		menu.addOption(7, 0, new EmptyOption(Material.ANVIL) {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
			{
				// Open the particle sub-menu
				try
				{
					MenuAPI.openMenu(player, getTestMenu(p));
				}
				catch (HeavenException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return menu;
	}

	public Menu getPlayerHeadMenu(Player p)
	{
		Menu menu = new Menu("§6Têtes -- HPS", 4);

		try (PreparedStatement ps = HeavenVIP.getMainConnection()
				.getConnection().prepareStatement("SELECT * from vip_pack WHERE effect_type = 'h' " +
						"ORDER BY vip_pack_id ASC"))
		{
			final ResultSet rs = ps.executeQuery();
			int x = 0;
			int y = 0;
			while (rs.next())
			{
				// Generate head

				final int price = rs.getInt("price");
				final String playerName = rs.getString("data");
				final String desc = rs.getString("description");

				ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, PLAYER);
				SkullMeta meta = (SkullMeta) head.getItemMeta();
				meta.setOwner(playerName);
				head.setItemMeta(meta);

				// Create menu entry
				menu.addOption(x, y, new Option(head, MenuUtils.generateShopItemLore(playerName, price, desc)) {
					@Override
					public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
					{
						QueriesHandler.addQuery(new UpdateHPSBalanceQuery(player.getName(), -price)
						{
							@Override
							public void onSuccess()
							{
								player.getInventory().addItem(current);
							}
							@Override
							public void onHeavenException(HeavenException ex)
							{
								ChatUtil.sendMessage(player, ex.getMessage());
							}
						});
					}
				});

				// Handle warping
				x++;
				// have we filled a line?
				if(x >= 9)
				{
					x = 0;
					y++;
				}
			}
			// Attach Navigation Bar
			MenuUtils.attachNavigationBar(menu, getMainVIPMenu(p), p, ++y);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return menu;
	}

	/**
	 * Returns a particle menu
	 * @param p Player
	 * @return
	 */
	public Menu getParticleMenu(Player p) {
		Menu menu = new Menu("§6Particules -- HPS", 2);

		// Get the list of particles
		try (PreparedStatement ps = HeavenVIP.getMainConnection()
				.getConnection().prepareStatement(
						"SELECT vip_pack.*, CASE WHEN EXISTS(" +
								"SELECT 1 FROM vip_bought WHERE vip_bought.user_uuid = ? AND vip_bought.pack_id = vip_pack.vip_pack_id LIMIT 1)" +
								"THEN 1 " +
								"ELSE 0 " +
								"END as owns " +
						"FROM vip_pack WHERE vip_pack.effect_type = 'p' ORDER BY vip_pack.month ASC"))
		{
			ps.setString(1, PlayerUtil.getUUID(p));
			final ResultSet rs = ps.executeQuery();
			int x = 0;
			int y = 0;
			// For each pack
			while (rs.next())
			{
				// Generate representative item
				// Wool durability: grey:7 green:5 other:14
				Material material = Material.WOOL;
				String displayName = ChatColor.translateAlternateColorCodes('§', rs.getString("name"));
				boolean owns = rs.getBoolean("owns");
				short itemDurability = (short) 14;
				int productId = rs.getInt("vip_pack_id");

				Date date = new Date(System.currentTimeMillis());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int actualMonth = cal.get(Calendar.MONTH) + 1;
				int packMonth = rs.getInt("month");

				if(owns)
					itemDurability = (short)5;
				else if(packMonth == actualMonth || packMonth == 0)
					itemDurability = (short)14;
				else
					itemDurability = (short)7;

				final short durability = itemDurability;
				final int price = rs.getInt("price");
				final String desc = rs.getString("description");
				// Create menu entry
				menu.addOption(x, y, new Option(material, itemDurability, displayName, MenuUtils.generateShopItemLore(displayName, price, desc)) {
					@Override
					public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
					{
						if(durability == 5) 
						{
							MenuAPI.openMenu(player, getItemEquipMenu(player, current.getItemMeta().getDisplayName(), productId, menu));
						}
						else if(durability == 7)
						{
							ChatUtil.sendMessage(player, ITEM_NOT_FOR_SALE);
						}
						else if(durability == 14)
						{
							Menu m = getItemBuyPrompt(player, price, productId, getParticleMenu(player));
							if(m != null)
								MenuAPI.openMenu(player, m);
							else 
							{
								ChatUtil.sendMessage(player, NO_HPS_ACCOUNT);
								try
								{
									MenuAPI.closeMenu(player);
								}
								catch (HeavenException e)
								{}
							}
						}
					}
				});
				// Handle wrapping
				x++;
				// have we filled a line?
				if(x >= 9)
				{
					x = 0;
					y++;
				}
			}
			// Attach Navigation Bar
			MenuUtils.attachNavigationBar(menu, getMainVIPMenu(p), p, ++y);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}

		return menu;
	}

	/**
	 * Returns a menu to equip a player with a VIP effect
	 * @param p
	 * @param packName
	 * @param packId
	 * @param lastMenu
	 * @return
	 */
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
										MenuAPI.getMenu(player).refresh(player);
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
										MenuAPI.getMenu(player).refresh(player);
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

	public Menu getItemBuyPrompt(final Player p, final int price, final int productId , final Menu lastMenu)
	{
		Menu menu = new Menu("§7Valider l'achat pour " + price, 1);

		// Validate
		menu.addOption(3, 0, new Option(Material.INK_SACK, (short)10, "Valider")
		{
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				// Buy
				QueriesHandler.addQuery(new UpdateHPSBalanceQuery(player.getName(), -price)
				{
					@Override
					public void onSuccess()
					{
						// Add item to bought list
						try (PreparedStatement ps = HeavenVIP.getMainConnection().getConnection()
								.prepareStatement("INSERT INTO vip_bought (user_uuid, pack_id, timestamp) VALUES (?, ?, ?);"))
						{

							ps.setString(1, PlayerUtil.getUUID(player));
							ps.setInt(2, productId);
							ps.setLong(3, System.currentTimeMillis());

							ps.executeUpdate();
							ps.close();
							try
							{
								MenuAPI.closeMenu(player);
								ChatUtil.sendMessage(player, ITEM_BOUGHT);
							}
							catch (HeavenException e)
							{}
							// TODO Tell the player he bought it.
						}
						catch (final SQLException ex)
						{
							ex.printStackTrace();
							Bukkit.getLogger().log(Level.WARNING, "ERROR WHEN ADDING VIP PERK AFTER HPS REMOVAL(" + player.getName() + ")");
						}	
					}
					@Override
					public void onHeavenException(HeavenException ex)
					{
						ChatUtil.sendMessage(player, ex.getMessage());
						try
						{
							MenuAPI.closeMenu(player);
						}
						catch (HeavenException e)
						{}
					}
				});
			}
		});

		// Cancel
		menu.addOption(5, 0, new Option(Material.INK_SACK, (short)1, "Annuler") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				MenuAPI.closeMenu(player);
				MenuAPI.openMenu(player, lastMenu);
			}
		});
		return menu;
	}



	public Menu getTestMenu(Player p) {
		Menu menu = new Menu("§6TEST", 3);

		// Emtpy menu
		menu.addOption(0, 0, new EmptyOption(Material.CLAY));

		// Count Option
		menu.addOption(1, 0, new CountOption(Material.WOOD) {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				ChatUtil.broadcastMessage("COUNT OF: " + current.getAmount());
			}
		});

		// Inventory Update test
		menu.addOption(2, 0, new Option(Material.STONE, (short)3, 1, ChatColor.GOLD + "Blablabla") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
			{
				// Open the particle sub-menu
				MenuAPI.getMenu(player).addOption(1, 2, new EmptyOption(Material.BEDROCK));
				MenuAPI.getMenu(player).refresh(player);
				ChatUtil.broadcastMessage("Blablabla");
			}
		});

		return menu;
	}

}
