package voxspell.toolbox;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import voxspell.gui.Settings;


/**
 * This background thread is managed by Settings class.
 * It will load words from given files and match them with corresponding definitions if it finds any existing ones.
 * It will also make a local copy of the importing file by simply rename the copy with a "." prefix and all further operation will be carried out only on this local copy.
 * This make sure the original file will not be affected by edition or deletion from the system.
 * All loading will be stored in the corresponding data structures in VoxDatabase.
 * When it finishes, it will inform Settings class to start the ImportDefinitionsWorker thread in case if the user imports both definitions files and categories files at the same time.
 * @author mason23
 *
 */
public class ImportListWorker extends SwingWorker<Void, Void> {
	
	private String DONE_PROPERTY = "notDone";

	private ArrayList<File> filesToWriteTo;
	private ArrayList<File> filesToReadFrom;

	public ImportListWorker(ArrayList<File> filesToWriteTo, ArrayList<File> filesToReadFrom){
		addPropertyChangeListener((PropertyChangeListener)Settings.getSettingsWindow());
		this.filesToWriteTo = filesToWriteTo;
		this.filesToReadFrom = filesToReadFrom;
	}
	
	private void writeToCorrespondingWordsFile(ArrayList<File> filesToWriteTo, ArrayList<File> filesToReadFrom){
		
		for (int i = 0 ; i<filesToWriteTo.size(); i++){
			
			try {
				Scanner scanner = new Scanner(new FileReader(filesToReadFrom.get(i)));
				FileWriter fw = new FileWriter(filesToWriteTo.get(i), true);
				
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					fw.write(line+"\n");
				}
				
				scanner.close();
				fw.close();
				
				VoxDatabase.writeToSysFile("customizedLists", filesToWriteTo.get(i).getName()+"\n", true);
				loadCustomizedWordsFile(filesToWriteTo.get(i));
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	private void loadCustomizedWordsFile(File fileToReadFrom){
		try {

			ArrayList<String> temp = new ArrayList<String>();

			ArrayList<ArrayList<String>> customizedCategoriesContents = new ArrayList<ArrayList<String>>();

			String categoryName = "";
			Scanner s1 = new Scanner(new FileReader(fileToReadFrom));
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
							
							VoxDatabase.addCategory(temp, fileToReadFrom.getName().substring(1));
							
							customizedCategoriesContents.add(temp);
							temp = new ArrayList<String>();
							categoryName = line1.substring(1);
							continue;
						}
					}else{
						if ((!(temp.isEmpty()))&&(temp.size()>=10)){

							VoxDatabase.addCategory(temp, categoryName);
							
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
					
					VoxDatabase.addCategory(temp, fileToReadFrom.getName().substring(1));
					
				}else{
					
					VoxDatabase.addCategory(temp, categoryName);
					
				}
				
				customizedCategoriesContents.add(temp);
			}

			s1.close();

			if (!(customizedCategoriesContents.isEmpty())){

				File tempfile = new File(VoxDatabase.wordlistsDirectory+fileToReadFrom.getName().substring(0,fileToReadFrom.getName().length()-".txt".length())+"_def.txt");
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

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		writeToCorrespondingWordsFile(filesToWriteTo, filesToReadFrom);
		return null;
	}

	@Override
	protected void done(){
		DONE_PROPERTY = "done";
		JOptionPane.showMessageDialog(Settings.getSettingsWindow(), "Finish importing words file, please wait for definitions files loading process (if any).\n"
				+ "Otherwise some definitions might be missing during the time of processing.", "Operation complete", JOptionPane.INFORMATION_MESSAGE);
		firePropertyChange(DONE_PROPERTY, "notDone", "done");
	}
}
