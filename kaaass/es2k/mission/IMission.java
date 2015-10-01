package kaaass.es2k.mission;

import kaaass.es2k.Main;
import kaaass.es2k.Main.SendType;

public abstract class IMission extends Thread {
	public int id;
	
	public IMission () {
		id = Main.missionManager.add(this);
	}
	
	public abstract SendType getInfo();
	
	public abstract void onStart();
	
	public abstract void onStop();
	
	public abstract void onRun();
	
	public abstract void onEnd();
	
	public abstract String getTitle();
	
	public abstract String getDesc();
	
	public abstract String getStates();
	
	public abstract Object getType();
	
	public abstract String getTypeName();
	
	public abstract IMission restart();
	
	public void start () {
		this.onStart();
		super.start();
	}
	
	public boolean end () {
		this.onStop();
		return super.interrupted();
	}
	
	public void run () {
		this.onRun();
		this.onEnd();
		Main.missionManager.endMission(id);
	}
	
	public void pause () {
		yield();
	}
	
	public boolean equals (Object o) {
		if (o instanceof IMission) {
			return ((IMission) o).id == this.id;
		}
		return false;
	}
}
