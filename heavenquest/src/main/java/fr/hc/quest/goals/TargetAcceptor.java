package fr.hc.quest.goals;

import org.bukkit.entity.Entity;

public interface TargetAcceptor
{
	boolean accept(Entity target);
}