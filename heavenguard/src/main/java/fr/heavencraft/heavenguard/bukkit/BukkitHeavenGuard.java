package fr.heavencraft.heavenguard.bukkit;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavenguard.api.RegionManager;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.listeners.PlayerListener;
import fr.heavencraft.heavenguard.bukkit.listeners.ProtectionEnvironmentListener;
import fr.heavencraft.heavenguard.bukkit.listeners.ProtectionPlayerListener;
import fr.heavencraft.heavenguard.common.HeavenGuard;
import fr.heavencraft.heavenguard.common.HeavenGuardInstance;
import fr.heavencraft.heavenguard.datamodel.SQLRegionProvider;

/*
 * Database looks like :
 * regions (
 *   id, name, parent_id,
 *   world, min_x, min_y, min_z, max_x, max_y, max_z,
 *   ... +flags
 * )
 * 
 * hg_regions_members (region_id, user_id, owner)
 * 
 * hg_uuid (id, uuid, last_name)
 * 
 */
public class BukkitHeavenGuard extends HeavenPlugin implements HeavenGuard
{
	private RegionProvider regionProvider;
	private RegionManager regionManager;

	private ConnectionProvider connectionHandler;

	public BukkitHeavenGuard()
	{
		HeavenGuardInstance.set(this);
	}

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();
			saveDefaultConfig();

			new PlayerListener(this);

			new ProtectionPlayerListener(this);
			new ProtectionEnvironmentListener(this);

			connectionHandler = ConnectionHandlerFactory.getConnectionHandler(loadDatabase(getConfig(), "database"));

			regionProvider = new SQLRegionProvider(connectionHandler);
			regionManager = new RegionManager(regionProvider);

			new RegionCommand(this);
			new RemoveRegionTask(this);
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public RegionProvider getRegionProvider()
	{
		return regionProvider;
	}

	public RegionManager getRegionManager()
	{
		return regionManager;
	}
}