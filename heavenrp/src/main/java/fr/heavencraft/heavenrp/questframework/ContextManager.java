package fr.heavencraft.heavenrp.questframework;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import fr.heavencraft.heavenrp.HeavenRP;

public class ContextManager
{
	private final static String Q_FLAG_LOAD = "SELECT * FROM quest_player_flags WHERE player = ?";
	private static ContextManager instance = null;

	/**
	 * Returns the instance of the object
	 * 
	 * @return
	 */
	public static ContextManager get()
	{
		if (ContextManager.instance == null)
			ContextManager.instance = new ContextManager();
		return ContextManager.instance;
	}

	/**
	 * Returns the Player's context
	 * 
	 * @param p
	 * @return
	 */
	public PlayerContext getPlayerContext(UUID p)
	{
		return PlayerContextCache.getPlayerContext(p);
	}

	/**
	 * Loads from database a player's context
	 * @param p
	 */
	public void loadPlayerContext(UUID p)
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(Q_FLAG_LOAD))
		{
			ps.setString(1, p.toString());
			final ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				//TODO load player context flags from DB
				QfFlag flag = new QfFlag(rs.getString("key"));
				
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

}
