package fr.heavencraft.heavenrp.jobs.actions;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class JobAction
{
	JobActionType type;
	Object something;

	public JobAction(JobActionType type, Material material)
	{
		this.type = type;
		this.something = material;
	}

	public JobAction(JobActionType type, EntityType entity)
	{
		this.type = type;
		this.something = entity;
	}
}