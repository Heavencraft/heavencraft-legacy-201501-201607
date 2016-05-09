package fr.heavencraft.heavencrea.users.color;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.heavencraft.deprecated.DeprecatedTabColorScoreboard;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.users.color.TabColor;
import fr.heavencraft.heavencrea.CreaPermissions;
import fr.heavencraft.heavencrea.HeavenCrea;
import fr.heavencraft.heavencrea.users.CreativeUser;

public class TabColorListener extends AbstractListener<HeavenCrea>
{
	private static final TabColor ARCHITECT_COLOR = TabColor.DARK_PURPLE;
	private static final TabColor TALENT_COLOR = TabColor.GREEN;
	private static final TabColor ADMIN_COLOR = TabColor.DARK_GRAY;

	private final DeprecatedTabColorScoreboard<HeavenCrea, CreativeUser> scoreboard;

	public TabColorListener(HeavenCrea plugin)
	{
		super(plugin);

		scoreboard = new DeprecatedTabColorScoreboard<HeavenCrea, CreativeUser>(plugin);
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		final Player player = event.getPlayer();
		final CreativeUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());

		if (player.hasPermission(CreaPermissions.HIDE_TABCOLOR))
		{
			if (user.getTabColor() != TabColor.WHITE)
			{
				user.setTabColor(TabColor.WHITE);
				scoreboard.setPlayerColor(player, TabColor.WHITE);
			}
		}
		
		else if (player.hasPermission(CreaPermissions.ADMIN_TABCOLOR))
		{
			if (user.getTabColor() != ADMIN_COLOR)
			{
				user.setTabColor(ADMIN_COLOR);
				scoreboard.setPlayerColor(player, ADMIN_COLOR);
			}
		}

		else if (player.hasPermission(CreaPermissions.ARCHITECT))
		{
			if (user.getTabColor() != ARCHITECT_COLOR)
			{
				user.setTabColor(ARCHITECT_COLOR);
				scoreboard.setPlayerColor(player, ARCHITECT_COLOR);
			}
		}

		else if (player.hasPermission(CreaPermissions.TALENT))
		{
			if (user.getTabColor() != TALENT_COLOR)
			{
				user.setTabColor(TALENT_COLOR);
				scoreboard.setPlayerColor(player, TALENT_COLOR);
			}
		}

		else if (user.getTabColor() != TabColor.WHITE)
		{
			user.setTabColor(TabColor.WHITE);
			scoreboard.setPlayerColor(player, TabColor.WHITE);
		}
	}
}