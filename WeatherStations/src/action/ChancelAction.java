package action;

import gui.SettingsDialog;

import java.net.URL;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class ChancelAction extends AbstractAction {
	private static final URL PATH_TO_ICON = ChancelAction.class
			.getResource("/gui/files/exit.png");

	public ChancelAction() {
		putValue(AbstractAction.SMALL_ICON, new ImageIcon(PATH_TO_ICON));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SettingsDialog.getInstance().setVisible(false);
	}
}