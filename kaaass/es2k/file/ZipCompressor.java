package kaaass.es2k.file;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class ZipCompressor {
	private File zipFile;

	public ZipCompressor(String outPathName) {
		zipFile = new File(outPathName);
	}
	
	public void compress(String inPathName) throws RuntimeException {
		File srcdir = new File(inPathName);
		if (!srcdir.exists()){
			throw new RuntimeException(inPathName + "²»´æÔÚ£¡");
		}
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		fileSet.setIncludes("*.log");
		fileSet.setIncludes("*.mobi");
		zip.addFileset(fileSet);
		zip.execute();
	}
}
