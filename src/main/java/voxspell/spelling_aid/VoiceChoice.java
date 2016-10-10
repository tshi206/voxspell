package voxspell.spelling_aid;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class VoiceChoice {
	private String whichChoice = "one";
	private String word = "";
	private File scm;
	
	private static VoiceChoice vc = null;
	
	private VoiceChoice(){}
	
	public static VoiceChoice getInstance(){
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
	
	public void updateSCM(){
		FileWriter writer;
		try {
			writer = new FileWriter(scm, false);
			writer.write(this.toString());
			writer.close();
		} catch (IOException e) {
			JFrame jf = new JFrame();
			JOptionPane op = new JOptionPane("Fatal error : IOException");
			jf.setLocationRelativeTo(null);
			jf.setLayout(new BorderLayout());
			jf.add(op, BorderLayout.SOUTH);
			op.setVisible(true);
			jf.setVisible(true);
		}
	}
	
	@Override
	public String toString(){
		if (whichChoice.equals("one")){
			return "(voice_kal_diphone)\n(SayText \""+word+"\")\n";
		}else{
			return "(voice_akl_nz_jdt_diphone)\n(SayText \""+word+"\")\n";
		}
	}
}
