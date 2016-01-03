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

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;
import fr.heavencraft.heavencore.utils.menu.options.Option;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public class MenuProvider
{
	private final static String ITEM_NOT_FOR_SALE = "Cet objet n'est pas a vendre en ce moment.";
	private final static String ITEM_BOUGHT = "Félécitaions! Vous venez d'acheter un nouvel objet! Vous pouvez vous en équiper dorénavant!";
	private final static String NO_HPS_ACCOUNT = "Vous n'avez pas de HPS, plus d'informations: www.heavencraft.fr";
	
	public Menu getMainVIPMenu(Player p) {
		Menu menu = new Menu("§6Aventages VIP -- HPS", 1);
		
		// Particles
		menu.addOption(0, 0, new Option(Material.NETHER_STAR, ChatColor.GOLD + "Particules") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
			{
				// Open the particle sub-menu
				MenuAPI.closeMenu(p);
				MenuAPI.openMenu(player, getParticleMenu(p));
			}
		});
		
		return menu;
	}
	
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
						"FROM vip_pack WHERE vip_pack.effect_type = 'p'"))
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
				else if(packMonth == actualMonth)
					itemDurability = (short)14;
				else
					itemDurability = (short)7;
				
				final short durability = itemDurability;
				final int price = rs.getInt("price");
				// Create menu entry
				menu.addOption(x, y, new Option(material, itemDurability, displayName) {
					@Override
					public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
					{
						if(durability == 5) 
						{
							//TODO open equip menu
							Bukkit.broadcastMessage("You already own this package.");
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
				
				// Handle warping
				x++;
				// have we filled a line?
				if(x >= 9)
				{
					x = 0;
					y++;
				}		
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return menu;
	}
	
	public Menu getItemBuyPrompt(final Player p, final int price, final int productId , final Menu lastMenu)
	{
		Menu menu = new Menu("§7Valider l'achat pour " + price, 1);
		
		try
		{
			int hpsCount = HpsManager.getBalance(p.getName());
			//TODO ad HPS count
			menu.addOption(0, 0, new Option(Material.GOLD_INGOT, "Mes HPS: " + ChatColor.YELLOW + hpsCount) {
				@Override
				public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
						throws HeavenException
				{}
			});
			
			
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
		}
		catch (HeavenException e)
		{
			// In most cases, this means the user has no account on the Website, and not enough HPS.
			ChatUtil.sendMessage(p, e.getMessage());
			return null;
		}
		return menu;
	}
}
