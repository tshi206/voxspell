package voxspell.gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxspell.toolbox.Festival;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;



/**
 * This is not a WindowPattern GUI meaning that its use must be one-off.
 * It's responsible for managing the GUI in order to allow users to choose their end-of-category options.
 * Its instantiation must be invoked whenever a quiz session ends.
 * It displays the result of the category based on the user's performance.
 * It will allow the user to choose his video reward if the performance is marked as excellent.
 * Otherwise it will just disable the reward option and let the user to decide whether to retry the same category or go back to main menu.
 * An opportunity of going to next category is given if a further category is available and the user performance is 'excellent'.
 * @author mason23
 *
 */
@SuppressWarnings("serial")
public class EndOfCategory extends JFrame {

	private JPanel contentPane;
	private JFrame endOfCategory = this;
	private JLabel result;
	private JLabel mastered;
	private JLabel faulted;
	private JLabel failed;
	private JLabel rewardUnlocked;
	private boolean hasUnlocked = false;

	private NewGame newGame = NewGame.getNewGameWindow();
	
	private int wordsCorrect;
	private int wordsFailed;

	/**
	 * Create the frame.
	 */
	public EndOfCategory(int wordsCorrect, int wordsFailed) {
		setBackground(new Color(204, 255, 255));
		setResizable(false);
		setAlwaysOnTop (true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
		
		this.wordsCorrect = wordsCorrect;
		this.wordsFailed = wordsFailed;
		
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
		result = lblResult;
		
		JLabel lblMastered = new JLabel("Mastered: ");
		lblMastered.setHorizontalAlignment(SwingConstants.CENTER);
		lblMastered.setForeground(new Color(102, 0, 255));
		lblMastered.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblMastered.setBounds(10, 259, 173, 25);
		panel.add(lblMastered);
		mastered = lblMastered;
		
		JLabel lblFaulted = new JLabel("Faulted: ");
		lblFaulted.setHorizontalAlignment(SwingConstants.CENTER);
		lblFaulted.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblFaulted.setForeground(new Color(102, 0, 255));
		lblFaulted.setBounds(193, 259, 173, 25);
		panel.add(lblFaulted);
		faulted = lblFaulted;
		
		JLabel lblFailed = new JLabel("Failed: ");
		lblFailed.setHorizontalAlignment(SwingConstants.CENTER);
		lblFailed.setForeground(new Color(102, 0, 255));
		lblFailed.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblFailed.setBounds(376, 259, 161, 25);
		panel.add(lblFailed);
		failed = lblFailed;
		
		JLabel lblYouHaveUnlocked = new JLabel("You have unlocked your video reward!");
		lblYouHaveUnlocked.setForeground(new Color(255, 0, 0));
		lblYouHaveUnlocked.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblYouHaveUnlocked.setHorizontalAlignment(SwingConstants.CENTER);
		lblYouHaveUnlocked.setBounds(83, 295, 382, 25);
		lblYouHaveUnlocked.setVisible(false);
		panel.add(lblYouHaveUnlocked);
		rewardUnlocked = lblYouHaveUnlocked;
		
		JButton btnBack = new JButton("Back to quiz mode");
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				endOfCategory.dispose();
				newGame.getEndOfLevelPanel().setVisible(true);
				if (hasUnlocked){
					newGame.getVideoReward().setEnabled(true);
					newGame.getNextCategory().setEnabled(true);
				}else if (!(hasUnlocked)){
					newGame.getVideoReward().setEnabled(false);
					newGame.getNextCategory().setEnabled(false);
				}
			}			
		});
		btnBack.setBackground(new Color(51, 0, 255));
		btnBack.setForeground(new Color(204, 255, 255));
		btnBack.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		btnBack.setBounds(131, 331, 279, 29);
		panel.add(btnBack);
		
		calculateStats();
	}

	/**
	 * Calculate the statistics for the session to be displayed.
	 * Showing overall performance for the session based on the suppied statistics.
	 */
	private void calculateStats(){
		if (wordsCorrect >= 8){
			Festival.endOfCategorySound("excellent.mp3");
			result.setText("Excellent!!");
			result.setForeground(Color.GREEN);
			unlockReward(true);
		}else if (wordsCorrect >= 5){
			Festival.endOfCategorySound("average.mp3");
			result.setText("Average");
			result.setForeground(Color.BLACK);
		}else if (wordsCorrect >= 3){
			Festival.endOfCategorySound("notbad.mp3");
			result.setText("...Not bad?");
			result.setForeground(Color.DARK_GRAY);
		}else{
			Festival.endOfCategorySound("poor.mp3");
			result.setText("Poor :)");
			result.setForeground(Color.RED);
		}
		
		mastered.setText("Mastered: "+wordsCorrect);
		faulted.setText("Faulted: "+(10 - wordsCorrect - wordsFailed));
		failed.setText("Failed: "+wordsFailed);
	}
	
	private void unlockReward(boolean flag){
		hasUnlocked = flag;
		rewardUnlocked.setVisible(true);
	}
	
	
	public boolean isRewardUnlocked() {
		return hasUnlocked;
	}

}
