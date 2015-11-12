package fr.heavencraft.heavenrp.provinces;

import fr.heavencraft.heavenrp.database.users.User;

public class ProvinceLeavingFees
{
	private static boolean ENABLE_RISING_FEES = false;
	/**
	 * Returns the fees the player has to pay if he leaves a province
	 * @param user
	 * @return fees (positive value)
	 */
	public static int getLeavingFees(User user)
	{
		
		if(!ENABLE_RISING_FEES)
			return 50;
		
		switch (user.getProvinceChanges())
		{
			case 0:
			case 1:
				return 50;
			case 2:
				return 250;
			case 3:
				return 740;
			case 4:
				return 1250;
			case 5:
				return 1700;
			case 6:
				return 2400;
			case 7:
				return 3000;
			case 8:
				return 3600;
			case 9:
				return 4200;
			
			default:
				return 5000;
		}
	}
}
