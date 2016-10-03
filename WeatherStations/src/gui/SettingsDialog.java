package gui;

import work.MyProperties;
import work.Weather;

import action.OkAction;
import action.ChancelAction;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JWindow;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;

import net.miginfocom.swing.MigLayout;

public final class SettingsDialog extends JDialog {
	private static SettingsDialog instance;
	private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
	private JComboBox<String> comboBox = new JComboBox<String>();
	private JCheckBox checkBox = new JCheckBox("Автооновлення погоди");

	private SettingsDialog() {
		setModal(true);
		setLayout(new BorderLayout(10, 30));
		setUndecorated(true);
		getContentPane().setBackground(Show.COLOR_BACKGROUND);
		add(getMainPanel(), BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.SOUTH);
		pack();
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20,
				20));
	}

	private JPanel getMainPanel() {
		JPanel panel = new JPanel(new MigLayout());
		panel.setOpaque(false);
		panel.add(new JLabel("Виберіть місто:"), "split 2");
		loadModel();
		comboBox.setModel(model);
		comboBox.setBackground(Color.BLACK);
		comboBox.setFocusable(false);
		comboBox.setRenderer(new MyListCellRenderer());
		panel.add(comboBox, "wrap");
		checkBox.setToolTipText("Автооновлення погоди кожної години.");
		panel.add(checkBox);
		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.add(new JButton(new OkAction()));
		panel.add(new JButton(new ChancelAction()));
		for (int i = 0; i < panel.getComponentCount(); i++) {
			panel.getComponent(i).setBackground(Color.BLACK);
			((JButton) panel.getComponent(i)).setFocusPainted(false);
		}
		return panel;
	}

	private void loadModel() {
		List<String> list = new ArrayList<String>(Weather.MAP_CITIES.values());
		Collections.sort(list);
		for (String city : list) {
			model.addElement(city);
		}
	}

	public static synchronized SettingsDialog getInstance() {
		if (instance == null) {
			instance = new SettingsDialog();
		}
		return instance;
	}

	public String getSelectedCity() {
		return comboBox.getSelectedItem().toString();
	}

	public Boolean isAutoUpdate() {
		return checkBox.isSelected();
	}

	public void setWindow(JWindow window) {
		comboBox.setSelectedItem(WeatherWindow.getInstance().getCity());
		checkBox.setSelected(Boolean.parseBoolean(MyProperties.getInstance()
				.getProperty("autoUpdate")));
		setLocationRelativeTo(window);
		setVisible(true);
	}
}