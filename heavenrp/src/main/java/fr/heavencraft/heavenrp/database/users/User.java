package fr.heavencraft.heavenrp.database.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavenrp.jobs.Job;
import fr.heavencraft.heavenrp.jobs.JobsProvider;

public class User
{
	private final int id;
	private final String uuid;
	private final String name;
	private final int balance;
	private final int homeNumber;
	private final Timestamp dealerLicense;
	private final Timestamp lastLogin;
	private final int provinceChanges;
	private final String jobName;
	private final int jobExperience;

	User(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		uuid = rs.getString("uuid");
		name = rs.getString("name");
		balance = rs.getInt("balance");
		homeNumber = rs.getInt("homeNumber");
		dealerLicense = rs.getTimestamp("dealer_license");
		lastLogin = rs.getTimestamp("last_login");
		provinceChanges = rs.getInt("province_changes");
		jobName = rs.getString("job_name");
		jobExperience = rs.getInt("job_experience");
	}

	public int getId()
	{
		return id;
	}

	public String getUUID()
	{
		return uuid;
	}

	public String getName()
	{
		return name;
	}

	public int getBalance()
	{
		return balance;
	}

	public int getHomeNumber()
	{
		return homeNumber;
	}

	public Date getLicenseExpireDate()
	{
		return dealerLicense;
	}

	public boolean hasDealerLicense()
	{
		if (dealerLicense == null)
			return false;

		return dealerLicense.after(new Date());
	}

	public boolean alreadyHasDealerLicense()
	{
		return dealerLicense != null;
	}

	public Timestamp getLastLogin()
	{
		return lastLogin;
	}

	// For backward compatibility (Vault)
	public void updateBalance(int delta) throws HeavenException
	{
		try
		{
			new UpdateUserBalanceQuery(this, delta).executeQuery();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SQLErrorException();
		}
	}

	/**
	 * Returns the amount of times, the user has switched provinces
	 * 
	 * @return
	 */
	public int getProvinceChanges()
	{
		return provinceChanges;
	}

	public Job getJob()
	{
		return JobsProvider.getJobByName(jobName);
	}

	public int getJobExperience()
	{
		return jobExperience;
	}
}