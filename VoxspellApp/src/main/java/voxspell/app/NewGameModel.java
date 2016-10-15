package voxspell.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import voxspell.gui.NewGame;
import voxspell.toolbox.Festival;
import voxspell.toolbox.VoiceChoice;
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

	private WordsCounter counter;
	private VoiceChoice vc;
	

	public NewGameModel(ArrayList<String> correspondingLevel, VoiceChoice vc, WordsCounter counter){
		//TODO
		this.vc=vc;
		this.counter=counter;
		this.correspondingLevel=correspondingLevel;
	}

	public String getWord() {
		return word;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().getSimpleName().equals("JButton")){
			JButton source = (JButton)e.getSource();
			if (source.getText().equals("Start!")){
				attempt = 0;
				lastAttemptFailed = false;
				turnend = false;
				word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				while (wordsAlreadyTested.contains(word)){
					word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				}
				wordsAlreadyTested.add(word);
				Festival festival = new Festival(this, NewGame.getNewGameWindow(), lastAttemptFailed);
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
					if (turnend){
						if (wc==11){
							if(wordsCorrect>8){
								@SuppressWarnings("unused")
								FinishWindows finishwindows = new FinishWindows(frame, 
										sysfiles, contents, filenames, correspondingLevel, vc, counter, true,wl);
								itself.dispose();
							}else{
								@SuppressWarnings("unused")
								FinishWindows finishwindows = new FinishWindows(frame, 
										sysfiles, contents, filenames, correspondingLevel, vc, counter, false,wl);
								itself.dispose();
							}
						}else{
							source.setText("Start!");
							source.doClick();
						}
					}
				}
			}else if(source.getText().equals("Rehear")){
				Festival festival = new Festival(this, NewGame.getNewGameWindow(), lastAttemptFailed);
				festival.festivalGenerator(word, this);
			}
		}
	}

	private void checkWordCorrect(){
		Festival festival = new Festival(this, NewGame.getNewGameWindow(), lastAttemptFailed);
		if (NewGame.getNewGameWindow().getTextField().getText().toLowerCase().equals(word.toLowerCase())){
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
			turnend=true;
			NewGame.getNewGameWindow().getAccuracy().setText("Rate of correctness so far: "+counter.getAccuracy()+"%");
			wc++;
		}else{
			if (attempt==1){
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
				wc++;
				turnend=true;
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
	
	private void savePoint(){
		VoxDatabase.saveChangedFile("mastered");
		VoxDatabase.saveChangedFile("faulted");
		VoxDatabase.saveChangedFile("failed");
		VoxDatabase.saveChangedFile("masteredhistory");
		VoxDatabase.saveChangedFile("faultedhistory");
		VoxDatabase.saveChangedFile("failedhistory");
	}
}
