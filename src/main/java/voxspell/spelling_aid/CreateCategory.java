package voxspell.spelling_aid;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateCategory extends JFrame {

	protected String[] fn;
	protected File[] files;
	protected ArrayList<File> sysfiles;
	protected ArrayList<ArrayList<String>> contents;
	protected ArrayList<String> filenames;
	protected ArrayList<ArrayList<String>> levelContents;
	protected String[] levels;
	
	private JPanel contentPane;
	private JTextField textField;
	private JTextArea t1;
	private JTextArea t2;

	private WindowListener wl;
	private SpellingAid frame;
	private JFrame itself = this;

	/**
	 * Create the frame.
	 */
	public CreateCategory(SpellingAid jf, WindowListener wl, File[] files, String[] fn, ArrayList<File> sysf, ArrayList<ArrayList<String>> contents,
			ArrayList<String> names, ArrayList<ArrayList<String>> levelC, String[] levels) {
		this.fn = fn;
		this.files = files;
		this.sysfiles = sysf;
		this.contents = contents;
		this.filenames = names;
		this.levelContents = levelC;
		this.levels = levels;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 497, 459);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.wl=wl;
		addWindowListener(wl);
		frame = jf;
		frame.setVisible(false);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 24, 473, 423);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblWordsonePer = new JLabel("Words (one per line)");
		lblWordsonePer.setBounds(12, 53, 168, 19);
		panel.add(lblWordsonePer);
		
		JLabel lblShortDefinitionone = new JLabel("Short Definition (one per line)");
		lblShortDefinitionone.setBounds(213, 54, 248, 16);
		panel.add(lblShortDefinitionone);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 72, 191, 284);
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		t1 = textArea;
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(213, 72, 248, 284);
		panel.add(scrollPane_1);
		
		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		t2 = textArea_1;

		JLabel lbldefinitionAreaCan = new JLabel("(Definition area can be left blank)");
		lbldefinitionAreaCan.setBounds(213, 357, 360, 19);
		panel.add(lbldefinitionAreaCan);

		JButton btnSubmit = new JButton("submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((t1.getText().equals(""))||(t1.getText().matches("^\\d+$"))||(t1.getText().matches("^\\s+$")))){
					JOptionPane.showMessageDialog(itself, "No Words found", "Warning", JOptionPane.OK_OPTION);
				}else if (textField.getText().matches("\\s*")){
					JOptionPane.showMessageDialog(itself, "Category must have a name", "Warning", JOptionPane.OK_OPTION);
				}else{
					String temp = t1.getText();
					String[] ta = temp.split("\\n+");
					if (ta.length<10){
						JOptionPane.showMessageDialog(itself, "Minimum number of words must be ten", "Warning", JOptionPane.OK_OPTION);
					}else{
						File temp1 = new File(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/."+textField.getText());
						boolean hasDefinition = false;
						if (!(t2.getText().matches("\\s*"))){
							hasDefinition = true;
						}
						if (hasDefinition){
							File temp2 = new File(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/."+textField.getText()+"_def");
							if (!(temp2.exists())){
								try {
									temp2.createNewFile();
								} catch (IOException e1) {
									itself.dispose();
									e1.printStackTrace();
								}
							}
						}
						try {
							if (!(temp1.exists())){
								temp1.createNewFile();
							}
							FileWriter fw = new FileWriter(sysfiles.get(filenames.indexOf("customizedLists")), true);
							fw.write("."+textField.getText()+"\n");
							fw.close();
							fw = new FileWriter(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/."+textField.getText(), false);
							fw.write(t1.getText()+"\n");
							fw.close();
							ArrayList<String> temparray = new ArrayList<String>();
							Scanner s = new Scanner(new FileReader(temp1));
							while (s.hasNext()){
								String line = s.nextLine();
								if (line.equals("")){
									continue;
								}
								temparray.add(line);
							}
							s.close();
							levelContents.add(temparray);
							SpellingAid.categories.add(textField.getText());
							SpellingAid.level.addItem(textField.getText());
							if (hasDefinition){
								fw = new FileWriter(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/."+textField.getText()+"_def", false);
								fw.write(t2.getText()+"\n");
								fw.close();
								s = new Scanner(new FileReader(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/."+textField.getText()+"_def"));
								int count = 0;
								while (s.hasNext()){
									String line = s.nextLine();
									if (line.equals("")){
										continue;
									}
									SpellingAid.dictionary.put(temparray.get(count), line);
									count++;
									if (count==temparray.size()){
										break;
									}
								}
								s.close();
							}
							itself.dispose();
						} catch (IOException e1) {
							itself.dispose();
							e1.printStackTrace();
						}
					}
				}
			}
		});
		btnSubmit.addMouseListener(new Mouse(btnSubmit));
		btnSubmit.setBounds(12, 387, 104, 23);
		panel.add(btnSubmit);
		
		JButton btnBackToMain = new JButton("back to main menu");
		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				itself.dispose();
			}
		});
		btnBackToMain.addMouseListener(new Mouse(btnBackToMain));
		btnBackToMain.setBounds(264, 388, 197, 23);
		panel.add(btnBackToMain);
		
		JLabel lblNameOfCategory = new JLabel("name of category:");
		lblNameOfCategory.setBounds(12, 28, 168, 13);
		panel.add(lblNameOfCategory);
		
		textField = new JTextField();
		textField.setBounds(198, 28, 263, 17);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblCreateCategory = new JLabel("Create Category");
		lblCreateCategory.setBounds(80, 12, 284, 43);
		contentPane.add(lblCreateCategory);
		lblCreateCategory.setFont(new Font("Utopia", Font.BOLD, 12));
		lblCreateCategory.setVerticalAlignment(SwingConstants.TOP);
		lblCreateCategory.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.setVisible(true);
	}
}
