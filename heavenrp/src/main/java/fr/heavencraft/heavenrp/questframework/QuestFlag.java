package fr.heavencraft.heavenrp.questframework;

import java.util.UUID;

public class QuestFlag
{

	private final UUID flagId;
	
	public QuestFlag(UUID flagId)
	{
		this.flagId = flagId;
	}

	public UUID getFlagId()
	{
		return flagId;
	}
	
	
	
}
