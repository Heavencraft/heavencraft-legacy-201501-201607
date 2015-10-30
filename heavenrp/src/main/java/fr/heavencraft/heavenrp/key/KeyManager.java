package fr.heavencraft.heavenrp.key;

import static fr.heavencraft.utils.PlayerUtil.getUUID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.exceptions.SQLErrorException;
import fr.heavencraft.heavenrp.HeavenRP;

public class KeyManager
{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	private static boolean isToday(Date date)
	{
		return dateFormat.format(date).equals(dateFormat.format(new Date()));
	}

	public static final String HEAVENCRAFT = ChatColor.WHITE + "Heaven" + ChatColor.AQUA + "craft";
	private static final int MAX_KEYS_PER_DAY = 5;

	public static void giveKey(Player player, String key) throws HeavenException
	{
		final String playerName = player.getName();
		final String uuid = getUUID(player);

		final int nbKeys = getNbKeysToday(uuid);

		if (nbKeys >= MAX_KEYS_PER_DAY)
			throw new HeavenException("Le joueur {%1$s} a déjà reçu {%2$s} clés aujourd'hui.", playerName, nbKeys);

		giveBook(player, key);
		incrementKeys(uuid);
	}

	private static void giveBook(Player player, String key)
	{
		final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		final BookMeta meta = (BookMeta) book.getItemMeta();

		meta.setTitle(key);
		meta.setAuthor(HEAVENCRAFT);

		book.setItemMeta(meta);
		player.getInventory().addItem(book);
		player.updateInventory();
	}

	private static int getNbKeysToday(String uuid) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement("SELECT date, nb FROM dungeon_keys WHERE uuid = ?"))
		{
			ps.setString(1, uuid);
			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
			{
				createKeys(uuid);
				return 0;
			}

			final Timestamp date = rs.getTimestamp("date");

			if (isToday(date))
				return rs.getInt("nb");
			else
				return 0;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	private static void createKeys(String uuid) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement("INSERT INTO dungeon_keys (uuid) VALUES (?);"))
		{
			ps.setString(1, uuid);
			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	private static void incrementKeys(String uuid) throws SQLErrorException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"UPDATE dungeon_keys SET nb = nb + 1 WHERE uuid = ?"))
		{
			ps.setString(1, uuid);
			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}
}
