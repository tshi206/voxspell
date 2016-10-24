package voxspell.toolbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

import voxspell.app.VoxMain;
import voxspell.app.VoxModel;
import voxspell.gui.NewGame;
import voxspell.gui.Settings;
import voxspell.gui.Stats;
import voxspell.gui.WindowPattern;

/**
 * This is the most complex class within this application.
 * It initializes and maintains all the abstract data structure storing corresponding files' contents.
 * These data structures are accessible to other classes in runtime and can be modified and changed.
 * It maintains all paths to every specific system's folders, using absolute path names.
 * It also provides all the static methods implementing the actual File I/O operation.
 * It manages FileLoadingWorker and VLCPathSearcher.
 * It manages the user information and decides who is logged in when the system starts up.
 * @author mason23
 *
 */
public class VoxDatabase {
	
	public static String currentWorkingDirectory = System.getProperty("user.dir");
	public static String wordlistsDirectory = System.getProperty("user.dir")+"/src/main/java/voxspell/resources/wordlists/";
	public static String sysfilesDirectory = System.getProperty("user.dir")+"/src/main/java/voxspell/resources/sysfiles/";
	public static String videosDirectory = System.getProperty("user.dir")+"/src/main/java/voxspell/resources/videos/";
	public static String helpDirectory = System.getProperty("user.dir")+"/src/main/java/voxspell/resources/help/";
	public static String usrDirectory = System.getProperty("user.dir")+"/src/main/java/voxspell/resources/usrInfo/";
	
	// names of default categories
	private static String[] levels = {"Level One","Level Two","Level Three","Level Four","Level Five","Level Six","Level Seven","Level Eight","Level Nine","Level Ten","Level Eleven"};
	
	// runtime structure of all categories logged in the system
	protected static ArrayList<String> categories = new ArrayList<String>();
	// runtime structure of all definitions logged in the system
	protected static HashMap<String, String> dictionary = new HashMap<String, String>();
	

	// scm file used by festival to generate speech
	protected static File scm = new File(VoxDatabase.sysfilesDirectory+".sound.scm");
	
	// system files logs users' informations
	protected static File rememberedUsr = new File(VoxDatabase.sysfilesDirectory+"rememberedUsr");
	protected static File registeredUsr = new File(VoxDatabase.sysfilesDirectory+".registeredUsr");
	
	// file logs users' settings
	protected static File defaultSettings = new File(VoxDatabase.sysfilesDirectory+".defaultSettings");
	
	protected static File mastered = new File(VoxDatabase.sysfilesDirectory+".mastered"); // file records mastered words, no words duplicated
	protected static File failed = new File(VoxDatabase.sysfilesDirectory+".failed"); // file records failed words, no words duplicated
	protected static File faulted = new File(VoxDatabase.sysfilesDirectory+".faulted"); // file records faulted words, no words duplicated
	protected static File masteredhistory = new File(VoxDatabase.sysfilesDirectory+".masteredhistory"); // file records mastered words (can be duplicated)
	protected static File faultedhistory = new File(VoxDatabase.sysfilesDirectory+".faultedhistory"); // file records faulted words (can be duplicated)
	protected static File failedhistory = new File(VoxDatabase.sysfilesDirectory+".failedhistory"); // file records failed words (can be duplicated)
	
	protected static File records = new File(VoxDatabase.sysfilesDirectory+".records"); // file records users' highest amount of mastered words (words can be duplicated)
	
	protected static File customizedLists = new File(VoxDatabase.sysfilesDirectory+".userCategories"); // file logs names of all the imported or created categories
	
	
	protected static ArrayList<ArrayList<String>> levelContents = new ArrayList<ArrayList<String>>(); //contents of all categories
	
	
	protected static ArrayList<File> sysfiles = new ArrayList<File>();
	protected static ArrayList<ArrayList<String>> contents = new ArrayList<ArrayList<String>>(); //contents of all sys files (e.g. .mastered, .defaultSettings, etc.)
	protected static ArrayList<String> filenames = new ArrayList<String>();
	
	// names of all sys files
	static String[] fn = {"rememberedUsr", "registeredUsr", "usrSettings", "mastered", "failed", "faulted", "masteredhistory", "faultedhistory", "failedhistory", "records", "customizedLists"};
	// array structure storing all sys files
	static File[] files = {rememberedUsr, registeredUsr, defaultSettings, mastered, failed, faulted, masteredhistory, faultedhistory, failedhistory, records, customizedLists};
	
	// counts the words from the startup until the close of the application, used to provide accuracy of spelling.
	protected static WordsCounter counter = WordsCounter.getWordsCounter();
	
	// represent the contents of the selected category used in Quiz Mode
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
		System.out.println("Current working dir: "+VoxDatabase.currentWorkingDirectory);

		
		
		//Load user information
		try {
			
			if (!(rememberedUsr.exists())){
				rememberedUsr.createNewFile();
			}
			
			Scanner scanner = new Scanner(new FileReader(rememberedUsr));
			String username = "";
			while (scanner.hasNext()){
				String line = scanner.nextLine();
				username = line;
			}
			scanner.close();
			if (!(username.equals(""))){
				
				VoxDatabase.loadUsrInfo(username, false);
				
				for (WindowPattern wp : VoxModel.getVoxModel().getGuis()){
					wp.getLogin().setEnabled(false);
					wp.getLogoff().setEnabled(true);
					wp.updateUsr(username);
				}
				
				VoxMain.isUserBack = true;
				VoxMain.username = username;
				
			}else{
				
				for (WindowPattern wp : VoxModel.getVoxModel().getGuis()){
					wp.getLogin().setEnabled(true);
					wp.getLogoff().setEnabled(false);
				}
				
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//start files setup
		FileLoadingWorker fw = new FileLoadingWorker(true, true, true, true);
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
	 * This method can only be used to .mastered, .faulted, .failed and their corresponding history files (including .records file).
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
	
	/**
	 * Create a words file using the name given in system's words files' directory
	 * @param name
	 * @return the file that has been created
	 */
	public static File createWordsFile(String name){		
		File temp = new File(VoxDatabase.wordlistsDirectory+"."+name);
		if (!(temp.exists())){
			try {
				temp.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return temp;
	}
	
	/**
	 * Write to the words file in terms of the specified name, supplied contents, and an option of whether the file should be overwrite by appending or not
	 * @param name
	 * @param content
	 * @param appendable
	 */
	public static void writeToWordsFile(String name, String content, boolean appendable){
		try {
			FileWriter fw = new FileWriter(VoxDatabase.wordlistsDirectory+"."+name, appendable);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write to the system's file in terms of the specified name, supplied contents, and an option of whether the file should be overwrite by appending or not
	 * @param name
	 * @param content
	 * @param appendable
	 */
	public static void writeToSysFile(String name, String content, boolean appendable){
		try {
			FileWriter fw = new FileWriter(VoxDatabase.getSysfiles().get(VoxDatabase.getFilenames().indexOf(name)), appendable);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete system's file.
	 * This method only deletes .mastered, .failed, .faulted, .masteredhistory, .faultedhistory, and .failedhistory files.
	 */
	public static void deleteSysFile(){
		for (int i = 3; i<9; i++){
			VoxDatabase.getSysfiles().get(i).delete();
			try {
				VoxDatabase.getSysfiles().get(i).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			VoxDatabase.getContents().get(i).removeAll(VoxDatabase.getContents().get(i));
			Stats.getStatsWindow().getComboBox().setSelectedIndex(Stats.lastSelectedIndex);
		}
	}
	
	/**
	 * Creating a category in the application and updating all relevant GUIs.
	 * @param categoryContents
	 * @param categoryName
	 */
	public static void addCategory(ArrayList<String> categoryContents, String categoryName){
		VoxDatabase.getLevelContents().add(categoryContents);
		VoxDatabase.getCategories().add(categoryName);
		Settings.getSettingsWindow().getCategory().addItem(categoryName);
		Stats.getStatsWindow().getComboBox().addItem(categoryName);
	}

	/**
	 * Deleting a category in the application and updating all relevant GUIs.
	 * @param categoryContents
	 * @param categoryName
	 */
	public static void deleteCategory(ArrayList<String> categoryContents, String categoryName){
		VoxDatabase.getLevelContents().remove(categoryContents);
		VoxDatabase.getCategories().remove(categoryName);
		Settings.getSettingsWindow().getCategory().removeItem(categoryName);
		Stats.getStatsWindow().getComboBox().removeItem(categoryName);
	}
	
	/**
	 * Change current system's files directory to the specified user directory based on the name passed in.
	 * The second parameter determines whether the actual file loading would happen.
	 * The reloadFile flag should be false when this method is used in system's startup process to avoid multi-thread conflicts.
	 * Otherwise it should be set to true.
	 * This method will first remove abstract structures' contents before reloading them using files in specified users' directory.
	 * Note that this method will not modify '.rememberedUsr', '.registeredUsr', and customizedList ('.userCategories') files.
	 * If reloadFile flag is true, this method will update the GUIs by calling FileLoadingWorker.
	 * @param usrName
	 * @param reloadFile
	 */
	public static void loadUsrInfo(String usrName, boolean reloadFile){
		
		File newDir = new File(VoxDatabase.usrDirectory+usrName);
		if (!(newDir.exists())){
			newDir.mkdir();
		}
		
		String usrDir = VoxDatabase.usrDirectory+usrName+"/";
		
		File[] temp = VoxDatabase.getFiles();
		for (int i = 2; i<10; i++){
			temp[i] = new File(usrDir+temp[i].getName());
		}
		
		VoxDatabase.getContents().removeAll(contents);
		VoxDatabase.getFilenames().removeAll(filenames);
		VoxDatabase.getSysfiles().removeAll(sysfiles);
		
		if (reloadFile){
			FileLoadingWorker flw = new FileLoadingWorker(false, true , false, false);
			flw.execute();
		}
		
	}
	
	/**
	 * This method reset the system's environment to default system directory.
	 * This method will also update relevant data structures' contents as well as updating the GUIs by calling FileLoadingWorker.
	 */
	public static void backToAnonymous(){
		
		String usrDir = VoxDatabase.sysfilesDirectory;
		
		File[] temp = VoxDatabase.getFiles();
		for (int i = 2; i<9; i++){
			temp[i] = new File(usrDir+temp[i].getName());
		}
		
		VoxDatabase.getContents().removeAll(contents);
		VoxDatabase.getFilenames().removeAll(filenames);
		VoxDatabase.getSysfiles().removeAll(sysfiles);
		
		FileLoadingWorker flw = new FileLoadingWorker(false, true , false, false);
		flw.execute();
		
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

	/**
	 * Update NewGame's model with supplied selectedCategory
	 * @param selectedCategory - contents of the selected category
	 */
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


	public static String[] getLevels() {
		return levels;
	}

	public static String[] getFn() {
		return fn;
	}

	public static File[] getFiles() {
		return files;
	}

}
