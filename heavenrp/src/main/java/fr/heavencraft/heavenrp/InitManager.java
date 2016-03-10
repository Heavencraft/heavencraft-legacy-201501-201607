package fr.heavencraft.heavenrp;

import fr.heavencraft.heavencore.bukkit.commands.AccepterCommand;
import fr.heavencraft.heavencore.bukkit.commands.CreacheatCommand;
import fr.heavencraft.heavencore.bukkit.commands.EndercheatCommand;
import fr.heavencraft.heavencore.bukkit.commands.FillCommand;
import fr.heavencraft.heavencore.bukkit.commands.HeadCommand;
import fr.heavencraft.heavencore.bukkit.commands.InventoryCommand;
import fr.heavencraft.heavencore.bukkit.commands.NoentitiesCommand;
import fr.heavencraft.heavencore.bukkit.commands.PoofCommand;
import fr.heavencraft.heavencore.bukkit.commands.RejoindreCommand;
import fr.heavencraft.heavencore.bukkit.commands.RoucoupsCommand;
import fr.heavencraft.heavencore.bukkit.commands.SpawnmobCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpCommand;
import fr.heavencraft.heavencore.bukkit.commands.TphereCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpposCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpworldCommand;
import fr.heavencraft.heavencore.bukkit.listeners.AntiCheatListener;
import fr.heavencraft.heavencore.bukkit.listeners.AntiLagListener;
import fr.heavencraft.heavencore.bukkit.listeners.ColoredSignsListener;
import fr.heavencraft.heavencore.bukkit.listeners.CookieSignListener;
import fr.heavencraft.heavencore.bukkit.listeners.JumpListener;
import fr.heavencraft.heavencore.bukkit.listeners.LinkSignListener;
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
import fr.heavencraft.heavenrp.commands.province.ProvinceCommand;
import fr.heavencraft.heavenrp.commands.warps.WarpCommand;
import fr.heavencraft.heavenrp.dungeon.DungeonCommand;
import fr.heavencraft.heavenrp.dungeon.DungeonCreatureListener;
import fr.heavencraft.heavenrp.dungeon.DungeonManager;
import fr.heavencraft.heavenrp.dungeon.DungeonPlayerListener;
import fr.heavencraft.heavenrp.dungeon.DungeonSignListener;
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
import fr.heavencraft.heavenrp.jobs.JobActionListener;
import fr.heavencraft.heavenrp.jobs.JobsCommand;
import fr.heavencraft.heavenrp.key.KeySignListener;
import fr.heavencraft.heavenrp.provinces.ProvinceEffectTask;
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
		new HeadCommand(plugin);
		new InventoryCommand(plugin);
		new NoentitiesCommand(plugin);
		new PoofCommand(plugin);
		new RejoindreCommand(plugin);
		new RoucoupsCommand(plugin);
		new SpawnmobCommand(plugin);
		new TpCommand(plugin);
		new TphereCommand(plugin);
		new TpposCommand(plugin);
		new TpworldCommand(plugin);

		/*
		 * from HeavenRP
		 */
		
		// Dungeon
		new DungeonCommand(plugin);

		// Economy
		new BourseCommand(plugin);
		new EntrepriseCommand(plugin);
		new HurtCommand(plugin);
		new LivretproCommand(plugin);
		new PayerCommand(plugin);
		new MoneyTask(plugin);

		// Homes
		new BuyhomeCommand(plugin);
		new HomeCommand(plugin);
		new SethomeCommand(plugin);
		new TphomeCommand(plugin);
		
		// Provinces
		new ProvinceCommand(plugin);
		new ProvinceEffectTask(plugin);

		// Horses
		new ChevalCommand(plugin);

		// HPs
		new HpsCommand(plugin);

		// Jobs
		new JobsCommand(plugin);
		new JobActionListener(plugin);

		// Key
		new KeyCommand(plugin);

		// Warp
		new WarpCommand(plugin);

		/*
		 * A trier
		 */

		// Commandes modérateurs
		new EventCommand(plugin);
		new ModpackCommand(plugin);
		new Pvp4Command(plugin);
		new PvpCommand(plugin);

		// Commandes joueurs
		new EncheresCommand(plugin);
		new LicenceCommand(plugin);
		new MaireCommand(plugin);
		new MairesCommand(plugin);
		new ParcelleCommand(plugin);
	}

	private static void initListeners(HeavenRP plugin)
	{
		/*
		 * from HeavenCore
		 */

		new AntiCheatListener(plugin);
		new AntiLagListener(plugin);
		new ColoredSignsListener(plugin);
		new CookieSignListener(plugin);
		new NoChatListener(plugin);
		new RedstoneLampListener(plugin);

		/*
		 * HeavenRP
		 */
		
		// Dungeon
		new DungeonCreatureListener(plugin);
		new DungeonPlayerListener(plugin);
		new DungeonSignListener(plugin);
		new DungeonManager();
		
		// Economy
		new EconomyListener();
		new GoldDropListener(plugin);
		new LivretProSignListener(plugin);
		new LivretSignListener(plugin);

		// General
		new BourseListener(plugin);
		new PumpkinLampListener(plugin);
		new RecipeManager();
		new ServerListener();
		new WatchListener(plugin);

		// Users
		new UserListener(plugin);

		// Horses
		new HorsesListener(plugin);

		// Key
		new KeySignListener(plugin);

		// Provinces
		ProvinceScoreboard.initialize();
		new ProvinceListener(plugin);
		new ProvinceSignListener(plugin);

		// Warps
		new WarpSignListener(plugin);

		// Worlds
		WorldsManager.init();
		new WorldsListener(plugin);

		/*
		 * A trier
		 */

		// Listeners
		new JumpListener(plugin);
		new LampadaireListener(plugin);
		new LinkSignListener(plugin);
		new PVP4Manager(plugin);
		new PVPManager(plugin);
		new SnowballListener(plugin);

		// Teams
	}
}