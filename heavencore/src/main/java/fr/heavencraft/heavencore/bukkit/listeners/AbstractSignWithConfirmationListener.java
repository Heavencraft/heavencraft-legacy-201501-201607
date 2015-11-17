package fr.heavencraft.heavencore.bukkit.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public abstract class AbstractSignWithConfirmationListener extends AbstractSignListener
{
	private static final String CONFIRM_MSG = "Cliquez une nouvelle fois sur le panneau pour confirmer.";

	private final Map<UUID, Click> clicksByPlayer = new HashMap<UUID, Click>();

	public AbstractSignWithConfirmationListener(HeavenPlugin plugin, String tag, String permission)
	{
		super(plugin, tag, permission);
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		if (hasClicked(player.getUniqueId(), sign.getX(), sign.getY(), sign.getZ()))
		{
			onSecondClick(player, sign);
		}
		else
		{
			onFirstClick(player, sign);
			ChatUtil.sendMessage(player, CONFIRM_MSG);
		}
	}

	protected abstract void onFirstClick(Player player, Sign sign) throws HeavenException;

	protected abstract void onSecondClick(Player player, Sign sign) throws HeavenException;

	private boolean hasClicked(UUID player, int x, int y, int z)
	{
		final long timestamp = System.currentTimeMillis();
		final Click click = clicksByPlayer.remove(player);

		if (click != null && click.x == x && click.y == y && click.z == z
				&& (timestamp - click.timestamp) < 60000)
		{
			return true;
		}
		else
		{
			clicksByPlayer.put(player, new Click(x, y, z, timestamp));
			return false;
		}
	}

	private class Click
	{
		int x, y, z;
		long timestamp;

		public Click(int x, int y, int z, long timestamp)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.timestamp = timestamp;
		}
	}
}