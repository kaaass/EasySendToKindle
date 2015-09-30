package kaaass.es2k.mission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kaaass.es2k.Main;
import kaaass.es2k.Main.SendType;
import kaaass.es2k.crashreport.ErrorUtil;
import kaaass.es2k.file.FileUtil;
import kaaass.es2k.mail.MailUtil;
import kaaass.es2k.mail.MailUtil.Result;

public class MailMission extends IMission {
	String[] file = new String[0];
	List<File> fileT = new ArrayList<File>();
	MailUtil mail = null;
	Result result = null;
	
	MailMission () {
		
	}
	
	public MailMission (String par) {
		super();
		String[] s = new String[1];
		s[0] = par;
		this.file = s;
		Main.missionManager.todo(this.id);
	}
	
	public MailMission (String[] par) {
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
		for (int ii = 0; ii < file.length; ii++) {
			if (file[ii].endsWith(".pdf") && Main.des1.isSelected()) {
				switch (Main.comboF.getSelectedIndex()) {
				case 0:
					try {
						FileUtil.pdf2txt(file[ii]);
					} catch (Exception e) {
						e.printStackTrace();
						(new ErrorUtil(e)).dealWithException();
					}
					file[ii] = file[ii].substring(0, file[ii].length() - 4) + ".txt";
					fileT.add(new File(file[ii]));
					break;
				}
			}
		}
	}
	
	@Override
	public void onRun() {
		this.mail = new MailUtil(Main.isDebug);
		this.result = mail.send("Kindle推送邮件", "<p>本邮件是程序自动推送的邮件，请勿回复，谢谢！</p>", file);
		if (!result.isSuccess()) {
			boolean origin = Main.otherM;
			Main.missionFrame.redraw();
			for (int i = 0; i < 3; i++) {
				if (i == 2) {
					Main.otherM = true;
				}
				result = mail.send("Kindle推送邮件", "<p>本邮件是程序自动推送的邮件，请勿回复，谢谢！</p>", file);
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
		if (!result.isSuccess()) {
			System.out.println("Send error!");
		} else {
			System.out.println("Send ok!");
		}
	}

	@Override
	public void onStop() {
		for (File f: fileT) {
			f.delete();
		}
	}

	@Override
	public String getDesc() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("\n任务类型：推送任务");
		stringbuilder.append("\n推送文件：");
		for (String s: file) {
			File f = new File(s);
			stringbuilder.append("\n  " + f.getName());
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
	public void reDo() {
		this.result = null;
	}
}
