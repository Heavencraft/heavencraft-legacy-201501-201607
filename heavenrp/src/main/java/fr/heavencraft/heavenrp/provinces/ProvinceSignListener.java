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
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignWithConfirmationListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.provinces.RemoveProvinceQuery;
import fr.heavencraft.heavenrp.database.provinces.UpdateProvinceQuery;
import fr.heavencraft.heavenrp.database.users.IncrementProvinceChangesCountQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;
import fr.heavencraft.heavenrp.scoreboards.ProvinceScoreboard;

public class ProvinceSignListener extends AbstractSignWithConfirmationListener
{
	private static final String JOIN = "Rejoindre";
	private static final String LEAVE = "Quitter";
	private static final String BLUE_JOIN = ChatColor.BLUE + JOIN;
	private static final String BLUE_LEAVE = ChatColor.BLUE + LEAVE;

	private static final String JOIN_MSG = "Vous vous apprêtez à rejoindre la province {%1$s}.";
	private static final String LEAVE_MSG = "Vous vous apprêtez à quitter votre province. Les frais de dossier s'élèvent à {%1$s} pièces d'or.";

	public ProvinceSignListener(HeavenRP plugin)
	{
		super(plugin, "Province", RPPermissions.PROVINCE_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		final String line = event.getLine(1);

		if (line.equalsIgnoreCase(JOIN))
		{
			ProvincesManager.getProvinceByName(event.getLine(2)); // On vérifie si la province existe.
			event.setLine(1, BLUE_JOIN);
			return true;
		}

		if (line.equalsIgnoreCase(LEAVE))
		{
			event.setLine(1, BLUE_LEAVE);
			return true;
		}

		return false;
	}

	@Override
	protected void onFirstClick(Player player, Sign sign) throws HeavenException
	{
		final String line = sign.getLine(1);

		if (line.equals(BLUE_JOIN))
		{
			ChatUtil.sendMessage(player, JOIN_MSG, sign.getLine(2));
		}
		else if (line.equals(BLUE_LEAVE))
		{
			ChatUtil.sendMessage(player, LEAVE_MSG, getLeavingFees(UserProvider.getUserByName(player.getName())));
		}
	}

	@Override
	protected void onSecondClick(Player player, Sign sign) throws HeavenException
	{
		final String line = sign.getLine(1);

		if (line.equals(BLUE_JOIN))
		{
			onJoinSignClick(player, sign.getLine(2));
		}
		else if (line.equals(BLUE_LEAVE))
		{
			onLeaveSignClick(player);
		}
	}

	private void onJoinSignClick(final Player player, String provinceName) throws HeavenException
	{
		final User user = UserProvider.getUserByName(player.getName());

		if (ProvincesManager.getProvinceByUser(user) != null)
			throw new HeavenException("Vous êtes déjà habitant d'une province");

		final Province province = ProvincesManager.getProvinceByName(provinceName);

		// Prepare Query Chain
		final List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateProvinceQuery(user, province));
		queries.add(new IncrementProvinceChangesCountQuery(user));
		QueriesHandler.addQuery(new BatchQuery(queries)
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
		final User user = UserProvider.getUserByName(player.getName());

		if (ProvincesManager.getProvinceByUser(user) == null)
			throw new HeavenException("Vous n'êtes habitant d'aucune province.");

		final int fees = getLeavingFees(user);
		final List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateUserBalanceQuery(user, fees));
		queries.add(new RemoveProvinceQuery(user));
		QueriesHandler.addQuery(new BatchQuery(queries)
		{
			@Override
			public void onSuccess()
			{
				// Apply province colors
				ProvinceScoreboard.applyTeamColor(player, null);

				ChatUtil.sendMessage(player, "Vous ne faîtes plus partie d'aucune province.");
				ChatUtil.sendMessage(player, "Les frais de dossier vous ont coûté {%d} pièces d'or.", fees);
			}

			@Override
			public void onHeavenException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		});
	}

	/**
	 * Returns the fees the player has to pay if he leaves a province
	 * 
	 * @param user
	 * @return fees (positive value)
	 */
	public static int getLeavingFees(User user)
	{
		switch (user.getProvinceChanges())
		{
			case 0:
			case 1:
				return 50;
			case 2:
				return 250;
			case 3:
				return 740;
			case 4:
				return 1250;
			case 5:
				return 1700;
			case 6:
				return 2400;
			case 7:
				return 3000;
			case 8:
				return 3600;
			case 9:
				return 4200;

			default:
				return 5000;
		}
	}
}