package voxspell.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.ArrayList;
import java.util.Random;


import javax.swing.JButton;


import voxspell.gui.Review;
import voxspell.toolbox.Festival;
import voxspell.toolbox.VoxDatabase;
import voxspell.toolbox.WordsCounter;

public class ReviewModel implements ActionListener{

	private boolean lastAttemptFailed;
	private boolean turnend;
	private int wc = 0;
	private int attempt;
	private int wordsCorrect=0;
	private String word = "You have not start the game, please start the game first";
	private int totalWords;


	protected int exit;

	private ArrayList<String> wordsAlreadyTested= new ArrayList<String>();
	protected ArrayList<File> sysfiles = VoxDatabase.getSysfiles();
	protected ArrayList<ArrayList<String>> contents = VoxDatabase.getContents();
	protected ArrayList<String> filenames = VoxDatabase.getFilenames();
	private ArrayList<String> correspondingLevel = new ArrayList<String>(); 

	private WordsCounter counter = WordsCounter.getWordsCounter();


	public ReviewModel(ArrayList<String> correspondingLevel){
		this.correspondingLevel.addAll(correspondingLevel);
		if (correspondingLevel.size()==0){
			Review.getReviewWindow().getProgressBar().setValue(0);
			Review.getReviewWindow().getProgressBar().setString(0+" / "+0);
			Review.getReviewWindow().getProgressBar().setMaximum(0);
			Review.getReviewWindow().getSubmit().setEnabled(false);
			Review.getReviewWindow().getSubmit().setText("Start!");
			Review.getReviewWindow().getRehear().setEnabled(false);
			Review.getReviewWindow().getHint().setText("Hint: ");
			Review.getReviewWindow().getTextField().setText("You have no failed words currently. Well done!");
			Review.getReviewWindow().getTextField().setEditable(false);
			Review.getReviewWindow().getTextField().grabFocus();
			Review.getReviewWindow().getEndOfLevelPanel().setVisible(false);
			Festival.festivalGenerator("You have no failed words currently. Well done!");
		}else{
			if (correspondingLevel.size()>=10){
				totalWords = 10;
			}else{
				totalWords = correspondingLevel.size();
			}
			Review.getReviewWindow().getProgressBar().setValue(0);
			Review.getReviewWindow().getProgressBar().setString(0+" / "+totalWords);
			Review.getReviewWindow().getProgressBar().setMaximum(totalWords);
			Review.getReviewWindow().getSubmit().setEnabled(true);
			Review.getReviewWindow().getSubmit().setText("Start!");
			Review.getReviewWindow().getRehear().setEnabled(true);
			Review.getReviewWindow().getHint().setText("Hint: ");
			Review.getReviewWindow().getTextField().setText("Press ENTER or click Start! to begin your quiz!");
			Review.getReviewWindow().getTextField().setEditable(true);
			Review.getReviewWindow().getTextField().grabFocus();
			Review.getReviewWindow().getEndOfLevelPanel().setVisible(false);
		}
	}

	public String getWord() {
		return word;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().getSimpleName().equals("JButton")){
			JButton source = (JButton)e.getSource();
			if (source.getText().equals("Start!")){
				Review.getReviewWindow().getTextField().setText("");
				attempt = 0;
				lastAttemptFailed = false;
				turnend = false;
				word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				while (wordsAlreadyTested.contains(word)){
					word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				}
				wordsAlreadyTested.add(word);
				Festival festival = new Festival(Review.getReviewWindow(), lastAttemptFailed);
				festival.festivalGenerator(lastAttemptFailed, this);
				Review.getReviewWindow().getSubmit().setText("Submit!");
				if (VoxDatabase.getDictionary().get(word)!=null){
					Review.getReviewWindow().getHint().setText("Hint: "+VoxDatabase.getDictionary().get(word));
				}else{
					Review.getReviewWindow().getHint().setText("Sorry. Currently no definition for this word.");
				}
				exit = festival.getExitState();
			}else if (source.getText().equals("Submit!")){
				if(exit == 0 ){
					checkWordCorrect();
					Review.getReviewWindow().getTextField().setText("");
					if (turnend){
						if (wc == (totalWords)){
							Review.getReviewWindow().getEndOfLevelPanel().setVisible(true);
							Review.getReviewWindow().getSubmit().setEnabled(false);
							Review.getReviewWindow().getRehear().setEnabled(false);
							Review.getReviewWindow().getBackToMain().setEnabled(true);
						}
					}
				}
			}else if (source.getText().equals("Rehear")){
				Festival festival = new Festival(Review.getReviewWindow(), lastAttemptFailed);
				festival.festivalGenerator(word, this);
			}else if (source.equals(Review.getReviewWindow().getBackToMain())){
				
				Review.getReviewWindow().setVisible(false);
				
			}else if (source.equals(Review.getReviewWindow().get_continue())){
				
				Review.getReviewWindow().updateModel(VoxDatabase.getContents().get(4));;
				Review.getReviewWindow().getEndOfLevelPanel().setVisible(false);
				
			}
		}
	}

	public int getTotalWords() {
		return totalWords;
	}

	public boolean isTurnend() {
		return turnend;
	}

	private void checkWordCorrect(){
		Festival festival = new Festival(Review.getReviewWindow(), lastAttemptFailed);
		if (Review.getReviewWindow().getTextField().getText().toLowerCase().equals(word.toLowerCase())){
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
				Review.getReviewWindow().getProgressBar().setValue(wordsCorrect);
				Review.getReviewWindow().getProgressBar().setString(wordsCorrect+" / "+totalWords);
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
			Review.getReviewWindow().getAccuracy().setText("Rate of correctness so far: "+counter.getAccuracy()+"%");
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
			counter.count(false);
			Review.getReviewWindow().getAccuracy().setText("Rate of correctness so far: "+counter.getAccuracy()+"%");
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
		VoxDatabase.saveChangedFile("records");
	}
}
