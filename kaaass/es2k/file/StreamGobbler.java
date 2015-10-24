package kaaass.es2k.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
	InputStream is;
	String type;
	StringBuilder str = new StringBuilder();

	public StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(type + ">" + line);
				str.append("CMD out:" + type + ">" + line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public String getOut() {
		return str.toString();
	}
}
