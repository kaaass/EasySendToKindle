package kaaass.es2k.packet.pack;

import java.util.ArrayList;
import java.util.List;

import kaaass.es2k.log.Log;
import kaaass.es2k.log.Logger;
import kaaass.es2k.packet.Packet;
import kaaass.es2k.packet.PacketContainer;
import kaaass.es2k.packet.Path;
import kaaass.es2k.packet.PacketContainer.Key;

public class DataPacket extends Packet {
	private static Logger logger = new Logger("DataPacket");
	
	private Path keyPath;
	private Path keyTagList;
	private Path keyKeyList;
	
	public DataPacket path(Path pth) {
		this.putObject(this.keyPath, pth);
		return this;
	}
	
	public Path getPath() {
		return (Path) this.getObject(this.keyPath);
	}
	
	public DataPacket tagList(List<PacketContainer> list) {
		this.putObject(this.keyPath, list);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public List<PacketContainer> getTagList() {
		Object o = this.getObject(this.keyPath);
		if (o instanceof List<?>) {
			return (List<PacketContainer>) o;
		}
		return (new ArrayList<PacketContainer>());
	}
	
	public DataPacket keyList(List<Key> list) {
		this.putObject(this.keyPath, list);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public List<Key> getKeyList() {
		Object o = this.getObject(this.keyPath);
		if (o instanceof List<?>) {
			return (List<Key>) o;
		}
		return (new ArrayList<Key>());
	}
	
	@Override
	public void onCreate() {
		this.keyPath = this.getRootPath().key("path");
		this.addTag(this.keyPath);
		this.keyTagList = this.getRootPath().key("tag_list");
		this.addTag(this.keyTagList);
		this.keyKeyList = this.getRootPath().key("key_list");
		this.addTag(this.keyKeyList);
	}

	@Override
	public void onUpdate(Path pth) {
		Log.d(logger, "On updating path:" + pth.toString());
	}
}
