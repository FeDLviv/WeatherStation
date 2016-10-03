package gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.SwingUtilities;

public class MyMouseAdapter extends MouseAdapter {
	private int startX;
	private int startY;

	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			startX = e.getX();
			startY = e.getY();
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			Point location = e.getLocationOnScreen();
			e.getComponent().setLocation(location.x - startX,
					location.y - startY);
		}
	}
}