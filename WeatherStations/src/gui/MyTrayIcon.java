package gui;

import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.TrayIcon;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.SwingUtilities;

public class MyTrayIcon extends TrayIcon {
	private MenuItem exitItem = new MenuItem("Вихід");

	public MyTrayIcon(Image image) {
		super(image);
		setImageAutoSize(true);
		setToolTip("WeatherStation");
		setPopupMenu(getMenu());
		setListeners();
	}

	private PopupMenu getMenu() {
		PopupMenu menu = new PopupMenu();
		menu.add(exitItem);
		return menu;
	}

	private void setListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)
						&& e.getClickCount() == 2) {
					WeatherWindow.getInstance().setAlwaysOnTop(true);
					WeatherWindow.getInstance().setAlwaysOnTop(false);
				}
			}
		});
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WeatherWindow.getInstance().exit();
			}
		});
	}
}