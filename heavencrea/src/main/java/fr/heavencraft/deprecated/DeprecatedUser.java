package fr.heavencraft.deprecated;

import java.util.Date;
import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.users.color.TabColor;

public interface DeprecatedUser
{
	UUID getUniqueId();

	String getName();

	Date getLastLogin();

	TabColor getTabColor();

	void updateName(String name) throws HeavenException;

	void updateLastLogin(Date date) throws SQLErrorException;
}