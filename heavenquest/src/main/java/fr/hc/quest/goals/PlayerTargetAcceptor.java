package fr.hc.quest.goals;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.citizensnpcs.api.CitizensAPI;

public class PlayerTargetAcceptor implements TargetAcceptor
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private static PlayerTargetAcceptor instance;

	private PlayerTargetAcceptor()
	{
	}

	public static PlayerTargetAcceptor get()
	{
		if (instance == null)
			instance = new PlayerTargetAcceptor();

		return instance;
	}

	@Override
	public boolean accept(Entity target)
	{
		if (target == null)
		{
			log.info("Refused : null");
			return false;
		}

		if (target.getType() != EntityType.PLAYER)
		{
			log.info("Refused : not a player");
			return false;
		}

		if (CitizensAPI.getNPCRegistry().isNPC(target))
		{
			log.info("Refused : citizen");
			return false;
		}

		log.info("Accepter");
		return true;
	}
}