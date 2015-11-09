package fr.heavencraft.heavenrp.economy.enterprise;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.exceptions.EnterpriseNotFoundException;

public class EnterprisesManager
{
	// Base de données
	// enterprises (id, name)
	// enterprises_members (enterprise_id, user_id, owner)
	// users (id, name, etc...)

	public static class Enterprise
	{
		private final int _id;
		private final String _name;

		private Enterprise(ResultSet rs) throws SQLException
		{
			_id = rs.getInt("id");
			_name = rs.getString("name");
		}

		public String getName()
		{
			return _name;
		}

		public void addMember(User user, boolean owner) throws HeavenException
		{
			try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
					"REPLACE INTO enterprises_members (enterprise_id, user_id, owner) " + "VALUES (?, ?, ?);"))
			{
				ps.setInt(1, _id);
				ps.setInt(2, user.getId());
				ps.setBoolean(3, owner);

				ps.executeUpdate();
				ps.close();
			}
			catch (final SQLException ex)
			{
				ex.printStackTrace();
				throw new SQLErrorException();
			}
		}

		public boolean isMember(String name, boolean owner) throws HeavenException
		{
			try (PreparedStatement ps = HeavenRP.getConnection()
					.prepareStatement("SELECT em.owner " + "FROM enterprises_members em, users u "
							+ "WHERE em.enterprise_id = ? " + "AND em.user_id = u.id " + "AND u.name = ? "
							+ "LIMIT 1;"))
			{
				ps.setInt(1, _id);
				ps.setString(2, name);

				final ResultSet rs = ps.executeQuery();

				if (!rs.next())
					return false;

				return owner ? rs.getBoolean("owner") : true;
			}

			catch (final SQLException ex)
			{
				ex.printStackTrace();
				throw new SQLErrorException();
			}
		}

		public void removeMember(User user) throws HeavenException
		{
			try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
					"DELETE FROM enterprises_members " + "WHERE enterprise_id = ? " + "AND user_id = ?;"))
			{
				ps.setInt(1, _id);
				ps.setInt(2, user.getId());

				ps.executeUpdate();
				ps.close();
			}
			catch (final SQLException ex)
			{
				ex.printStackTrace();
				throw new SQLErrorException();
			}
		}

		public List<String> getMembers(boolean owner) throws SQLErrorException
		{
			final List<String> members = new ArrayList<String>();

			try (PreparedStatement ps = HeavenRP.getConnection()
					.prepareStatement("SELECT u.name " + "FROM enterprises_members em, users u "
							+ "WHERE enterprise_id = ? " + "AND em.user_id = u.id "
							+ (owner ? "AND owner = 1" : "")))
			{
				ps.setInt(1, _id);
				final ResultSet rs = ps.executeQuery();

				while (rs.next())
					members.add(rs.getString("name"));
			}
			catch (final SQLException ex)
			{
				ex.printStackTrace();
				throw new SQLErrorException();
			}

			return members;
		}
	} // Enterprise

	public static Enterprise getEnterpriseByName(String name) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection()
				.prepareStatement("SELECT * FROM enterprises WHERE name = ? LIMIT 1"))
		{
			ps.setString(1, name);
			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new EnterpriseNotFoundException(name);

			return new Enterprise(rs);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public static void createEnterprise(String name) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection()
				.prepareStatement("INSERT INTO enterprises (name) VALUES (?);"))
		{
			ps.setString(1, name);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Une entreprise existe déjà avec le nom {%1$s}.", name);

			BankAccountsManager.createBankAccount(name, BankAccountType.ENTERPRISE);
		}
		catch (SQLIntegrityConstraintViolationException ex)
		{
			throw new HeavenException("Une entreprise existe déjà avec le nom {%1$s}.", name);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public static void deleteEnterprise(String name) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection()
				.prepareStatement("DELETE FROM enterprises WHERE name = ? LIMIT 1;"))
		{
			ps.setString(1, name);

			if (ps.executeUpdate() != 1)
				throw new EnterpriseNotFoundException(name);

			BankAccountsManager.deleteBankAccount(name, BankAccountType.ENTERPRISE);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}
}