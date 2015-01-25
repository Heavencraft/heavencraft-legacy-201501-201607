package fr.heavencraft.heavencore.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
	public static boolean isToday(Date date)
	{
		if (date == null)
			return false;

		final Calendar calToday = Calendar.getInstance();
		final Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		return calToday.get(Calendar.ERA) == calDate.get(Calendar.ERA)
				&& calToday.get(Calendar.YEAR) == calDate.get(Calendar.YEAR)
				&& calToday.get(Calendar.DAY_OF_YEAR) == calDate.get(Calendar.DAY_OF_YEAR);
	}
}