package fr.heavencraft.heavenvip.heads;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import fr.heavencraft.heavenvip.HeavenVIP;

public class HeadProvider
{
	private final static String SELECT_HEADS = "SELECT * from vip_head ORDER BY price DESC";

	/**
	 * Returns a collection of available heads
	 * @return
	 */
	public static Collection<Head> getSoldHeads() {
		Collection<Head> headCollection = new ArrayList<Head>();

		try (PreparedStatement ps = HeavenVIP.getMainConnection()
				.getConnection().prepareStatement(SELECT_HEADS))
		{
			final ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Head hd = new Head(rs.getInt("price"),
						rs.getString("name"),
						rs.getString("description"));
				
				headCollection.add(hd);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return headCollection;
	}
}
