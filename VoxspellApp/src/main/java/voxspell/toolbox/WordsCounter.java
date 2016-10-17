package voxspell.toolbox;

import java.text.DecimalFormat;

public class WordsCounter {
	
	private static WordsCounter counter = null;
	
	private int wordsCorrect;
	private int wordsTotal;

	private WordsCounter(){
		this.wordsCorrect=0;
		this.wordsTotal=0;
	}

	public static WordsCounter getWordsCounter(){
		if (counter == null){
			counter = new WordsCounter();
		}
		return counter;
	}
	
	public void count(boolean Correct){
		if(Correct){
			wordsCorrect=wordsCorrect+1;
		}
		wordsTotal=wordsTotal+1;
	}

	private double calculateAccuracy(){
		if(wordsTotal==0){
			return 100;
		}
		double accuracyPercentage=((double)wordsCorrect/wordsTotal)*100;
		return accuracyPercentage;
	}
	
	public String getAccuracy(){
		String accuracy = new DecimalFormat(".##").format(this.calculateAccuracy());
		return accuracy;
	}

}
