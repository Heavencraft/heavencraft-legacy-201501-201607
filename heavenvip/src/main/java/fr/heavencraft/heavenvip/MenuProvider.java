package fr.heavencraft.heavenvip;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;
import fr.heavencraft.heavencore.utils.menu.options.Option;

public class MenuProvider
{
	
	public Menu getMainVIPMenu(Player p) {
		Menu menu = new Menu("§7Aventages VIP -- HPS", 1);
		
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
		
		Menu menu = new Menu("§7Particules -- HPS", 2);
		// Get the list of particles
		try (PreparedStatement ps = HeavenVIP.getMainConnection()
				.getConnection().prepareStatement(
						"SELECT vip_pack.*, CASE WHEN EXISTS(" +
						"SELECT 1 FROM vip_bought WHERE vip_bought.user_id = '19034' AND vip_bought.pack_id = vip_pack.vip_pack_id LIMIT 1)" +
						"THEN 1 " +
						"ELSE 0 " +
						"END as owns " +
						"FROM vip_pack WHERE vip_pack.effect_type = 'p'"))
		{
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
				short durability = (short) 14;
				
				Date date = new Date(System.currentTimeMillis());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int actualMonth = cal.get(Calendar.MONTH) + 1;
				int packMonth = rs.getInt("month");
				
				if(owns)
					durability = (short)5;
				else if(packMonth == actualMonth)
					durability = (short)14;
				else
					durability = (short)7;
				
				// Create menu entry
				menu.addOption(x, y, new Option(material, durability, displayName) {
					@Override
					public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
					{
						
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

}
