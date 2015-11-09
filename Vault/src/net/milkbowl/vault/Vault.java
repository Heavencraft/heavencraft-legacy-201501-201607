/* This file is part of Vault.

    Vault is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Vault is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Vault.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.milkbowl.vault;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.nijikokun.register.payment.Methods;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.plugins.Economy_HeavenRP;
import net.milkbowl.vault.permission.Permission;
import net.milkbowl.vault.permission.plugins.Permission_PermissionsBukkit;
import net.milkbowl.vault.permission.plugins.Permission_SuperPerms;

public class Vault extends JavaPlugin
{

	private static Logger log;
	private Permission perms;
	private String newVersionTitle = "";
	private double newVersion = 0;
	private double currentVersion = 0;
	private String currentVersionTitle = "";
	private ServicesManager sm;

	@Override
	public void onDisable()
	{
		// Remove all Service Registrations
		getServer().getServicesManager().unregisterAll(this);
		Bukkit.getScheduler().cancelTasks(this);
	}

	@Override
	public void onEnable()
	{
		log = getLogger();
		currentVersionTitle = getDescription().getVersion().split("-")[0];
		currentVersion = Double.valueOf(currentVersionTitle.replaceFirst("\\.", ""));
		sm = getServer().getServicesManager();
		// set defaults
		getConfig().addDefault("update-check", true);
		getConfig().options().copyDefaults(true);
		saveConfig();

		// Load Vault Addons
		loadEconomy();
		loadPermission();

		getCommand("vault-info").setExecutor(this);
		getCommand("vault-convert").setExecutor(this);
		getServer().getPluginManager().registerEvents(new VaultListener(), this);

		log.info(String.format("Enabled Version %s", getDescription().getVersion()));
	}

	/**
	 * Attempts to load Economy Addons
	 */
	private void loadEconomy()
	{
		// Try to load HeavenRP
		hookEconomy("HeavenRP", Economy_HeavenRP.class, ServicePriority.Normal,
				"fr.heavencraft.heavenrp.HeavenRP");
	}

	/**
	 * Attempts to load Permission Addons
	 */
	private void loadPermission()
	{
		// Try to load PermissionsBukkit
		hookPermission("PermissionsBukkit", Permission_PermissionsBukkit.class, ServicePriority.Normal,
				"com.platymuus.bukkit.permissions.PermissionsPlugin");

		Permission perms = new Permission_SuperPerms(this);
		sm.register(Permission.class, perms, this, ServicePriority.Lowest);
		log.info(String.format("[Permission] SuperPermissions loaded as backup permission system."));

		this.perms = sm.getRegistration(Permission.class).getProvider();
	}

	private void hookEconomy(String name, Class<? extends Economy> hookClass, ServicePriority priority,
			String... packages)
	{
		try
		{
			if (packagesExists(packages))
			{
				Economy econ = hookClass.getConstructor(Plugin.class).newInstance(this);
				sm.register(Economy.class, econ, this, priority);
				log.info(String.format("[Economy] %s found: %s", name, econ.isEnabled() ? "Loaded" : "Waiting"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.severe(String.format(
					"[Economy] There was an error hooking %s - check to make sure you're using a compatible version!",
					name));
		}
	}

	private void hookPermission(String name, Class<? extends Permission> hookClass, ServicePriority priority,
			String... packages)
	{
		try
		{
			if (packagesExists(packages))
			{
				Permission perms = hookClass.getConstructor(Plugin.class).newInstance(this);
				sm.register(Permission.class, perms, this, priority);
				log.info(String.format("[Permission] %s found: %s", name,
						perms.isEnabled() ? "Loaded" : "Waiting"));
			}
		}
		catch (Exception e)
		{
			log.severe(String.format(
					"[Permission] There was an error hooking %s - check to make sure you're using a compatible version!",
					name));
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if (!sender.hasPermission("vault.admin"))
		{
			sender.sendMessage("You do not have permission to use that command!");
			return true;
		}

		if (command.getName().equalsIgnoreCase("vault-info"))
		{
			infoCommand(sender);
			return true;
		}
		else if (command.getName().equalsIgnoreCase("vault-convert"))
		{
			convertCommand(sender, args);
			return true;
		}
		else
		{
			// Show help
			sender.sendMessage("Vault Commands:");
			sender.sendMessage("  /vault-info - Displays information about Vault");
			sender.sendMessage("  /vault-convert [economy1] [economy2] - Converts from one Economy to another");
			return true;
		}
	}

	private void convertCommand(CommandSender sender, String[] args)
	{
		Collection<RegisteredServiceProvider<Economy>> econs = getServer().getServicesManager()
				.getRegistrations(Economy.class);
		if (econs == null || econs.size() < 2)
		{
			sender.sendMessage("You must have at least 2 economies loaded to convert.");
			return;
		}
		else if (args.length != 2)
		{
			sender.sendMessage(
					"You must specify only the economy to convert from and the economy to convert to. (names should not contain spaces)");
			return;
		}
		Economy econ1 = null;
		Economy econ2 = null;
		String economies = "";
		for (RegisteredServiceProvider<Economy> econ : econs)
		{
			String econName = econ.getProvider().getName().replace(" ", "");
			if (econName.equalsIgnoreCase(args[0]))
			{
				econ1 = econ.getProvider();
			}
			else if (econName.equalsIgnoreCase(args[1]))
			{
				econ2 = econ.getProvider();
			}
			if (economies.length() > 0)
			{
				economies += ", ";
			}
			economies += econName;
		}

		if (econ1 == null)
		{
			sender.sendMessage("Could not find " + args[0] + " loaded on the server, check your spelling.");
			sender.sendMessage("Valid economies are: " + economies);
			return;
		}
		else if (econ2 == null)
		{
			sender.sendMessage("Could not find " + args[1] + " loaded on the server, check your spelling.");
			sender.sendMessage("Valid economies are: " + economies);
			return;
		}

		sender.sendMessage("This may take some time to convert, expect server lag.");
		for (OfflinePlayer op : Bukkit.getServer().getOfflinePlayers())
		{
			if (econ1.hasAccount(op))
			{
				if (econ2.hasAccount(op))
				{
					continue;
				}
				econ2.createPlayerAccount(op);
				econ2.depositPlayer(op, econ1.getBalance(op));
			}
		}
		sender.sendMessage("Converson complete, please verify the data before using it.");
	}

	private void infoCommand(CommandSender sender)
	{
		// Get String of Registered Economy Services
		String registeredEcons = null;
		Collection<RegisteredServiceProvider<Economy>> econs = getServer().getServicesManager()
				.getRegistrations(Economy.class);
		for (RegisteredServiceProvider<Economy> econ : econs)
		{
			Economy e = econ.getProvider();
			if (registeredEcons == null)
			{
				registeredEcons = e.getName();
			}
			else
			{
				registeredEcons += ", " + e.getName();
			}
		}

		// Get String of Registered Permission Services
		String registeredPerms = null;
		Collection<RegisteredServiceProvider<Permission>> perms = getServer().getServicesManager()
				.getRegistrations(Permission.class);
		for (RegisteredServiceProvider<Permission> perm : perms)
		{
			Permission p = perm.getProvider();
			if (registeredPerms == null)
			{
				registeredPerms = p.getName();
			}
			else
			{
				registeredPerms += ", " + p.getName();
			}
		}

		String registeredChats = null;
		Collection<RegisteredServiceProvider<Chat>> chats = getServer().getServicesManager()
				.getRegistrations(Chat.class);
		for (RegisteredServiceProvider<Chat> chat : chats)
		{
			Chat c = chat.getProvider();
			if (registeredChats == null)
			{
				registeredChats = c.getName();
			}
			else
			{
				registeredChats += ", " + c.getName();
			}
		}

		// Get Economy & Permission primary Services
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		Economy econ = null;
		if (rsp != null)
		{
			econ = rsp.getProvider();
		}
		Permission perm = null;
		RegisteredServiceProvider<Permission> rspp = getServer().getServicesManager()
				.getRegistration(Permission.class);
		if (rspp != null)
		{
			perm = rspp.getProvider();
		}
		Chat chat = null;
		RegisteredServiceProvider<Chat> rspc = getServer().getServicesManager().getRegistration(Chat.class);
		if (rspc != null)
		{
			chat = rspc.getProvider();
		}
		// Send user some info!
		sender.sendMessage(String.format("[%s] Vault v%s Information", getDescription().getName(),
				getDescription().getVersion()));
		sender.sendMessage(String.format("[%s] Economy: %s [%s]", getDescription().getName(),
				econ == null ? "None" : econ.getName(), registeredEcons));
		sender.sendMessage(String.format("[%s] Permission: %s [%s]", getDescription().getName(),
				perm == null ? "None" : perm.getName(), registeredPerms));
		sender.sendMessage(String.format("[%s] Chat: %s [%s]", getDescription().getName(),
				chat == null ? "None" : chat.getName(), registeredChats));
	}

	/**
	 * Determines if all packages in a String array are within the Classpath
	 * This is the best way to determine if a specific plugin exists and will be
	 * loaded. If the plugin package isn't loaded, we shouldn't bother waiting
	 * for it!
	 * 
	 * @param packages String Array of package names to check
	 * @return Success or Failure
	 */
	private static boolean packagesExists(String... packages)
	{
		try
		{
			for (String pkg : packages)
			{
				Class.forName(pkg);
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public double updateCheck(double currentVersion)
	{
		try
		{
			URL url = new URL("https://api.curseforge.com/servermods/files?projectids=33184");
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(5000);
			conn.addRequestProperty("User-Agent", "Vault Update Checker");
			conn.setDoOutput(true);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final String response = reader.readLine();
			final JSONArray array = (JSONArray) JSONValue.parse(response);

			if (array.size() == 0)
			{
				getLogger().warning("No files found, or Feed URL is bad.");
				return currentVersion;
			}
			// Pull the last version from the JSON
			newVersionTitle = ((String) ((JSONObject) array.get(array.size() - 1)).get("name"))
					.replace("Vault", "").trim();
			return Double.valueOf(newVersionTitle.replaceFirst("\\.", "").trim());
		}
		catch (Exception e)
		{
			log.info("There was an issue attempting to check for the latest version.");
		}
		return currentVersion;
	}

	public class VaultListener implements Listener
	{

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerJoin(PlayerJoinEvent event)
		{
			Player player = event.getPlayer();
			if (perms.has(player, "vault.update"))
			{
				try
				{
					if (newVersion > currentVersion)
					{
						player.sendMessage("Vault " + newVersion + " is out! You are running " + currentVersion);
						player.sendMessage("Update Vault at: http://dev.bukkit.org/server-mods/vault");
					}
				}
				catch (Exception e)
				{
					// Ignore exceptions
				}
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent event)
		{
			if (event.getPlugin().getDescription().getName().equals("Register")
					&& packagesExists("com.nijikokun.register.payment.Methods"))
			{
				if (!Methods.hasMethod())
				{
					try
					{
						Method m = Methods.class.getMethod("addMethod", Methods.class);
						m.setAccessible(true);
						m.invoke(null, "Vault", new net.milkbowl.vault.VaultEco());
						if (!Methods.setPreferred("Vault"))
						{
							log.info("Unable to hook register");
						}
						else
						{
							log.info("[Vault] - Successfully injected Vault methods into Register.");
						}
					}
					catch (SecurityException e)
					{
						log.info("Unable to hook register");
					}
					catch (NoSuchMethodException e)
					{
						log.info("Unable to hook register");
					}
					catch (IllegalArgumentException e)
					{
						log.info("Unable to hook register");
					}
					catch (IllegalAccessException e)
					{
						log.info("Unable to hook register");
					}
					catch (InvocationTargetException e)
					{
						log.info("Unable to hook register");
					}
				}
			}
		}
	}
}
