package kaaass.es2k.crashreport;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import kaaass.es2k.Main;
import kaaass.es2k.file.FileUtil;
import kaaass.es2k.mail.MailUtil;

public class ErrorUtil {
	private MailUtil.Result result = null;
	Exception e = null;
	String ex = null;

	public ErrorUtil(MailUtil.Result result) {
		this.result = result;
	}

	public ErrorUtil(Exception e) {
		this.e = e;
	}

	public ErrorUtil(String e) {
		this.ex = e;
	}

	public String getString() {
		if (!result.isSuccess() && ex == null) {
			String code = result.e.toString();
			code = code.substring(code.indexOf(":") + 2, code.length() - 1);
			if (code.startsWith("Could not connect to SMTP host")) {
				return "�������������ŷ������������������ӻ���smtp�������Ƿ���ȷ��";
			}
			return FileUtil.getInfo(code);
		}
		return "δ֪����";
	}

	public void dealWithResult() {
		if (result == null) {
			String str = (new CrashReport(ex)).saveCrashReport();
			JOptionPane.showMessageDialog(null, "������Ϣ:ת���δ֪����"
					+ "\n������Ϣ�洢�ڳ���Ŀ¼��CrashReport�ļ����µ�" + str
					+ "�С�\n������ԣ���ͨ���˵�->����->����������֪ͨ�����ߡ�", "����:δ��ȷ����",
					JOptionPane.ERROR_MESSAGE);
		} else if (!result.isSuccess()) {
			String str = (new CrashReport(result.e, getString()))
					.saveCrashReport();
			JOptionPane.showMessageDialog(null, "������Ϣ:" + getString()
					+ "\n������Ϣ�洢�ڳ���Ŀ¼��CrashReport�ļ����µ�" + str
					+ "�С�\n������ԣ���ͨ���˵�->����->����������֪ͨ�����ߡ�", "����:δ��ȷ����",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void dealWithException() {
		String str = (new CrashReport(e)).saveCrashReport();
		JOptionPane.showMessageDialog(null, "������δ֪���󣬴�����Ϣ�洢��:" + str
				+ "\n������ԣ���ͨ���˵�->����->����������֪ͨ�����ߡ�", "����:δ֪����",
				JOptionPane.ERROR_MESSAGE);
		FileUtil.deleteDirectory("mobi/");
		File f = new File("mobi.zip");
		if (f.exists()) {
			f.delete();
		}
		if (Main.missionManager.running) {
			Main.missionManager.mList.get(Main.missionManager.todoList.get(0))
					.end();
		}
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
