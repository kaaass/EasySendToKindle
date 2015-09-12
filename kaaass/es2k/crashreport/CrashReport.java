package kaaass.es2k.crashreport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashReport {
	private Throwable e;
	private String description = null;
	SimpleDateFormat timeF = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	SimpleDateFormat timeF2 = new SimpleDateFormat("日期:yyyy-MM-dd 时间:HH:mm:ss");

	public CrashReport(Throwable e) {
		this(e, e.toString());
	}

	public CrashReport(Throwable e, String description) {
		this.e = e;
		this.description = description;
	}

	public String saveCrashReport() {
		File cr = null;
		FileWriter fileWriter = null;
		try {
			if (!(new File("CrashReport/").isDirectory())) {
				new File("CrashReport/").mkdir();
			}
			cr = new File("CrashReport/crash-report_" + timeF.format(new Date()) + ".log");
			if (cr.exists()) {
				cr.delete();
			}
			cr.createNewFile();
			fileWriter = new FileWriter(cr);
			StringBuilder stringbuilder = new StringBuilder();
			stringbuilder.append("------------- Crash Report -------------\n");
			stringbuilder.append("#这是程序自动生成的Crash Report");
			stringbuilder.append("\n\n");
			stringbuilder.append(timeF2.format(new Date()));
			stringbuilder.append("\n");
			stringbuilder.append("描述:");
			if (description == null) {
				stringbuilder.append(e.toString());
			} else {
				stringbuilder.append(this.description);
			}
			stringbuilder.append("\n\n");
			stringbuilder.append("#以下是错误信息:");
			stringbuilder.append("\n\n");
			stringbuilder.append(getCauseStackTraceOrString());
			stringbuilder.append("----------------------------------------");
			fileWriter.write(stringbuilder.toString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			(new ErrorUtil(e)).dealWithException();
		} catch (SecurityException e) {
			e.printStackTrace();
			(new ErrorUtil(e)).dealWithException();
		}
		return cr.getName();
	}

	public String getCauseStackTraceOrString() {
		StringWriter stringwriter = null;
		PrintWriter printwriter = null;
		Object object = this.e;
		if (((Throwable) object).getMessage() == null) {
			if ((object instanceof NullPointerException)) {
				object = new NullPointerException(this.description);
			} else if ((object instanceof StackOverflowError)) {
				object = new StackOverflowError(this.description);
			} else if ((object instanceof OutOfMemoryError)) {
				object = new OutOfMemoryError(this.description);
			}
			((Throwable) object).setStackTrace(this.e.getStackTrace());
		}
		String s = ((Throwable) object).toString();
		try {
			stringwriter = new StringWriter();
			printwriter = new PrintWriter(stringwriter);
			((Throwable) object).printStackTrace(printwriter);
			s = stringwriter.toString();
		} finally {
			try {
				stringwriter.close();
				printwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
				(new ErrorUtil(e)).dealWithException();
			}
		}
		return s;
	}
}
