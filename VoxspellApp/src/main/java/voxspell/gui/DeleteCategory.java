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
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class DeleteCategory extends JFrame {

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
		btnDelete.setBackground(new Color(51, 102, 255));
		btnDelete.setForeground(new Color(204, 255, 255));
		btnDelete.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnDelete.setBounds(123, 194, 189, 30);
		contentPane.add(btnDelete);
		if (temp.isEmpty()){
			btnDelete.setEnabled(false);
		}
		
		
		
		JLabel lblyouCanOnly = new JLabel("*You can only delete your customized categories.");
		lblyouCanOnly.setBounds(55, 110, 328, 14);
		contentPane.add(lblyouCanOnly);
		
	}


	public JComboBox<String> getComboBox() {
		return comboBox;
	}
}
