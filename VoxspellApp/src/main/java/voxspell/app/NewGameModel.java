package voxspell.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.ArrayList;
import java.util.Random;


import javax.swing.JButton;

import voxspell.gui.EndOfCategory;
import voxspell.gui.NewGame;
import voxspell.gui.Settings;
import voxspell.toolbox.Festival;
import voxspell.toolbox.VoxDatabase;
import voxspell.toolbox.WordsCounter;


/**
 * NewGameModel is responsible for the gaming logic of Quiz Mode.
 * It is corresponding to NewGame which is a GUI representation of this model.
 * This class is a ActionListener of NewGame.
 * @author mason23
 *
 */
public class NewGameModel implements ActionListener{

	private boolean lastAttemptFailed;
	private boolean turnend;
	private int wc = 1;
	private int attempt;
	private int wordsCorrect=0;
	private int wordsFailed=0;
	private String word = "You have not start the game, please start the game first";

	private EndOfCategory eofc = null;

	protected int exit;

	private ArrayList<String> wordsAlreadyTested= new ArrayList<String>();
	protected ArrayList<File> sysfiles = VoxDatabase.getSysfiles();
	protected ArrayList<ArrayList<String>> contents = VoxDatabase.getContents();
	protected ArrayList<String> filenames = VoxDatabase.getFilenames();
	private ArrayList<String> correspondingLevel; 

	private WordsCounter counter = WordsCounter.getWordsCounter();


	/**
	 * Reset the NewGame GUI environment whenever this constructor is called.
	 * A new instance should be linked to NewGame whenever a new Quiz Mode session starts.
	 * @param correspondingLevel
	 */
	public NewGameModel(ArrayList<String> correspondingLevel){
		this.correspondingLevel=correspondingLevel; // The category's content for a quiz session
		NewGame.getNewGameWindow().getProgressBar().setValue(0);
		NewGame.getNewGameWindow().getProgressBar().setString(0+" / 10");
		NewGame.getNewGameWindow().getCurrentCategory().setText("Current category: "+VoxDatabase.getCategories().get(VoxDatabase.getLevelContents().indexOf(correspondingLevel)));
		NewGame.getNewGameWindow().getSubmit().setEnabled(true);
		NewGame.getNewGameWindow().getSubmit().setText("Start!");
		NewGame.getNewGameWindow().getRehear().setEnabled(true);
		NewGame.getNewGameWindow().getEndOfLevelPanel().setVisible(false);
		NewGame.getNewGameWindow().getVideoReward().setEnabled(true);
		NewGame.getNewGameWindow().getNextCategory().setEnabled(true);
		NewGame.getNewGameWindow().getHint().setText("Hint: ");
		NewGame.getNewGameWindow().getTextField().setText("Press ENTER or click Start! to begin your quiz!");
		NewGame.getNewGameWindow().getTextField().grabFocus();
	}

	/**
	 * Return the word of a quiz in order to let Festival speaks.
	 * @return word;
	 */
	public String getWord() {
		return word;
	}

	
	/**
	 * Actions when a spelling is submitted in NewGame.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().getSimpleName().equals("JButton")){
			JButton source = (JButton)e.getSource();
			if (source.getText().equals("Start!")){ // Start a quiz
				NewGame.getNewGameWindow().getTextField().setText("");
				attempt = 0;
				lastAttemptFailed = false;
				turnend = false;
				word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				while (wordsAlreadyTested.contains(word)){ // make sure no repeated word being asked again
					word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				}
				wordsAlreadyTested.add(word);
				Festival festival = new Festival(NewGame.getNewGameWindow(), lastAttemptFailed);
				festival.festivalGenerator(lastAttemptFailed, this);
				NewGame.getNewGameWindow().getSubmit().setText("Submit!");
				if (VoxDatabase.getDictionary().get(word)!=null){ // gather the definition corresponding to the word
					NewGame.getNewGameWindow().getHint().setText("Hint: "+VoxDatabase.getDictionary().get(word));
				}else{
					NewGame.getNewGameWindow().getHint().setText("Sorry. Currently no definition for this word.");
				}
				exit = festival.getExitState();
			}else if (source.getText().equals("Submit!")){
				if(exit == 0 ){
					checkWordCorrect();
					NewGame.getNewGameWindow().getTextField().setText("");
					if (turnend){
						if (wc == 11){ // session ends when the number of words being asked is greater than 10
							eofc = VoxModel.createEndOfCategoryWindow(wordsCorrect, wordsFailed); // end of category window generated.
							NewGame.getNewGameWindow().getSubmit().setEnabled(false);
							NewGame.getNewGameWindow().getRehear().setEnabled(false);
						}
					}
				}
			}else if (source.getText().equals("Rehear")){ //rehear the word
				Festival festival = new Festival(NewGame.getNewGameWindow(), lastAttemptFailed);
				festival.festivalGenerator(word, this);
			}else if (source.equals(NewGame.getNewGameWindow().getBackToMain())){ // back to main menu
				
				if (eofc!=null){ // also close end of category window if exists
					eofc.dispose();
				}
				
				Settings.getSettingsWindow().getCategory().setSelectedIndex(VoxDatabase.getLevelContents().indexOf(correspondingLevel));
				
			}else if (source.equals(NewGame.getNewGameWindow().getRetryCategory())){ // retry - reallocate a new model for the GUI using the same category
				
				Settings.getSettingsWindow().getCategory().setSelectedIndex(VoxDatabase.getLevelContents().indexOf(correspondingLevel));
				
			}else if (source.equals(NewGame.getNewGameWindow().getNextCategory())){ // next category - allocate a new model for the GUI using the next category, this will fail if no more category remained
				
				if ((VoxDatabase.getLevelContents().indexOf(correspondingLevel)+1)==VoxDatabase.getLevelContents().size()){
					NewGame.getNewGameWindow().getNextCategory().setText("Last Category");
					NewGame.getNewGameWindow().getNextCategory().setEnabled(false);
				}else{
					Settings.getSettingsWindow().getCategory().setSelectedIndex(VoxDatabase.getLevelContents().indexOf(correspondingLevel)+1);
				}
				
			}
		}
	}

	
	/**
	 * report whether a quiz is finished
	 * @return boolean - true is a quiz is finished otherwise false
	 */
	public boolean isTurnend() {
		return turnend;
	}

	/**
	 * Check whether a submission of a quiz is correct or not.
	 * Different voices will be generated by Festival depending on the correctness.
	 * Words will be logged in corresponding system's files depending on the result.
	 */
	private void checkWordCorrect(){
		Festival festival = new Festival(NewGame.getNewGameWindow(), lastAttemptFailed);
		if (NewGame.getNewGameWindow().getTextField().getText().toLowerCase().equals(word.toLowerCase())){
			turnend=true;
			wc++;
			festival.festivalGenerator("correct", this);
			if (!(lastAttemptFailed)){	
				if (contents.get(filenames.indexOf("mastered")).size()==0){
					contents.get(filenames.indexOf("mastered")).add(word);
				}else if (!(contents.get(filenames.indexOf("mastered")).contains(word))){
					contents.get(filenames.indexOf("mastered")).add(word);
				}
				if (contents.get(filenames.indexOf("faulted")).contains(word)){
					contents.get(filenames.indexOf("faulted")).remove(word);
				}
				if (contents.get(filenames.indexOf("failed")).contains(word)){
					contents.get(filenames.indexOf("failed")).remove(word);
				}
				contents.get(filenames.indexOf("masteredhistory")).add(word);
				contents.get(filenames.indexOf("records")).add(word);
				savePoint();
				wordsCorrect++;
				counter.count(true);
				NewGame.getNewGameWindow().getProgressBar().setValue(wordsCorrect);
				NewGame.getNewGameWindow().getProgressBar().setString(wordsCorrect+" / 10");
			}else if (lastAttemptFailed){
				if (contents.get(filenames.indexOf("faulted")).size()==0){
					contents.get(filenames.indexOf("faulted")).add(word);
				}else if (!(contents.get(filenames.indexOf("faulted")).contains(word))){
					contents.get(filenames.indexOf("faulted")).add(word);
				}
				if (contents.get(filenames.indexOf("mastered")).contains(word)){
					contents.get(filenames.indexOf("mastered")).remove(word);
				}
				if (contents.get(filenames.indexOf("failed")).contains(word)){
					contents.get(filenames.indexOf("failed")).remove(word);
				}
				contents.get(filenames.indexOf("faultedhistory")).add(word);
				savePoint();
				counter.count(false);
			}
			NewGame.getNewGameWindow().getAccuracy().setText("Rate of correctness so far: "+counter.getAccuracy()+"%");
		}else{
			if (attempt==1){
				wc++;
				turnend=true;
				festival.festivalGenerator("incorrect", this);
				if (contents.get(filenames.indexOf("failed")).size()==0){
					contents.get(filenames.indexOf("failed")).add(word);
				}else if (!(contents.get(filenames.indexOf("failed")).contains(word))){
					contents.get(filenames.indexOf("failed")).add(word);
				}
				if (contents.get(filenames.indexOf("mastered")).contains(word)){
					contents.get(filenames.indexOf("mastered")).remove(word);
				}
				if (contents.get(filenames.indexOf("faulted")).contains(word)){
					contents.get(filenames.indexOf("faulted")).remove(word);
				}
				contents.get(filenames.indexOf("failedhistory")).add(word);
				savePoint();
			}else if (!(lastAttemptFailed)){
				lastAttemptFailed = true;
				festival.festivalGenerator(lastAttemptFailed, this);
				attempt++;
				return;
			}
			wordsFailed++;
			counter.count(false);
			NewGame.getNewGameWindow().getAccuracy().setText("Rate of correctness so far: "+counter.getAccuracy()+"%");
		}
	}
	
	/**
	 * Return the number of quizzes being asked.
	 * @return words count - total number of quizzes being asked so far in a session
	 */
	public int getWc() {
		return wc;
	}

	/**
	 * Save changes in the system's file.
	 * Logging words in corresponding files depending on the result of correctness check.
	 */
	private void savePoint(){
		VoxDatabase.saveChangedFile("mastered");
		VoxDatabase.saveChangedFile("faulted");
		VoxDatabase.saveChangedFile("failed");
		VoxDatabase.saveChangedFile("masteredhistory");
		VoxDatabase.saveChangedFile("faultedhistory");
		VoxDatabase.saveChangedFile("failedhistory");
		VoxDatabase.saveChangedFile("records");
	}
}
