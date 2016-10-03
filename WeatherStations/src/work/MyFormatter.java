package work;

import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;

public class MyFormatter extends Formatter {
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd MMMM yyyy  HH:mm:ss", new Locale("uk"));

	@Override
	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder();
		builder.append(DATE_FORMAT.format(record.getMillis())).append("  ")
				.append(record.getLevel()).append(": ")
				.append(record.getMessage()).append(LINE_SEPARATOR);
		if (record.getThrown() != null) {
			try (StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);) {
				record.getThrown().printStackTrace(pw);
				builder.append("CLASS: ").append(record.getSourceClassName())
						.append(" METHOD: ")
						.append(record.getSourceMethodName())
						.append(LINE_SEPARATOR).append("STACK TRACE:")
						.append(LINE_SEPARATOR).append(sw.toString())
						.append(LINE_SEPARATOR);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
}