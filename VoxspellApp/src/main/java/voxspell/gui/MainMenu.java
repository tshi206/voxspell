package voxspell.gui;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * This is a WindowPattern children class managing all accesses to most of the GUI windows.
 * Actual implementation of GUI swapping is done by VoxModel, which is a listener of this GUI.
 * @author mason23
 *
 */
@SuppressWarnings("serial")
public class MainMenu extends WindowPattern{

	private static MainMenu mainMenuGUI = null;
	
	
	/**
	 * Create the frame.
	 */
	private MainMenu() {
		super();
	}
	

	public static MainMenu getmainMenuWindow(){
		if (mainMenuGUI == null){
			mainMenuGUI = new MainMenu();
		}
		return mainMenuGUI;
	}
	
	@Override
	void paintWindow(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblVoxspell = new JLabel("Voxspell");
		lblVoxspell.setForeground(new Color(102, 0, 255));
		lblVoxspell.setFont(new Font("Comic Sans MS", Font.BOLD, 58));
		lblVoxspell.setHorizontalAlignment(SwingConstants.CENTER);
		lblVoxspell.setHorizontalTextPosition(SwingConstants.CENTER);
		lblVoxspell.setBounds(129, 22, 459, 115);
		panel.add(lblVoxspell);
		
		JButton newGame = new JButton("New Game");
		newGame.addActionListener(voxModel);
		newGame.setBackground(new Color(0, 102, 255));
		newGame.setForeground(new Color(204, 255, 255));
		newGame.setHorizontalTextPosition(SwingConstants.CENTER);
		newGame.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		newGame.setBounds(88, 176, 136, 100);
		panel.add(newGame);
		
		JButton stats = new JButton("Stats");
		stats.addActionListener(voxModel);
		stats.setForeground(new Color(204, 255, 255));
		stats.setBackground(new Color(0, 102, 255));
		stats.setHorizontalTextPosition(SwingConstants.CENTER);
		stats.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		stats.setBounds(287, 176, 136, 100);
		panel.add(stats);
		
		JButton scoreboard = new JButton("Scoreboard");
		scoreboard.addActionListener(voxModel);
		scoreboard.setBackground(new Color(0, 102, 255));
		scoreboard.setForeground(new Color(204, 255, 255));
		scoreboard.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		scoreboard.setHorizontalTextPosition(SwingConstants.CENTER);
		scoreboard.setBounds(487, 176, 136, 100);
		panel.add(scoreboard);
		
		JButton review = new JButton("Review Mistakes");
		review.addActionListener(voxModel);
		review.setBackground(new Color(0, 102, 255));
		review.setForeground(new Color(204, 255, 255));
		review.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
		review.setBounds(88, 316, 136, 100);
		panel.add(review);
		
		JButton settings = new JButton("Settings");
		settings.addActionListener(voxModel);
		settings.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		settings.setHorizontalTextPosition(SwingConstants.CENTER);
		settings.setForeground(new Color(204, 255, 255));
		settings.setBackground(new Color(0, 102, 255));
		settings.setBounds(287, 316, 136, 100);
		panel.add(settings);
		
		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu.dispose();
				System.exit(0);
			}
		});
		quit.setForeground(new Color(204, 255, 255));
		quit.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		quit.setBackground(new Color(0, 102, 255));
		quit.setBounds(487, 316, 136, 100);
		panel.add(quit);
		
		
		JLabel lblNewLabel_1 = new JLabel("Your sincere spelling assistant :)");
		lblNewLabel_1.setForeground(new Color(102, 0, 153));
		lblNewLabel_1.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		lblNewLabel_1.setBounds(208, 138, 303, 26);
		panel.add(lblNewLabel_1);
	}

}
