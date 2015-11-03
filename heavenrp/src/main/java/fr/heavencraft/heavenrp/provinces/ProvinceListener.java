package fr.heavencraft.heavenrp.provinces;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;
import fr.heavencraft.heavenrp.scoreboards.ProvinceScoreboard;

public class ProvinceListener extends AbstractListener<HeavenPlugin>
{
	public ProvinceListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		Player player = event.getPlayer();

		User user = UserProvider.getUserByName(player.getName());
		Province province = ProvincesManager.getProvinceByUser(user);

		// Apply province colors
		ProvinceScoreboard.applyTeamColor(player, province);

		if (province == null)
			ChatUtil.sendMessage(player, "Vous n'avez pas encore choisi de province.");
	}

	// @EventHandler
	// private void onNameTag(AsyncPlayerReceiveNameTagEvent event) throws
	// HeavenException
	// {
	// String playerName = event.getNamedPlayer().getName();
	//
	// if (playerName.length() > 14)
	// {
	// return;
	// }
	//
	// Province province = UserProvider.getUserByName(playerName).getProvince();
	//
	// if (province == null)
	// {
	// return;
	// }
	//
	// event.setTag(province.getColor() + playerName);
	// }
}