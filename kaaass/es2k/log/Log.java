package kaaass.es2k.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	private static SimpleDateFormat timeF = new SimpleDateFormat("HH:mm:ss");

	// private static SimpleDateFormat timeF2 = new SimpleDateFormat(
	// "yyyy-MM-dd HH:mm:ss");

	public static void v(Logger logger, String str) {
		System.out.println("[" + timeF.format(new Date()) + "][" + "Verbose:"
				+ logger.getName() + "]" + str);
	}

	public static void d(Logger logger, String str) {
		System.out.println("[" + timeF.format(new Date()) + "][" + "Debug:"
				+ logger.getName() + "]" + str);
	}

	public static void i(Logger logger, String str) {
		System.out.println("[" + timeF.format(new Date()) + "][" + "Info:"
				+ logger.getName() + "]" + str);
	}

	public static void w(Logger logger, String str) {
		System.out.println("[" + timeF.format(new Date()) + "][" + "Warning:"
				+ logger.getName() + "]" + str);
	}

	public static void e(Logger logger, String str) {
		System.out.println("[" + timeF.format(new Date()) + "][" + "Error:"
				+ logger.getName() + "]" + str);
	}

	public static void a(Logger logger, String str) {
		System.out.println("[" + timeF.format(new Date()) + "][" + "Assert:"
				+ logger.getName() + "]" + str);
	}
}
