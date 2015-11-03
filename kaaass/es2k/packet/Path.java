package kaaass.es2k.packet;

import java.util.ArrayList;
import java.util.List;

public class Path {
	private final static String AUTHOR = "kaaass";

	public final static int NULL = -1;
	public final static int ROOT = 0;
	public final static int TAG = 1;
	public final static int KEY = 2;

	private int type = NULL;
	private String root = null;
	private List<String> tag = new ArrayList<String>();
	private String key = null;

	public Path() {
	}

	public Path(String root) {
		this.root = root;
		this.type = ROOT;
	}

	public String getRoot() {
		return this.root;
	}

	public Path root(String root) {
		Path p = this;
		p.root = root;
		if (p.type < ROOT) {
			p.type = ROOT;
		}
		return p;
	}

	public String getTag(int index) {
		if (index < 0 || index > this.tag.size() - 1) {
			index = this.tag.size() - 1;
		}
		return this.tag.get(index);
	}

	public String[] getTagArray() {
		String[] s = new String[this.tag.size()];
		for (int i = 0; i < this.tag.size(); i++) {
			s[i] = this.tag.get(i);
		}
		return s;
	}

	public List<String> getTagList() {
		return this.tag;
	}

	public Path tag(String... tag) {
		Path p = this;
		for (String i : tag) {
			p.tag.add(i);
		}
		if (p.type < TAG) {
			p.type = TAG;
		}
		return p;
	}

	public Path setTag(String tag) {
		this.tag.clear();
		this.tag.add(tag);
		if (this.type < TAG) {
			this.type = TAG;
		}
		return this;
	}

	public Path setTag(String[] tag) {
		this.tag.clear();
		for (String i : tag) {
			this.tag.add(i);
		}
		if (this.type < TAG) {
			this.type = TAG;
		}
		return this;
	}

	public Path setTag(List<String> tag) {
		this.tag = tag;
		if (this.type < TAG) {
			this.type = TAG;
		}
		return this;
	}

	public String getKey() {
		return this.key;
	}

	public Path key(String key) {
		Path p = this;
		p.key = key;
		p.type = KEY;
		return p;
	}

	public String toString() {
		String tag = "";
		switch (this.type) {
		case NULL:
			return AUTHOR + "://";
		case ROOT:
			return AUTHOR + "://" + this.root;
		case TAG:
			for (String i : this.tag) {
				tag += i + "/";
			}
			return AUTHOR + "://" + this.root + ":" + tag;
		case KEY:
			for (String i : this.tag) {
				tag += i + "/";
			}
			return AUTHOR + "://" + this.root + ":" + tag + this.key;
		}
		return null;
	}

	public Path parse(String pth) {
		if (pth.equals(AUTHOR + "://")) {
			this.type = NULL;
		} else if (pth.startsWith(AUTHOR + "://")) {
			String p = pth.substring(AUTHOR.length() + 3);
			this.root = p.split(":")[0];
			if (p.split(":").length > 1) {
				p = p.split(":")[1];
				for (String i : p.split("/")) {
					this.tag.add(i);
				}
				if (pth.endsWith("/")) {
					this.type = TAG;
				} else {
					this.key = this.tag.get(this.tag.size() - 1);
					this.tag.remove(this.tag.size() - 1);
					this.type = KEY;
				}
			} else {
				this.type = ROOT;
			}
		}
		return this;
	}

	public int getType() {
		return this.type;
	}

	public boolean equals(Object o) {
		if (o instanceof Path) {
			return this.type == ((Path) o).type && this.root == ((Path) o).root
					&& this.tag.equals(((Path) o).tag)
					&& this.key == ((Path) o).key;
		}
		return false;
	}

	public boolean equalsRoot(Path pth) {
		return this.type >= ROOT && this.root == pth.root;
	}

	public boolean equalsLoc(Path pth) {
		return this.type >= ROOT && this.root == pth.root
				&& this.tag.equals(pth.tag);
	}
}
