package voxspell.toolbox;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import javax.swing.JOptionPane;

import javax.swing.SwingWorker;

import voxspell.app.VoxModel;

public class FileLoadingWorker extends SwingWorker<Void, Void> {
	protected static FileReader wordlist;
	protected static FileReader userCategories;
	protected String[] fn;
	protected File[] files;
	protected ArrayList<File> sysfiles;
	protected ArrayList<ArrayList<String>> contents;
	protected ArrayList<String> filenames;
	protected ArrayList<ArrayList<String>> levelContents;
	protected String[] levels;
	
	public FileLoadingWorker(File[] files, String[] fn, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, ArrayList<ArrayList<String>> levelContents, String[] levels){
		this.fn = fn;
		this.files = files;
		this.sysfiles = sysfiles;
		this.contents = contents;
		this.filenames = filenames;
		this.levelContents = levelContents;
		this.levels = levels;
		
	}
	
	@Override
	protected Void doInBackground(){
		
		System.out.println("Project files loading starts......");
		if (levelContents.isEmpty()){
			try {
				wordlist = new FileReader(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/wordlists/NZCER-spelling-lists.txt");
				for (@SuppressWarnings("unused") String s : levels){
					levelContents.add(new ArrayList<String>());
				}
				Scanner scanner = new Scanner(wordlist);
				int count = -1;
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					if (line.contains("%")){
						count++;
						continue;
					}
					levelContents.get(count).add(line);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Error! Required file(s) cannot be found! Please check if NZCER-spelling-lists.txt file have been modified or deleted.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		//create all sys files if needed otherwise load them.
		loadFiles(files, fn, sysfiles, contents, filenames, levelContents);
		
		
		for (String s : levels){
			VoxDatabase.categories.add(s);
		}
		try {
			Scanner s = new Scanner(new FileReader(sysfiles.get(sysfiles.size()-1)));
			while (s.hasNext()){
				String line = s.nextLine();
				if (line.equals("")){
					continue;
				}
				ArrayList<String> temp = new ArrayList<String>();
				line = line.substring(1, line.length());
				VoxDatabase.categories.add(line);
				Scanner s1 = new Scanner(new FileReader(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/."+line));
				while (s1.hasNext()){
					String line1 = s1.nextLine();
					if (line1.equals("")){
						continue;
					}
					temp.add(line1);
				}
				levelContents.add(temp);
				s1.close();
				
				File tempfile = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/."+line+"_def");
				if (tempfile.exists()){
					Scanner s2 = new Scanner(new FileReader(tempfile));
					int count = 0;
					while (s2.hasNext()){
						String line2 = s2.nextLine();
						if (line2.equals("")){
							continue;
						}
						VoxDatabase.dictionary.put(temp.get(count), line2);
						count++;
						if (count==temp.size()){
							break;
						}
					}
					s2.close();
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		videoSetup();
		return null;
	}
	
	private void loadFiles(File[] files, String[] fn, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, ArrayList<ArrayList<String>> levelContents){
		try {
			for (File file : files){
				if (!(file.exists())){
					file.createNewFile();
				}
			}
			int i = 0;
			for (File file : files){
				Scanner scanner = new Scanner(new FileReader(file));
				contents.add(new ArrayList<String>());
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					contents.get(i).add(line);
				}
				i++;
				scanner.close();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error! Fatal error occurs! Program will not be functioning correctly.", "Warning", JOptionPane.WARNING_MESSAGE);
		} 		
		
		for (int i=0; i<fn.length; i++){
			sysfiles.add(files[i]);
			filenames.add(fn[i]);
		}
	}
	
	private void videoSetup(){
		ProcessBuilder p = null;
		try {
			File bunny = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/videos/big_buck_bunny_1_minute.avi");
			String path = bunny.getCanonicalPath();
			path = path.substring(0, path.length()-bunny.getName().length());
			File bunny_negative = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/videos/big_buck_bunny_1_minute_converted.avi");
			if (!(bunny_negative.exists())){ //dead code LOL
				p = new ProcessBuilder("/bin/bash","-c", "ffmpeg -i "+bunny.getCanonicalPath()+" -vf negate "+
						path+"big_buck_bunny_1_minute_converted.avi");
			}
		} catch (IOException e2) {
			JOptionPane.showMessageDialog(null, "Unresolvable error occurs!! Required video file does not exist.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
		Process process = null;
		try {
			process = p.start();
			int exit = process.waitFor();
			if (exit==0){
				System.out.println("successfully converted video file.");
			}
		} catch (IOException | InterruptedException e1) {
			JOptionPane.showMessageDialog(null, "Unresolvable error occurs!! Underlying commands are possibly not functioning.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}


	@Override
	protected void done(){
		System.out.println("Project files loading done.");
	}
}

