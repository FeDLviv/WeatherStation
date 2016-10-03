package action;

import gui.WeatherWindow;

import java.net.URL;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class ExitAction extends AbstractAction {
	private static final URL PATH_TO_ICON = ExitAction.class
			.getResource("/gui/files/exit.png");

	public ExitAction() {
		putValue(AbstractAction.SMALL_ICON, new ImageIcon(PATH_TO_ICON));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WeatherWindow.getInstance().exit();
	}
}