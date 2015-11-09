package fr.heavencraft.heavencore.bukkit.listeners;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class CookieSignListener extends AbstractSignListener
{
	private static final Random rand = new Random();

	public CookieSignListener(HeavenPlugin plugin)
	{
		super(plugin, "Cookie", CorePermissions.COOKIE_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		if (rand.nextInt(10) != 0)
			return;

		ItemStack cookie = new ItemStack(Material.COOKIE);
		ItemMeta meta = cookie.getItemMeta();
		meta.setDisplayName("Cookie de l'amitié");
		cookie.setItemMeta(meta);
		player.getInventory().addItem(cookie);
		player.updateInventory();
	}

	@Override
	protected void onSignBreak(Player player, Sign sign) throws HeavenException
	{
	}
}