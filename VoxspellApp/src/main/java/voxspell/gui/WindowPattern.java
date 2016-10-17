package voxspell.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxspell.app.VoxModel;

public abstract class WindowPattern extends JFrame {
	
	protected JPanel contentPane;
	protected JFrame menu = this;
	protected JPanel panel;
	protected JLabel userInfo;
	protected JMenuBar mBar;
	protected JMenu loginlogout;
	protected JMenuItem login;
	protected JMenuItem logoff;
	protected JMenu help;
	protected JMenuItem intro;
	protected JMenuItem functionalities;
	protected JMenuItem author;
	
	protected String username = "anonymous";
	
	protected static VoxModel voxModel = VoxModel.getVoxModel();
	
	public WindowPattern() {
		setBounds(100, 100, 721, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setAlwaysOnTop (true);
		
		panel = new JPanel();
		panel.setBackground(new Color(204, 255, 255));
		panel.setBounds(0, 0, 718, 519);
		contentPane.add(panel);
		panel.setLayout(null);
		
		paintWindow();
		
		paintMenuBar();
		
		setResizable(false);
		addWindowListener(VoxModel.getVoxModel());
	}
	
	/**
	 * Customizable window self-painting behavior
	 */
	abstract void paintWindow();
	
	/**
	 * print menu bar as well as user info for every sub-windows.
	 */
	void paintMenuBar(){
		JLabel lblNewLabel = new JLabel("Hello! "+username);
		lblNewLabel.setBounds(12, 33, 706, 15);
		panel.add(lblNewLabel);
		userInfo = lblNewLabel;
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 730, 21);
		panel.add(menuBar);
		mBar = menuBar;
		
		JMenu mnLoginlogout = new JMenu("Log in/Log off");
		menuBar.add(mnLoginlogout);
		loginlogout = mnLoginlogout;
		
		JMenuItem mntmLogin = new JMenuItem("Log in");
		mnLoginlogout.add(mntmLogin);
		login = mntmLogin;
		
		JMenuItem mntmLogOff = new JMenuItem("Log off");
		mnLoginlogout.add(mntmLogOff);
		logoff = mntmLogOff;
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		help = mnHelp;
		
		JMenuItem mntmIntro = new JMenuItem("Intro");
		mnHelp.add(mntmIntro);
		intro = mntmIntro;
		
		JMenuItem mntmFunctonalities = new JMenuItem("Functonalities");
		mnHelp.add(mntmFunctonalities);
		functionalities = mntmFunctonalities;
		
		JMenuItem mntmAuthor = new JMenuItem("Author");
		mnHelp.add(mntmAuthor);
		author = mntmAuthor;
	}
	
	/**
	 * update the userInfo label content.
	 * @param usrname - String, the username to be updated
	 */
	public void updateUsr(String usrname){
		username = usrname;
		userInfo.setText("Hello! "+username);
	}
}
