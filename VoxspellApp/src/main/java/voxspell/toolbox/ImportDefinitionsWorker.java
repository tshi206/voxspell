package voxspell.toolbox;


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



public class ImportDefinitionsWorker extends SwingWorker<Void, Void> {

	
	private ArrayList<File> filesToWriteTo;
	private ArrayList<File> filesToReadFrom;

	public ImportDefinitionsWorker(ArrayList<File> filesToWriteTo, ArrayList<File> filesToReadFrom){
		this.filesToWriteTo = filesToWriteTo;
		this.filesToReadFrom = filesToReadFrom;
	}
	
	private void writeToCorrespondingWordsFile(ArrayList<File> filesToWriteTo, ArrayList<File> filesToReadFrom){
		
		for (int i = 0 ; i<filesToWriteTo.size(); i++){
			
			try {
				Scanner scanner = new Scanner(new FileReader(filesToReadFrom.get(i)));
				FileWriter fw = new FileWriter(filesToWriteTo.get(i), false);
				
				String newContent = "";
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					newContent = newContent + line + "\n";
				}
				
				fw.write(newContent);
				scanner.close();
				fw.close();
				
				loadDefinitionsFile(filesToWriteTo.get(i));
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	private void loadDefinitionsFile(File fileToReadFrom){
		
		File tempfile = new File(VoxDatabase.wordlistsDirectory+fileToReadFrom.getName().substring(0, fileToReadFrom.getName().length()-"_def.txt".length())+".txt");
		if (tempfile.exists()){
			
			ArrayList<String> tempContents = new ArrayList<String>();
			
			try {
				Scanner s1 = new Scanner(new FileReader(tempfile));
				while (s1.hasNext()){
					String line1 = s1.nextLine();
					if (line1.equals("")){
						continue;
					}
					if (line1.startsWith("%")){
						continue;
					}
					tempContents.add(line1);
				}
				s1.close();

				Scanner s2 = new Scanner(new FileReader(fileToReadFrom));
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
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	@Override
	protected Void doInBackground() throws Exception {
		writeToCorrespondingWordsFile(filesToWriteTo, filesToReadFrom);
		return null;
	}

	@Override
	protected void done(){
		JOptionPane.showMessageDialog(Settings.getSettingsWindow(), "Definitions loading completed.", "Operation complete", JOptionPane.INFORMATION_MESSAGE);
		filesToReadFrom = null;
	}
}
