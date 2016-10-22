package voxspell.gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxspell.toolbox.CreateCategoryWorker;
import voxspell.toolbox.VoxDatabase;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class CreateCategory extends JFrame {

	private JFrame itself = this;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public CreateCategory() {
		
		setBackground(new Color(204, 255, 255));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 440);
		setLocationRelativeTo(null);
		setAlwaysOnTop (true);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 255, 255));
		panel.setBounds(0, 0, 480, 440);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblCreateCategory = new JLabel("Create Category");
		lblCreateCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
		lblCreateCategory.setForeground(new Color(102, 0, 255));
		lblCreateCategory.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreateCategory.setBounds(71, 11, 317, 66);
		panel.add(lblCreateCategory);
		
		JLabel lblWordsonePer = new JLabel("Words (one per line)");
		lblWordsonePer.setBounds(10, 86, 135, 14);
		panel.add(lblWordsonePer);
		
		JLabel lblDefinitionsonePer = new JLabel("Corresponding definitions (one per line)");
		lblDefinitionsonePer.setBounds(228, 86, 226, 14);
		panel.add(lblDefinitionsonePer);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 111, 208, 205);
		panel.add(scrollPane);
		
		final JTextArea words = new JTextArea();
		scrollPane.setViewportView(words);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(228, 111, 226, 205);
		panel.add(scrollPane_1);
		
		final JTextArea definitions = new JTextArea();
		scrollPane_1.setViewportView(definitions);
		
		JLabel lbldefinitionsFieldCan = new JLabel("(Definitions field can be left blank)");
		lbldefinitionsFieldCan.setBounds(228, 314, 226, 14);
		panel.add(lbldefinitionsFieldCan);
		
		JLabel lblminimumNumberOf = new JLabel("*Minimum number of words is ten");
		lblminimumNumberOf.setBounds(10, 314, 196, 14);
		panel.add(lblminimumNumberOf);
		
		JButton btnSubmitAndClose = new JButton("Submit and close");
		btnSubmitAndClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((words.getText().equals(""))||(words.getText().matches("^\\d+$"))||(words.getText().matches("^\\s+$")))){
					JOptionPane.showMessageDialog(itself, "No Words found", "Warning", JOptionPane.OK_OPTION);
				}else if (textField.getText().matches("\\s*")){
					JOptionPane.showMessageDialog(itself, "Category must have a name", "Warning", JOptionPane.OK_OPTION);
				}else{
					String temp = words.getText();
					String[] ta = temp.split("\\n+");
					if (ta.length<10){
						JOptionPane.showMessageDialog(itself, "Minimum number of words must be ten", "Warning", JOptionPane.OK_OPTION);
					}else{
						
						if (VoxDatabase.getCategories().contains(textField.getText())){
							JOptionPane.showMessageDialog(itself, "Creation fails: A category with the same name ("+textField.getText()+") already exists", "Warning", JOptionPane.OK_OPTION);
							return;
						}else{
							for (String word : ta){
								if (word.startsWith("%")){
									word = word.substring(1);
									if (VoxDatabase.getCategories().contains(word)){
										JOptionPane.showMessageDialog(itself, "Creation fails: A category with the same name ("+word+") already exists", "Warning", JOptionPane.OK_OPTION);
										return;
									}
								}
							}
						}
						
						boolean hasDefinition = false;
						if (!(definitions.getText().matches("\\s*"))){
							hasDefinition = true;
						}

						File temp1 = VoxDatabase.createWordsFile(textField.getText()+".txt");
						VoxDatabase.writeToSysFile("customizedLists", "."+textField.getText()+".txt"+"\n", true);
						VoxDatabase.writeToWordsFile(textField.getText()+".txt", words.getText()+"\n", false);


						if (hasDefinition){
							VoxDatabase.createWordsFile(textField.getText()+"_def.txt");
							VoxDatabase.writeToWordsFile(textField.getText()+"_def.txt", definitions.getText()+"\n", false);
						}

						CreateCategoryWorker ccw = new CreateCategoryWorker(temp1);
						ccw.execute();

						itself.dispose();

					}
				}
			}
		});
		btnSubmitAndClose.setBackground(new Color(51, 102, 255));
		btnSubmitAndClose.setForeground(new Color(204, 255, 255));
		btnSubmitAndClose.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		btnSubmitAndClose.setBounds(127, 367, 206, 23);
		panel.add(btnSubmitAndClose);
		
		JLabel lblName = new JLabel("Name of category:");
		lblName.setBounds(37, 340, 108, 23);
		panel.add(lblName);
		
		textField = new JTextField();
		textField.setBounds(148, 339, 284, 23);
		panel.add(textField);
		textField.setColumns(10);
		
	}
}
