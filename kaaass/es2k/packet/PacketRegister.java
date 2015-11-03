package kaaass.es2k.packet;

import java.util.ArrayList;
import java.util.List;

import kaaass.es2k.packet.pack.DataPacket;
import kaaass.es2k.packet.pack.TestPacket;

public class PacketRegister {
	public static List<Packet> packetList = new ArrayList<Packet>();
	public static List<Integer> saveableList = new ArrayList<Integer>();

	public static List<Packet> runList = new ArrayList<Packet>();

	public static int regist(Packet p) {
		packetList.add(p);
		if (p instanceof SaveablePacket) {
			saveableList.add(packetList.size() - 1);
		}
		return packetList.size() - 1;
	}

	public static PacketContainer getContainerByPath(Path pth) {
		if (pth.getType() == Path.NULL)
			return null;
		for (Packet i : packetList)
			if (pth.equalsRoot(i.getRootPath()))
				return getConByPath(pth, i.getContainer());
		return null;
	}

	private static PacketContainer getConByPath(Path pth, PacketContainer p) {
		if (pth.getType() != Path.TAG)
			return null;
		for (PacketContainer i :p.getTagList()) {
			if (i.getPath().equals(pth)) {
				return i;
			} else if (i.getPath().equalsLoc(pth)) {
				return getConByPath(i.getPath(), i);
			}
		}
		return null;
	}

	public static void init() {
		regist(new DataPacket());
		regist(new TestPacket());
	}
}
