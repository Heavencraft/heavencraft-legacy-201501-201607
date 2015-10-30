package fr.heavencraft.heavenrp.key;

import static fr.heavencraft.utils.DevUtil.getPlugin;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.listeners.sign.SignListener;

public class DonjonSignListener extends SignListener
{
	public DonjonSignListener()
	{
		super("Donjon", RPPermissions.DONJON_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		ItemStack item = null;

		for (Entry<Integer, ? extends ItemStack> entry : player.getInventory().all(Material.WRITTEN_BOOK).entrySet())
		{
			BookMeta meta = (BookMeta) entry.getValue().getItemMeta();

			if (!meta.getAuthor().equals(KeyManager.HEAVENCRAFT))
				continue;

			if (meta.getTitle().equals(sign.getLine(1)))
			{
				item = entry.getValue();
				break;
			}
		}

		if (item == null)
			return;

		if (item.getAmount() > 1)
			item.setAmount(item.getAmount() - 1);
		else
			player.getInventory().remove(item);

		Block signBlock = sign.getBlock();
		org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
		Block redstoneBlock = signBlock.getRelative(signData.getAttachedFace());

		Bukkit.getServer()
				.getScheduler()
				.runTaskLater(
						getPlugin(),
						new RestoreBlockTask(redstoneBlock.getWorld().getName(), redstoneBlock.getX(), redstoneBlock
								.getY(), redstoneBlock.getZ(), redstoneBlock.getType()), 40);

		redstoneBlock.setType(Material.REDSTONE_BLOCK);
	}

	class RestoreBlockTask extends BukkitRunnable
	{
		String _world;
		int _x;
		int _y;
		int _z;
		Material _type;

		public RestoreBlockTask(String world, int x, int y, int z, Material type)
		{
			_world = world;
			_x = x;
			_y = y;
			_z = z;
			_type = type;
		}

		@Override
		public void run()
		{
			Bukkit.getServer().getWorld(_world).getBlockAt(_x, _y, _z).setType(_type);
		}
	}
}