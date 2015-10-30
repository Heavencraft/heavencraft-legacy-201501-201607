package fr.heavencraft.heavenrp.database.bankaccounts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.economy.enterprise.EnterprisesManager;

public class BankAccount
{
	private final int id;
	private final String owner;
	private final BankAccountType type;
	private int balance;

	BankAccount(ResultSet rs) throws HeavenException, SQLException
	{
		id = rs.getInt("id");
		owner = rs.getString("owner");
		type = BankAccountType.getByCode(rs.getString("type"));
		balance = rs.getInt("balance");
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return owner;
	}

	public List<String> getOwnersNames()
	{
		final List<String> owners = new ArrayList<String>();

		switch (type)
		{
			case USER:
				owners.add(owner);
				break;
			case TOWN:
				try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
						"SELECT u.name FROM users u, mayors m WHERE u.id = m.user_id AND region_name = ?"))
				{
					ps.setString(1, owner);
					final ResultSet rs = ps.executeQuery();

					while (rs.next())
						owners.add(rs.getString("name"));
				}
				catch (final SQLException ex)
				{
					ex.printStackTrace();
				}

				break;

			case ENTERPRISE:

				try
				{
					owners.addAll(EnterprisesManager.getEnterpriseByName(owner).getMembers(false));
				}
				catch (HeavenException ex)
				{
					ex.printStackTrace();
					// Erreur :D
				}
				break;
		}

		return owners;
	}

	public List<CommandSender> getOwners()
	{
		final List<CommandSender> owners = new ArrayList<CommandSender>();

		for (final String name : getOwnersNames())
		{
			final Player player = Bukkit.getPlayer(name);

			if (player != null)
				owners.add(player);
		}

		return owners;
	}

	public int getBalance()
	{
		return balance;
	}
}