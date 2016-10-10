package voxspell.spelling_aid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class FinishWindows extends JFrame {
	private SpellingAid mainMenu;
	private JFrame itself;

	private JLabel jl;
	private JPanel jp=new JPanel();
	private JTextArea message=new JTextArea();
	private JTextArea currentStats=new JTextArea();

	private JButton stay=new JButton("Stay");
	private JButton next=new JButton("Next");
	private JButton re=new JButton("Return");
	private JButton play=new JButton("Play");

	private ImageIcon victory;
	private ImageIcon defeat;
	
	private ArrayList<File> sysfiles;
	private ArrayList<ArrayList<String>> contents;
	private ArrayList<String> filenames;
	private ArrayList<String> correspondingLevel;
	private Counter counter;
	private VoiceChoice vc = null;
	private WindowListener wl;

	public FinishWindows(SpellingAid mainMenu, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, ArrayList<String> correspondingLevel, VoiceChoice vc, Counter counter, boolean victory, WindowListener wl){
		this.mainMenu=mainMenu;
		this.sysfiles=sysfiles;
		this.contents=contents;
		this.filenames=filenames;
		this.correspondingLevel=correspondingLevel;
		this.counter=counter;
		this.vc=vc;
		this.wl=wl;
		itself=this;

		set();
		mainMenu.setVisible(false);
		buildWindowsListener();
		jl=new JLabel();
		message.setEditable(false);
		setStats();

		if(victory){
			buildVictory();
		}else{
			buildDefeat();
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		if(mainMenu.giveCurrentLevel(correspondingLevel)==(SpellingAid.categories.size()-1)){
			next.setEnabled(false);
		}
		
		jp.add(jl);
		jp.add(message);
		jp.add(currentStats);
		buildbuttons();

		this.add(jp);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
	}

	public void set(){
		Font font = new Font("Verdana", Font.BOLD, 12);
		message.setFont(font);
		message.setForeground(Color.BLUE);
		message.setBackground(new Color(100, 255, 150));
		currentStats.setFont(font);
		currentStats.setForeground(Color.BLUE);
		currentStats.setBackground(new Color(100, 255, 150));

		currentStats.setPreferredSize(new Dimension(470,100));
		stay.setPreferredSize(new Dimension(230,30));
		next.setPreferredSize(new Dimension(230,30));
		re.setPreferredSize(new Dimension(230,30));
		play.setPreferredSize(new Dimension(230,30));
	}

	public void buildVictory(){
		buildVictoryImage();
		String s="\n Congradualation you passed the level \n"
				+"\n Press Stay button to stay at the level and practise more \n"
				+"\n Press Next button to go to the next level\n"
				+"\n Press Return button to return to the main menu\n"
				+"\n Press play button to play the video as the reward of victory \n";
		message.setText(s);
		setSize(new Dimension(480,600));
		message.setPreferredSize(new Dimension(470,160));

	}

	public void buildDefeat(){
		buildDefeatImage();
		String s="\n Level failed \n"
				+"\n Press Stay button to stay at the level and practise more \n"
				+"\n Press Return button to return to the main menu \n";
		message.setText(s);
		next.setVisible(false);
		play.setVisible(false);
		setSize(new Dimension(490,480));
		message.setPreferredSize(new Dimension(470,100));
	}

	public void buildVictoryImage(){
		victory = new ImageIcon("./target/classes/voxspell/spelling_aid/victory_logo.JPG");
		victory.setImage(victory.getImage().getScaledInstance(victory.getIconWidth(),victory.getIconHeight(), Image.SCALE_SMOOTH));
		jl.setMaximumSize(new Dimension(victory.getIconWidth(),victory.getIconHeight()));
		jl.setIcon(victory);
	}

	public void buildDefeatImage(){
		defeat = new ImageIcon("./target/classes/voxspell/spelling_aid/defeat.png");
		defeat.setImage(defeat.getImage().getScaledInstance(defeat.getIconWidth(),defeat.getIconHeight(), Image.SCALE_SMOOTH));
		jl.setMaximumSize(new Dimension(defeat.getIconWidth(),defeat.getIconHeight()));
		jl.setIcon(defeat);
	}

	public void setStats(){
		int masterNumber=0;
		int faultedNumber=0;
		int failedNumber=0;
		ArrayList<String> masterd=contents.get(filenames.indexOf("mastered"));
		ArrayList<String> faulted=contents.get(filenames.indexOf("faulted"));
		ArrayList<String> failed=contents.get(filenames.indexOf("failed"));
		for(String s: masterd){
			if(correspondingLevel.contains(s)){
				masterNumber++;	
			}
		}
		for(String s: faulted){
			if(correspondingLevel.contains(s)){
				faultedNumber++;	
			}
		}		
		for(String s: failed){
			if(correspondingLevel.contains(s)){
				failedNumber++;	
			}
		}
		String output="\n Mastered word in current leverl: "+masterNumber+" \n"
				+ "\n faulted word in current leverl: "+faultedNumber+" \n"
				+ "\n failed word in current leverl: "+failedNumber+" \n";
		currentStats.setText(output);
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
				mainMenu.setVisible(true);
				mainMenu.setLocationRelativeTo(null);
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

	public void buildbuttons(){
		jp.add(stay);
		jp.add(next);
		jp.add(re);
		jp.add(play);
		buildStay();
		buildNext();
		buildRe();
		buildPlay();
	}

	public void buildStay(){
		stay.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				itself.dispose();
				@SuppressWarnings("unused")
				NewSpellingQuiz n = new NewSpellingQuiz(mainMenu, sysfiles, contents, filenames,
						correspondingLevel, vc, counter,wl);	
				
//				for (String s : correspondingLevel){
//					System.out.println(s);
//				}
			}

		});
	}

	public void buildNext(){
		next.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> nextLevel = mainMenu.nextLevel(correspondingLevel);
				itself.dispose();
				NewSpellingQuiz n = new NewSpellingQuiz(mainMenu, sysfiles, contents, filenames,
						nextLevel, vc, counter,wl);
			}

		});

	}

	public void buildRe(){
		re.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainMenu.setVisible(true);
				mainMenu.setLocationRelativeTo(null);
				itself.dispose();
			}
		});
	}

	public void buildPlay(){
		play.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				itself.dispose();
				VideoPlayer vp = new VideoPlayer(wl);
			}

		});
	}

}
