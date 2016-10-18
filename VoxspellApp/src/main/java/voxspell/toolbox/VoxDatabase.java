package voxspell.toolbox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import voxspell.app.VoxModel;
import voxspell.gui.NewGame;
import voxspell.gui.Settings;

public class VoxDatabase {

	private static String[] levels = {"Level One","Level Two","Level Three","Level Four","Level Five","Level Six","Level Seven","Level Eight","Level Nine","Level Ten","Level Eleven"};
	protected static ArrayList<String> categories = new ArrayList<String>();
	protected static HashMap<String, String> dictionary = new HashMap<String, String>();
	


	protected static File scm = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.sound.scm");
	
	protected static File importedLists = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/importedLists");
	protected static File registeredUsr = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/registeredUsr");
	protected static File defaultSettings = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.defaultSettings");
	
	protected static File mastered = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.mastered");
	protected static File failed = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.failed");
	protected static File faulted = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.faulted");
	protected static File masteredhistory = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.masteredhistory");
	protected static File faultedhistory = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.faultedhistory");
	protected static File failedhistory = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.failedhistory");
	
	
	protected static File customizedLists = new File(VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.userCategories");
	
	
	protected static ArrayList<ArrayList<String>> levelContents = new ArrayList<ArrayList<String>>(); //contents of individual categories
	
	
	protected static ArrayList<File> sysfiles = new ArrayList<File>();
	protected static ArrayList<ArrayList<String>> contents = new ArrayList<ArrayList<String>>(); //contents of individual sys files (e.g. .mastered)
	protected static ArrayList<String> filenames = new ArrayList<String>();
	
	
	static String[] fn = {"importedLists", "registeredUsr", "usrSettings", "mastered", "failed", "faulted", "masteredhistory", "faultedhistory", "failedhistory", "customizedLists"};
	static File[] files = {importedLists, registeredUsr, defaultSettings, mastered, failed, faulted, masteredhistory, faultedhistory, failedhistory, customizedLists};
	
	protected static WordsCounter counter = WordsCounter.getWordsCounter();
	
	protected static ArrayList<String> selectedCategory;

	
	
	private static VoxDatabase db = null;
	
	private VoxDatabase(){};
	
	public static VoxDatabase getVoxDatabase(){
		if (db == null){
			db = new VoxDatabase();
		}
		return db;
	}

	/**
	 * CALL THIS METHOD WHENEVER LOGING EVENT FIRES, ALSO EVERY TIME WHEN APPLICATION STARTS UP.
	 */
	public static void projectSetup(){
		
		System.out.println("System boots up............");
		System.out.println("Current working dir: "+VoxModel.currentWorkingDirectory);

		
		
		//Load user information TODO -- unimplemented, will change the file list in VoxDatabase depending on user 
//		UsrInfoLoadingWorker uw = new UsrInfoLoadingWorker();
//		uw.execute();
		
		//start files setup
		FileLoadingWorker fw = new FileLoadingWorker(files, fn, sysfiles, contents, filenames, levelContents, levels, categories);
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
	

	/**
	 * This method can only be used to .mastered, .faulted, .failed and their corresponding history files.
	 * Use the GENERAL VERSION of saveChangedFile method to write to the other sys files.
	 * @param destination
	 */
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
	
	/**
	 * GENERAL FILE WRITTER.
	 * @param file
	 * @param input
	 * @param isAppendable
	 */
	public static void saveChangedFile(File file, String input, boolean isAppendable){
		FileWriter writer;
		try {
			writer = new FileWriter(file, isAppendable);
			writer.write(input);
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
	
	
	public static ArrayList<String> getSelectedCategory() {
		return selectedCategory;
	}

	public static void setSelectedCategory(ArrayList<String> selectedCategory) {
		VoxDatabase.selectedCategory = selectedCategory;
		NewGame.getNewGameWindow().updateModel(selectedCategory);
	}

	public static ArrayList<String> getCategories() {
		return categories;
	}
	
	
	public static ArrayList<ArrayList<String>> getLevelContents() {
		return levelContents;
	}

}
