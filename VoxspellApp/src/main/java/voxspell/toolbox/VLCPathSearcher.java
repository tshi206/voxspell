package voxspell.toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


import javax.swing.JOptionPane;

import javax.swing.SwingWorker;

import voxspell.app.VoxModel;
import voxspell.gui.VideoPlayer;

public class VLCPathSearcher extends SwingWorker<Void, Void> {

	private Boolean findInDefault = false;
	private String pathFound = "";
	private File searchinglog = new File(VoxModel.currentWorkingDirectory+"/.searchinglog");
	private ArrayList<String> log = new ArrayList<String>();
	
	public VLCPathSearcher(){
		if (!(searchinglog.exists())){
			try {
				searchinglog.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File dir = new File("/usr/lib");
		File[] matchingFiles = dir.listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        return pathname.getName().startsWith("libvlc");
		    }
		});
		findInDefault = matchingFiles.length > 0;
		System.out.println("Searching libvlc.so in /usr/lib is in progress............");
		for (File f : matchingFiles){
			try {
				System.out.println(f.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		if (findInDefault){
			System.out.println("Successfully located external library (libvlc.so) in default path.");
		}else{
			System.out.println("Failed to locate external library (libvlc.so) in default path.");
			System.out.println("Searching process will now start..................");
			
			ProcessBuilder p = null;
			p = new ProcessBuilder("/bin/bash","-c", "find / -name \"libvlc.so*\" | grep libvlc > "
					+ VoxModel.currentWorkingDirectory+"/.searchinglog");
			Process process = null;
			int exit = 0;
			try {
				process = p.start();
				exit = process.waitFor();
			} catch (IOException | InterruptedException e1) {
				JOptionPane.showMessageDialog(null, "Fatal: Underlying commands are not functioning.", "Warning", JOptionPane.ERROR_MESSAGE);
			}
			if (exit==0){
				FileReader fr = null;
				try {
					fr = new FileReader(searchinglog);
					Scanner scanner = new Scanner(fr);
					while (scanner.hasNext()){
						String line = scanner.nextLine();
						if (line.contains("voxspell")){
							continue;
						}
						log.add(line);
					}
					scanner.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			if (log.isEmpty()){
				System.out.println("Failed to relocate required library (libvlc.so). Videoplayer will not be functional.");
			}else{
				for (String s : log){
					System.out.println(s);
					exit = 0;
					p = new ProcessBuilder("/bin/bash","-c", "dirname "+s);
					process = null;
					try {
						process = p.start();
						exit = process.waitFor();
						if (exit==0){
							InputStream ip = process.getInputStream();
							BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ip));
							String path;
							while ((path = bufferedReader.readLine()) != null) {
								System.out.println(path);
								pathFound = pathFound+":"+path;
							}
							ip.close();
							bufferedReader.close();
						}
					} catch (IOException | InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	@Override
	protected void done(){
		if (!(log.isEmpty())){
			System.out.println("Searching result: path added ---->\n"
					+ pathFound);
			VideoPlayer.setAdditionalPath(pathFound);
			System.out.println("Relocating VLC library successfully done.");
		}
	}
}
