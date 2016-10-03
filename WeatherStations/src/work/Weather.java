package work;

import gui.Show;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.io.File;
import java.io.IOException;

import java.nio.file.Paths;

import javax.imageio.ImageIO;

import java.net.URL;

import javax.swing.ImageIcon;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class Weather {
	private static final String APPID = "&APPID=" + MyProperties.getInstance().getProperty("APPID");
	private static final Logger LOGGER = Logger.getLogger(Show.LOGGER_NAME);
	public static final Map<Integer, String> MAP_CITIES = new HashMap<Integer, String>();
	private static final String PATH_TO_FILE_CITIES = Paths.get("files", "cities.xml").toString();
	private static final String TEMPERATURE_CHAR = (char) 176 + "C";
	private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?mode=xml&lang=ua&id=";
	private static final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=xml&lang=ua&cnt=4&id=";
	private static final String ICON_URL = "http://openweathermap.org/img/w/";
	private static final String CURRENT_CITY = "current/city/@id";
	private static final String CURRENT_DATE = "current/lastupdate/@value";
	private static final String CURRENT_ICON = "current/weather/@icon";
	private static final String CURRENT_TEMPERATURE = "current/temperature/@value";
	private static final String CURRENT_COMMENT = "current/weather/@value";
	private static final String CURRENT_MIN_TEMPERATURE = "weatherdata/forecast/time[1]/temperature/@min";
	private static final String CURRENT_MAX_TEMPERATURE = "weatherdata/forecast/time[1]/temperature/@max";
	private static final String FORECAST_ICON = "weatherdata/forecast/time/symbol";
	private static final String FORECAST_TEMPERATURE = "weatherdata/forecast/time/temperature";
	private static final SimpleDateFormat DATE_FORMAT_XML = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy EEE", new Locale("uk"));
	private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("E", new Locale("uk"));
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", new Locale("uk"));
	private int idCity;
	private DocumentBuilder builder;
	private XPath xpath;
	private Document weatherXML;
	private Document forecastXML;
	private GregorianCalendar calendar = new GregorianCalendar();

	public Weather(int idCity) {
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			xpath = XPathFactory.newInstance().newXPath();
			loadListCities();
			this.idCity = idCity;
			DATE_FORMAT_XML.setTimeZone(TimeZone.getTimeZone("UTC"));
		} catch (ParserConfigurationException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё СЃС‚РІРѕСЂРµРЅРЅС– РµРєР·РµРјРїР»СЏСЂР° РєР»Р°СЃР° Weather", e);
		}
	}

	private void loadListCities() {
		try {
			Document xml = builder.parse(new File(PATH_TO_FILE_CITIES));
			NodeList list = (NodeList) xpath.evaluate("list/city", xml, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				MAP_CITIES.put(Integer.parseInt(((Element) list.item(i)).getAttribute("id")),
						((Element) list.item(i)).getAttribute("name"));
			}
		} catch (XPathExpressionException | SAXException | IOException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё С‡РёС‚Р°РЅРЅС–/Р°РЅР°Р»С–Р·С– С„Р°Р№Р»Р° " + PATH_TO_FILE_CITIES, e);
		}
	}

	public void setCity(int idCity) {
		this.idCity = idCity;
	}

	public void updateWeather() throws SAXException, IOException, ParseException, XPathExpressionException {
		weatherXML = builder.parse(WEATHER_URL + idCity + APPID);
		forecastXML = builder.parse(FORECAST_URL + idCity + APPID);
		String dateXML = getValue(CURRENT_DATE, weatherXML).replaceAll("T", " ");
		Date date = DATE_FORMAT_XML.parse(dateXML);
		calendar.setTime(date);
	}

	private String getValue(String pathXML, Document doc) throws XPathExpressionException {
		return xpath.evaluate(pathXML, doc);
	}

	public String getWeatherCurrentCity() {
		try {
			return MAP_CITIES.get(Integer.parseInt(getValue(CURRENT_CITY, weatherXML)));
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё Р°РЅР°Р»С–Р·С– С€Р»СЏС…Сѓ XML", e);
		}
		return null;
	}

	public String getWeatherCurrentDate() {
		return DATE_FORMAT.format(calendar.getTime());
	}

	public String getWeatherLastUpdateTime() {
		return TIME_FORMAT.format(calendar.getTime());
	}

	public String getForecastDay(int nextDay) {
		GregorianCalendar cal = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE) + nextDay);
		return DAY_FORMAT.format(cal.getTime()).toUpperCase();
	}

	private ImageIcon getIcon(String nameIcon) {
		String url = ICON_URL + nameIcon + ".png";
		try {
			return new ImageIcon(ImageIO.read(new URL(url)));
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё С‡РёС‚Р°РЅРЅС– С„Р°Р№Р»Р° " + url, e);
		}
		return null;
	}

	public ImageIcon getWeatherCurrentIcon() {
		try {
			return getIcon(getValue(CURRENT_ICON, weatherXML));
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё Р°РЅР°Р»С–Р·С– С€Р»СЏС…Сѓ XML", e);
		}
		return null;
	}

	public ImageIcon getForecastIcon(int nextDay) {
		try {
			NodeList list = (NodeList) xpath.evaluate(FORECAST_ICON, forecastXML, XPathConstants.NODESET);
			return getIcon(((Element) list.item(nextDay)).getAttribute("var"));
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё Р°РЅР°Р»С–Р·С– С€Р»СЏС…Сѓ XML", e);
		}
		return null;
	}

	public String getWeatherCurrentTemperature() {
		try {
			return rendererTemperature(new Double(getValue(CURRENT_TEMPERATURE, weatherXML)));
		} catch (NumberFormatException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё РєРѕРЅРІРµСЂС‚Р°С†С–С— РґР°РЅРёС…", e);
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё Р°РЅР°Р»С–Р·С– С€Р»СЏС…Сѓ XML", e);
		}
		return null;
	}

	private String rendererTemperature(Double temperature) {
		Integer value = new Integer((int) (temperature - 273.15));
		int symbol = Integer.compare(value, 0);
		if (symbol > 0) {
			return "+" + value.toString() + TEMPERATURE_CHAR;
		} else {
			return value.toString() + TEMPERATURE_CHAR;
		}
	}

	public String getWeatherCurrentComment() {
		try {
			return getComment(getValue(CURRENT_COMMENT, weatherXML));
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё Р°РЅР°Р»С–Р·С– С€Р»СЏС…Сѓ XML", e);
		}
		return null;
	}

	private String getComment(String value) {
		switch (value) {
		case "РїРѕРјС–СЂРЅРёР№ РґРѕС‰":
		case "Р»РµРіРєР° Р·Р»РёРІР°":
			return "РЅРµРІРµР»РёРєРёР№ РґРѕС‰";
		default:
			return value;
		}
	}

	public String getForecastComment(int nextDay) {
		try {
			NodeList list = (NodeList) xpath.evaluate(FORECAST_ICON, forecastXML, XPathConstants.NODESET);
			return getComment(((Element) list.item(nextDay)).getAttribute("name"));
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё Р°РЅР°Р»С–Р·С– С€Р»СЏС…Сѓ XML", e);
		}
		return null;
	}

	public String getWeatherMinMaxTemperature() {
		try {
			return getRangeTemperature(getValue(CURRENT_MIN_TEMPERATURE, forecastXML),
					getValue(CURRENT_MAX_TEMPERATURE, forecastXML));
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё Р°РЅР°Р»С–Р·С– С€Р»СЏС…Сѓ XML", e);
		}
		return null;
	}

	private String getRangeTemperature(String minValue, String maxValue) {
		try {
			String min = rendererTemperature(new Double(minValue));
			String max = rendererTemperature(new Double(maxValue));
			return min + " ~ " + max;
		} catch (NumberFormatException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё РєРѕРЅРІРµСЂС‚Р°С†С–С— РґР°РЅРёС…", e);
		}
		return null;
	}

	public String getForecastTemperature(int nextDay) {
		try {
			NodeList list = (NodeList) xpath.evaluate(FORECAST_TEMPERATURE, forecastXML, XPathConstants.NODESET);
			return getRangeTemperature(((Element) list.item(nextDay)).getAttribute("min"),
					((Element) list.item(nextDay)).getAttribute("max"));
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "РџРѕРјРёР»РєР° РїСЂРё Р°РЅР°Р»С–Р·С– С€Р»СЏС…Сѓ XML", e);
		}
		return null;
	}
}