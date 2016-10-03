package gui;

import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.geom.RoundRectangle2D;

public class MyComponentAdapter extends ComponentAdapter {
	
	public void componentResized(ComponentEvent e) {
		Window window = (Window) e.getComponent();
		window.revalidate();
		window.setShape(new RoundRectangle2D.Double(0, 0, window.getWidth(),
				window.getHeight(), 20, 20));
	}
}