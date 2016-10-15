package voxspell.toolbox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import voxspell.app.VoxModel;

public class VoxDatabase {

	private static String[] levels = {"Level One","Level Two","Level Three","Level Four","Level Five","Level Six","Level Seven","Level Eight","Level Nine","Level Ten","Level Eleven"};
	protected static ArrayList<String> categories = new ArrayList<String>();
	protected static HashMap<String, String> dictionary = new HashMap<String, String>();
	


	protected File scm = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.sound.scm");
	
	
	protected File mastered = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.mastered");
	protected File failed = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.failed");
	protected File faulted = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.faulted");
	protected File masteredhistory = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.masteredhistory");
	protected File faultedhistory = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.faultedhistory");
	protected File failedhistory = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.failedhistory");
	protected File customizedLists = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.userCategories");
	
	
	protected static ArrayList<ArrayList<String>> levelContents = new ArrayList<ArrayList<String>>();
	
	
	protected static ArrayList<File> sysfiles = new ArrayList<File>();
	protected static ArrayList<ArrayList<String>> contents = new ArrayList<ArrayList<String>>(); //contents of individual sys files (e.g. .mastered)
	protected static ArrayList<String> filenames = new ArrayList<String>();
	
	
	String[] fn = {"mastered", "failed", "faulted", "masteredhistory", "faultedhistory", "failedhistory", "customizedLists"};
	File[] files = {mastered, failed, faulted, masteredhistory, faultedhistory, failedhistory, customizedLists};
	
	protected WordsCounter counter = WordsCounter.getWordsCounter();
	
	private static VoxDatabase db = null;
	
	private VoxDatabase(){};
	
	public static VoxDatabase getVoxDatabase(){
		if (db == null){
			db = new VoxDatabase();
		}
		return db;
	}

	public void projectSetup(){

		System.out.println("System boots up............");
		System.out.println("Current working dir: "+VoxModel.currentWorkingDirectory);

		//start files setup
		FileLoadingWorker fw = new FileLoadingWorker(files, fn, sysfiles, contents, filenames, levelContents, levels);
		fw.execute();

		//create scm file
		try {
			if (!(scm.exists())){
				scm.createNewFile();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fail to load scm file.", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		//finishes files setup
		
		//setting up VoiceChoice
		VoiceChoice vc = VoiceChoice.getVoiceChoice();
		vc.setSCM(scm);
		//done
		
		//Searching external vlc library
		VLCPathSearcher vlcSearcher = new VLCPathSearcher();
		vlcSearcher.execute();
		//all File IO done
	}
	
	public ArrayList<String> nextLevel(ArrayList<String> currentLevel){
		int levelNow=levelContents.indexOf(currentLevel);
		return levelContents.get(levelNow+1);
	}
	
	public int giveCurrentLevel(ArrayList<String> currentLevel){
		int levelNow=levelContents.indexOf(currentLevel);
		return levelNow;
	}
	
	public static void saveChangedFile(String destination){
		String s = "";
		FileWriter writer;
		try {
			writer = new FileWriter(sysfiles.get(filenames.indexOf(destination)), false);
			for (String str : contents.get(filenames.indexOf(destination))){
				s = s + str + "\n";
			}
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fail to write sys files.", "Warning", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public static ArrayList<String> getFilenames() {
		return filenames;
	}

	public static ArrayList<ArrayList<String>> getContents() {
		return contents;
	}

	public static ArrayList<File> getSysfiles() {
		return sysfiles;
	}

	public static HashMap<String, String> getDictionary() {
		return dictionary;
	}
}
