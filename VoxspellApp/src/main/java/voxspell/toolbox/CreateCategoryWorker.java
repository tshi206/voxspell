package voxspell.toolbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


public class CreateCategoryWorker extends SwingWorker<Void, Void> {

	private File fileToReadFrom;

	public CreateCategoryWorker(File fileToReadFrom){
		this.fileToReadFrom = fileToReadFrom;
	}

	@Override
	protected Void doInBackground() throws Exception {
		loadCustomizedWordsFile(fileToReadFrom);
		return null;
	}

	public void loadCustomizedWordsFile(File fileToReadFrom){
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
						if (temp.isEmpty()){
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
						if (!(temp.isEmpty())){

							VoxDatabase.addCategory(temp, fileToReadFrom.getName().substring(1));
							
							customizedCategoriesContents.add(temp);
						}
						temp = new ArrayList<String>();
						categoryName = line1.substring(1);
						continue;
					}
				}
				temp.add(line1);
			}

			if (!(temp.isEmpty())){
				if (categoryName.equals("")){
					VoxDatabase.categories.add(fileToReadFrom.getName().substring(1));
				}else{
					VoxDatabase.categories.add(categoryName);
				}

				VoxDatabase.addCategory(temp, fileToReadFrom.getName().substring(1));
				
				customizedCategoriesContents.add(temp);
			}

			s1.close();

			if (!(customizedCategoriesContents.isEmpty())){

				File tempfile = new File(VoxDatabase.wordlistsDirectory+fileToReadFrom.getName()+"_def");
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
	protected void done(){
		JOptionPane.showMessageDialog(null, "Finish creating category", "Operation complete", JOptionPane.INFORMATION_MESSAGE);
	}
}
