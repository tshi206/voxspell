package voxspell.gui;


import javax.swing.JFrame;
import javax.swing.JPanel;

import voxspell.app.NewGameModel;
import voxspell.toolbox.WordsCounter;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class NewGame extends WindowPattern implements KeyListener{

	private JTextField textField;
	private static NewGame newGameGUI = null;
	
	private JLabel CurrentCategory;
	protected JButton submit;
	protected JButton rehear;
	protected JButton re;
	protected JTextArea hint;
	protected JProgressBar progressBar;
	protected JLabel accuracy;
	protected JPanel endOfLevelPanel;
	protected JButton retryCategory;
	protected JButton nextCategory;
	protected JButton videoReward;
	
	private ActionListener model = null;
	
	
	/**
	 * Create the frame.
	 */
	private NewGame() {
		super();
	}
	
	public static NewGame getNewGameWindow(){
		if (newGameGUI == null){
			newGameGUI = new NewGame();
		}
		return newGameGUI;
	}
	
	@Override
	void paintWindow() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JLabel lblQuizMode = new JLabel("Quiz Mode");
		lblQuizMode.setForeground(new Color(102, 0, 255));
		lblQuizMode.setFont(new Font("Comic Sans MS", Font.BOLD, 58));
		lblQuizMode.setHorizontalTextPosition(SwingConstants.CENTER);
		lblQuizMode.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuizMode.setBounds(163, 23, 403, 113);
		panel.add(lblQuizMode);
		
		JLabel lblCurrentCategory = new JLabel("Current category: ");
		lblCurrentCategory.setForeground(new Color(102, 0, 255));
		lblCurrentCategory.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblCurrentCategory.setBounds(163, 137, 403, 28);
		panel.add(lblCurrentCategory);
		CurrentCategory = lblCurrentCategory;
		
		JLabel lblRateOfCorrectness = new JLabel("Rate of correctness so far: "+WordsCounter.getWordsCounter().getAccuracy()+"%");
		lblRateOfCorrectness.setForeground(new Color(102, 0, 255));
		lblRateOfCorrectness.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblRateOfCorrectness.setBounds(163, 183, 403, 28);
		panel.add(lblRateOfCorrectness);
		accuracy = lblRateOfCorrectness;
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(new Color(204, 255, 255));
		inputPanel.setBounds(23, 294, 672, 70);
		panel.add(inputPanel);
		inputPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(140, 0, 403, 70);
		inputPanel.add(textField);
		textField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		textField.setColumns(10);
		textField.addKeyListener(this);
		textField.grabFocus();

		JButton startandsubmit = new JButton("Start!");
		startandsubmit.setBounds(555, 0, 117, 70);
		inputPanel.add(startandsubmit);
		startandsubmit.setBackground(new Color(51, 102, 255));
		startandsubmit.setForeground(new Color(204, 255, 255));
		startandsubmit.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		submit = startandsubmit;
		
		JButton rehear = new JButton("Rehear");
		rehear.setBounds(0, 0, 128, 70);
		inputPanel.add(rehear);
		rehear.setBackground(new Color(51, 102, 255));
		rehear.setForeground(new Color(204, 255, 255));
		rehear.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		this.rehear = rehear;
		
		JButton btnBackToMain = new JButton("Back to main menu");
		btnBackToMain.addActionListener(voxModel);
		btnBackToMain.setForeground(new Color(204, 255, 255));
		btnBackToMain.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		btnBackToMain.setBackground(new Color(51, 102, 255));
		btnBackToMain.setBounds(252, 445, 214, 25);
		panel.add(btnBackToMain);
		re = btnBackToMain;
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBorderPainted(false);
		progressBar.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		progressBar.setBackground(new Color(204, 255, 255));
		progressBar.setMaximum(10);
		progressBar.setString("0");
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(51, 255, 0));
		progressBar.setBounds(163, 376, 403, 28);
		panel.add(progressBar);
		this.progressBar = progressBar;
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		scrollPane.setBounds(163, 223, 403, 59);
		panel.add(scrollPane);
		
		JTextArea txtrHint = new JTextArea();
		txtrHint.setEditable(false);
		txtrHint.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		txtrHint.setText("Hint: ");
		scrollPane.setViewportView(txtrHint);
		hint = txtrHint;
		
		JPanel endOfLevelPanel = new JPanel();
		endOfLevelPanel.setVisible(false);
		endOfLevelPanel.setBackground(new Color(204, 255, 255));
		endOfLevelPanel.setBounds(130, 402, 464, 41);
		panel.add(endOfLevelPanel);
		endOfLevelPanel.setLayout(null);
		this.endOfLevelPanel = endOfLevelPanel;
		
		JButton btnRetryCategory = new JButton("Retry category");
		btnRetryCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
		btnRetryCategory.setForeground(new Color(204, 255, 255));
		btnRetryCategory.setBackground(new Color(51, 102, 255));
		btnRetryCategory.setBounds(12, 12, 139, 25);
		endOfLevelPanel.add(btnRetryCategory);
		retryCategory = btnRetryCategory;
		
		JButton btnNextCategory = new JButton("Next Category");
		btnNextCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
		btnNextCategory.setBackground(new Color(51, 102, 255));
		btnNextCategory.setForeground(new Color(204, 255, 255));
		btnNextCategory.setBounds(163, 12, 139, 25);
		endOfLevelPanel.add(btnNextCategory);
		nextCategory = btnNextCategory; 
		
		JButton btnVideoReward = new JButton("Video Reward");
		btnVideoReward.addActionListener(voxModel);
		btnVideoReward.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
		btnVideoReward.setForeground(new Color(204, 255, 255));
		btnVideoReward.setBackground(new Color(51, 102, 255));
		btnVideoReward.setBounds(314, 12, 138, 25);
		endOfLevelPanel.add(btnVideoReward);
		videoReward = btnVideoReward;
		
	}

	
	public JLabel getCurrentCategory() {
		return CurrentCategory;
	}

	private void addActionListener(ActionListener model) {
		submit.addActionListener(model);
		rehear.addActionListener(model);
		re.addActionListener(model);
		retryCategory.addActionListener(model);
		nextCategory.addActionListener(model);
	}
	
	private void removeActionListener(ActionListener model) {
		submit.removeActionListener(model);
		rehear.removeActionListener(model);
		re.removeActionListener(model);
		retryCategory.removeActionListener(model);
		nextCategory.removeActionListener(model);
	}

	public JButton getVideoReward() {
		return videoReward;
	}

	public JButton getNextCategory() {
		return nextCategory;
	}

	public JButton getRetryCategory() {
		return retryCategory;
	}

	public JPanel getEndOfLevelPanel() {
		return endOfLevelPanel;
	}

	public JLabel getAccuracy() {
		return accuracy;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JTextField getTextField() {
		return textField;
	}

	public JTextArea getHint() {
		return hint;
	}

	public JButton getSubmit() {
		return submit;
	}

	public JButton getRehear() {
		return rehear;
	}

	public JButton getBackToMain() {
		return re;
	}
	
	public void updateModel(ArrayList<String> newCategory){
		if (model != null){
			removeActionListener(model);
		}
		model = new NewGameModel(newCategory);
		addActionListener(model);
	}

	public NewGameModel getModel() {
		return (NewGameModel)model;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
	        submit.doClick();
	    }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
