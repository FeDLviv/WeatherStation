package action;

import gui.WeatherWindow;
import gui.ProgressWindow;

import javax.swing.SwingWorker;

public class UpdateWorker extends SwingWorker<Void, Void> {
	private static final WeatherWindow WEATHER_WINDOW = WeatherWindow
			.getInstance();
	private ProgressWindow window;

	public UpdateWorker(ProgressWindow window) {
		this.window = window;
		window.setWindow(WEATHER_WINDOW);
	}

	@Override
	protected Void doInBackground() throws Exception {
		WEATHER_WINDOW.updateLabels();
		WEATHER_WINDOW.restartTimer();
		return null;
	}

	@Override
	protected void done() {
		window.setVisible(false);
		WEATHER_WINDOW.pack();
	}
}