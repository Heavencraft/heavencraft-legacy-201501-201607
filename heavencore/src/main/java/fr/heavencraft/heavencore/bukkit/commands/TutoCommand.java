package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public final class TutoCommand extends AbstractCommandExecutor
{
	private final String world;
	private final double x, y, z;

	public TutoCommand(HeavenPlugin plugin, String world, double x, double y, double z)
	{
		super(plugin, "tuto");

		// Do not create a Location object, because at the time this command is
		// initialized, the world could be not loaded.
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		plugin.teleportPlayer(player, new Location(Bukkit.getWorld(world), x, y, z));
		plugin.sendMessage(player, "Vous avez été téléporté au tutoriel.");
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		plugin.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}