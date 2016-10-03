package action;

import gui.ProgressWindow;

import java.text.SimpleDateFormat;

import java.util.Locale;
import java.util.Calendar;

import java.net.URL;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class UpdateAction extends AbstractAction {
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm", new Locale("uk"));
	private static final ProgressWindow PROGRESS_WINDOW = new ProgressWindow();
	private static final URL PATH_TO_ICON = UpdateAction.class
			.getResource("/gui/files/update.png");

	public UpdateAction() {
		putValue(AbstractAction.SMALL_ICON, new ImageIcon(PATH_TO_ICON));
		putValue(AbstractAction.SHORT_DESCRIPTION,
				TIME_FORMAT.format(Calendar.getInstance().getTime()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new UpdateWorker(PROGRESS_WINDOW).execute();
		putValue(AbstractAction.SHORT_DESCRIPTION,
				TIME_FORMAT.format(Calendar.getInstance().getTime()));
	}
}