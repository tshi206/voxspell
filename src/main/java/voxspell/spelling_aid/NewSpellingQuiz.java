package voxspell.spelling_aid;

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

@SuppressWarnings("serial")
class NewSpellingQuiz extends JFrame implements ActionListener {
	public static String currentWorkingDirectory = System.getProperty("user.dir");

	private JTextField txt = new JTextField();
	private JTextArea txtOutput = new JTextArea(10, 20);
	private	JTextArea hint = new JTextArea(10, 20);
	private JLabel prompt = new JLabel("Click \"start!\" to begin!");
	private JLabel accuracy = new JLabel();
	private JLabel currentAccuracy=new JLabel();
	private SpellingAid frame;

	private JFrame itself;
	private JPanel jp= new JPanel();
	protected JButton submit = new JButton("Start!");
	protected JButton rehear=new JButton("Rehear");
	protected JButton re=new JButton("Return");

	private boolean lastAttemptFailed;
	private boolean turnend;
	private int wc = 1;
	private int attempt;
	private int wordsCorrect=0;
	private int wordsFailed=0;
	private String word = "You have not start the game, please start the game first";
	protected int exit;

	private ArrayList<String> wordsAlreadyTested= new ArrayList<String>();
	protected ArrayList<File> sysfiles;
	protected ArrayList<ArrayList<String>> contents;
	protected ArrayList<String> filenames;
	private ArrayList<String> correspondingLevel; 

	private Counter counter;
	private VoiceChoice vc;
	protected ProcessBuilder p = null;
	protected Process process = null;
	private WindowListener wl;
	
	public NewSpellingQuiz(SpellingAid jf, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, ArrayList<String> correspondingLevel, VoiceChoice vc, Counter counter, WindowListener wl) {

		super("Spelling Aid: New Quiz Session");
		this.vc=vc;
		this.sysfiles = sysfiles;
		this.contents = contents;
		this.filenames = filenames;
		this.counter=counter;
		this.correspondingLevel=correspondingLevel;
		this.itself=this;
		this.wl=wl;
		frame = jf;
		frame.setVisible(false);

		set();
		buildWindowsListener();

		ImageIcon welcomeMsg = new ImageIcon("./target/classes/voxspell/spelling_aid/NewSession.JPG");
		welcomeMsg.setImage(welcomeMsg.getImage().getScaledInstance(welcomeMsg.getIconWidth(),welcomeMsg.getIconHeight(), Image.SCALE_DEFAULT));
		JLabel jl = new JLabel(welcomeMsg);
		jl.setIcon(welcomeMsg);

		submit.addActionListener(this);
		re.addActionListener(this);
		rehear.addActionListener(this);
		submit.addMouseListener(new Mouse(submit));
		re.addMouseListener(new Mouse(re));
		rehear.addMouseListener(new Mouse(rehear));
		accuracy.setText(counter.printAccuracy());
		currentAccuracy.setText("Your score in current category: "+ wordsCorrect+"/10");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jl.setVisible(true);
		jp.add(jl);
		String s = "\nPlease listen to the voice and try to spell the word\n"
				+ "\nYou will have ten words to spell\n"
				+ "\nFor each of them you get two chances to attempt\n"
				+ "\nPlease listen carefully and good luck!\n";
		txtOutput.setText(s);
		txtOutput.setEditable(false);
		prompt.setVisible(true);
		accuracy.setVisible(true);	
		jp.add(txtOutput);
		jp.add(prompt);
		jp.add(txt);
		jp.add(accuracy);
		jp.add(currentAccuracy);
		
		//TODO - definition
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setPreferredSize(new Dimension(360, 35));
		scrollPane_1.setViewportView(hint);
		hint.setEditable(false);
		hint.setText("Hint: ");
		jp.add(scrollPane_1);
		
		jp.add(submit);
		jp.add(rehear);
		jp.add(re);

		jp.setVisible(true);
		add(jp);

		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
	}

	public void set(){
		txtOutput.setBackground(new Color(100, 255, 150));
		Font font = new Font("Verdana", Font.BOLD, 12);
		txtOutput.setFont(font);
		txtOutput.setForeground(Color.BLUE);
		txtOutput.setPreferredSize(new Dimension(360, 150));
		
		setSize(400, 505);
		setMinimumSize(new Dimension(400, 505));
		submit.setPreferredSize(new Dimension(240,35));
		submit.setMinimumSize(new Dimension(240,35));
		submit.setMaximumSize(new Dimension(240,35));
		re.setPreferredSize(new Dimension(150,25));
		rehear.setPreferredSize(new Dimension(150,25));
		jp.setPreferredSize(new Dimension(380, 475));
		jp.setMaximumSize(new Dimension(380, 475));
		txt.setPreferredSize(new Dimension(200,20));
		accuracy.setPreferredSize(new Dimension(290, 15));
		currentAccuracy.setPreferredSize(new Dimension(290, 15));
	}
	
	public void buildWindowsListener(){
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowClosing(WindowEvent e) {
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(submit)){
			if (submit.getText().equals("Start!")){
				attempt = 0;
				lastAttemptFailed = false;
				turnend = false;
				word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				while (wordsAlreadyTested.contains(word)){
					word = correspondingLevel.get(new Random().nextInt(correspondingLevel.size()));
				}
				wordsAlreadyTested.add(word);
				festivalGenerator(lastAttemptFailed);
				prompt.setText("Spell word "+wc+" of 10");
				submit.setText("Submit!");
				if (SpellingAid.dictionary.get(word)!=null){
					hint.setText("Hint: "+SpellingAid.dictionary.get(word));
				}else{
					hint.setText("Sorry. Currently no definition for this word.");
				}
			}else if (submit.getText().equals("Submit!")){
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
							submit.setText("Start!");
						}
					}
				}
			}
		}else if(e.getSource().equals(rehear)){
			festivalGenerator(word);
		}else if(e.getSource().equals(re)){
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			this.dispose();
		}
	}

	private void festivalGenerator(boolean lastAttemptFailed){
		if (lastAttemptFailed){
			String s = "Incorrect, try once more. "+word+", "+word+".";
			vc.setWord(s);
			vc.updateSCM();
		}else{
			String s = "Please spell the word "+word;
			vc.setWord(s);
			vc.updateSCM();
		}
		//System.out.println(2);
		String bashcmd = "festival -b "+ currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.sound.scm";
		p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		exit = 0;
		Speaker speaker=new Speaker(this);
		speaker.execute();
		//System.out.println(3);
	}

	private void festivalGenerator(String s){
		vc.setWord(s);
		vc.updateSCM();
		String bashcmd= "festival -b "+ currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.sound.scm";
		p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		exit = 0;
		Speaker speaker=new Speaker(this);
		speaker.execute();
	}

	private void checkWordCorrect(){
		if (txt.getText().toLowerCase().equals(word.toLowerCase())){
			festivalGenerator("Correct");
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
				SpellingAid.write("mastered");
				SpellingAid.write("faulted");
				SpellingAid.write("failed");
				SpellingAid.write("masteredhistory");
				wordsCorrect++;
				counter.count(true);
				currentAccuracy.setText("Your score in current session: "+ wordsCorrect+"/10");
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
				SpellingAid.write("mastered");
				SpellingAid.write("faulted");
				SpellingAid.write("failed");
				SpellingAid.write("faultedhistory");
				counter.count(false);
			}
			turnend=true;
			accuracy.setText(counter.printAccuracy());
			wc++;
			submit.setText("Start!");
		}else{
			if (attempt==1){
				festivalGenerator("Incorrect");
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
				SpellingAid.write("mastered");
				SpellingAid.write("faulted");
				SpellingAid.write("failed");
				SpellingAid.write("failedhistory");
				wc++;
				turnend=true;
			}else if (!(lastAttemptFailed)){
				lastAttemptFailed = true;
				festivalGenerator(lastAttemptFailed);
				attempt++;
				return;
			}
			wordsFailed++;
			counter.count(false);
			accuracy.setText(counter.printAccuracy());
			if(wordsFailed == 2){
				@SuppressWarnings("unused")
				IfContinue ifcontinue = new IfContinue(frame, this, sysfiles, contents, filenames, correspondingLevel, vc, counter,wl);
			}
		}
	}

	class Speaker extends SwingWorker<Void, Void>{
		private NewSpellingQuiz newSpellingQuiz;

		public Speaker(NewSpellingQuiz newSpellingQuiz){
			this.newSpellingQuiz=newSpellingQuiz;
		}

		@Override
		protected Void doInBackground() throws Exception {
			newSpellingQuiz.submit.setEnabled(false);
			newSpellingQuiz.re.setEnabled(false);
			newSpellingQuiz.rehear.setEnabled(false);
			newSpellingQuiz.process=newSpellingQuiz.p.start();
			newSpellingQuiz.exit=newSpellingQuiz.process.waitFor();
			return null;
		}

		@Override
		protected void done(){
			newSpellingQuiz.submit.setEnabled(true);
			newSpellingQuiz.re.setEnabled(true);
			newSpellingQuiz.rehear.setEnabled(true);
		}

	}

}
