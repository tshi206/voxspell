package voxspell.gui;



import java.awt.Color;

import voxspell.toolbox.Festival;
import voxspell.toolbox.ImportDefinitionsWorker;
import voxspell.toolbox.ImportListWorker;
import voxspell.toolbox.TXTFilter;
import voxspell.toolbox.VoiceChoice;
import voxspell.toolbox.VoxDatabase;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SwingConstants;
import javax.swing.JButton;

/**
 * This is not a WindowPattern nor a one-off GUI window.
 * It itself implements the logic of managing all sub-setting-windows.
 * Therefore it is the model for itself.
 * However, actual implementation of file I/O is always done via VoxDatabase or via relevant classes in voxspell.toolbox package.
 * This GUI just manages all its relative sub-windows (e.g. CreateCategory).
 * It also listens to the background thread (see @ImportListWorker).
 * Logging of settings is also implemented within this class using VoxDatabase tool methods.
 * This class is tightly linked to a couple of background thread and the VoxDatabase.
 * VoxDatabase can be regarded as a super model of this class.
 * @author mason23
 *
 */
@SuppressWarnings("serial")
public class Settings extends JFrame implements WindowListener, PropertyChangeListener{
	
	private boolean hasDefinitions = false;
	private ArrayList<File> _definitionsCandidates = null;
	
	private Settings itself = this;
	
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
					JOptionPane.showMessageDialog(itself, "Due to the amount of system's vocabulary,\n"
							+ "some words might not be pronounced by Voice Two.\n"
							+ "If that's the case, please change back to Voice One which has a larger amount of supporting vocabulary.", "Notice", JOptionPane.INFORMATION_MESSAGE);
				}
			}			
		});
		comboBox_1.setBounds(215, 53, 203, 31);
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
				VoiceChoice.getVoiceChoice().setSpeed(choice);
			}
		});
		comboBox_2.setBounds(215, 95, 203, 31);
		panel.add(comboBox_2);
		speed = comboBox_2;

		JButton btnImport = new JButton("Import Category");
		btnImport.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser(VoxDatabase.wordlistsDirectory);
				fc.setAcceptAllFileFilterUsed(false);
				fc.setMultiSelectionEnabled(true);
				fc.addChoosableFileFilter(new TXTFilter());
				int returnVal = fc.showOpenDialog(itself);

				ArrayList<File> wordsCandidates = new ArrayList<File>();
				ArrayList<File> definitionsCandidates = new ArrayList<File>();
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					
					File[] files = fc.getSelectedFiles();
					for (File file : files){
						try {
							
							if (file.getName().endsWith("_def.txt")){
								
								File temp = new File(VoxDatabase.wordlistsDirectory+"."+file.getName());
								if (temp.exists()){
									temp.delete();
								}

								definitionsCandidates.add(file);
								
							}else{

								boolean importFailed = false;

								File temp = new File(VoxDatabase.wordlistsDirectory+"."+file.getName());
								if (temp.exists()){
									JOptionPane.showMessageDialog(itself, "This file ("+file.getName()+") has already been included in the program's directory therefore cannot be imported.\n"
											+ "If you do want to import please either rename the file or remove all relevant categories via\n"
											+ "Help ---> Delete imported category", "Warning", JOptionPane.WARNING_MESSAGE);
									continue;
								}
								
								Scanner scanner = new Scanner(new FileReader(file));
								while (scanner.hasNext()){
									String line = scanner.nextLine();
									if (line.equals("")){
										continue;
									}
									if (line.startsWith("%")){
										String str = line.substring(1);
										if (VoxDatabase.getCategories().contains(str)){
											JOptionPane.showMessageDialog(itself, "A category with the same name ("+str+") has already been created in the application therefore this file cannot be imported.\n"
													+ "If you do want to import please either rename the categories containing this file or remove all relevant categories via\n"
													+ "Help ---> Delete imported category", "Warning", JOptionPane.WARNING_MESSAGE);
											scanner.close();
											importFailed = true;
											break;
										}
									}
								}
								scanner.close();
								
								if (importFailed){
									continue;
								}
								
								wordsCandidates.add(file);
							}
							

						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
					if (!(definitionsCandidates.isEmpty())){
						hasDefinitions = true;
						_definitionsCandidates = definitionsCandidates;
					}
					
					if (!(wordsCandidates.isEmpty())){
						ArrayList<File> filesToWriteTo = new ArrayList<File>();
						for (File file : wordsCandidates){
							File temp = VoxDatabase.createWordsFile(file.getName());
							filesToWriteTo.add(temp);
						}
						ImportListWorker ilw = new ImportListWorker(filesToWriteTo, wordsCandidates);
						ilw.execute();
					}else{
						if (hasDefinitions){
							startLoadingDefinitions();
						}
					}
					
				}
			}
		});
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
				updateSettings(VoxDatabase.getSysfiles().get(2), selectedCategory, voiceChoice, voiceSpeed);
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

	
	/**
	 * USE WHEN SETTINGS ARE CHANGED
	 * @param configFile - 2nd IN SYSFILES
	 */
	public void updateSettings(File configFile, String categoryName, String voiceName, String speedLevel){
		
		if (!(configFile.exists())){
			JOptionPane.showMessageDialog(itself, "Cannot find settings file: updating usr's settings failed");
			return;
		}
		
		String settingsContent = categoryName+"\n"+voiceName+"\n"+speedLevel+"\n";
		
		try {
			FileWriter fw = new FileWriter(configFile, false);
			fw.write(settingsContent);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * USE WHEN PROJECT IS LOADING OR USER IS CHANGED
	 * @param configFile - 2nd IN SYSFILES
	 */
	public void loadSettings(ArrayList<String> configs){

		boolean haveSettings = false;
		String categoryName = "";
		String voiceName = "";
		String speedLevel = "";
		if (!(configs.isEmpty())){
			categoryName = configs.get(0);
			voiceName = configs.get(1);
			speedLevel = configs.get(2);
			haveSettings = true;
		}

		previousSettings = new String[3];
		if (haveSettings){
			category.setSelectedItem(categoryName);
			speed.setSelectedItem(speedLevel);
			choice.setSelectedItem(voiceName);
			previousSettings[0] = selectedCategory;
			previousSettings[1] = voiceChoice;
			previousSettings[2] = voiceSpeed;
		}else{
			category.setSelectedIndex(0);
			speed.setSelectedIndex(1);
			choice.setSelectedIndex(0);
			previousSettings[0] = selectedCategory;
			previousSettings[1] = voiceChoice;
			previousSettings[2] = voiceSpeed;
		}

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
				speed.setSelectedIndex(1);
				choice.setSelectedIndex(0);
			}else{
				category.setSelectedItem(previousSettings[0]);
				speed.setSelectedItem(previousSettings[2]);
				choice.setSelectedItem(previousSettings[1]);
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (hasDefinitions){
			
			startLoadingDefinitions();
			
		}
		
	}
	
	private void startLoadingDefinitions(){
		
		if (_definitionsCandidates != null){
			
			ArrayList<File> filesToWriteTo = new ArrayList<File>();
			for (File file : _definitionsCandidates){
				File temp = VoxDatabase.createWordsFile(file.getName());
				filesToWriteTo.add(temp);
			}
			ImportDefinitionsWorker idw = new ImportDefinitionsWorker(filesToWriteTo, _definitionsCandidates);
			idw.execute();
			
		}
		
	}
	
}
