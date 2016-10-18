package voxspell.toolbox;

import java.io.IOException;

import javax.swing.SwingWorker;

import voxspell.app.NewGameModel;
import voxspell.app.ReviewModel;
import voxspell.app.VoxModel;
import voxspell.gui.NewGame;
import voxspell.gui.Review;

public class Festival {

	protected ProcessBuilder p = null;
	protected Process process = null;
	private int exit;


	private NewGame newGame;

	private Review review;
	
	private boolean reviewMode;
	
	private VoiceChoice vc = VoiceChoice.getVoiceChoice();

	/**
	 * ONLY USE THIS CONSTRUCTOR WHEN YOU WANT TO INVOKE THE GENERAL PURPOSE GENERATE.
	 * OTHERWISE USE THE OTHER CONSTRUCTOR!
	 */
	public Festival(){};
	
	public Festival(NewGame newGame, boolean lastAttemptFailed){
		this.newGame = newGame;
		reviewMode = false;
	}

	public Festival(Review newGame, boolean lastAttemptFailed){
		this.review = newGame;
		reviewMode = true;
	}

	public void festivalGenerator(boolean lastAttemptFailed, NewGameModel ngm){
		if (lastAttemptFailed){
			String s = "Incorrect, try once more. "+ngm.getWord()+", "+ngm.getWord()+".";
			vc.setWord(s);
			vc.updateSCM();
		}else{
			String s = "Please spell the word "+ngm.getWord();
			vc.setWord(s);
			vc.updateSCM();
		}
		String bashcmd = "festival -b "+ VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.sound.scm";
		p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		Speaker speaker=new Speaker(newGame);
		speaker.execute();
	}

	public void festivalGenerator(boolean lastAttemptFailed, ReviewModel Rmodel){
		if (lastAttemptFailed){
			String s = "Incorrect, try once more. "+Rmodel.getWord()+", "+Rmodel.getWord()+".";
			vc.setWord(s);
			vc.updateSCM();
		}else{
			String s = "Please spell the word "+Rmodel.getWord();
			vc.setWord(s);
			vc.updateSCM();
		}
		String bashcmd = "festival -b "+ VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.sound.scm";
		p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		Speaker speaker=new Speaker(review);
		speaker.execute();
	}
	
	/**
	 * USE ONLY WHEN FESTIVAL OBJECT IS CONSTRUCTED USING NEWGAME OBJECT
	 * @param s
	 * @param ngm
	 */
	public void festivalGenerator(String s, NewGameModel ngm){
		vc.setWord(s);
		vc.updateSCM();
		String bashcmd= "festival -b "+ VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.sound.scm";
		p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		Speaker speaker=new Speaker(newGame);
		speaker.execute();
	}
	
	/**
	 * USE ONLY WHEN FESTIVAL OBJECT IS CONSTRUCTED USING REVIEW OBJECT
	 * @param s
	 * @param Rmodel
	 */
	public void festivalGenerator(String s, ReviewModel Rmodel){
		vc.setWord(s);
		vc.updateSCM();
		String bashcmd= "festival -b "+ VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.sound.scm";
		p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		Speaker speaker=new Speaker(review);
		speaker.execute();
	}

	/**
	 * GENERAL PURPOSE GENERATOR. DO NOT USE IN NEWGAME OR REVIEW MODE.
	 * @param s
	 */
	public static void festivalGenerator(String s){
		
		VoiceChoice.getVoiceChoice().setWord(s);
		VoiceChoice.getVoiceChoice().updateSCM();
		
		GeneralFestivalWorker gfw = new GeneralFestivalWorker();
		gfw.execute();
	}

	public int getExitState() {
		return exit;
	}

	
	class Speaker extends SwingWorker<Void, Void>{

		private NewGame newSpellingQuiz = null;

		private Review review = null;

		public Speaker(NewGame newSpellingQuiz){
			this.newSpellingQuiz=newSpellingQuiz;
		}

		public Speaker(Review review){
			this.review = review;
		}

		@Override
		protected Void doInBackground() throws Exception {
			if (!(reviewMode)){
				newSpellingQuiz.getSubmit().setEnabled(false);
				newSpellingQuiz.getBackToMain().setEnabled(false);
				newSpellingQuiz.getRehear().setEnabled(false);
				process=p.start();
				exit=process.waitFor();
			}else{
				review.getSubmit().setEnabled(false);
				review.getBackToMain().setEnabled(false);
				review.getRehear().setEnabled(false);
				process=p.start();
				exit=process.waitFor();
			}

			return null;
		}

		@Override
		protected void done(){
			if (!(reviewMode)){
				newSpellingQuiz.getSubmit().setEnabled(true);
				newSpellingQuiz.getBackToMain().setEnabled(true);
				newSpellingQuiz.getRehear().setEnabled(true);
				if (newSpellingQuiz.getModel().isTurnend()){
					if (newSpellingQuiz.getModel().getWc()<11){
						newSpellingQuiz.getSubmit().setText("Start!");
						newSpellingQuiz.getSubmit().doClick();
					}
				}
			}else{
				review.getSubmit().setEnabled(true);
				review.getBackToMain().setEnabled(true);
				review.getRehear().setEnabled(true);
				if (review.getModel().isTurnend()){
					if (review.getModel().getWc()<review.getModel().getTotalWords()){
						review.getSubmit().setText("Start!");
						review.getSubmit().doClick();
					}
				}
			}
		}
	}
}


class GeneralFestivalWorker extends SwingWorker<Void, Void>{

	@Override
	protected Void doInBackground() throws Exception {
		
		String bashcmd= "festival -b "+ VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.sound.scm";
		ProcessBuilder p = new ProcessBuilder("/bin/bash","-c", bashcmd);

		try {
			Process process = p.start();
			@SuppressWarnings("unused")
			int exit=process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
