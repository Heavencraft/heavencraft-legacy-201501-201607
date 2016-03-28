package fr.heavencraft.heavensurvival.bukkit;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavensurvival.bukkit.teleport.ProtectionCommand;
import fr.heavencraft.heavensurvival.bukkit.teleport.SelectionManager;
import fr.heavencraft.heavensurvival.bukkit.users.LitCommand;
import fr.heavencraft.heavensurvival.bukkit.worlds.DifficultyTask;
import fr.heavencraft.heavensurvival.bukkit.worlds.SetspawnCommand;
import fr.heavencraft.heavensurvival.bukkit.worlds.SpawnCommand;
import fr.heavencraft.heavensurvival.common.HeavenSurvival;
import fr.heavencraft.heavensurvival.common.HikariConnectionProvider;

public class BukkitHeavenSurvival extends HeavenPlugin implements HeavenSurvival
{
	private final ConnectionProvider connectionProvider;

	public BukkitHeavenSurvival()
	{
		connectionProvider = new HikariConnectionProvider();
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	@Override
	public void onEnable()
	{
		new ProtectionCommand(this);
		new LitCommand(this);
		new SpawnCommand(this);
		new SetspawnCommand(this);

		new NoChatListener(this);
		new SelectionManager(this);
		new DifficultyTask(this);
	}
}