package fr.heavencraft.heavenrp.provinces;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.async.queries.BatchQuery;
import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.async.queries.Query;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.provinces.RemoveProvinceQuery;
import fr.heavencraft.heavenrp.database.provinces.UpdateProvinceQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;
import fr.heavencraft.heavenrp.scoreboards.ProvinceScoreboard;

public class ProvinceSignListener extends AbstractSignListener
{
	private static final String JOIN = "Rejoindre";
	private static final String LEAVE = "Quitter";

	public ProvinceSignListener(HeavenRP plugin)
	{
		super(plugin, "Province", RPPermissions.PROVINCE_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		if (event.getLine(1).equalsIgnoreCase(JOIN))
		{
			// On vérifie si la province existe.
			ProvincesManager.getProvinceByName(event.getLine(2));

			event.setLine(1, ChatColor.BLUE + JOIN);
			return true;
		}

		else if (event.getLine(1).equalsIgnoreCase(LEAVE))
		{
			event.setLine(1, ChatColor.BLUE + LEAVE);
			return true;
		}

		else
			return false;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		if (sign.getLine(1).equals(ChatColor.BLUE + JOIN))
			onJoinSignClick(player, sign.getLine(2));

		else if (sign.getLine(1).equals(ChatColor.BLUE + LEAVE))
			onLeaveSignClick(player);
	}

	private void onJoinSignClick(final Player player, String provinceName) throws HeavenException
	{
		User user = UserProvider.getUserByName(player.getName());

		if (ProvincesManager.getProvinceByUser(user) != null)
			throw new HeavenException("Vous êtes déjà habitant d'une province");

		final Province province = ProvincesManager.getProvinceByName(provinceName);

		QueriesHandler.addQuery(new UpdateProvinceQuery(user, province)
		{
			@Override
			public void onSuccess()
			{
				// Apply province colors
				ProvinceScoreboard.applyTeamColor(player, province);

				ChatUtil.sendMessage(player, "Vous venez de rejoindre la province de {%1$s}.",
						province.getName());
			}

			@Override
			public void onHeavenException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		});
	}

	private void onLeaveSignClick(final Player player) throws HeavenException
	{
		User user = UserProvider.getUserByName(player.getName());

		if (ProvincesManager.getProvinceByUser(user) == null)
			throw new HeavenException("Vous n'êtes habitant d'aucune province.");

		List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateUserBalanceQuery(user, -50));
		queries.add(new RemoveProvinceQuery(user));
		QueriesHandler.addQuery(new BatchQuery(queries)
		{
			@Override
			public void onSuccess()
			{
				// Apply province colors
				ProvinceScoreboard.applyTeamColor(player, null);

				ChatUtil.sendMessage(player, "Vous ne faîtes plus partie d'aucune province.");
				ChatUtil.sendMessage(player, "Les frais de dossier vous ont coûté {50} pièces d'or.");
			}

			@Override
			public void onHeavenException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		});
	}
}