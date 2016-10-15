package voxspell.gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class EndOfCategory extends JFrame {

	private JPanel contentPane;
	private JFrame endOfCategory = this;

	/**
	 * Create the frame.
	 */
	public EndOfCategory(int wordCorrect, int wordFailed, NewGame newGame) {
		setBackground(new Color(204, 255, 255));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 553, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 255, 255));
		panel.setBounds(0, 0, 547, 371);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblLevelComplete = new JLabel("LEVEL COMPLETE!");
		lblLevelComplete.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
		lblLevelComplete.setForeground(new Color(102, 0, 255));
		lblLevelComplete.setHorizontalAlignment(SwingConstants.CENTER);
		lblLevelComplete.setBounds(10, 11, 527, 62);
		panel.add(lblLevelComplete);
		
		JLabel lblAndYourPerformance = new JLabel("and your performance is:");
		lblAndYourPerformance.setHorizontalAlignment(SwingConstants.CENTER);
		lblAndYourPerformance.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblAndYourPerformance.setForeground(new Color(0, 0, 0));
		lblAndYourPerformance.setBounds(131, 84, 279, 25);
		panel.add(lblAndYourPerformance);
		
		JLabel lblResult = new JLabel("");
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblResult.setFont(new Font("Comic Sans MS", Font.BOLD, 86));
		lblResult.setForeground(new Color(255, 0, 0));
		lblResult.setBounds(10, 113, 527, 135);
		panel.add(lblResult);
		
		JLabel lblMastered = new JLabel("Mastered: ");
		lblMastered.setHorizontalAlignment(SwingConstants.CENTER);
		lblMastered.setForeground(new Color(102, 0, 255));
		lblMastered.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblMastered.setBounds(10, 259, 173, 25);
		panel.add(lblMastered);
		
		JLabel lblFaulted = new JLabel("Faulted: ");
		lblFaulted.setHorizontalAlignment(SwingConstants.CENTER);
		lblFaulted.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblFaulted.setForeground(new Color(102, 0, 255));
		lblFaulted.setBounds(193, 259, 173, 25);
		panel.add(lblFaulted);
		
		JLabel lblFailed = new JLabel("Failed: ");
		lblFailed.setHorizontalAlignment(SwingConstants.CENTER);
		lblFailed.setForeground(new Color(102, 0, 255));
		lblFailed.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblFailed.setBounds(376, 259, 161, 25);
		panel.add(lblFailed);
		
		JLabel lblYouHaveUnlocked = new JLabel("You have unlocked your video reward!");
		lblYouHaveUnlocked.setForeground(new Color(255, 0, 0));
		lblYouHaveUnlocked.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblYouHaveUnlocked.setHorizontalAlignment(SwingConstants.CENTER);
		lblYouHaveUnlocked.setBounds(83, 295, 382, 25);
		panel.add(lblYouHaveUnlocked);
		
		JButton btnBack = new JButton("Back to quiz mode");
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				endOfCategory.dispose();
				//TODO
			}			
		});
		btnBack.setBackground(new Color(51, 0, 255));
		btnBack.setForeground(new Color(204, 255, 255));
		btnBack.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		btnBack.setBounds(131, 331, 279, 29);
		panel.add(btnBack);
	}

}
