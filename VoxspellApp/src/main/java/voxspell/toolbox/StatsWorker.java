package voxspell.toolbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.SwingWorker;

import voxspell.app.Words;
import voxspell.gui.Stats;


/**
 * This background thread is managed by Stats.
 * It uses the abstract data structures of Words to calculate the results and populating then into specific data structure for the use of JTable.
 * When it finishes, it will inform the Stats object to update its JTable using the resulting data structure.
 * @author mason23
 *
 */
public class StatsWorker extends SwingWorker<Void,Void> {


	protected ArrayList<ArrayList<String>> contents;

	protected ArrayList<String> wordlistOfTheLevel;
	
	private Vector<Vector<String>> rowResult = new Vector<Vector<String>>();
	
	private Vector<String> columnNames = new Vector<String>();
	
	private ArrayList<Words> hist;
	
	private boolean isShowAll = false;
	
	/**
	 * THIS CONSTRUCTOR IS USED FOR SHOWALL CHECKBOX ONLY
	 * @param contents
	 */
	public StatsWorker(ArrayList<ArrayList<String>> contents){
		
		this.contents = contents;
		isShowAll = true;
		columnNames.add("Words");
		columnNames.add("Mastered times");
		columnNames.add("Faulted times");
		columnNames.add("Failed times");
	}
	
	
	/**
	 * THIS CONSTRUCTOR IS USED FOR EACH CATEGORY ONLY
	 * @param contents
	 */
	public StatsWorker(ArrayList<ArrayList<String>> contents,
			ArrayList<String> wordlistOfTheLevel){
		
		this.contents = contents;
		this.wordlistOfTheLevel = wordlistOfTheLevel;
		columnNames.add("Words");
		columnNames.add("Mastered times");
		columnNames.add("Faulted times");
		columnNames.add("Failed times");
	}
	
	@Override
	protected Void doInBackground() throws Exception { 
		logGenerator();
		
		for (Words word : hist){
			Vector<String> row = new Vector<String>();
			row.add(word.name);
			row.add(word.mastered+"");
			row.add(word.faulted+"");
			row.add(word.failed+"");
			rowResult.add(row);
		}
		
		return null;
	}
	
	@Override
	protected void done(){
		
		Stats.getStatsWindow().updateTable(rowResult, columnNames);
		
	}
	
	private void logGenerator(){
		
		ArrayList<String> temp = new ArrayList<String>();
		for (int i=3; i<6; i++){
			temp.addAll(contents.get(i));
		}
		
		hist = new ArrayList<Words>();


		if (isShowAll){
			for (String str : temp){


				int mastered=0, faulted=0, failed=0;

				for (String t : contents.get(6)){
					if (str.equals(t)){
						mastered++;
					}
				}
				for (String t : contents.get(7)){
					if (str.equals(t)){
						faulted++;
					}
				}
				for (String t : contents.get(8)){
					if (str.equals(t)){
						failed++;
					}
				}
				hist.add(new Words(mastered, faulted, failed, str));

				
			}
			Collections.sort(hist);
			return;
		}


		for (String str : temp){
			if (wordlistOfTheLevel.contains(str)){

				int mastered=0, faulted=0, failed=0;
				
				for (String t : contents.get(6)){
					if (str.equals(t)){
						mastered++;
					}
				}
				for (String t : contents.get(7)){
					if (str.equals(t)){
						faulted++;
					}
				}
				for (String t : contents.get(8)){
					if (str.equals(t)){
						failed++;
					}
				}
				hist.add(new Words(mastered, faulted, failed, str));

				
			}
		}
		Collections.sort(hist);
	}
	
}
