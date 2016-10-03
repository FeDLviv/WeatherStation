package work;

import gui.Show;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Paths;

public final class MyProperties extends Properties {
	private static final Logger LOGGER = Logger.getLogger(Show.LOGGER_NAME);
	private static final String PATH_TO_FILE = Paths.get("files",
			"settings.properties").toString();
	private static MyProperties instance;

	private MyProperties() {
	}

	public static synchronized Properties getInstance() {
		if (instance == null) {
			getProperties();
		}
		return instance;
	}

	private static void getProperties() {
		instance = new MyProperties();
		try {
			instance.load(new FileInputStream(PATH_TO_FILE));
		} catch (IOException e) {
			instance = null;
			LOGGER.log(Level.WARNING, "Помилка при читанні файла "
					+ PATH_TO_FILE, e);
		}
	}

	public static synchronized void changeCoordinate(String x, String y) {
		if (instance == null) {
			instance = new MyProperties();
		}
		instance.setProperty("x", x);
		instance.setProperty("y", y);
		storeProperties();
	}

	public static synchronized void changeCity(Integer idCity) {
		if (instance == null) {
			instance = new MyProperties();
		}
		instance.setProperty("city", idCity.toString());
		storeProperties();
	}

	public static synchronized void changeAutoUpdate(Boolean value) {
		if (instance == null) {
			instance = new MyProperties();
		}
		instance.setProperty("autoUpdate", value.toString());
		storeProperties();
	}

	private static void storeProperties() {
		try {
			FileOutputStream file = new FileOutputStream(PATH_TO_FILE);
			instance.store(file, "OpenWeatherMap");
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Помилка при записі файла "
					+ PATH_TO_FILE, e);
		}
	}
}