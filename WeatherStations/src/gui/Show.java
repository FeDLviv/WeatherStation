package gui;

import work.MyProperties;
import work.ProxyAuthenticator;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Paths;

import java.net.Authenticator;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class Show {
	public static final String LOGGER_NAME = "WeatherStation";
	public static final Color COLOR_BACKGROUND = Color.BLACK;
	public static final Color COLOR_TEXT = Color.WHITE;
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

	private static void setLogger() {
		try {
			FileInputStream inStream = new FileInputStream(Paths.get("files",
					"logger.properties").toFile());
			LogManager.getLogManager().readConfiguration(inStream);
			LOGGER.info("Запуск програми (документування)");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	private static void setAuthenticator() {
		if ((Boolean.parseBoolean(MyProperties.getInstance().getProperty(
				"proxy")))) {
			Authenticator.setDefault(new ProxyAuthenticator());
		}
	}

	private static void setLAF() {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.put("text", COLOR_TEXT);
					UIManager.put("info", COLOR_BACKGROUND);
					break;
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					LOGGER.log(Level.WARNING, "Помилка при налаштуванні LAF", e);
				}
			}
		}
	}

	public static void main(String[] arr) {
		setLogger();
		setAuthenticator();
		setLAF();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				WeatherWindow.getInstance();
			}
		});
	}
}