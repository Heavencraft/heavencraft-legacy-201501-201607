package fr.heavencraft.heavenrp;

import fr.heavencraft.heavencore.bukkit.commands.AccepterCommand;
import fr.heavencraft.heavencore.bukkit.commands.RejoindreCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpCommand;
import fr.heavencraft.heavencore.bukkit.commands.TphereCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpposCommand;
import fr.heavencraft.heavencore.bukkit.listeners.ColoredSignsListener;
import fr.heavencraft.heavencore.bukkit.listeners.JumpListener;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.bukkit.listeners.RedstoneLampListener;
import fr.heavencraft.heavenrp.commands.economy.BourseCommand;
import fr.heavencraft.heavenrp.commands.economy.EntrepriseCommand;
import fr.heavencraft.heavenrp.commands.economy.LivretproCommand;
import fr.heavencraft.heavenrp.commands.economy.PayerCommand;
import fr.heavencraft.heavenrp.commands.homes.BuyhomeCommand;
import fr.heavencraft.heavenrp.commands.homes.HomeCommand;
import fr.heavencraft.heavenrp.commands.homes.SethomeCommand;
import fr.heavencraft.heavenrp.commands.homes.TphomeCommand;
import fr.heavencraft.heavenrp.commands.horses.ChevalCommand;
import fr.heavencraft.heavenrp.commands.hps.HpsCommand;
import fr.heavencraft.heavenrp.commands.key.KeyCommand;
import fr.heavencraft.heavenrp.commands.teleport.SpawnCommand;
import fr.heavencraft.heavenrp.commands.teleport.TutoCommand;
import fr.heavencraft.heavenrp.commands.warps.WarpCommand;
import fr.heavencraft.heavenrp.economy.EconomyListener;
import fr.heavencraft.heavenrp.economy.GoldDropListener;
import fr.heavencraft.heavenrp.economy.LivretProSignListener;
import fr.heavencraft.heavenrp.economy.LivretSignListener;
import fr.heavencraft.heavenrp.economy.MoneyTask;
import fr.heavencraft.heavenrp.general.BourseListener;
import fr.heavencraft.heavenrp.general.HurtCommand;
import fr.heavencraft.heavenrp.general.PumpkinLampListener;
import fr.heavencraft.heavenrp.general.RecipeManager;
import fr.heavencraft.heavenrp.general.ServerListener;
import fr.heavencraft.heavenrp.general.WatchListener;
import fr.heavencraft.heavenrp.general.users.UserListener;
import fr.heavencraft.heavenrp.horses.HorsesListener;
import fr.heavencraft.heavenrp.key.DonjonSignListener;
import fr.heavencraft.heavenrp.provinces.ProvinceListener;
import fr.heavencraft.heavenrp.provinces.ProvinceSignListener;
import fr.heavencraft.heavenrp.scoreboards.ProvinceScoreboard;
import fr.heavencraft.heavenrp.warps.WarpSignListener;
import fr.heavencraft.heavenrp.worlds.WorldsListener;
import fr.heavencraft.heavenrp.worlds.WorldsManager;
import fr.lorgan17.heavenrp.commands.mod.EventCommand;
import fr.lorgan17.heavenrp.commands.mod.ModpackCommand;
import fr.lorgan17.heavenrp.commands.mod.Pvp4Command;
import fr.lorgan17.heavenrp.commands.mod.PvpCommand;
import fr.lorgan17.heavenrp.commands.user.EncheresCommand;
import fr.lorgan17.heavenrp.commands.user.LicenceCommand;
import fr.lorgan17.heavenrp.commands.user.MaireCommand;
import fr.lorgan17.heavenrp.commands.user.MairesCommand;
import fr.lorgan17.heavenrp.commands.user.ParcelleCommand;
import fr.lorgan17.heavenrp.listeners.LampadaireListener;
import fr.lorgan17.heavenrp.listeners.PVP4Manager;
import fr.lorgan17.heavenrp.listeners.PVPManager;
import fr.lorgan17.heavenrp.listeners.SnowballListener;

public class InitManager
{
	public static void init(HeavenRP plugin)
	{
		initCommands(plugin);
		initListeners(plugin);

		new ActionsHandler();
		new QueriesHandler();
	}

	private static void initCommands(HeavenRP plugin)
	{
		/*
		 * from HeavenCore
		 */

		new AccepterCommand(plugin);
		new CreacheatCommand(plugin);
		new EndercheatCommand(plugin);
		new FillCommand(plugin);
		new GcCommand(plugin);
		new HeadCommand(plugin);
		new InventoryCommand(plugin);
		new NoentitiesCommand(plugin);
		new PoofCommand(plugin);
		new RejoindreCommand(plugin);
		new RoucoupsCommand(plugin);
		new SpawnmobCommand(plugin);
		new SpectatorCommand(plugin);
		new TpCommand(plugin);
		new TphereCommand(plugin);
		new TpposCommand(plugin);
		new TpworldCommand(plugin);

		/*
		 * from HeavenRP
		 */

		// Economy
		new BourseCommand(plugin);
		new EntrepriseCommand(plugin);
		new HurtCommand(plugin);
		new LivretproCommand(plugin);
		new PayerCommand(plugin);
		new MoneyTask();

		// Homes
		new BuyhomeCommand(plugin);
		new HomeCommand(plugin);
		new SethomeCommand(plugin);
		new TphomeCommand(plugin);

		// Horses
		new ChevalCommand(plugin);

		// HPs
		new HpsCommand(plugin);

		// Key
		new KeyCommand();

		// Teleport
		new SpawnCommand();
		new TutoCommand();

		// Warp
		new WarpCommand();

		/*
		 * A trier
		 */

		// Commandes modérateurs
		new EventCommand();
		new ModpackCommand();
		new Pvp4Command();
		new PvpCommand();

		// Commandes joueurs
		new EncheresCommand();
		new LicenceCommand();
		new MaireCommand();
		new MairesCommand();
		new ParcelleCommand();
	}

	private static void initListeners(HeavenRP plugin)
	{
		/*
		 * from HeavenCore
		 */

		new AntiCheatListener();
		new AntiLagListener();
		new ColoredSignsListener();
		new CookieSignListener();
		new NoChatListener();
		new RedstoneLampListener();

		/*
		 * HeavenRP
		 */

		// Economy
		new EconomyListener();
		new GoldDropListener();
		new LivretProSignListener();
		new LivretSignListener();

		// General
		new BourseListener();
		new PumpkinLampListener();
		new RecipeManager();
		new ServerListener();
		new WatchListener();

		// Users
		new UserListener();

		// Horses
		new HorsesListener();

		// Key
		new DonjonSignListener();

		// Provinces
		ProvinceScoreboard.initialize();
		new ProvinceListener();
		new ProvinceSignListener();

		// Warps
		new WarpSignListener();

		// Worlds
		WorldsManager.init();
		new WorldsListener();

		/*
		 * A trier
		 */

		// Listeners
		new JumpListener(plugin);
		new LampadaireListener();
		new LinkSignListener();
		new PVP4Manager();
		new PVPManager();
		new SnowballListener();

		// Teams
	}
}