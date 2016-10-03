package gui;

import java.awt.Component;
import java.awt.Color;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;

public class MyListCellRenderer extends DefaultListCellRenderer {

	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean hasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value,
				index, isSelected, hasFocus);
		if (isSelected) {
			label.setBackground(Color.GRAY);
		} else {
			label.setBackground(Show.COLOR_BACKGROUND);
		}
		return label;
	}
}