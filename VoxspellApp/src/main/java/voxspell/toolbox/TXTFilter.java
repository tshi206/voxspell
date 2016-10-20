package voxspell.toolbox;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

public class TXTFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
	    if (f.isDirectory()) {
	        return true;
	    }

	    String filename = f.getName();
	    String extension = FilenameUtils.getExtension(filename);
	    if (extension != null) {
	        if (extension.equals("txt")) {
	                return true;
	        } else {
	            return false;
	        }
	    }

		return false;
	}

	@Override
	public String getDescription() {
		return ".txt files only";
	}

}
