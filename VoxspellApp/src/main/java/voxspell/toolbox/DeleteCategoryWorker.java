package voxspell.toolbox;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import voxspell.gui.DeleteCategory;
import voxspell.gui.Settings;


/**
 * This is a background thread handles the actual implementation of deleting a customized category.
 * It uses a bunch of VoxDatabase file I/O methods to support its implementation.
 * Its purpose is to delete the corresponding categories file and delete the contents in the abstract data structures of categories in the application as well.
 * @author mason23
 *
 */
public class DeleteCategoryWorker extends SwingWorker<Void, Void> {
	
	private String categoryName;
	
	private DeleteCategory itself;
	
	public DeleteCategoryWorker(DeleteCategory itself, String categoryName){
		this.categoryName = categoryName;
		this.itself = itself;
	}

	
	@Override
	protected Void doInBackground() throws Exception {
		Scanner scanner = new Scanner(new FileReader(VoxDatabase.getSysfiles().get(VoxDatabase.getSysfiles().size()-1)));
		ArrayList<String> customizedCategories = new ArrayList<String>();
  		while (scanner.hasNext()){
  			String line = scanner.nextLine();
  			customizedCategories.add(line);
		}
  		scanner.close();
  		
  		if (!(customizedCategories.isEmpty())){
  			if (customizedCategories.contains("."+categoryName)){
  				File temp = new File(VoxDatabase.wordlistsDirectory+"."+categoryName);
  				if (temp.exists()){
  					ArrayList<String> deleteCandidates = new ArrayList<String>();
  					scanner = new Scanner(new FileReader(temp));
  					while (scanner.hasNext()){
  						String line1 = scanner.nextLine();
  						if (line1.startsWith("%")){
  							String cateforyFound = line1.substring(1);
  							deleteCandidates.add(cateforyFound);
  						}
  					}
  					if (!(deleteCandidates.isEmpty())){
  						for (String candidates : deleteCandidates){
  							if (candidates.equals(categoryName)){
  								continue;
  							}
  							VoxDatabase.deleteCategory(VoxDatabase.getLevelContents().get(VoxDatabase.getCategories().indexOf(candidates)), candidates);
  							itself.getComboBox().removeItem(candidates);
  						}
  					}
  					temp.delete();
  					if (VoxDatabase.getCategories().contains(temp.getName().substring(1))){
							VoxDatabase.deleteCategory(VoxDatabase.getLevelContents().get(VoxDatabase.getCategories().indexOf(temp.getName().substring(1))), temp.getName().substring(1));
							itself.getComboBox().removeItem(temp.getName().substring(1));
					}
  					customizedCategories.remove(temp.getName());
  					String newContent = "";
  					for (String s : customizedCategories){
  						newContent = newContent + s + "\n";
  					}
  					FileWriter fw = new FileWriter(VoxDatabase.getSysfiles().get(VoxDatabase.getSysfiles().size()-1), false);
  					fw.write(newContent);
  					fw.close();
  				}
  			}else{
  				boolean isFileFound = false;
  				for (String categoryFileName : customizedCategories){
  					File temp = new File(VoxDatabase.wordlistsDirectory+categoryFileName);
  					if (temp.exists()){
  						ArrayList<String> deleteCandidates = new ArrayList<String>();
  						scanner = new Scanner(new FileReader(temp));
  						while (scanner.hasNext()){
  	  						String line1 = scanner.nextLine();
  	  						if (line1.startsWith("%")){
  	  							String cateforyFound = line1.substring(1);
  	  							deleteCandidates.add(cateforyFound);
  	  							if (cateforyFound.equals(categoryName)){
  	  								isFileFound = true;
  	  							}
  	  						}
  	  					}
  						if (isFileFound){
  							for (String candidates : deleteCandidates){
  	  							if (candidates.equals(categoryName)){
  	  								continue;
  	  							}
  	  							VoxDatabase.deleteCategory(VoxDatabase.getLevelContents().get(VoxDatabase.getCategories().indexOf(candidates)), candidates);
  	  							itself.getComboBox().removeItem(candidates);
  	  						}
  							temp.delete();
  							if (VoxDatabase.getCategories().contains(temp.getName().substring(1))){
  								VoxDatabase.deleteCategory(VoxDatabase.getLevelContents().get(VoxDatabase.getCategories().indexOf(temp.getName().substring(1))), temp.getName().substring(1));
  								itself.getComboBox().removeItem(temp.getName().substring(1));
  							}
  							customizedCategories.remove(temp.getName());
  		  					String newContent = "";
  		  					for (String s : customizedCategories){
  		  						newContent = newContent + s + "\n";
  		  					}
  		  					FileWriter fw = new FileWriter(VoxDatabase.getSysfiles().get(VoxDatabase.getSysfiles().size()-1), false);
  		  					fw.write(newContent);
  		  					fw.close();
  						}
  					}
  				}
  			}
  		}
  		
		return null;
	}

	
	@Override
	protected void done(){
		JOptionPane.showMessageDialog(Settings.getSettingsWindow(), "Deletion completed.", "Operation complete", JOptionPane.INFORMATION_MESSAGE);
	}
}
