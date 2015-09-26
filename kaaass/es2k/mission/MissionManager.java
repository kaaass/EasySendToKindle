package kaaass.es2k.mission;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kaaass.es2k.Main;
import kaaass.es2k.crashreport.ErrorUtil;

public class MissionManager {
	public List<IMission> mList = new ArrayList<IMission>();
	public List<IMission> todoList = new ArrayList<IMission>();
	public int id = 0;
	int[] sendCounter = new int[2];
	public boolean running = false;
	
	public int add (IMission m) {
		m.id = id;
		if (mList.size() - 1 < id) {
			mList.add(id, m);
		} else {
			mList.set(id, m);
		}
		return id;
	}
	
	public void todo (int id) {
		todoList.add(mList.get(id));
		sendCounter[0]++;
	}
	
	public void runMission () {
		todoList.get(0).start();
		running = true;
		Main.des4.setText("执行中，已完成" + (sendCounter[0] - 1) + "件，剩余" 
				+ todoList.size() + "件");
	}
	
	public void endMission (int id) {
		todoList.remove(mList.get(id));
		if (mList.get(id) instanceof MailMission) {
			if (!((MailMission) mList.get(id)).getResult().isSuccess()) {
				sendCounter[1]++;
			}
		}
		if (todoList.isEmpty()) {
			JOptionPane.showMessageDialog(null, String.format("任务完成!推送 %d 次，失败 %d 次。", sendCounter[0], sendCounter[1]));
			running = false;
			Main.des4.setText("完毕。已完成" + sendCounter[0] + "件，失败" 
					+ sendCounter[1] + "件");
			sendCounter = new int[2];
		} else {
			this.runMission();
		}
	}
	
	public void pause () {
		try {
			todoList.get(0).wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
			(new ErrorUtil(e)).dealWithException();
		}
	}
	
	public void resume () {
		todoList.get(0).notify();
	}
}
