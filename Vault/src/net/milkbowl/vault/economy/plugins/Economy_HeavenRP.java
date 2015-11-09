package net.milkbowl.vault.economy.plugins;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class Economy_HeavenRP extends AbstractEconomy
{
	private static final Logger log = Logger.getLogger("Minecraft");

	private final String name = "HeavenRP";
	private final Plugin plugin;
	private HeavenRP economy;

	public Economy_HeavenRP(Plugin plugin)
	{
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(new EconomyServerListener(this), plugin);

		if (economy == null)
		{
			Plugin heavenrp = plugin.getServer().getPluginManager().getPlugin(name);
			if (heavenrp != null && heavenrp.isEnabled())
			{
				economy = (HeavenRP) heavenrp;
				log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), name));
			}
		}
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "HeavenRP";
	}

	@Override
	public boolean hasBankSupport()
	{
		return false;
	}

	@Override
	public int fractionalDigits()
	{
		return 0;
	}

	@Override
	public String format(double amount)
	{
		return null;
	}

	@Override
	public String currencyNamePlural()
	{
		return "pièces d'or";
	}

	@Override
	public String currencyNameSingular()
	{
		return "pièce d'or";
	}

	@Override
	public boolean hasAccount(String playerName)
	{
		return true;
	}

	@Override
	public boolean hasAccount(String playerName, String worldName)
	{
		return false;
	}

	@Override
	public double getBalance(String playerName)
	{
		try
		{
			return UserProvider.getUserByName(playerName).getBalance();
		}
		catch (HeavenException ex)
		{
			ex.printStackTrace();
			return 0;
		}
	}

	@Override
	public boolean has(String playerName, double amount)
	{
		return getBalance(playerName) >= amount;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount)
	{
		try
		{
			UserProvider.getUserByName(playerName).updateBalance(-toInt(amount));
			return new EconomyResponse(amount, getBalance(playerName), ResponseType.SUCCESS, "");
		}
		catch (HeavenException ex)
		{
			ex.printStackTrace();
			return new EconomyResponse(amount, getBalance(playerName), ResponseType.FAILURE, ex.getMessage());
		}
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, double amount)
	{
		try
		{
			UserProvider.getUserByName(playerName).updateBalance(toInt(amount));
			return new EconomyResponse(amount, getBalance(playerName), ResponseType.SUCCESS, "");
		}
		catch (HeavenException ex)
		{
			ex.printStackTrace();
			return new EconomyResponse(amount, getBalance(playerName), ResponseType.FAILURE, ex.getMessage());
		}
	}

	@Override
	public boolean createPlayerAccount(String playerName)
	{
		return false;
	}

	/*
	 * We don't support account by world
	 */

	@Override
	public double getBalance(String playerName, String world)
	{
		return 0;
	}

	@Override
	public boolean has(String playerName, String worldName, double amount)
	{
		return false;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount)
	{
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount)
	{
		return null;
	}

	@Override
	public boolean createPlayerAccount(String playerName, String worldName)
	{
		return false;
	}

	/*
	 * We don't support bank
	 */

	@Override
	public EconomyResponse createBank(String name, String player)
	{
		return null;
	}

	@Override
	public EconomyResponse deleteBank(String name)
	{
		return null;
	}

	@Override
	public EconomyResponse bankBalance(String name)
	{
		return null;
	}

	@Override
	public EconomyResponse bankHas(String name, double amount)
	{
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount)
	{
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount)
	{
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName)
	{
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName)
	{
		return null;
	}

	@Override
	public List<String> getBanks()
	{
		return null;
	}

	private static int toInt(double amount) throws HeavenException
	{
		if (amount % 1 == 0)
		{
			return (int) amount;
		}
		else
		{
			throw new HeavenException("{%1$s} n'est pas un nombre entier.", amount);
		}
	}

	public class EconomyServerListener implements Listener
	{
		Economy_HeavenRP economy = null;

		public EconomyServerListener(Economy_HeavenRP economy)
		{
			this.economy = economy;
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent event)
		{
			if (economy.economy == null)
			{
				Plugin digicoin = event.getPlugin();
				if (digicoin.getDescription().getName().equals(economy.name))
				{
					economy.economy = (HeavenRP) digicoin;
					log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(),
							economy.name));
				}
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginDisable(PluginDisableEvent event)
		{
			if (economy.economy != null)
			{
				if (event.getPlugin().getDescription().getName().equals(economy.name))
				{
					economy.economy = null;
					log.info(String.format("[%s][Economy] %s unhooked.", plugin.getDescription().getName(),
							economy.name));
				}
			}
		}
	}
}