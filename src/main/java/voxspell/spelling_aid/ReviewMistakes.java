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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
class ReviewMistakes extends JFrame implements ActionListener {
	private JTextField txt = new JTextField();
	private JTextArea txtOutput = new JTextArea(10, 20);
	private JLabel prompt = new JLabel("Click \"start!\" to begin!");
	private JLabel accuracy = new JLabel();
	private JLabel currentAccuracy=new JLabel();
	private SpellingAid frame;
	private JButton re=new JButton("Return");
	private JButton rehear=new JButton("Rehear");
	private JButton submit = new JButton("Start!");
	private JPanel jp = new JPanel();
	private Counter counter;

	private boolean lastAttemptFailed;
	private boolean turnend;
	private int wc = 1;
	private int trials = 4;
	private int attempt;
	private String lastword = "";
	private String word = "You have not start the game, please start the game first";
	private int exit;
	private int wordsCorrect=0;
	private VoiceChoice vc;

	protected ArrayList<File> sysfiles;
	protected ArrayList<ArrayList<String>> contents;
	protected ArrayList<String> filenames;
	private ArrayList<String> correspondingLevel;
	private ArrayList<String> correspondingLevelfailed= new ArrayList<String>();

	private int wordsFailed=0;
	protected Process process=null;
	protected ProcessBuilder p = null;

	public ReviewMistakes(SpellingAid jf, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, VoiceChoice vc, Counter counter, ArrayList<String> correspondingLevel) {

		super("Spelling Aid: Review Mistakes");
		this.sysfiles = sysfiles;
		this.contents = contents;
		this.filenames = filenames;
		this.vc = vc;
		this.counter=counter;
		this.correspondingLevel=correspondingLevel;
		buildWindowsListener();

		if (contents.get(filenames.indexOf("failed")).size()==0){
			final JFrame newFrame = new JFrame();
			newFrame.setLayout(new BorderLayout());
			JPanel np = new JPanel();
			np.setLayout(new BorderLayout());
			JLabel jlabel = new JLabel();
			jlabel.setText("No failed words found! Well done!!");
			jlabel.setSize(100, 100);
			jlabel.setVisible(true);
			np.add(jlabel,BorderLayout.CENTER);
			ActionListener l = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					newFrame.dispose();
				}

			};
			JButton jb = new JButton("OK");
			jb.addActionListener(l);
			np.add(jb, BorderLayout.SOUTH);
			newFrame.add(np);
			newFrame.setVisible(true);
			newFrame.setLocationRelativeTo(null);
			newFrame.pack();
		}else{
			if (contents.get(filenames.indexOf("failed")).size()<10){
				trials = contents.get(filenames.indexOf("failed")).size()+1;
			}
			frame = jf;
			frame.setVisible(false);

			set();
			ImageIcon welcomeMsg = new ImageIcon("./target/classes/voxspell/spelling_aid/ReviewMistakes.JPG");
			welcomeMsg.setImage(welcomeMsg.getImage().getScaledInstance(welcomeMsg.getIconWidth(),
					welcomeMsg.getIconHeight(), Image.SCALE_DEFAULT));
			JLabel jl = new JLabel(welcomeMsg);
			jl.setIcon(welcomeMsg);
			submit.addActionListener(this);
			re.addActionListener(this);
			rehear.addActionListener(this);
			submit.addMouseListener(new Mouse(submit));
			re.addMouseListener(new Mouse(re));
			rehear.addMouseListener(new Mouse(rehear));

			accuracy.setText(counter.printAccuracy());
			accuracy.setVisible(true);
			currentAccuracy.setText("Your score in current revision: "+ wordsCorrect+"/10");

			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			jl.setVisible(true);
			jp.add(jl);
			String s = "\nPlease listem to the voice and try to spell the word\n"
					+ "\nYou will have at most ten words to spell\n"
					+ "\nthe number of words could be less if the number of \n"
					+ "failed words is less than three.\n"
					+ "\nFor each of them you get two chances to attempt\n"
					+ "\nPlease listen carefully and good luck!\n";
			txtOutput.setText(s);
			txtOutput.setEditable(false);
			prompt.setVisible(true);
			jp.add(txtOutput);
			jp.add(prompt);
			jp.add(txt);
			jp.add(accuracy);
			jp.add(currentAccuracy);
			jp.add(submit);
			jp.add(rehear);
			jp.add(re);

			jp.setVisible(true);
			add(jp);

			this.setVisible(true);
			this.setLocationRelativeTo(null);
			this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

			try {
				Scanner scanner = new Scanner(new FileReader("./target/classes/voxspell/spelling_aid/.failed"));
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					if(correspondingLevel.contains(line)){
						correspondingLevelfailed.add(line);
					}
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				final JFrame newFrame = new JFrame();
				newFrame.setLayout(new BorderLayout());
				JPanel np = new JPanel();
				np.setLayout(new BorderLayout());
				JLabel jlabel = new JLabel();
				jlabel.setText("Error! Required file(s) cannot be found! "
						+ "Please check if programs' files have been modified or deleted.");
				jlabel.setSize(100, 100);
				jlabel.setVisible(true);
				np.add(jlabel,BorderLayout.CENTER);
				ActionListener l = new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						newFrame.setVisible(false);
						System.exit(1);
					}

				};
				JButton jb = new JButton("OK");
				jb.addActionListener(l);
				np.add(jb, BorderLayout.SOUTH);
				newFrame.add(np);
				newFrame.setVisible(true);
				newFrame.setLocationRelativeTo(null);
				newFrame.pack();
			}

		}
	}

	public void set(){
		Font font = new Font("Verdana", Font.BOLD, 12);
		txtOutput.setFont(font);
		txtOutput.setForeground(Color.BLUE);
		txtOutput.setBackground(new Color(100, 255, 150));

		setSize(430, 520);
		setMinimumSize(new Dimension(430,520));
		jp.setPreferredSize(new Dimension(400,500));
		jp.setMaximumSize(new Dimension(400,500));
		txtOutput.setPreferredSize(new Dimension(400,190));
		submit.setPreferredSize(new Dimension(260,35));
		re.setPreferredSize(new Dimension(150,25));
		rehear.setPreferredSize(new Dimension(150,25));
		txt.setPreferredSize(new Dimension(230,20));
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
		if(e.getSource().equals(submit)){
			if (submit.getText().equals("Return!")){

				frame.setVisible(true);
				this.dispose();
			}else if (submit.getText().equals("Start!")){
				attempt = 0;
				lastAttemptFailed = false;
				turnend = false;
				word = correspondingLevelfailed.get(new Random().nextInt(correspondingLevelfailed.size()));
				while (word.equals(lastword)){
					word = correspondingLevelfailed.get(new Random().nextInt(correspondingLevelfailed.size()));
				}
				festivalGenerator(lastAttemptFailed);
				prompt.setText("Spell word "+wc+" of "+(trials-1));
				submit.setText("Submit!");
				lastword = word;
			}else if (submit.getText().equals("Submit!")){
				if (exit==0){
					checkword();
					if (turnend){
						if (wc==trials){
							submit.setText("Return!");
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
			String bashcmd = "festival -b "+ SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.sound.scm";
			p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		}else{
			String s = "Please spell the word "+word;
			vc.setWord(s);
			vc.updateSCM();
			String bashcmd = "festival -b "+ SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.sound.scm";
			p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		}
		exit = 0;
		Speaker speaker = new Speaker(this);
		speaker.execute();
	}

	private void festivalGenerator(String s){
		vc.setWord(s);
		vc.updateSCM();
		String bashcmd= "festival -b "+ SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.sound.scm";
		p = new ProcessBuilder("/bin/bash","-c", bashcmd);
		exit = 0;
		Speaker speaker = new Speaker(this);
		speaker.execute();
	}

	private void checkword(){
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
				correspondingLevelfailed.remove(word);
				SpellingAid.write("mastered");
				SpellingAid.write("faulted");
				SpellingAid.write("failed");
				SpellingAid.write("masteredhistory");
				wordsCorrect++;
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
				correspondingLevelfailed.remove(word);
				SpellingAid.write("mastered");
				SpellingAid.write("faulted");
				SpellingAid.write("failed");
				SpellingAid.write("faultedhistory");
			}

			wc++;
			counter.count(true);
			accuracy.setText(counter.printAccuracy());
			if (wc==trials){
				submit.setText("Return!");
			}else{
				submit.setText("Start!");
			}
		}else{
			if (attempt==1){
				festivalGenerator("Incorrect");
				festivalGenerator("Sorry, you fail on the same word again.");
				festivalGenerator("Please hear carefully and try to spell it correctly next time.");
				festivalGenerator("The spelling of the word " + word +" is :");
				String temp = new String(word);
				char[] c = temp.toCharArray();
				for (char ch : c){
					festivalGenerator("" + ch );
				}
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
		}
	}
	
	class Speaker extends SwingWorker<Void, Void>{
		private ReviewMistakes reviewMistakes;

		public Speaker(ReviewMistakes reviewMistakes){
			this.reviewMistakes=reviewMistakes;
		}

		@Override
		protected Void doInBackground() throws Exception {
			System.out.println("word speak in");
			reviewMistakes.submit.setEnabled(false);
			reviewMistakes.re.setEnabled(false);
			reviewMistakes.rehear.setEnabled(false);
			reviewMistakes.process=reviewMistakes.p.start();
			reviewMistakes.exit=reviewMistakes.process.waitFor();
			return null;
		}

		@Override
		protected void done(){
			System.out.println("word speak out");
			reviewMistakes.submit.setEnabled(true);
			reviewMistakes.re.setEnabled(true);
			reviewMistakes.rehear.setEnabled(true);
		}

	}
}