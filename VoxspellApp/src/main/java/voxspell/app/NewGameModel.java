package voxspell.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.ArrayList;
import java.util.Random;


import javax.swing.JButton;


import voxspell.gui.NewGame;
import voxspell.gui.Settings;
import voxspell.toolbox.Festival;
import voxspell.toolbox.VoxDatabase;
import voxspell.toolbox.WordsCounter;

public class NewGameModel implements ActionListener{

	private boolean lastAttemptFailed;
	private boolean turnend;
	private int wc = 1;
	private int attempt;
	private int wordsCorrect=0;
	private int wordsFailed=0;
	private String word = "You have not start the game, please start the game first";


	protected int exit;

	private ArrayList<String> wordsAlreadyTested= new ArrayList<String>();
	protected ArrayList<File> sysfiles = VoxDatabase.getSysfiles();
	protected ArrayList<ArrayList<String>> contents = VoxDatabase.getContents();
	protected ArrayList<String> filenames = VoxDatabase.getFilenames();
	private ArrayList<String> correspondingLevel; 

	private WordsCounter counter = WordsCounter.getWordsCounter();


	public NewGameModel(ArrayList<String> correspondingLevel){
		this.correspondingLevel=correspondingLevel;
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

	public String getWord() {
		return word;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().getSimpleName().equals("JButton")){
			JButton source = (JButton)e.getSource();
			if (source.getText().equals("Start!")){
				NewGame.getNewGameWindow().getTextField().setText("");
				attempt = 0;
				lastAttemptFailed = false;
				turnend = false;
				word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				while (wordsAlreadyTested.contains(word)){
					word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				}
				wordsAlreadyTested.add(word);
				Festival festival = new Festival(NewGame.getNewGameWindow(), lastAttemptFailed);
				festival.festivalGenerator(lastAttemptFailed, this);
				NewGame.getNewGameWindow().getSubmit().setText("Submit!");
				if (VoxDatabase.getDictionary().get(word)!=null){
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
						if (wc == 11){
							VoxModel.createEndOfCategoryWindow(wordsCorrect, wordsFailed);
							NewGame.getNewGameWindow().getSubmit().setEnabled(false);
							NewGame.getNewGameWindow().getRehear().setEnabled(false);
						}
					}
				}
			}else if (source.getText().equals("Rehear")){
				Festival festival = new Festival(NewGame.getNewGameWindow(), lastAttemptFailed);
				festival.festivalGenerator(word, this);
			}else if (source.equals(NewGame.getNewGameWindow().getBackToMain())){
				
				Settings.getSettingsWindow().getCategory().setSelectedIndex(VoxDatabase.getLevelContents().indexOf(correspondingLevel));
				
			}else if (source.equals(NewGame.getNewGameWindow().getRetryCategory())){
				
				Settings.getSettingsWindow().getCategory().setSelectedIndex(VoxDatabase.getLevelContents().indexOf(correspondingLevel));
				
			}else if (source.equals(NewGame.getNewGameWindow().getNextCategory())){
				
				if ((VoxDatabase.getLevelContents().indexOf(correspondingLevel)+1)==VoxDatabase.getLevelContents().size()){
					NewGame.getNewGameWindow().getNextCategory().setText("Last Category");
					NewGame.getNewGameWindow().getNextCategory().setEnabled(false);
				}else{
					Settings.getSettingsWindow().getCategory().setSelectedIndex(VoxDatabase.getLevelContents().indexOf(correspondingLevel)+1);
				}
				
			}
		}
	}

	public boolean isTurnend() {
		return turnend;
	}

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
	
	public int getWc() {
		return wc;
	}

	private void savePoint(){
		VoxDatabase.saveChangedFile("mastered");
		VoxDatabase.saveChangedFile("faulted");
		VoxDatabase.saveChangedFile("failed");
		VoxDatabase.saveChangedFile("masteredhistory");
		VoxDatabase.saveChangedFile("faultedhistory");
		VoxDatabase.saveChangedFile("failedhistory");
	}
}
