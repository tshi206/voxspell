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


	private NewGameModel NGmodel = null;
	private NewGame newGame = null;

	private ReviewModel Rmodel = null;
	private Review review = null;
	
	private VoiceChoice vc = VoiceChoice.getVoiceChoice();

	/**
	 * ONLY USE THIS CONSTRUCTOR WHEN YOU WANT TO INVOKE THE GENERAL PURPOSE GENERATE.
	 * OTHERWISE USE THE OTHER CONSTRUCTOR!
	 */
	public Festival(){};
	
	public Festival(NewGameModel model, NewGame newGame, boolean lastAttemptFailed){
		this.NGmodel = model;
		this.newGame = newGame;
		festivalGenerator(lastAttemptFailed, NGmodel);
	}

	public Festival(ReviewModel model, Review newGame, boolean lastAttemptFailed){
		this.Rmodel = model;
		this.review = newGame;
		festivalGenerator(lastAttemptFailed, Rmodel);
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
		exit = 0;
		Speaker speaker=new Speaker(newGame);
		speaker.execute();
	}

	public void festivalGenerator(boolean lastAttemptFailed, ReviewModel Rmodel){
		//TODO
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
		exit = 0;
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
		exit = 0;
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
		String bashcmd= "festival -b "+ VoxModel.currentWorkingDirectory+"/target/classes/voxspell/resources/sysfiles/.sound.scm";
		ProcessBuilder p = new ProcessBuilder("/bin/bash","-c", bashcmd);

		try {
			Process process = p.start();
			@SuppressWarnings("unused")
			int exit=process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
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
			if (review==null){
				newSpellingQuiz.getSubmit().setEnabled(false);
				newSpellingQuiz.getRe().setEnabled(false);
				newSpellingQuiz.getRehear().setEnabled(false);
				process=p.start();
				exit=process.waitFor();
			}else if (newSpellingQuiz==null){
				review.getSubmit().setEnabled(false);
				review.getRe().setEnabled(false);
				review.getRehear().setEnabled(false);
				process=p.start();
				exit=process.waitFor();
			}

			return null;
		}

		@Override
		protected void done(){
			if (review==null){
				newSpellingQuiz.getSubmit().setEnabled(true);
				newSpellingQuiz.getRe().setEnabled(true);
				newSpellingQuiz.getRehear().setEnabled(true);
			}else if (newSpellingQuiz==null){
				review.getSubmit().setEnabled(true);
				review.getRe().setEnabled(true);
				review.getRehear().setEnabled(true);
			}			
		}
	}
}
