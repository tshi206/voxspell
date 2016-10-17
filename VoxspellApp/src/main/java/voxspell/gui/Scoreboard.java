package voxspell.gui;

import java.awt.Color;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class Scoreboard extends WindowPattern {

	private JTable table;

	private static Scoreboard scoreboardGUI = null;

	/**
	 * Create the frame.
	 */
	private Scoreboard() {
		super();
	}

	public static Scoreboard getScoreboardWindow(){
		if (scoreboardGUI == null){
			scoreboardGUI = new Scoreboard();
		}
		return scoreboardGUI;
	}

	@Override
	void paintWindow() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JLabel lblScoreboard = new JLabel("Scoreboard");
		lblScoreboard.setFont(new Font("Comic Sans MS", Font.BOLD, 58));
		lblScoreboard.setForeground(new Color(102, 0, 255));
		lblScoreboard.setHorizontalAlignment(SwingConstants.CENTER);
		lblScoreboard.setBounds(132, 37, 430, 74);
		panel.add(lblScoreboard);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(62, 122, 590, 294);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setBackground(new Color(255, 255, 0));
		table.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		table.setForeground(new Color(102, 0, 255));
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setShowGrid(false);
		scrollPane.setViewportView(table);
		
		JButton btnBackToMain = new JButton("Back to main menu");
		btnBackToMain.addActionListener(voxModel);
		btnBackToMain.setBackground(new Color(51, 102, 255));
		btnBackToMain.setForeground(new Color(204, 255, 255));
		btnBackToMain.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnBackToMain.setBounds(201, 429, 283, 34);
		panel.add(btnBackToMain);
	}

}
