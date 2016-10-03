package gui;

import java.net.URL;

import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class ProgressWindow extends JWindow {
	private static final URL PATH_TO_FILE = ProgressWindow.class
			.getResource("files/update.gif");

	public ProgressWindow() {
		getContentPane().setBackground(Show.COLOR_BACKGROUND);
		setOpacity(0.7F);
		/*
		 * SwingX JXBusyLabel label = new JXBusyLabel(new Dimension(
		 * dimension.height / 4, dimension.height / 4)); label.setBusy(true);
		 */
		JLabel label = new JLabel(new ImageIcon(PATH_TO_FILE));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setToolTipText("Оновлення прогноза погоди");
		add(label);
	}

	public void setWindow(JWindow window) {
		setSize(window.getSize());
		setShape(window.getShape());
		setLocationRelativeTo(window);
		setVisible(true);
	}
}