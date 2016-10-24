package voxspell.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import voxspell.toolbox.CreateCategoryWorker;
import voxspell.toolbox.VoxDatabase;

import javax.swing.JComboBox;


/**
 * This is not a WindowPattern GUI meaning that its use must be one-off.
 * It's responsible for managing the GUI in order to allow users to edit their imported categories.
 * Its instantiation must be invoked in the menu bar under Help ---> Edit imported category.
 * It reuses the CreateCategoryWorker to generate the new category after edition.
 * It will first delete the old one and apply the same logic to check the validity of the new submission.
 * If the new one is failed to submit, the old version of the category will be restored.
 * @author mason23
 *
 */
@SuppressWarnings("serial")
public class EditImportedFiles extends JFrame implements WindowListener, ActionListener{

	private JFrame itself = this;
	private JPanel contentPane;
	private JComboBox<String> comboBox;
	private JTextArea words;
	private JTextArea definitions;

	/**
	 * Create the frame.
	 */
	public EditImportedFiles() {
		setBackground(new Color(204, 255, 255));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 474, 450);
		setLocationRelativeTo(null);
		setAlwaysOnTop (true);
		setResizable(false);
		
		addWindowListener(this);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 255, 255));
		panel.setBounds(0, 0, 474, 450);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblCreateCategory = new JLabel("Edit Category");
		lblCreateCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
		lblCreateCategory.setForeground(new Color(102, 0, 255));
		lblCreateCategory.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreateCategory.setBounds(71, 11, 317, 66);
		panel.add(lblCreateCategory);
		
		JLabel lblWordsonePer = new JLabel("Words (one per line)");
		lblWordsonePer.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		lblWordsonePer.setBounds(10, 114, 135, 14);
		panel.add(lblWordsonePer);
		
		JLabel lblDefinitionsonePer = new JLabel("Corresponding definitions (one per line)");
		lblDefinitionsonePer.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		lblDefinitionsonePer.setBounds(228, 114, 226, 14);
		panel.add(lblDefinitionsonePer);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 139, 208, 205);
		panel.add(scrollPane);
		
		final JTextArea words = new JTextArea();
		scrollPane.setViewportView(words);
		this.words = words;
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(228, 139, 226, 205);
		panel.add(scrollPane_1);
		
		final JTextArea definitions = new JTextArea();
		scrollPane_1.setViewportView(definitions);
		this.definitions = definitions;
		
		JLabel lbldefinitionsFieldCan = new JLabel("(Definitions field can be left blank)");
		lbldefinitionsFieldCan.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		lbldefinitionsFieldCan.setBounds(228, 342, 226, 14);
		panel.add(lbldefinitionsFieldCan);
		
		JLabel lblminimumNumberOf = new JLabel("*Minimum number of words is ten");
		lblminimumNumberOf.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		lblminimumNumberOf.setBounds(10, 342, 196, 14);
		panel.add(lblminimumNumberOf);
		
		JButton btnSubmitAndClose = new JButton("Submit and close");
		btnSubmitAndClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((words.getText().equals(""))||(words.getText().matches("^\\d+$"))||(words.getText().matches("^\\s+$")))){
					JOptionPane.showMessageDialog(itself, "No Words found", "Warning", JOptionPane.OK_OPTION);
				}else{
					String temp = words.getText();
					String[] ta = temp.split("\\n+");
					if (ta.length<10){
						JOptionPane.showMessageDialog(itself, "Minimum number of words must be ten", "Warning", JOptionPane.OK_OPTION);
					}else{
						
						String targetFile = "."+comboBox.getItemAt(comboBox.getSelectedIndex());
						ArrayList<String> currentCategories = new ArrayList<String>();
						if (VoxDatabase.getCategories().contains(targetFile.substring(1))){
							currentCategories.add(targetFile.substring(1));
						}

						try {
							Scanner scanner = new Scanner(new FileReader(VoxDatabase.wordlistsDirectory+targetFile));
							while (scanner.hasNext()){
								String line = scanner.nextLine();
								if (line.startsWith("%")){
									String s = line.substring(1);
									if (VoxDatabase.getCategories().contains(s)){
										currentCategories.add(s);
									}
								}
							}
							scanner.close();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						
						
						if (!(currentCategories.isEmpty())){
							ArrayList<ArrayList<String>> backupLevelContents = new ArrayList<ArrayList<String>>();
							for (String name : currentCategories){
								backupLevelContents.add(VoxDatabase.getLevelContents().get(VoxDatabase.getCategories().indexOf(name)));
							}
							
							for (int i = 0 ; i<currentCategories.size(); i++){
								VoxDatabase.deleteCategory(backupLevelContents.get(i), currentCategories.get(i));
							}
							
							if (VoxDatabase.getCategories().contains(targetFile.substring(1))){
								JOptionPane.showMessageDialog(itself, "Creation fails: A category with the same name ("+targetFile.substring(1)+") already exists", "Warning", JOptionPane.OK_OPTION);
								for (int i = 0 ; i<currentCategories.size(); i++){
									VoxDatabase.addCategory(backupLevelContents.get(i), currentCategories.get(i));
								}
								return;
							}else{
								for (String word : ta){
									if (word.startsWith("%")){
										word = word.substring(1);
										if (VoxDatabase.getCategories().contains(word)){
											JOptionPane.showMessageDialog(itself, "Creation fails: A category with the same name ("+word+") already exists", "Warning", JOptionPane.OK_OPTION);
											for (int i = 0 ; i<currentCategories.size(); i++){
												VoxDatabase.addCategory(backupLevelContents.get(i), currentCategories.get(i));
											}
											return;
										}
									}
								}
							}
							
							boolean hasDefinition = false;
							if (!(definitions.getText().matches("\\s*"))){
								hasDefinition = true;
							}

							VoxDatabase.writeToWordsFile(targetFile.substring(1), words.getText()+"\n", false);

							if (hasDefinition){
								File def = new File(VoxDatabase.wordlistsDirectory+targetFile.substring(0, targetFile.length()-".txt".length())+"_def.txt");
								if (!(def.exists())){
									VoxDatabase.createWordsFile(targetFile.substring(1, targetFile.length()-".txt".length())+"_def.txt");
								}
								VoxDatabase.writeToWordsFile(targetFile.substring(1, targetFile.length()-".txt".length())+"_def.txt", definitions.getText()+"\n", false);
							}else{
								File def = new File(VoxDatabase.wordlistsDirectory+targetFile.substring(0, targetFile.length()-".txt".length())+"_def.txt");
								if (def.exists()){
									def.delete();
								}
							}
							
							CreateCategoryWorker ccw = new CreateCategoryWorker(new File(VoxDatabase.wordlistsDirectory+targetFile));
							ccw.execute();

							itself.dispose();
							
						}else{
							JOptionPane.showMessageDialog(itself, "This is not a valid words file. Please have a look at\n"
									+ "Help ---> functionalities ---> files format");
						}

					}
				}
			}
		});
		btnSubmitAndClose.setForeground(new Color(102, 0, 255));
		btnSubmitAndClose.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		btnSubmitAndClose.setBounds(127, 367, 206, 23);
		panel.add(btnSubmitAndClose);
		
		JLabel lblName = new JLabel("File name:");
		lblName.setFont(new Font("Comic Sans MS", Font.PLAIN, 17));
		lblName.setBounds(60, 77, 108, 23);
		panel.add(lblName);
		
		

		ArrayList<String> importedLists = new ArrayList<String>();

		try {
			Scanner scanner = new Scanner(new FileReader(VoxDatabase.getSysfiles().get(VoxDatabase.getSysfiles().size()-1)));

			while (scanner.hasNext()){
				String line = scanner.nextLine();
				line = line.substring(1);
				importedLists.add(line);
			}
			
			scanner.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		final JComboBox<String> comboBox;
		if (importedLists.isEmpty()){
			comboBox = new JComboBox<String>();
			comboBox.setEnabled(false);
		}else{
			comboBox = new JComboBox<String>(importedLists.toArray(new String[importedLists.size()]));
			comboBox.addActionListener(this);
		}
		comboBox.setBounds(157, 76, 270, 24);
		panel.add(comboBox);
		this.comboBox = comboBox;
		
		if (importedLists.isEmpty()){
			btnSubmitAndClose.setEnabled(false);
		}else{
			comboBox.setSelectedIndex(0);
		}
		
		JLabel background = new JLabel(new ImageIcon(VoxDatabase.picsDirectory+"sea.png"));
		background.setBounds(0, 0, 474, 450);
		panel.add(background);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		MainMenu.getmainMenuWindow().getDelete().setEnabled(true);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		String targetFile = "."+comboBox.getItemAt(comboBox.getSelectedIndex());
		File wordsFile = new File(VoxDatabase.wordlistsDirectory+targetFile);
		File definitionsFile = new File(VoxDatabase.wordlistsDirectory+targetFile.substring(0, targetFile.length()-".txt".length())+"_def.txt");

		try {
			
			if (wordsFile.exists()){
				Scanner scanner = new Scanner(new FileReader(wordsFile));
				String content = "";
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					content = content + line + "\n";
				}
				
				words.setText(content);
				scanner.close();
				
			}
			
			if (definitionsFile.exists()){
				Scanner scanner = new Scanner(new FileReader(definitionsFile));
				String content = "";
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					content = content + line + "\n";
				}
				
				definitions.setText(content);
				scanner.close();
			}else{
				definitions.setText("");
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}
}

