package kaaass.es2k.packet;

import kaaass.es2k.file.PackSaver;
import kaaass.es2k.packet.pack.DataPacket;

public abstract class SaveablePacket extends Packet{
	public static PackSaver packSaver = new PackSaver();
	
	public SaveablePacket() {
		super();
	}
	
	public SaveablePacket(Path path) {
		super(path);
	}

	@Override
	public void onCreate() {
		this.onCreate(this.load());
	}
	
	@Override
	public void onUpdate(Path pth) {
		DataPacket dp = this.load();
		this.onSave(dp);
		packSaver.writeData();
		this.onFileUpdated(pth);
	}
	
	@Override
	public boolean getSaveable() {
		return true;
	}
	
	private DataPacket load() {
		DataPacket dp = packSaver.readData(this.getRootPath());
		this.onLoad(dp);
		return dp;
	}
	
	public abstract void onLoad(DataPacket dataSaver);
	
	public abstract void onSave(DataPacket dataSaver);
	
	public abstract void onFileUpdated(Path pth);
	
	public abstract void onCreate(DataPacket data);
}
