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
	protected static ContextManager get()
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
	protected PlayerContext getPlayerContext(UUID p)
	{
		return PlayerContextCache.getPlayerContext(p);
	}

	/**
	 * Loads from database a player's context
	 * 
	 * @param p
	 */
	protected void loadPlayerContext(UUID p)
	{
		
		// TODO use IFlagList to retrive a list of global flags and add them to cache
		
		// Does the player already have a context?
		PlayerContext pCtx = this.getPlayerContext(p);
		if (pCtx == null)
			pCtx = new PlayerContext();

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(Q_FLAG_LOAD))
		{
			ps.setString(1, p.toString());
			final ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				// load player context flags from DB
				int dataType = rs.getInt("type");

				try
				{
					QfFlag flag = new QfFlag();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

}
