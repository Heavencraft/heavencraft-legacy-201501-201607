package fr.heavencraft.heavencore.users;

import java.util.UUID;

import fr.heavencraft.heavencore.users.color.TabColor;

public interface User
{
	public UUID getUniqueId();

	public String getName();

	public TabColor getTabColor();
}