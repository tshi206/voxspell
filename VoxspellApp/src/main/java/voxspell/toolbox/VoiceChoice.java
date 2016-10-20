package voxspell.toolbox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class VoiceChoice {
	private String speed = "Normal";
	private String whichChoice = "one";
	private String word = "";
	private File scm;
	
	private static VoiceChoice vc = null;
	
	private VoiceChoice(){}
	
	public static VoiceChoice getVoiceChoice(){
		if (vc == null){
			vc = new VoiceChoice();
			return vc;
		}else{
			return vc;
		}
	}
	
	public void setChoice(String s){
		whichChoice = s;
	}
	
	public void setWord(String s){
		word = s;
	}

	public void setSCM(File scm){
		this.scm = scm;
	}
	
	public void setSpeed(String s){
		speed = s;
	}
	
	public void updateSCM(){
		FileWriter writer;
		try {
			writer = new FileWriter(scm, false);
			writer.write(this.toString());
			writer.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error! Cannot find festival scm file in sysfiles folder.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	@Override
	public String toString(){
		String speedLevel;
		if (speed.equals("Normal")){
			speedLevel = "";
		}else if (speed.equals("Slow")){
			speedLevel = "(Parameter.set 'Duration_Stretch 2.2)";
		}else{
			speedLevel = "(Parameter.set 'Duration_Stretch 0.8)";
		}
		if (whichChoice.equals("one")){
			return "(voice_kal_diphone)\n"+speedLevel+"\n(SayText \""+word+"\")\n";
		}else{
			return "(voice_akl_nz_jdt_diphone)\n"+speedLevel+"\n(SayText \""+word+"\")\n";
		}
	}
}
