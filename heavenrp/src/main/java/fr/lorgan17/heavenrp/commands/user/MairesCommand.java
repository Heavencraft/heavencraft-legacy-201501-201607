package fr.lorgan17.heavenrp.commands.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.utils.ChatUtil;

public class MairesCommand extends AbstractCommandExecutor
{

	public MairesCommand(HeavenRP plugin)
	{
		super(plugin, "maires");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"SELECT u.name, GROUP_CONCAT(DISTINCT m.region_name ORDER BY m.region_name DESC SEPARATOR ', ') AS villes FROM mayors m, users u WHERE u.id = m.user_id GROUP BY m.user_id"))
		{
			final ResultSet rs = ps.executeQuery();

			if (rs.first())
			{
				ChatUtil.sendMessage(sender, "Liste des maires connectés :");

				do
				{
					if (Bukkit.getPlayer(rs.getString(1)) != null)
						ChatUtil.sendMessage(sender, "- " + rs.getString(1) + " ({" + rs.getString(2) + "})");
				}
				while (rs.next());
			}
			else
			{
				ChatUtil.sendMessage(sender, "Aucun maire n'est connecté");
			}

		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}