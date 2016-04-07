package fr.heavencraft.heavenevent.timer;

public class Timer
{

	public static void loadTimer()
	{

		TimerConfigurationEditor.loadDefaultConfig();

		// during an event, start the Timer
		if (TimerConfigurationEditor.start)
			TimerScoreboard.initScoreboard();
	}
}
