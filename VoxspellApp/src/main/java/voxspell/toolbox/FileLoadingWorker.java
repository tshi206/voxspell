package voxspell.toolbox;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import javax.swing.SwingWorker;

import voxspell.gui.Settings;
import voxspell.gui.Stats;

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
	protected ArrayList<String> categories;
	
	private boolean loadFiles = true;
	private boolean loadCustomizedCategories = true;
	private boolean setupVideo = true;
	
	public FileLoadingWorker(boolean loadFiles, boolean loadCustomizedCategories, boolean setupVideo){
		
		this.loadFiles = loadFiles;
		this.loadCustomizedCategories = loadCustomizedCategories;
		this.setupVideo = setupVideo;
		
		this.fn = VoxDatabase.getFn();
		this.files = VoxDatabase.getFiles();
		this.sysfiles = VoxDatabase.getSysfiles();
		this.contents = VoxDatabase.getContents();
		this.filenames = VoxDatabase.getFilenames();
		this.levelContents = VoxDatabase.getLevelContents();
		this.levels = VoxDatabase.getLevels();
		this.categories = VoxDatabase.getCategories();
	}
	
	@Override
	protected Void doInBackground(){
		
		System.out.println("Project files loading starts......");
		
		loadDefaultCategories();
		
		
		if (loadFiles){
			//create all sys files if needed otherwise load them.
			loadFiles(files, fn, sysfiles, contents, filenames);
		}
		
		
		if (loadCustomizedCategories){
			loadCustomizedCategories();
		}
		
		
		if (setupVideo){
			videoSetup();
		}
		
		
		return null;
	}
	
	private void loadFiles(File[] files, String[] fn, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames){
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
			File bunny = new File(VoxDatabase.videosDirectory+"big_buck_bunny_1_minute.avi");
			String path = bunny.getCanonicalPath();
			path = path.substring(0, path.length()-bunny.getName().length());
			File bunny_negative = new File(VoxDatabase.videosDirectory+"big_buck_bunny_1_minute_converted.avi");
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

	private void loadDefaultCategories(){
		if (levelContents.isEmpty()){
			try {
				wordlist = new FileReader(VoxDatabase.wordlistsDirectory+".NZCER-spelling-lists.txt");
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
				
				for (String s : levels){
					categories.add(s);
				}

				scanner.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Error! Required file(s) cannot be found! Please check if NZCER-spelling-lists.txt file have been modified or deleted.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	private void loadCustomizedCategories(){
		try {
			Scanner s = new Scanner(new FileReader(sysfiles.get(sysfiles.size()-1)));
			while (s.hasNext()){
				String line = s.nextLine();
				if (line.equals("")){
					continue;
				}
				
				line = line.substring(1, line.length());
				
				ArrayList<String> temp = new ArrayList<String>();
				
				ArrayList<ArrayList<String>> customizedCategoriesContents = new ArrayList<ArrayList<String>>();
				
				String categoryName = "";
				Scanner s1 = new Scanner(new FileReader(VoxDatabase.wordlistsDirectory+"."+line));
				while (s1.hasNext()){
					String line1 = s1.nextLine();
					if (line1.equals("")){
						continue;
					}
					if (line1.startsWith("%")){
						if (categoryName.equals("")){
							if ((temp.isEmpty())||(temp.size()<10)){
								categoryName = line1.substring(1);
								continue;
							}else{
								VoxDatabase.categories.add(line);
								levelContents.add(temp);
								customizedCategoriesContents.add(temp);
								temp = new ArrayList<String>();
								categoryName = line1.substring(1);
								continue;
							}
						}else{
							if ((!(temp.isEmpty()))&&(temp.size()>=10)){
								VoxDatabase.categories.add(categoryName);
								levelContents.add(temp);
								customizedCategoriesContents.add(temp);
							}
							temp = new ArrayList<String>();
							categoryName = line1.substring(1);
							continue;
						}
					}
					temp.add(line1);
				}
				
				if ((!(temp.isEmpty()))&&(temp.size()>=10)){
					if (categoryName.equals("")){
						VoxDatabase.categories.add(line);
					}else{
						VoxDatabase.categories.add(categoryName);
					}
					
					levelContents.add(temp);
					customizedCategoriesContents.add(temp);
				}
				
				s1.close();
				
				if (!(customizedCategoriesContents.isEmpty())){
					
					File tempfile = new File(VoxDatabase.wordlistsDirectory+"."+line.substring(0,line.length()-".txt".length())+"_def.txt");
					if (tempfile.exists()){
						
						ArrayList<String> tempContents = new ArrayList<String>();
						for (ArrayList<String> array : customizedCategoriesContents){
							for (String word : array){
								tempContents.add(word);
							}
						}
						
						Scanner s2 = new Scanner(new FileReader(tempfile));
						int count = 0;
						while (s2.hasNext()){
							String line2 = s2.nextLine();
							if (line2.equals("")){
								count++;
								continue;
							}
							if (line2.startsWith("%")){
								continue;
							}
							VoxDatabase.dictionary.put(tempContents.get(count), line2);
							count++;
							if (count==tempContents.size()){
								break;
							}
						}
						s2.close();
					}
				}
				
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void done(){
		System.out.println("Project files loading done.");
		
		Settings.getSettingsWindow();
		
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(VoxDatabase.getCategories().toArray(new String[VoxDatabase.getCategories().size()]));
		Stats.getStatsWindow().getComboBox().setModel(model);
		Stats.getStatsWindow().getComboBox().setSelectedIndex(0);
	}
	
}

