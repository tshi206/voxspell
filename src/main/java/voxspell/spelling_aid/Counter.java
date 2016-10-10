package voxspell.spelling_aid;

import java.text.DecimalFormat;

public class Counter {
	private int wordsCorrect;
	private int wordsTotal;

	public Counter(){
		this.wordsCorrect=0;
		this.wordsTotal=0;
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
	
	public String printAccuracy(){
		String number = new DecimalFormat(".##").format(this.calculateAccuracy());
		String accuracy="\n Your Score in current session: "+number+"% \n";
		return accuracy;
	}

}
