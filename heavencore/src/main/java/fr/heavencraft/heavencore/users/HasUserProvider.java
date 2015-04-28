package fr.heavencraft.heavencore.users;

public interface HasUserProvider<T extends User>
{
	UserProvider<T> getUserProvider();
}