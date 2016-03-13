package fr.heavencraft.heavencore.providers;

import fr.heavencraft.heavencore.exceptions.StopServerException;

public interface Provider
{
	void clearCache() throws StopServerException;
}