package fr.heavencraft.heavenrp.database.bankaccounts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.exceptions.SQLErrorException;
import fr.heavencraft.heavenrp.HeavenRP;

public class BankAccountsManager
{

	public static void createBankAccount(String owner, BankAccountType type) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"INSERT INTO bank_account (owner, type) VALUE (?, ?);"))
		{
			ps.setString(1, owner);
			ps.setString(2, type.getCode());

			ps.executeUpdate();
		}
		catch (final MySQLIntegrityConstraintViolationException ex)
		{
			ex.printStackTrace();
			throw new HeavenException("Le compte en banque {%1$s} existe déjà.", owner);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public static void deleteBankAccount(String name, BankAccountType type) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"DELETE FROM bank_account WHERE owner = ? AND type = ? LIMIT 1;"))
		{
			ps.setString(1, name);
			ps.setString(2, type.getCode());

			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public static BankAccount getBankAccount(String name, BankAccountType type) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"SELECT * FROM bank_account WHERE owner = ? AND type = ? LIMIT 1"))
		{
			ps.setString(1, name);
			ps.setString(2, type.getCode());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new HeavenException("Le compte en banque {%1$s} n'existe pas.", name);

			return new BankAccount(rs);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public static BankAccount getBankAccountById(int id) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"SELECT ba.id, ba.owner, ba.type, ba.balance FROM bank_account ba WHERE ba.id = ? LIMIT 1"))
		{
			ps.setInt(1, id);
			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new HeavenException("Le compte en banque {%1$s} n'existe pas.", id);

			return new BankAccount(rs);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new HeavenException("Le compte en banque {%1$s} n'existe pas.", id);
		}
	}

	public static List<BankAccount> getAccountByOwner(String owner) throws HeavenException
	{
		final List<BankAccount> result = new ArrayList<BankAccount>();

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"(SELECT ba.id, ba.owner, ba.type, ba.balance "
						+ // Sélection des comptes de villes
						"FROM bank_account ba, mayors m, users u " + "WHERE ba.type = 'T' "
						+ "AND ba.owner = m.region_name " + "AND m.user_id = u.id " + "AND u.name = ?) "
						+ "UNION "
						+ "(SELECT ba.id, ba.owner, ba.type, ba.balance "
						+ // Sélection des comptes d'entreprises
						"FROM bank_account ba, enterprises e, enterprises_members em, users u "
						+ "WHERE ba.type ='E' " + "AND ba.owner = e.name " + "AND e.id = em.enterprise_id "
						+ "AND em.user_id = u.id " + "AND u.name = ?);"))
		{
			ps.setString(1, owner);
			ps.setString(2, owner);
			final ResultSet rs = ps.executeQuery();

			while (rs.next())
				result.add(new BankAccount(rs));
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			new SQLErrorException();
		}

		return result;
	}
}