package fr.heavencraft.heavencore.users.balance;

import fr.heavencraft.heavencore.users.User;

public interface UserWithBalance extends User
{
	int getBalance();

	String getBalanceString();

	String getCurrencyName();
}