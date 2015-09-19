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
		mail = new MailUtil(false);
		result = mail.send("Kindle推送邮件", "<p>本邮件是程序自动推送的邮件，请勿回复，谢谢！</p>", file);
		if (!result.isSuccess()) {
			int i = 0;
			while (result.isSuccess() && i < 2) {
				result = mail.send("Kindle推送邮件", "<p>本邮件是程序自动推送的邮件，请勿回复，谢谢！</p>", file);
				i++;
			}
			if (!result.isSuccess()) {
				(new ErrorUtil(result)).dealWithResult();
			}
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
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Object getType() {
		return new MailMission();
	}
	
	public Result getResult() {
		return result;
	}
}
