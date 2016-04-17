package fr.heavencraft.heavenevent.timer;

public class Timer
{

	public static void loadTimer()
	{

		TimerConfigurationEditor.tryConfig();

		// during an event, start the Timer
		if (TimerConfigurationEditor.start)
			TimerScoreboard.initScoreboard();
	}
}
