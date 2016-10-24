package voxspell.gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxspell.toolbox.DeleteCategoryWorker;
import voxspell.toolbox.VoxDatabase;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * This is not a WindowPattern GUI meaning that its use must be one-off.
 * It's responsible for managing the GUI in order to allow users to delete their imported categories.
 * Its instantiation must be invoked in the menu bar under Help ---> Delete imported category.
 * @author mason23
 *
 */
@SuppressWarnings("serial")
public class DeleteCategory extends JFrame implements WindowListener{

	private DeleteCategory itself = this;
	
	private JPanel contentPane;
	
	private JComboBox<String> comboBox;
	

	/**
	 * Create the frame.
	 */
	public DeleteCategory() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setResizable(false);
		
		addWindowListener(this);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDeleteCategory = new JLabel("Delete Category");
		lblDeleteCategory.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeleteCategory.setForeground(new Color(102, 0, 255));
		lblDeleteCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
		lblDeleteCategory.setBounds(26, 11, 383, 75);
		contentPane.add(lblDeleteCategory);
		
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> defaultLists = new ArrayList<String>();
		for (String defaultLevels : VoxDatabase.getLevels()){
			defaultLists.add(defaultLevels);
		}
		for (String category : VoxDatabase.getCategories()){
			if (!(defaultLists.contains(category))){
				temp.add(category);
			}
		}
		
		final JComboBox<String> comboBox;
		if (temp.isEmpty()){
			comboBox = new JComboBox<String>();
		}else{
			comboBox = new JComboBox<String>(temp.toArray(new String[temp.size()]));
			comboBox.setSelectedIndex(0);
		}
		comboBox.setBounds(55, 125, 328, 30);
		contentPane.add(comboBox);
		this.comboBox = comboBox;
		
		
		
		JButton btnDelete = new JButton("Delete!");
		btnDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = JOptionPane.showConfirmDialog(itself, "You sure?", "Confirm please", JOptionPane.YES_NO_OPTION);
				
				if (returnVal == JOptionPane.YES_OPTION){
					String category = comboBox.getItemAt(comboBox.getSelectedIndex());
					VoxDatabase.deleteCategory(VoxDatabase.getLevelContents().get(VoxDatabase.getCategories().indexOf(category)), category);
					comboBox.removeItem(category);
					
					DeleteCategoryWorker dcw = new DeleteCategoryWorker(itself, category);
					dcw.execute();
				}
				
			}
			
		});
		btnDelete.setForeground(new Color(102, 0, 255));
		btnDelete.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnDelete.setBounds(123, 194, 189, 30);
		contentPane.add(btnDelete);
		if (temp.isEmpty()){
			btnDelete.setEnabled(false);
		}
		
		
		
		JLabel lblyouCanOnly = new JLabel("*You can only delete your customized categories.");
		lblyouCanOnly.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblyouCanOnly.setBounds(55, 110, 328, 14);
		contentPane.add(lblyouCanOnly);
		
		JLabel background = new JLabel(new ImageIcon(VoxDatabase.picsDirectory+"sea.png"));
		background.setBounds(0, 0, 450, 300);
		contentPane.add(background);
		
	}


	public JComboBox<String> getComboBox() {
		return comboBox;
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This allows only one instance of this GUI can be presented at a time.
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		MainMenu.getmainMenuWindow().getEdit().setEnabled(true);
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
