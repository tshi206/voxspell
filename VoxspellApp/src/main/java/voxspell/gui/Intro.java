package voxspell.gui;


/**
 * Simple one-off GUI showing the introduction of the application.
 * @author mason23
 *
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxspell.toolbox.VoxDatabase;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class Intro extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Intro() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 520);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIntroduction = new JLabel("Introduction");
		lblIntroduction.setForeground(new Color(102, 0, 255));
		lblIntroduction.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
		lblIntroduction.setHorizontalAlignment(SwingConstants.CENTER);
		lblIntroduction.setBounds(12, 12, 426, 45);
		contentPane.add(lblIntroduction);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 69, 404, 411);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		try {
			Scanner scanner = new Scanner(new FileReader(VoxDatabase.helpDirectory+"introduction"));
			String content = "";
			while (scanner.hasNext()){
				String line = scanner.nextLine();
				content = content+line+"\n";
			}
			scanner.close();
			textArea.setText(content);
			textArea.setEditable(false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
