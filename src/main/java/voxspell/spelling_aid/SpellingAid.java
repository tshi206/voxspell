package voxspell.spelling_aid;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.Image;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class SpellingAid extends JFrame implements ActionListener, WindowListener {
	public static String currentWorkingDirectory = System.getProperty("user.dir");
	
	NewSpellingQuiz n;
	ReviewMistakes r;
	ViewStatistics v;
	ClearStatistics c;
	VideoPlayer vp;
	
	private JButton New_Spelling_Quiz = new JButton("New Spelling Quiz");
	private JButton Review_Mistakes = new JButton("Review Mistakes");
	private JButton View_Statistics = new JButton("View Statistics");
	private JButton Clear_Statistics = new JButton("Clear Statistics");
	private JLabel selectVoice = new JLabel("select your favorite voice:");
	private JRadioButton voiceOne = new JRadioButton("Voice One");
	private JRadioButton voiceTwo = new JRadioButton("Voice Two");
	protected VoiceChoice vc = VoiceChoice.getInstance();
	
	private JLabel selectPreferredLevel = new JLabel("select your preferred category:");
	
	private String[] levels = {"Level One","Level Two","Level Three","Level Four","Level Five","Level Six","Level Seven","Level Eight","Level Nine","Level Ten","Level Eleven"};
	
	private JComboBox<String> level = new JComboBox<String>(levels);
	private JTextArea txtOutput = new JTextArea(10, 20);
	
	//non-history file
	protected File scm = new File(currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.sound.scm");
	
	protected File mastered = new File(currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.mastered");
	protected File failed = new File(currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.failed");
	protected File faulted = new File(currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.faulted");
	//protected File temptxt = new File("./src/spelling_aid/.temptxt"); removed as not needed
	//protected File history = new File("./src/spelling_aid/.history"); removed as not needed
	protected File masteredhistory = new File(currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.masteredhistory");
	protected File faultedhistory = new File(currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.faultedhistory");
	protected File failedhistory = new File(currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/.failedhistory");
	
	protected static ArrayList<ArrayList<String>> levelContents = new ArrayList<ArrayList<String>>();
	
	protected static ArrayList<File> sysfiles = new ArrayList<File>();
	protected static ArrayList<ArrayList<String>> contents = new ArrayList<ArrayList<String>>();
	protected static ArrayList<String> filenames = new ArrayList<String>();
	
	String[] fn = {"mastered", "failed", "faulted", "masteredhistory", "faultedhistory", "failedhistory"};
	File[] files = {mastered, failed, faulted, masteredhistory, faultedhistory, failedhistory};
	
	protected Counter counter=new Counter();
	
	public SpellingAid() {
		super("Spelling Aid");
		
		System.out.println("System boots up............");
		System.out.println("Current working dir: "+currentWorkingDirectory);

		//starting files setup
		FileLoadingWorker fw = new FileLoadingWorker(files, fn, sysfiles, contents, filenames, levelContents, levels);
		fw.execute();
		
		//create scm file
		try {
			if (!(scm.exists())){
				scm.createNewFile();
			}
		} catch (IOException e) {
			final JFrame newFrame = new JFrame();
			newFrame.setLayout(new BorderLayout());
			JPanel np = new JPanel();
			np.setLayout(new BorderLayout());
			JLabel jlabel = new JLabel();
			jlabel.setText("Error! Fatal error occurs! "
					+ "Program will not be functioning correctly.");
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
		//finishes files setup
		
		//setting up VoiceChoice
		vc.setSCM(scm);
		//done

		
		//Searching external vlc library
		VLCPathSearcher vlcSearcher = new VLCPathSearcher();
		vlcSearcher.execute();
		//all File IO done
		
		setSize(450, 480);
		setMinimumSize(new Dimension(450, 480));

		ImageIcon welcomeMsg = new ImageIcon("./target/classes/voxspell/spelling_aid/WelcomingMessage.JPG");
		welcomeMsg.setImage(welcomeMsg.getImage().getScaledInstance(welcomeMsg.getIconWidth(),
				welcomeMsg.getIconHeight(), Image.SCALE_DEFAULT));
		JLabel jl = new JLabel(welcomeMsg);
		jl.setIcon(welcomeMsg);
		New_Spelling_Quiz.setPreferredSize(new Dimension(200,25));
		Review_Mistakes.setPreferredSize(new Dimension(200,25));
		View_Statistics.setPreferredSize(new Dimension(200,25));
		Clear_Statistics.setPreferredSize(new Dimension(200,25));
		New_Spelling_Quiz.addActionListener(this);
		Review_Mistakes.addActionListener(this);
		View_Statistics.addActionListener(this);
		Clear_Statistics.addActionListener(this);
		New_Spelling_Quiz.addMouseListener(new Mouse(New_Spelling_Quiz));
		Review_Mistakes.addMouseListener(new Mouse(Review_Mistakes));
		View_Statistics.addMouseListener(new Mouse(View_Statistics));

		Clear_Statistics.addMouseListener(new Mouse(Clear_Statistics));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		jl.setVisible(true);
		JPanel jp = new JPanel();
		jp.setSize(445, 470);
		jp.setMaximumSize(new Dimension(445,475));

		jp.add(jl);
		jp.add(New_Spelling_Quiz);
		jp.add(Review_Mistakes);
		jp.add(View_Statistics);
		jp.add(Clear_Statistics);
		jp.add(selectVoice);
		jp.add(voiceOne);
		jp.add(voiceTwo);
		voiceOne.addActionListener(this);
		voiceTwo.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
		group.add(voiceOne);
		group.add(voiceTwo);
		voiceOne.doClick();

		jp.add(selectPreferredLevel);
		jp.add(level);
		String s = "\n New Spelling Quiz: to start a new spelling session\n"
				+ "\n Review Mistakes: to re-attempt the failed words\n"
				+ "\n View Statistics: to see the records of the past spellings\n"
				+ "\n Clear Statistics: to clear all history (cannot be reversed)\n"
				+ "\n Suggested starting level is Level One, the higher the level,"
				+ "\n the harder it would be. Finally, please enjoy spelling aid!\n";
		txtOutput.setText(s);
		txtOutput.setBackground(new Color(100, 255, 150));
		Font font = new Font("Verdana", Font.BOLD, 12);
		txtOutput.setFont(font);
		txtOutput.setForeground(Color.BLUE);
		txtOutput.setEditable(false);
		jp.add(txtOutput);
		jp.setVisible(true);
		setLocationRelativeTo(null);
		add(jp);
	}
	
	public static int whatLevelYouWant(String levelname, String[] levels){
		for (int i=0; i<levels.length; i++){
			if (levelname.equals(levels[i])){
				return i;
			}
		}
		throw new RuntimeException("No Such Level!!");
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(New_Spelling_Quiz)){
			n = new NewSpellingQuiz(this, sysfiles, contents, filenames,
					levelContents.get(whatLevelYouWant(level.getItemAt(level.getSelectedIndex()), levels)), vc, counter,this);
		}else if (e.getSource().equals(Review_Mistakes)){
			r = new ReviewMistakes(this, sysfiles, contents, filenames, vc, counter, 
					levelContents.get(whatLevelYouWant(level.getItemAt(level.getSelectedIndex()), levels)));
		}else if (e.getSource().equals(View_Statistics)){
			v = new ViewStatistics(this, this, sysfiles, contents, filenames, levels, levelContents);
		}else if (e.getSource().equals(Clear_Statistics)){
			c = new ClearStatistics(this, this, sysfiles, contents, filenames);
		}else if (e.getSource()==voiceOne){
			vc.setChoice("one");
		}else if (e.getSource()==voiceTwo){
			vc.setChoice("two");
		}
	}
	
	public static void main(String[] agrs){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SpellingAid frame = new SpellingAid();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
			}
		});
	}

	public static void write(String destination){
		String s = "";
		FileWriter writer;
		try {
			writer = new FileWriter(sysfiles.get(filenames.indexOf(destination)), false);
			for (String str : contents.get(filenames.indexOf(destination))){
				s = s + str + "\n";
			}
			writer.write(s);
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
	
	public ArrayList<String> nextLevel(ArrayList<String> currentLevel){
		int levelNow=levelContents.indexOf(currentLevel);
		return levelContents.get(levelNow+1);
	}
	
	public int giveCurrentLevel(ArrayList<String> currentLevel){
		int levelNow=levelContents.indexOf(currentLevel);
		return levelNow;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		if (e.getSource().getClass().toString().equals("class spelling_aid.NewSpellingQuiz")){
			this.setVisible(false);
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		if (e.getSource().getClass().toString().equals("class spelling_aid.ClearStatistics")){
			for (File f : files){
				if (!(f.exists())){
					FileLoadingWorker fw = new FileLoadingWorker(files, fn, sysfiles, contents, filenames, levelContents, levels);
					fw.execute();
					break;
				}
			}
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}else if (e.getSource().getClass().toString().equals("class spelling_aid.VideoPlayer")){
			n.dispose();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}else{
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}