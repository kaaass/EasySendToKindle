package kaaass.es2k.mission;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kaaass.es2k.Main;

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
		Main.pb.setValue((sendCounter[0] - todoList.size()) / sendCounter[0] * 100);
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
		} else {
			this.runMission();
		}
	}
}
