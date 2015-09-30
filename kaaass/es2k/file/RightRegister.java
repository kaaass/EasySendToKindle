package kaaass.es2k.file;

import kaaass.es2k.crashreport.ErrorUtil;

import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class RightRegister {
	private String dir = "*\\shell";
	
	private RegistryKey rkey = null;
	
	public RightRegister() {
		rkey = Registry.openSubkey(Registry.HKEY_CLASSES_ROOT, this.dir, 
				RegistryKey.ACCESS_WRITE);
	}
	
	public RightRegister(String dir) {
		this.dir = dir;
		rkey = Registry.openSubkey(Registry.HKEY_CLASSES_ROOT, this.dir, 
				RegistryKey.ACCESS_WRITE);
	}
	
	private void createSubDir(String subDirName, String key, String defaultValue)
			throws RegistryException {
		rkey.createSubKey(subDirName, "");
		rkey = Registry.openSubkey(Registry.HKEY_CLASSES_ROOT, dir + "\\"
				+ subDirName, RegistryKey.ACCESS_WRITE);
		rkey.setValue(new RegStringValue(rkey, key, defaultValue));
		this.dir = this.dir + "\\" + subDirName;
	}
	
	public void install () {
		try {
			this.createSubDir("ES2K", "", "将此文件推送给Kindle");
			this.createSubDir("Command", "", System.getProperty("java.class.path") + " -send \"%1\"");
		} catch (Exception e) {
			e.printStackTrace();
			(new ErrorUtil(e)).dealWithException();
		}
	}
	
	public void uninstall () {
		try {
			rkey.deleteSubKey("ES2K");
		} catch (Exception e) {
			e.printStackTrace();
			(new ErrorUtil(e)).dealWithException();
		}
	}
}
