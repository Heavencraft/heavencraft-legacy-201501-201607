package fr.heavencraft.heavencrea.users;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.heavencraft.deprecated.DeprecatedDefaultUserListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencrea.HeavenCrea;

public class CreativeUserListener extends DeprecatedDefaultUserListener<HeavenCrea, CreativeUser>
{
	public CreativeUserListener(HeavenCrea plugin)
	{
		super(plugin);
	}

	@Override
	@EventHandler
	protected void onPlayerLogin(PlayerLoginEvent event) throws HeavenException
	{
		super.onPlayerLogin(event);
	}

	@Override
	@EventHandler
	protected void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		super.onPlayerJoin(event);
	}

	@Override
	protected void onFirstConnection(Player player, CreativeUser user) throws HeavenException
	{
		user.updateBalance(100);
		ChatUtil.sendMessage(player, "Vous venez d'obtenir {100} jetons en vous connectant !");
	}
}