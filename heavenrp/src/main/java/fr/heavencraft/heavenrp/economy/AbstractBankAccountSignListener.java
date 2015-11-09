package fr.heavencraft.heavenrp.economy;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public abstract class AbstractBankAccountSignListener extends AbstractSignListener
{
	private static final String CONSULT = "Consulter";
	private static final String DEPOSIT = "Déposer";
	private static final String WITHDRAW = "Retirer";
	private static final String BLUE_CONSULT = ChatColor.BLUE + CONSULT;
	private static final String BLUE_DEPOSIT = ChatColor.BLUE + DEPOSIT;
	private static final String BLUE_WITHDRAW = ChatColor.BLUE + WITHDRAW;

	public AbstractBankAccountSignListener(HeavenPlugin plugin, String tag, String permission)
	{
		super(plugin, tag, permission);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event)
	{
		String line = event.getLine(1);

		if (line.equalsIgnoreCase(CONSULT))
		{
			event.setLine(1, BLUE_CONSULT);
			return true;
		}
		else if (line.equalsIgnoreCase(DEPOSIT))
		{
			event.setLine(1, BLUE_DEPOSIT);
			return true;
		}
		else if (line.equalsIgnoreCase(WITHDRAW))
		{
			event.setLine(1, BLUE_WITHDRAW);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		String line = sign.getLine(1);

		if (line.equals(BLUE_CONSULT))
		{
			onConsultSignClick(player);
		}
		else if (line.equals(BLUE_DEPOSIT))
		{
			onDepositSignClick(player);
		}
		else if (line.equals(BLUE_WITHDRAW))
		{
			onWithdrawSignClick(player);
		}
	}

	protected abstract void onConsultSignClick(Player player) throws HeavenException;

	protected abstract void onDepositSignClick(Player player) throws HeavenException;

	protected abstract void onWithdrawSignClick(Player player) throws HeavenException;
}