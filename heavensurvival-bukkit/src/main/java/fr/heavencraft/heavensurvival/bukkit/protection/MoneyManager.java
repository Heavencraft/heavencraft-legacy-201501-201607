package fr.heavencraft.heavensurvival.bukkit.protection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavensurvival.bukkit.teleport.NotEnoughNuggetsException;

public class MoneyManager
{
	private static Material _money = Material.GOLD_NUGGET;

	public static boolean hasEnough(Player player, int amount)
	{
		return player.getInventory().contains(_money, amount);
	}

	public static void pay(Player player, int amount) throws HeavenException
	{
		if (!hasEnough(player, amount))
			throw new NotEnoughNuggetsException(amount);

		int account = 0;

		for (final ItemStack item : player.getInventory().all(_money).values())
		{
			account += item.getAmount();
		}

		if (account < amount)
			throw new NotEnoughNuggetsException(amount);

		player.getInventory().remove(_money);
		account = account - amount;

		while (account > 0)
		{
			if (account > 64)
			{
				player.getInventory().addItem(new ItemStack(_money, 64));
				account -= 64;
			}
			else
			{
				player.getInventory().addItem(new ItemStack(_money, account));
				account = 0;
			}
		}

		player.updateInventory();
	}
}
