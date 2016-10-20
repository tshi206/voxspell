package voxspell.gui;



import java.awt.Color;

import voxspell.toolbox.Festival;
import voxspell.toolbox.VoiceChoice;
import voxspell.toolbox.VoxDatabase;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingConstants;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Settings extends JFrame implements WindowListener{
	
	private static Settings settings = null;
	
	private String selectedCategory;
	private String voiceChoice;
	private String voiceSpeed;
	
	private JComboBox<String> category;
	private JComboBox<String> choice;
	private JComboBox<String> speed;
	
	private String[] previousSettings = null;
	
	public static Settings getSettingsWindow(){
		if (settings == null){
			settings = new Settings();
		}
		return settings;
	}

	/**
	 * Create the frame.
	 */
	private Settings() {
		setAlwaysOnTop (true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(false);
		
		setTitle("Settings");
		setBackground(new Color(204, 255, 255));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setForeground(new Color(102, 0, 255));
		panel.setBackground(new Color(204, 255, 255));
		panel.setBounds(0, 0, 450, 300);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblSelectCategory = new JLabel("Select Category:");
		lblSelectCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectCategory.setForeground(new Color(102, 0, 255));
		lblSelectCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblSelectCategory.setBounds(10, 11, 195, 31);
		panel.add(lblSelectCategory);
		
		
		final JComboBox<String> comboBox = new JComboBox<String>(VoxDatabase.getCategories().toArray(new String[VoxDatabase.getCategories().size()]));
		comboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String choice = comboBox.getItemAt(comboBox.getSelectedIndex());
				selectedCategory = choice;
				VoxDatabase.setSelectedCategory(VoxDatabase.getLevelContents().get(VoxDatabase.getCategories().indexOf(choice)));
			}			
		});
		comboBox.setBounds(215, 11, 203, 31);
		comboBox.setSelectedIndex(0);
		panel.add(comboBox);
		category = comboBox;
		
		JLabel lblVoice = new JLabel("Voice:");
		lblVoice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVoice.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblVoice.setForeground(new Color(102, 0, 255));
		lblVoice.setBounds(10, 53, 195, 31);
		panel.add(lblVoice);
		
		String[] voices = {"Voice One","Voice Two"};
		final JComboBox<String> comboBox_1 = new JComboBox<String>(voices);
		comboBox_1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String choice = comboBox_1.getItemAt(comboBox_1.getSelectedIndex());
				voiceChoice = choice;
				if (choice.equals("Voice One")){
					VoiceChoice.getVoiceChoice().setChoice("one");
					Festival.festivalGenerator("Welcome to Vox spell");
				}else{
					VoiceChoice.getVoiceChoice().setChoice("two");
					Festival.festivalGenerator("Welcome to Vox spell");
				}
			}			
		});
		comboBox_1.setBounds(215, 53, 203, 31);
		comboBox_1.setSelectedIndex(0);
		panel.add(comboBox_1);
		choice = comboBox_1;
		
		JLabel lblVoiceSpeed = new JLabel("Voice Speed:");
		lblVoiceSpeed.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblVoiceSpeed.setForeground(new Color(102, 0, 255));
		lblVoiceSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVoiceSpeed.setBounds(10, 95, 195, 31);
		panel.add(lblVoiceSpeed);
		
		String[] speeds = {"Slow","Normal","Fast"};
		final JComboBox<String> comboBox_2 = new JComboBox<String>(speeds);
		comboBox_2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String choice = comboBox_2.getItemAt(comboBox_2.getSelectedIndex());
				voiceSpeed = choice;
				//TODO - speed implementation
			}
		});
		comboBox_2.setBounds(215, 95, 203, 31);
		panel.add(comboBox_2);
		speed = comboBox_2;
		
		JButton btnImport = new JButton("Import Category");
		btnImport.setForeground(new Color(102, 0, 255));
		btnImport.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnImport.setBounds(115, 137, 195, 31);
		panel.add(btnImport);
		
		JButton btnCreateCategory = new JButton("Create Category");
		btnCreateCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				CreateCategory cc = new CreateCategory();
				cc.setVisible(true);
			}
		});
		btnCreateCategory.setForeground(new Color(102, 0, 255));
		btnCreateCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnCreateCategory.setBounds(116, 179, 194, 31);
		panel.add(btnCreateCategory);
		
		JButton btnBackToMain = new JButton("Save and close");
		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previousSettings = new String[3];
				previousSettings[0] = selectedCategory;
				previousSettings[1] = voiceChoice;
				previousSettings[2] = voiceSpeed;
				settings.setVisible(false);
			}
		});
		btnBackToMain.setFont(new Font("Comic Sans MS", Font.BOLD, 17));
		btnBackToMain.setForeground(new Color(102, 0, 255));
		btnBackToMain.setBounds(116, 216, 194, 34);
		panel.add(btnBackToMain);

		addWindowListener(this);
	}

	
	public JComboBox<String> getCategory() {
		return category;
	}

	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		int exit = JOptionPane.showConfirmDialog(settings, "Close this page without saving any changes?", "Comfirm operation", JOptionPane.YES_NO_OPTION);
		if (exit == JOptionPane.YES_OPTION){
			if (previousSettings == null){
				category.setSelectedIndex(0);
				choice.setSelectedIndex(0);
				speed.setSelectedIndex(0);
			}else{
				category.setSelectedItem(previousSettings[0]);
				choice.setSelectedItem(previousSettings[1]);
				speed.setSelectedItem(previousSettings[2]);
			}
			settings.setVisible(false);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
