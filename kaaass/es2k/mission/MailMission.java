package kaaass.es2k.mission;

//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
import java.io.File;
//import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import kaaass.es2k.Main;
import kaaass.es2k.Main.SendType;
import kaaass.es2k.crashreport.ErrorUtil;
import kaaass.es2k.file.FileUtil;
import kaaass.es2k.file.StreamGobbler;
import kaaass.es2k.file.ZipCompressor;
import kaaass.es2k.mail.MailUtil;
import kaaass.es2k.mail.MailUtil.Result;

public class MailMission extends IMission {
	String[] file = new String[0];
	List<File> fileT = new ArrayList<File>();
	MailUtil mail = null;
	Result result = null;
	boolean hasMobi = false;

	MailMission() {

	}

	public MailMission(String par) {
		super();
		String[] s = new String[1];
		s[0] = par;
		this.file = s;
		Main.missionManager.todo(this.id);
	}

	public MailMission(String[] par) {
		super();
		this.file = par;
		Main.missionManager.todo(this.id);
	}

	@Override
	public SendType getInfo() {
		if (this.isAlive()) {
			if (result.isSuccess()) {
				return SendType.OK;
			} else {
				return SendType.ERROR;
			}
		}
		return SendType.SENDING;
	}

	@Override
	public void onStart() {
		// String tempPath = System.getProperty("java.io.tmpdir");
		boolean flag = false;
		for (int ii = 0; ii < file.length; ii++) {
			if (file[ii].toLowerCase().endsWith(".pdf")
					&& Main.des1.isSelected()) {
				if (!flag) {
					(new TimeDialog()).showDialog(null, "即将开始转码，请耐心等待。", 5);
					// JOptionPane.showMessageDialog(null, "即将开始转码，请耐心等待。");
					flag = true;
				}
				switch (Main.comboF.getSelectedIndex()) {
				case 0:
					FileUtil.pdf2html(null, file[ii], ii + ".html");
					String[] gen = new String[2];
					gen[0] = ".\\lib\\kindlegen.exe";
					gen[1] = ii + ".html";
					Runtime run = Runtime.getRuntime();
					try {
						Process p = run.exec(gen);
						StreamGobbler errorGobbler = new StreamGobbler(
								p.getErrorStream(), "ERROR");
						errorGobbler.start();
						StreamGobbler outGobbler = new StreamGobbler(
								p.getInputStream(), "STDOUT");
						outGobbler.start();
						int w = p.waitFor();
						System.out.println(w);
						int v = p.exitValue();
						System.out.println(v);
						if (v == 1) {
							String e = "\nError:\n" + errorGobbler.getOut()
									+ "\nOut:\n" + outGobbler.getOut();
							(new ErrorUtil(e)).dealWithResult();
						}
						File f = new File(ii + ".html");
						f.delete();
					} catch (Exception e) {
						e.printStackTrace();
						(new ErrorUtil(e)).dealWithException();
					}
					file[ii] = ii + ".mobi";
					File f = new File(file[ii]);
					fileT.add(f);
					FileUtil.makeDirs("mobi/");
					FileUtil.copyFileTo(f, new File("mobi/" + ii + ".mobi"));
					hasMobi = true;
					break;
				case 1:
					try {
						FileUtil.pdf2txt(file[ii]);
					} catch (Exception e) {
						e.printStackTrace();
						(new ErrorUtil(e)).dealWithException();
					}
					file[ii] = file[ii].substring(0, file[ii].length() - 4)
							+ ".txt";
					fileT.add(new File(file[ii]));
					break;
				}
			}
			if (file[ii].toLowerCase().endsWith(".epub")) {
				if (!flag) {
					(new TimeDialog()).showDialog(null, "即将开始转码，请耐心等待。", 5);
					// JOptionPane.showMessageDialog(null, "即将开始转码，请耐心等待。");
					flag = true;
				}
				String[] gen = new String[2];
				gen[0] = ".\\lib\\kindlegen.exe";
				gen[1] = file[ii];
				Runtime run = Runtime.getRuntime();
				try {
					Process p = run.exec(gen);
					StreamGobbler errorGobbler = new StreamGobbler(
							p.getErrorStream(), "ERROR");
					errorGobbler.start();
					StreamGobbler outGobbler = new StreamGobbler(
							p.getInputStream(), "STDOUT");
					outGobbler.start();
					int w = p.waitFor();
					System.out.println(w);
					int v = p.exitValue();
					System.out.println(v);
					if (v == 1) {
						String e = "\nError:\n" + errorGobbler.getOut()
								+ "\nOut:\n" + outGobbler.getOut();
						(new ErrorUtil(e)).dealWithResult();
					}
				} catch (Exception e) {
					e.printStackTrace();
					(new ErrorUtil(e)).dealWithException();
				}
				file[ii] = file[ii].substring(0, file[ii].length() - 4)
						+ "mobi";
				File f = new File(file[ii]);
				fileT.add(f);
				FileUtil.makeDirs("mobi/");
				FileUtil.copyFileTo(f, new File("mobi/" + ii + ".mobi"));
				hasMobi = true;
			}
			if (file[ii].toLowerCase().endsWith(".mobi")) {
				FileUtil.makeDirs("mobi/");
				File a = new File(file[ii]);
				// FileUtil.copyFileTo(a, new File("mobi/" + a.getName()));
				FileUtil.copyFileTo(a, new File("mobi/" + ii + ".mobi"));
				hasMobi = true;
			}
		}
		if (hasMobi) {
			try {
				(new ZipCompressor("mobi.zip")).compress("mobi/");
			} catch (RuntimeException e) {
				e.printStackTrace();
				(new ErrorUtil(e)).dealWithException();
			}
			fileT.add(new File("mobi.zip"));
			List<String> a = new ArrayList<String>();
			for (String s : file) {
				if (!s.toLowerCase().endsWith(".mobi")) {
					a.add(s);
				}
			}
			a.add((new File("mobi.zip")).getAbsolutePath());
			String[] b = new String[a.size()];
			for (int i = 0; i < a.size(); i++) {
				b[i] = a.get(i);
			}
			file = b;
		}
		if (flag) {
			(new TimeDialog()).showDialog(null, "转码结束，即将开始推送。", 5);
			// JOptionPane.showMessageDialog(null, "转码结束，即将开始推送。");
		}
	}

	@Override
	public void onRun() {
		this.mail = new MailUtil(Main.isDebug);
		this.result = mail.send("Kindle推送邮件", "<p>本邮件是程序自动推送的邮件，请勿回复，谢谢！</p>",
				file);
		if (!result.isSuccess()) {
			boolean origin = Main.otherM;
			Main.missionFrame.redraw();
			for (int i = 0; i < 3; i++) {
				if (i == 2) {
					Main.otherM = true;
				}
				result = mail.send("Kindle推送邮件",
						"<p>本邮件是程序自动推送的邮件，请勿回复，谢谢！</p>", file);
				Main.missionFrame.redraw();
				if (result.isSuccess()) {
					break;
				}
			}
			Main.otherM = origin;
			(new ErrorUtil(result)).dealWithResult();
		}
	}

	@Override
	public void onEnd() {
		for (File f : fileT) {
			f.delete();
		}
		if (hasMobi) {
			FileUtil.deleteDirectory("mobi/");
		}
		if (!result.isSuccess()) {
			System.out.println("Send error!");
		} else {
			System.out.println("Send ok!");
		}
	}

	@Override
	public void onStop() {
	}

	@Override
	public String getDesc() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("\n任务类型：推送任务");
		stringbuilder.append("\n推送文件：");
		for (String s : file) {
			File f = new File(s);
			if (f.getName().equals("mobi.zip")) {
				stringbuilder.append("\n  电子书文件");
			} else {
				stringbuilder.append("\n  " + f.getName());
			}
		}
		stringbuilder.append("\n文件位置：");
		stringbuilder.append("\n  " + file[0] + " 等");
		stringbuilder.append("\n推送状态：");
		stringbuilder.append("\n  " + getStates());
		return stringbuilder.toString();
	}

	@Override
	public Object getType() {
		return new MailMission();
	}

	public Result getResult() {
		return result;
	}

	@Override
	public String getTitle() {
		File f = new File(file[0]);
		return f.getName() + " 等文件";
	}

	@Override
	public String getStates() {
		if (this.result != null) {
			if (this.result.isSuccess()) {
				return "已完成";
			} else {
				if (Main.missionManager.todoList.isEmpty()) {
					return "推送错误";
				} else {
					if (Main.missionManager.todoList.get(0) == this.id) {
						return "推送错误,正在重试";
					} else {
						return "推送错误";
					}
				}
			}
		} else if (Main.missionManager.todoList.get(0) == this.id) {
			return "推送中";
		} else {
			return "排队中";
		}
	}

	@Override
	public String getTypeName() {
		return "推送任务";
	}

	@Override
	public IMission restart() {
		return new MailMission(this.file);
	}
}
