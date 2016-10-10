package voxspell.spelling_aid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IfContinue extends JFrame{

	private SpellingAid mainMenu;
	private JFrame jf;
	private JFrame itself;

	private JButton re=new JButton("Return");
	private JButton continu=new JButton("Continue");
	private JButton restart=new JButton("Restart");

	private JPanel main=new JPanel();
	private JPanel buttons=new JPanel();
	private JLabel message=new JLabel("Level Failed");

	private ArrayList<File> sysfiles;
	private ArrayList<ArrayList<String>> contents;
	private ArrayList<String> filenames;
	private ArrayList<String> correspondingLevel;
	private Counter counter;
	private VoiceChoice vc = null;
	private WindowListener wl;
	
	public IfContinue(SpellingAid mainMenu, JFrame jf, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, ArrayList<String> correspondingLevel, VoiceChoice vc, Counter counter, WindowListener wl){

		itself=this;
		this.mainMenu=mainMenu;
		this.jf=jf;
		this.vc=vc;
		this.sysfiles=sysfiles;
		this.contents=contents;
		this.filenames=filenames;
		this.correspondingLevel=correspondingLevel;
		this.counter=counter;
		this.wl=wl;
		
		jf.setVisible(false);
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		
		set();
		buildWindowsListener();
		add(main);
		main.add(message);
		main.add(buttons);
		buildcontinu();
		buildrestart();
		buildre();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public void set(){
		Font font = new Font("Verdana", Font.BOLD, 45);
		message.setFont(font);
		message.setForeground(Color.RED);
		message.setPreferredSize(new Dimension(100,200));
		message.setMaximumSize(new Dimension(360,100));
		
		setSize(700,180);
		main.setMaximumSize(new Dimension(400,180));
		re.setPreferredSize(new Dimension(180,50));
		continu.setPreferredSize(new Dimension(180,50));
		restart.setPreferredSize(new Dimension(180,50));

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
	
	public void buildre(){
		buttons.add(re);
		buttons.add(Box.createVerticalStrut(20));
		re.addMouseListener(new Mouse(re));
		re.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(true);
				mainMenu.setLocationRelativeTo(null);
				itself.dispose();
				jf.dispose();
			}

		});
	}

	public void buildcontinu(){
		buttons.add(continu);
		buttons.add(Box.createVerticalStrut(20));
		continu.addMouseListener(new Mouse(continu));
		continu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				jf.setVisible(true);
				jf.setLocationRelativeTo(null);
				itself.dispose();
			}
		});
	}

	public void buildrestart(){
		buttons.add(restart);
		buttons.add(Box.createVerticalStrut(20));
		restart.addMouseListener(new Mouse(restart));
		restart.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				itself.dispose();
				jf.dispose();
				NewSpellingQuiz n = new NewSpellingQuiz(mainMenu, sysfiles, contents, filenames,
						correspondingLevel, vc,counter,wl);
			}
		});
	}
}

