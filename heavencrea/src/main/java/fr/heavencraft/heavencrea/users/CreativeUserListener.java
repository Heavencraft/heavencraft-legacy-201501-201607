package fr.heavencraft.heavencrea.users;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.users.DefaultUserListener;
import fr.heavencraft.heavencrea.HeavenCrea;

public class CreativeUserListener extends DefaultUserListener<HeavenCrea, CreativeUser>
{
	public CreativeUserListener(HeavenCrea plugin)
	{
		super(plugin);
	}

	@Override
	protected void onFirstConnection(Player player, CreativeUser user) throws HeavenException
	{
		user.updateBalance(100);
		plugin.sendMessage(player, "Vous venez d'obtenir {100} jetons en vous connectant !");
	}
}