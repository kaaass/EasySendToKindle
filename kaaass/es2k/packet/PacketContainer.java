package kaaass.es2k.packet;

import java.util.ArrayList;
import java.util.List;

public class PacketContainer {
	public static List<PacketContainer> cList = new ArrayList<PacketContainer>();
	public int id;
	private Path path;
	private List<PacketContainer> tagList = new ArrayList<PacketContainer>();
	private List<Key> keyList = new ArrayList<Key>();

	public PacketContainer(Path path) {
		cList.add(this);
		this.id = cList.size() - 1;
		this.path = path;
	}

	public static int getIdByPath(Path pth) {
		for (PacketContainer a : cList) {
			if (pth.equals(a.path)) {
				return a.id;
			}
		}
		return -1;
	}

	public static PacketContainer getInstance(int id) {
		if (id >= 0 && id < cList.size()) {
			id = 0;
		}
		return cList.get(id);
	}

	public Path getPath() {
		return this.path;
	}

	public PacketContainer tag(Path tag) {
		if (tag.getType() == Path.TAG) {
			return getInstance(getIdByPath(tag));
		}
		return this;
	}

	public PacketContainer addTag(PacketContainer tag) {
		if (tag.id != this.id) {
			this.tagList.add(tag);
		}
		return this;
	}

	public PacketContainer setTag(PacketContainer tag) {
		if (tag.id != this.id) {
			int i = this.tagList.indexOf(tag);
			if (i < 0) {
				this.addTag(tag);
			} else {
				this.tagList.set(i, tag);
			}
		}
		return this;
	}

	public PacketContainer removeTag(PacketContainer tag) {
		if (tag.id != this.id) {
			this.tagList.remove(tag);
		}
		return this;
	}

	public PacketContainer putObject(Path path, Object o) {
		if (path.equalsLoc(this.path)) {
			this.keyList.add(new Key(path, o, Key.OBJECT));
		}
		return this;
	}

	public PacketContainer putShort(Path path, short i) {
		if (path.equalsLoc(this.path)) {
			this.keyList.add(new Key(path).setShort(i));
		}
		return this;
	}

	public PacketContainer putInteger(Path path, int i) {
		if (path.equalsLoc(this.path)) {
			this.keyList.add(new Key(path).setInteger(i));
		}
		return this;
	}

	public PacketContainer putLong(Path path, long i) {
		if (path.equalsLoc(this.path)) {
			this.keyList.add(new Key(path).setLong(i));
		}
		return this;
	}

	public PacketContainer putFloat(Path path, float i) {
		if (path.equalsLoc(this.path)) {
			this.keyList.add(new Key(path).setFloat(i));
		}
		return this;
	}

	public PacketContainer putDouble(Path path, double i) {
		if (path.equalsLoc(this.path)) {
			this.keyList.add(new Key(path).setDouble(i));
		}
		return this;
	}

	public PacketContainer putArray(Path path, Object o) {
		if (path.equalsLoc(this.path)) {
			if (o instanceof short[]) {
				this.keyList.add(new Key(path, o, Key.ARRAY, Key.META_SHORT));
			} else if (o instanceof int[]) {
				this.keyList.add(new Key(path, o, Key.ARRAY, Key.META_INT));
			} else if (o instanceof long[]) {
				this.keyList.add(new Key(path, o, Key.ARRAY, Key.META_LONG));
			} else if (o instanceof float[]) {
				this.keyList.add(new Key(path, o, Key.ARRAY, Key.META_FLOAT));
			} else if (o instanceof double[]) {
				this.keyList.add(new Key(path, o, Key.ARRAY, Key.META_DOUBLE));
			} else if (o instanceof String[]) {
				this.keyList.add(new Key(path, o, Key.ARRAY, Key.META_STRING));
			} else {
				this.keyList.add(new Key(path, o, Key.ARRAY, Key.META_NULL));
			}
		}
		return this;
	}

	public PacketContainer putBoolean(Path path, boolean i) {
		if (path.equalsLoc(this.path)) {
			this.keyList.add(new Key(path, i, Key.BOOLEAN));
		}
		return this;
	}

	public PacketContainer putString(Path path, String i) {
		if (path.equalsLoc(this.path)) {
			this.keyList.add(new Key(path, i, Key.STRING));
		}
		return this;
	}

	public PacketContainer removeKey(Path path) {
		if (path.equalsLoc(this.path)) {
			for (Key k : this.keyList) {
				if (k.path.equals(path)) {
					this.keyList.remove(k);
					break;
				}
			}
		}
		return this;
	}

	public List<Key> getKeyList() {
		return this.keyList;
	}

	public List<PacketContainer> getTagList() {
		return this.tagList;
	}

	public boolean getSaveable() {
		for (Key k : this.keyList) {
			if (!k.getSaveable()) {
				return false;
			}
		}
		for (PacketContainer a : this.tagList) {
			if (!a.getSaveable()) {
				return false;
			}
		}
		return true;
	}

	public boolean equals(Object o) {
		if (o instanceof PacketContainer) {
			return this.id == ((PacketContainer) o).id;
		}
		return false;
	}

	public class Key {
		public final static int NULL = -1;
		public final static int OBJECT = 0;
		public final static int INT = 1;
		public final static int FLOAT = 2;
		public final static int ARRAY = 3;
		public final static int BOOLEAN = 4;
		public final static int STRING = 5;

		public final static int META_NULL = -1;
		public final static int META_SHORT = 0;
		public final static int META_INT = 1;
		public final static int META_LONG = 2;
		public final static int META_FLOAT = 3;
		public final static int META_DOUBLE = 4;
		public final static int META_STRING = 5;

		private Path path;
		private int type;
		private Object value;
		private short valueS;
		private int valueI;
		private long valueL;
		private float valueF;
		private double valueD;
		private int metadata;

		public Key(Path path) {
			this.path = path;
			this.type = NULL;
		}

		public Key(Path path, Object value, int type) {
			this.path = path;
			this.value = value;
			this.type = type;
			this.metadata = META_NULL;
		}

		public Key(Path path, Object value, int type, int metadata) {
			this.path = path;
			this.value = value;
			this.type = type;
			this.metadata = metadata;
		}

		public Key(Path path, long value) {
			this.path = path;
			this.valueL = value;
			this.type = INT;
			this.metadata = META_LONG;
		}

		public Key(Path path, double value) {
			this.path = path;
			this.valueD = value;
			this.type = FLOAT;
			this.metadata = META_DOUBLE;
		}

		public Key update(Path path) {
			this.path = path;
			return this;
		}

		public Key update(Object value) {
			this.value = value;
			return this;
		}

		public Key update(Object value, int type, int metadata) {
			this.update(value);
			this.type = type;
			this.update(metadata);
			return this;
		}

		public Key update(int metadata) {
			this.metadata = metadata;
			return this;
		}

		public Path getPath() {
			return this.path;
		}

		public Object getValue() {
			return this.value;
		}

		public short getShort() {
			if (this.type == INT && this.type == META_SHORT) {
				return this.valueS;
			}
			return 0;
		}

		public Key setShort(short i) {
			this.type = INT;
			this.metadata = META_SHORT;
			this.valueS = i;
			return this;
		}

		public int getInt() {
			if (this.type == INT) {
				return this.valueI;
			}
			return 0;
		}

		public Key setInteger(int i) {
			this.type = INT;
			this.metadata = META_INT;
			this.valueI = i;
			return this;
		}

		public long getLong() {
			if (this.type == INT && this.type == META_LONG) {
				return this.valueL;
			}
			return 0;
		}

		public Key setLong(long i) {
			this.type = INT;
			this.metadata = META_LONG;
			this.valueL = i;
			return this;
		}

		public float getFloat() {
			if (this.type == FLOAT) {
				return this.valueF;
			}
			return 0F;
		}

		public Key setFloat(float i) {
			this.type = FLOAT;
			this.metadata = META_FLOAT;
			this.valueF = i;
			return this;
		}

		public double getDouble() {
			if (this.type == FLOAT && this.metadata == META_DOUBLE) {
				return this.valueD;
			}
			return 0D;
		}

		public Key setDouble(double i) {
			this.type = FLOAT;
			this.metadata = META_DOUBLE;
			this.valueD = i;
			return this;
		}

		public int getType() {
			return this.type;
		}

		public int getMetadata() {
			return this.metadata;
		}

		public boolean getSaveable() {
			return this.type == ARRAY ? this.metadata > 0 : this.type > 0;
		}

		public String getTypeName() {
			switch (this.type) {
			case OBJECT:
				return "object";
			case INT:
				return "int";
			case FLOAT:
				return "float";
			case ARRAY:
				return "array";
			case BOOLEAN:
				return "boolean";
			case STRING:
				return "string";
			}
			return null;
		}

		public String getMetaName() {
			switch (this.metadata) {
			case META_SHORT:
				return "short";
			case META_INT:
				return "int";
			case META_LONG:
				return "long";
			case META_FLOAT:
				return "float";
			case META_DOUBLE:
				return "double";
			case META_STRING:
				return "string";
			}
			return null;
		}
	}
}
