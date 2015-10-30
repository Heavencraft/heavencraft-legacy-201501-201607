package fr.heavencraft.heavenrp.database.bankaccounts;

import fr.heavencraft.exceptions.HeavenException;

public enum BankAccountType
{
	USER("U"),
	TOWN("T"),
	ENTERPRISE("E");

	public static BankAccountType getByCode(String code) throws HeavenException
	{
		if (code.equals("U"))
			return BankAccountType.USER;
		else if (code.equals("T"))
			return BankAccountType.TOWN;
		else if (code.equals("E"))
			return BankAccountType.ENTERPRISE;
		else
			throw new HeavenException("Le type de livret {%1$s} est invalide.", code);
	}

	private final String _code;

	BankAccountType(String code)
	{
		_code = code;
	}

	public String getCode()
	{
		return _code;
	}
}
