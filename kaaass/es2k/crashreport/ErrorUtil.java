package kaaass.es2k.crashreport;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import kaaass.es2k.file.FileUtil;
import kaaass.es2k.mail.MailUtil;

public class ErrorUtil {
	private MailUtil.Result result;
	Exception e;

	public ErrorUtil(MailUtil.Result result) {
		this.result = result;
	}

	public ErrorUtil(Exception e) {
		this.e = e;
	}

	public String getString() {
		if (!result.isSuccess()) {
			String code = result.e.toString();
			code = code.substring(code.indexOf(":") + 2, code.length() - 1);
			if (code.startsWith("Could not connect to SMTP host:")) {
				return "不能连接至发信服务器，请检查网络连接或者smtp服务器是否正确。";
			}
			return FileUtil.getInfo(code);
		}
		return "未知错误";
	}
	
	public void dealWithResult() {
		if (!result.isSuccess()) {
			String str = (new CrashReport(result.e, getString())).saveCrashReport();
			JOptionPane.showMessageDialog(null, "错误信息:" + getString()
					+ "\n具体信息存储于程序目录的CrashReport文件夹下的" + str + "中。\n如果可以，请通过菜单->帮助->反馈错误来通知开发者。",
					"错误:未正确推送", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void dealWithException() {
		String str = (new CrashReport(e)).saveCrashReport();
		JOptionPane.showMessageDialog(null, "发生了未知错误，错误信息存储在:" + str
				+ "\n如果可以，请通过菜单->帮助->反馈错误来通知开发者。", "错误:未知错误",
				JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	public static String getCauseStackString(Exception e) {
		StringWriter stringwriter = null;
		PrintWriter printwriter = null;
		Object object = e;
		if (((Throwable) object).getMessage() == null) {
			((Throwable) object).setStackTrace(e.getStackTrace());
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
			} catch (IOException e1) {
				e1.printStackTrace();
				(new ErrorUtil(e1)).dealWithException();
			}
		}
		return s;
	}
}
