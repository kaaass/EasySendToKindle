package kaaass.es2k.packet;

import kaaass.es2k.packet.PacketContainer.Key;

public abstract class Packet {
	public static int id;
	public int subid;
	
	private Path root = new Path();
	private Path cur = new Path();
	private PacketContainer packTag;

	public Packet() {
		this.root.root("defalut");
		this.cur = this.root;
		this.packTag = new PacketContainer(this.root);
		PacketRegister.runList.add(this);
		this.subid = PacketRegister.runList.size() - 1;
		this.onCreate();
	}

	public Packet(Path root) {
		this.root = root;
		if (root.getType() != Path.ROOT) {
			this.root = new Path();
			this.root.root("defalut");
		}
		this.cur = this.root;
		this.packTag = new PacketContainer(this.root);
		PacketRegister.runList.add(this);
		this.subid = PacketRegister.runList.size() - 1;
		this.onCreate();
	}

	public Packet tag(Path tag) {
		if (tag.getType() == Path.TAG && this.cur.equalsLoc(tag)) {
			this.cur = tag;
		}
		return this;
	}

	public Packet addTag(Path tag) {
		if (tag.getType() == Path.TAG && this.cur.equalsLoc(tag)) {
			this.packTag.tag(cur).addTag(new PacketContainer(tag));
		}
		this.onUpdate(tag);
		return this;
	}

	public Packet removeTag(Path tag) {
		if (tag.getType() == Path.TAG && this.cur.equalsLoc(tag)) {
			this.packTag.tag(cur).removeTag(
					PacketContainer.getInstance(PacketContainer
							.getIdByPath(tag)));
		}
		this.onUpdate(tag);
		return this;
	}

	public Packet putObject(Path key, Object o) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putObject(key, o);
		}
		this.onUpdate(key);
		return this;
	}

	public Object getObject(Path key) {
		for (Key k: this.packTag.getKeyList()) {
			if (k.getPath().equals(key)) {
				return k.getValue();
			}
		}
		return null;
	}
	
	public Packet putShort(Path key, short i) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putShort(key, i);
		}
		this.onUpdate(key);
		return this;
	}
	
	public short getShort(Path key) {
		for (Key k: this.packTag.getKeyList()) {
			if (k.getPath().equals(key) && k.getMetadata() == Key.META_SHORT) {
				return k.getShort();
			}
		}
		return 0;
	}
	
	public Packet putInteger(Path key, int i) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putInteger(key, i);
		}
		this.onUpdate(key);
		return this;
	}
	
	public int getInteger(Path key) {
		for (Key k: this.packTag.getKeyList()) {
			if (k.getPath().equals(key) && k.getMetadata() == Key.META_INT) {
				return k.getInt();
			}
		}
		return 0;
	}
	
	public Packet putLong(Path key, long i) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putLong(key, i);
		}
		this.onUpdate(key);
		return this;
	}
	
	public long getLong(Path key) {
		for (Key k: this.packTag.getKeyList()) {
			if (k.getPath().equals(key) && k.getMetadata() == Key.META_LONG) {
				return k.getLong();
			}
		}
		return 0;
	}
	
	public Packet putFloat(Path key, float i) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putFloat(key, i);
		}
		this.onUpdate(key);
		return this;
	}
	
	public float getFloat(Path key) {
		for (Key k: this.packTag.getKeyList()) {
			if (k.getPath().equals(key) && k.getMetadata() == Key.META_FLOAT) {
				return k.getFloat();
			}
		}
		return 0F;
	}
	
	public Packet putDouble(Path key, double i) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putDouble(key, i);
		}
		this.onUpdate(key);
		return this;
	}
	
	public double getDouble(Path key) {
		for (Key k: this.packTag.getKeyList()) {
			if (k.getPath().equals(key) && k.getMetadata() == Key.META_DOUBLE) {
				return k.getDouble();
			}
		}
		return 0D;
	}
	
	public Packet putArray(Path key, Object o) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putArray(key, o);
		}
		this.onUpdate(key);
		return this;
	}
	
	public Object getArray(Path key) {
		return this.getObject(key);
	}
	
	public Packet putBoolean(Path key, boolean i) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putBoolean(key, i);
		}
		this.onUpdate(key);
		return this;
	}
	
	public boolean getBoolean(Path key) {
		for (Key k: this.packTag.getKeyList()) {
			if (k.getPath().equals(key) && k.getType() == Key.BOOLEAN) {
				return (Boolean) k.getValue();
			}
		}
		return false;
	}
	
	public Packet putString(Path key, String i) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).putString(key, i);
		}
		this.onUpdate(key);
		return this;
	}
	
	public String getString(Path key) {
		for (Key k: this.packTag.getKeyList()) {
			if (k.getPath().equals(key) && k.getType() == Key.STRING) {
				return (String) k.getValue();
			}
		}
		return null;
	}
	
	public Packet removeKey(Path key) {
		if (key.getType() == Path.KEY && this.cur.equalsLoc(key)) {
			this.packTag.tag(cur).removeKey(key);
		}
		this.onUpdate(key);
		return this;
	}
	
	public Path getRootPath() {
		return this.root;
	}
	
	public PacketContainer getContainer() {
		return this.packTag;
	}
	
	public boolean equals (Object o) {
		if (o instanceof Packet) {
			return this.subid == ((Packet) o).subid;
		}
		return false;
	}
	
	public boolean getSaveable() {
		return false;
	}
	
	public abstract void onCreate();
	
	public abstract void onUpdate(Path pth);
}