package action;

import gui.WeatherWindow;
import gui.SettingsDialog;

import java.net.URL;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class SettingsAction extends AbstractAction {
	private static final URL PATH_TO_ICON = SettingsAction.class
			.getResource("/gui/files/settings.png");

	public SettingsAction() {
		putValue(AbstractAction.SMALL_ICON, new ImageIcon(PATH_TO_ICON));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SettingsDialog.getInstance().setWindow(WeatherWindow.getInstance());
	}
}