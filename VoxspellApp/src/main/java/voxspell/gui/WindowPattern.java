package voxspell.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxspell.app.VoxModel;
import voxspell.toolbox.VoxDatabase;

@SuppressWarnings("serial")
public abstract class WindowPattern extends JFrame {
	
	protected JPanel contentPane;
	protected JFrame menu = this;
	protected JPanel panel;
	protected JLabel userInfo;
	protected JMenuBar mBar;
	protected JMenu loginlogout;
	protected JMenuItem login;
	protected JMenuItem logoff;
	protected JMenuItem dontRememberAnyone;
	protected JMenu help;
	protected JMenuItem intro;
	protected JMenuItem functionalities;
	protected JMenuItem edit;
	protected JMenuItem delete;
	protected JMenuItem clear;
	
	
	protected String username = "anonymous";
	
	protected WindowPattern wp = this;
	
	
	
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
		mntmLogin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginWindow lw = new LoginWindow();
				lw.setVisible(true);
			}
			
		});
		mnLoginlogout.add(mntmLogin);
		login = mntmLogin;
		
		JMenuItem mntmLogOff = new JMenuItem("Log off");
		mntmLogOff.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				VoxDatabase.backToAnonymous();
				for (WindowPattern wp : VoxModel.getVoxModel().getGuis()){
					wp.getLogin().setEnabled(true);
					wp.getLogoff().setEnabled(false);
					wp.updateUsr("anonymous");
				}
				
				JOptionPane.showMessageDialog(wp, "You will now log in as "+"anonymous"+".");
				
			}
			
		});
		mnLoginlogout.add(mntmLogOff);
		logoff = mntmLogOff;
		
		JMenuItem mntmDontRememberAnyone = new JMenuItem("Don't remember anyone next time");
		mntmDontRememberAnyone.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int option = JOptionPane.showConfirmDialog(wp, "This operation may affect other users.\n"
						+ "Continue?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION){
					VoxDatabase.writeToSysFile("rememberedUsr", "", false);
				}
				
			}
			
		});
		mnLoginlogout.add(mntmDontRememberAnyone);
		dontRememberAnyone = mntmDontRememberAnyone;
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		help = mnHelp;
		
		JMenuItem mntmIntro = new JMenuItem("Intro");
		mnHelp.add(mntmIntro);
		intro = mntmIntro;
		
		JMenuItem mntmFunctonalities = new JMenuItem("Functonalities");
		mnHelp.add(mntmFunctonalities);
		functionalities = mntmFunctonalities;
		
		JMenuItem mntmEdit = new JMenuItem("Edit imported category");
		mntmEdit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(wp, "Editing imported files may let you lose some of your statistics.\n"
						+ "Continue?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION){
					EditImportedFiles eif = new EditImportedFiles();
					eif.setVisible(true);
					delete.setEnabled(false);
				}
			}
			
		});
		mnHelp.add(mntmEdit);
		edit = mntmEdit;
		
		JMenuItem mntmDelete = new JMenuItem("Delete imported category");
		mntmDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteCategory dc = new DeleteCategory();
				dc.setVisible(true);
				edit.setEnabled(false);
			}
			
		});
		mnHelp.add(mntmDelete);
		delete = mntmDelete;
		
		JMenuItem mntmClear = new JMenuItem("Clear your statistics");
		mntmClear.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(wp, "Are you sure? Clearing statistics will not change your scoreboard record.", "Confirm", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION){
					VoxDatabase.deleteSysFile();
				}
			}
			
		});
		mnHelp.add(mntmClear);
		clear = mntmClear;
		
	}
	
	public JMenuItem getLogin() {
		return login;
	}

	public JMenuItem getLogoff() {
		return logoff;
	}

	public JMenuItem getDelete() {
		return delete;
	}

	public JMenuItem getEdit() {
		return edit;
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
