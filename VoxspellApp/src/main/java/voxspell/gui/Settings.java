package voxspell.gui;


import javax.swing.JInternalFrame;

import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Settings extends JFrame {
	
	private static Settings settings = null;
	
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
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setLocationRelativeTo(null);
		setResizable(false);
		
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
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(215, 11, 203, 31);
		panel.add(comboBox);
		
		JLabel lblVoice = new JLabel("Voice:");
		lblVoice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVoice.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblVoice.setForeground(new Color(102, 0, 255));
		lblVoice.setBounds(10, 53, 195, 31);
		panel.add(lblVoice);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(215, 53, 203, 31);
		panel.add(comboBox_1);
		
		JLabel lblVoiceSpeed = new JLabel("Voice Speed:");
		lblVoiceSpeed.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblVoiceSpeed.setForeground(new Color(102, 0, 255));
		lblVoiceSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVoiceSpeed.setBounds(10, 95, 195, 31);
		panel.add(lblVoiceSpeed);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(215, 95, 203, 31);
		panel.add(comboBox_2);
		
		JButton btnImport = new JButton("Import Category");
		btnImport.setForeground(new Color(102, 0, 255));
		btnImport.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnImport.setBounds(115, 137, 195, 31);
		panel.add(btnImport);
		
		JButton btnCreateCategory = new JButton("Create Category");
		btnCreateCategory.setForeground(new Color(102, 0, 255));
		btnCreateCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnCreateCategory.setBounds(116, 179, 194, 31);
		panel.add(btnCreateCategory);
		
		JButton btnBackToMain = new JButton("Save and close");
		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settings.setVisible(false);;
			}
		});
		btnBackToMain.setFont(new Font("Comic Sans MS", Font.BOLD, 17));
		btnBackToMain.setForeground(new Color(102, 0, 255));
		btnBackToMain.setBounds(116, 216, 194, 34);
		panel.add(btnBackToMain);

	}
}
