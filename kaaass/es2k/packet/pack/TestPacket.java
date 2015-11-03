package kaaass.es2k.packet.pack;

import kaaass.es2k.log.Log;
import kaaass.es2k.log.Logger;
import kaaass.es2k.packet.Path;
import kaaass.es2k.packet.SaveablePacket;

public class TestPacket extends SaveablePacket {

	public static Logger logger = new Logger("TestPacket");

	public TestPacket() {
		super(new Path("TestPacket"));
		this.putFloat(this.getRootPath().key("123"), 123124.1F);
		Log.d(logger, this.getRootPath().key("123").toString());
		Log.d(logger, String.valueOf(this.getFloat(this.getRootPath().key("123"))));
		this.putString(this.getRootPath().key("stringtest"), "I'm a string test!!");
		this.tag(this.getRootPath().tag("a")).putInteger(this.getRootPath().tag("a"), 10000);
		this.tag(this.getRootPath().tag("a", "b")).putInteger(this.getRootPath().tag("a", "b"), 10086);
	}
	
	@Override
	public void onLoad(DataPacket dataSaver) {
		Log.d(logger, "onLoad");
	}

	@Override
	public void onSave(DataPacket dataSaver) {
		Log.d(logger, "onSave");
	}

	@Override
	public void onFileUpdated(Path pth) {
		Log.d(logger, "onFileUpdated");
	}

	@Override
	public void onCreate(DataPacket data) {
		Log.d(logger, "onCreate");
	}

}
