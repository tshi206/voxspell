package voxspell.gui;

import java.awt.Color;


import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class Stats extends WindowPattern {

	private JTable table;

	private static Stats statsGUI = null;

	/**
	 * Create the frame.
	 */
	private Stats() {
		super();
	}

	public static Stats getStatsWindow(){
		if (statsGUI == null){
			statsGUI = new Stats();
		}
		return statsGUI;
	}

	@Override
	void paintWindow() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JLabel lblYourStats = new JLabel("Your Stats");
		lblYourStats.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourStats.setFont(new Font("Comic Sans MS", Font.BOLD, 58));
		lblYourStats.setForeground(new Color(102, 0, 255));
		lblYourStats.setBounds(191, 37, 325, 79);
		panel.add(lblYourStats);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(204, 255, 255));
		scrollPane.setBounds(112, 115, 573, 303);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		table.setRowSelectionAllowed(false);
		table.setShowGrid(false);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setBackground(new Color(255, 255, 0));
		table.setForeground(new Color(51, 0, 255));
		table.setGridColor(new Color(255, 255, 255));
		scrollPane.setViewportView(table);
		
		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setForeground(new Color(102, 0, 255));
		lblCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblCategory.setBounds(10, 113, 92, 34);
		panel.add(lblCategory);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBox.setBounds(10, 158, 92, 34);
		panel.add(comboBox);
		
		JButton btnBackToMain = new JButton("Back to main menu");
		btnBackToMain.addActionListener(voxModel);
		btnBackToMain.setBackground(new Color(51, 102, 255));
		btnBackToMain.setForeground(new Color(204, 255, 255));
		btnBackToMain.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnBackToMain.setBounds(201, 429, 283, 34);
		panel.add(btnBackToMain);
	}
}
