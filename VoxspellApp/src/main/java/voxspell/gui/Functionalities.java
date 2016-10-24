package voxspell.gui;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import voxspell.toolbox.VoxDatabase;

/**
 * Simple one-off GUI showing user manual regarding system's functionalities
 * @author mason23
 *
 */
@SuppressWarnings("serial")
public class Functionalities extends JFrame {

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public Functionalities() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 520);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIntroduction = new JLabel("Functionalities");
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
			Scanner scanner = new Scanner(new FileReader(VoxDatabase.helpDirectory+"functionalities"));
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
