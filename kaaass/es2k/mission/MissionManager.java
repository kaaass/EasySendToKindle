package kaaass.es2k.mission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kaaass.es2k.Main;
import kaaass.es2k.crashreport.ErrorUtil;

public class MissionManager {
	public List<IMission> mList = new ArrayList<IMission>();
	public List<Integer> todoList = new ArrayList<Integer>();
	public List<Integer> eList = new ArrayList<Integer>();
	public int id = 0;
	int sendCounter = 0;
	public boolean running = false;
	
	public int add (IMission m) {
		m.id = id;
		if (mList.size() - 1 < id) {
			mList.add(id, m);
		} else {
			mList.set(id, m);
		}
		return id++;
	}
	
	public void todo (int id) {
		todoList.add(id);
	}
	
	public void runMission () {
		if (!running && !eList.isEmpty()) {
			for (int i: eList) {
				todoList.add(i);
				mList.set(i, mList.get(i).restart());
			}
		}
		if (!running && !todoList.isEmpty()) {
			mList.get(todoList.get(0)).start();
			running = true;
		}
		if (todoList.isEmpty()) {
			return;
		}
		Main.des4.setText("ִ���У������" + (mList.size() - todoList.size()) + "����ʣ��" 
				+ todoList.size() + "��");
		Main.missionFrame.redraw();
	}
	
	public void endMission (int id) {
		todoList.remove(0);
		if (mList.get(id) instanceof MailMission) {
			if (!((MailMission) mList.get(id)).getResult().isSuccess()) {
				sendCounter++;
				eList.add(id);
			}
		}
		if (todoList.isEmpty()) {
			switch(MissionFrame.combo0.getSelectedIndex()){
			case 0:
				JOptionPane.showMessageDialog(null, String.format("�������!���� %d �Σ�ʧ�� %d �Ρ�", mList.size(), sendCounter));
				break;
			case 1:
				System.exit(0);
				break;
			case 2:
				try {
					Runtime.getRuntime().exec("Shutdown /s");
				} catch (IOException e) {
					e.printStackTrace();
					(new ErrorUtil(e)).dealWithException();
				} 
				break;
			case 3:
				try {
					Runtime.getRuntime().exec("Shutdown /l");
				} catch (IOException e) {
					e.printStackTrace();
					(new ErrorUtil(e)).dealWithException();
				} 
				break;
			case 4:
				try {
					Runtime.getRuntime().exec("Shutdown /r");
				} catch (IOException e) {
					e.printStackTrace();
					(new ErrorUtil(e)).dealWithException();
				} 
				break;
			default:
				JOptionPane.showMessageDialog(null, String.format("�������!���� %d �Σ�ʧ�� %d �Ρ�", mList.size(), sendCounter));
				break;
			}
			running = false;
			Main.des4.setText("��ϡ������" + mList.size() + "����ʧ��" 
					+ sendCounter + "��");
		} else {
			this.runMission();
		}
		Main.missionFrame.redraw();
	}
	
	public void clear () {
		for (int i = 0; i < mList.size(); i++) {
			if (todoList.indexOf(i) < 0 && eList.indexOf(i) < 0) {
				mList.set(i, null);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void pause () {
		this.mList.get(this.todoList.get(0)).suspend();
		Main.des4.setText("����ͣ");
	}
	
	@SuppressWarnings("deprecation")
	public void resume () {
		this.mList.get(this.todoList.get(0)).resume();
		Main.des4.setText("ִ���У������" + (mList.size() - todoList.size()) + "����ʣ��" 
				+ todoList.size() + "��");
	}
}
