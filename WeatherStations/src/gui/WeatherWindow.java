package gui;

import work.MyProperties;
import work.Weather;

import action.SettingsAction;
import action.UpdateAction;
import action.ExitAction;

import java.text.ParseException;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.IOException;

import java.net.URL;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemTray;
import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JWindow;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.Timer;

import javax.imageio.ImageIO;

import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import net.miginfocom.swing.MigLayout;

public final class WeatherWindow extends JWindow {
	private static final Logger LOGGER = Logger.getLogger(Show.LOGGER_NAME);
	private static WeatherWindow instance;
	private final Font myFont = new Font("Courier New", Font.BOLD, 28);
	private final int numberForecast = 3;
	private Weather weather = new Weather(Integer.parseInt(MyProperties
			.getInstance().getProperty("city")));
	private JLabel[] arrLblCurrent = new JLabel[7];
	private JLabel[][] arrLblForecast = new JLabel[3][3];
	private JButton butUpdate = new JButton(new UpdateAction());
	private Timer timer;

	private WeatherWindow() {
		getContentPane().setBackground(Show.COLOR_BACKGROUND);
		setOpacity(0.8F);
		setLocation(
				Integer.parseInt(MyProperties.getInstance().getProperty("x")),
				Integer.parseInt(MyProperties.getInstance().getProperty("y")));
		add(setCurrentPanel(), BorderLayout.NORTH);
		add(setForecastPanel(), BorderLayout.CENTER);
		add(setButtonPanel(), BorderLayout.SOUTH);
		setListeners();
		createTimer();
		setTimer();
		updateLabels();
		pack();
		setSystemTray();
		setVisible(true);
	}

	private JPanel setCurrentPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new MigLayout("", "[]push[]push[]", "0[]0[]0[]0[]"));
		for (int i = 0; i < arrLblCurrent.length; i++) {
			arrLblCurrent[i] = new JLabel();
		}
		panel.add(arrLblCurrent[0], "span, center, wrap");
		panel.add(arrLblCurrent[1], "bottom, center");
		panel.add(arrLblCurrent[2], "top, gapleft 50, span 2 2");
		panel.add(arrLblCurrent[3], "center, wrap");
		panel.add(arrLblCurrent[4], "center");
		panel.add(arrLblCurrent[5], "center, wrap");
		panel.add(arrLblCurrent[6], "center, skip 3");
		arrLblCurrent[0].setFont(myFont);
		arrLblCurrent[3].setFont(myFont);
		arrLblCurrent[4].setToolTipText("Останнє оновлення");
		return panel;
	}

	private JPanel setForecastPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new MigLayout("", "push[]push []push []push", "0[]0"));
		JPanel[] arrPanel = new JPanel[numberForecast];
		for (int i = 0; i < numberForecast; i++) {
			arrPanel[i] = new JPanel(new MigLayout("", "5[]5", "0[]0[]0[]0"));
			arrPanel[i].setOpaque(false);
			for (int j = 0; j < arrLblForecast.length; j++) {
				arrLblForecast[i][j] = new JLabel();
				arrPanel[i].add(arrLblForecast[i][j], "center, wrap");
				arrPanel[i].setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(0, 3, 0, 3),
						BorderFactory.createLineBorder(Color.GRAY.darker(), 1,
								true)));
			}
			panel.add(arrPanel[i]);
		}
		return panel;
	}

	private JPanel setButtonPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.add(new JButton(new SettingsAction()));
		panel.add(butUpdate);
		panel.add(new JButton(new ExitAction()));
		for (int i = 0; i < panel.getComponentCount(); i++) {
			panel.getComponent(i).setBackground(Color.BLACK);
		}
		return panel;
	}

	private void setListeners() {
		addComponentListener(new MyComponentAdapter());
		MyMouseAdapter listener = new MyMouseAdapter();
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}

	private void createTimer() {
		timer = new Timer(3600000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				butUpdate.doClick();
			}
		});
	}

	public void setTimer() {
		if (Boolean.parseBoolean(MyProperties.getInstance().getProperty(
				"autoUpdate"))) {
			timer.start();
		} else {
			timer.stop();
		}
	}

	public void restartTimer() {
		if (timer.isRunning()) {
			timer.restart();
		}
	}

	public void updateLabels() {
		try {
			weather.updateWeather();
			arrLblCurrent[0].setText(weather.getWeatherCurrentCity());
			arrLblCurrent[1].setText(weather.getWeatherCurrentDate());
			arrLblCurrent[2].setIcon(weather.getWeatherCurrentIcon());
			arrLblCurrent[3].setText(weather.getWeatherCurrentTemperature());
			arrLblCurrent[4].setText(weather.getWeatherLastUpdateTime());
			arrLblCurrent[5].setText(weather.getWeatherCurrentComment());
			arrLblCurrent[6].setText(weather.getWeatherMinMaxTemperature());
			for (int i = 0; i < arrLblForecast.length; i++) {
				arrLblForecast[i][0].setText(weather.getForecastDay(i + 1));
				arrLblForecast[i][1].setIcon(weather.getForecastIcon(i + 1));
				arrLblForecast[i][1].setToolTipText((weather
						.getForecastComment(i + 1)));
				arrLblForecast[i][2].setText(weather
						.getForecastTemperature(i + 1));
			}
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING,
					"Помилка при аналізі шляху XML (оновлення погоди)", e);
		} catch (SAXException e) {
			LOGGER.log(Level.WARNING, "Помилка (оновлення погоди)", e);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING,
					"Помилка при читанні файла (оновлення погоди)", e);
		} catch (ParseException e) {
			LOGGER.log(Level.WARNING,
					"Помилка при парсинзі (оновлення погоди)", e);
		}
	}

	private void setSystemTray() {
		if (!SystemTray.isSupported()) {
			LOGGER.info("Системний трей не підтримується");
			return;
		}
		SystemTray tray = SystemTray.getSystemTray();
		try {
			URL url = getClass().getResource("files/weather.png");
			tray.add(new MyTrayIcon(ImageIO.read(url)));
		} catch (AWTException | IOException e) {
			LOGGER.log(Level.WARNING,
					"Помилка при додаванні іконки в системний трей", e);
		}
	}

	public static synchronized WeatherWindow getInstance() {
		if (instance == null) {
			instance = new WeatherWindow();
		}
		return instance;
	}

	public void exit() {
		MyProperties.changeCoordinate(Integer.toString(getX()),
				Integer.toString(getY()));
		LOGGER.info("Вихід з програми");
		System.exit(0);
	}

	public String getCity() {
		return arrLblCurrent[0].getText();
	}

	public void changeCity(int idCity) {
		weather.setCity(idCity);
		butUpdate.doClick();
	}
}