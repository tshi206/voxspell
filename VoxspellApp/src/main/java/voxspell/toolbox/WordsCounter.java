package voxspell.toolbox;

import java.text.DecimalFormat;

/**
 * This class records the number of successful spellings (mastered times) from the time when system starts up to it is closed.
 * It generates the accuracy of spelling based on its records.
 * It is managed by NewGameModel and ReviewModel, which are responsible for suppling the changes to this class.
 * @author mason23
 *
 */
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
	
	/**
	 * Counts the word if supplied flag is correct.
	 * This will update the records of accuracy.
	 * @param Correct
	 */
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
	
	/**
	 * Returns the accuracy of spelling.
	 * @return
	 */
	public String getAccuracy(){
		String accuracy = new DecimalFormat(".##").format(this.calculateAccuracy());
		return accuracy;
	}

}
