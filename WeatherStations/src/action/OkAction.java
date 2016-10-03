package action;

import work.MyProperties;
import work.Weather;

import gui.WeatherWindow;
import gui.SettingsDialog;

import java.util.Map.Entry;

import java.net.URL;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class OkAction extends AbstractAction {
	private static final URL PATH_TO_ICON = OkAction.class
			.getResource("/gui/files/ok.png");

	public OkAction() {
		putValue(AbstractAction.SMALL_ICON, new ImageIcon(PATH_TO_ICON));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WeatherWindow window = WeatherWindow.getInstance();
		SettingsDialog dialog = SettingsDialog.getInstance();
		if (!dialog.getSelectedCity().equals(window.getCity())) {
			for (Entry<Integer, String> entry : Weather.MAP_CITIES.entrySet()) {
				if (entry.getValue().equals(dialog.getSelectedCity())) {
					Integer idCity = entry.getKey();
					MyProperties.changeCity(idCity);
					window.changeCity(idCity);
					break;
				}
			}
		}
		if (Boolean.parseBoolean(MyProperties.getInstance().getProperty(
				"autoUpdate")) != dialog.isAutoUpdate()) {
			MyProperties.changeAutoUpdate(dialog.isAutoUpdate());
			window.setTimer();
		}
		dialog.setVisible(false);
	}
}