package voxspell.gui;

import java.awt.Color;


import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import voxspell.toolbox.StatsWorker;
import voxspell.toolbox.VoxDatabase;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class Stats extends WindowPattern implements ActionListener {

	private JTable table;

	private JComboBox<String> comboBox;

	private JScrollPane scrollPane;

	private JCheckBox showAll;
	
	public static int lastSelectedIndex = 0;

	private static Stats statsGUI = null;

	/**
	 * Create the frame.
	 */
	private Stats() {
		super();
		delete.setEnabled(false);
		clear.setEnabled(false);
	}

	public static Stats getStatsWindow(){
		if (statsGUI == null){
			statsGUI = new Stats();
		}
		return statsGUI;
	}

	@Override
	void paintWindow() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JLabel lblYourStats = new JLabel("Your Stats");
		lblYourStats.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourStats.setFont(new Font("Comic Sans MS", Font.BOLD, 58));
		lblYourStats.setForeground(new Color(102, 0, 255));
		lblYourStats.setBounds(191, 37, 325, 79);
		panel.add(lblYourStats);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(204, 255, 255));
		scrollPane.setBounds(112, 115, 573, 303);
		panel.add(scrollPane);
		this.scrollPane = scrollPane;
		
		table = new JTable();
		table.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		table.setRowSelectionAllowed(false);
		table.setShowGrid(false);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setBackground(new Color(204, 255, 255));
		table.setForeground(new Color(51, 0, 255));
		table.setGridColor(new Color(255, 255, 255));
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setBackground(new Color(204, 255, 255));
		
		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setForeground(new Color(102, 0, 255));
		lblCategory.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblCategory.setBounds(10, 113, 92, 34);
		panel.add(lblCategory);
		
		JComboBox<String> comboBox = new JComboBox<String>(VoxDatabase.getCategories().toArray(new String[VoxDatabase.getCategories().size()]));
		comboBox.addActionListener(this);
		comboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBox.setBounds(10, 158, 92, 34);
		panel.add(comboBox);
		this.comboBox = comboBox;
		
		JButton btnBackToMain = new JButton("Back to main menu");
		btnBackToMain.addActionListener(voxModel);
		btnBackToMain.setBackground(new Color(51, 102, 255));
		btnBackToMain.setForeground(new Color(204, 255, 255));
		btnBackToMain.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnBackToMain.setBounds(201, 429, 283, 34);
		panel.add(btnBackToMain);
		
		JCheckBox showAll = new JCheckBox("Show all tested words");
		showAll.addActionListener(this);
		showAll.setBackground(new Color(204, 255, 255));
		showAll.setForeground(new Color(51, 102, 255));
		showAll.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
		showAll.setBounds(10, 429, 180, 34);
		panel.add(showAll);
		this.showAll = showAll;
		
	}

	public JTable getTable() {
		return table;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		lastSelectedIndex = comboBox.getSelectedIndex();
		if (e.getSource().equals(showAll)){
			JCheckBox checkbox = (JCheckBox)e.getSource();
			if (checkbox.isSelected()){
				StatsWorker sw = new StatsWorker(VoxDatabase.getContents());
				sw.execute();
			}else{
				ArrayList<String> wordlistOfTheLevel = VoxDatabase.getLevelContents().get(comboBox.getSelectedIndex());
				StatsWorker sw = new StatsWorker(VoxDatabase.getContents(), wordlistOfTheLevel);
				sw.execute();
			}
		}else{
			if (!(showAll.isSelected())){
				ArrayList<String> wordlistOfTheLevel = VoxDatabase.getLevelContents().get(comboBox.getSelectedIndex());
				StatsWorker sw = new StatsWorker(VoxDatabase.getContents(), wordlistOfTheLevel);
				sw.execute();
			}else{
				StatsWorker sw = new StatsWorker(VoxDatabase.getContents());
				sw.execute();
			}
		}
	}

	public void updateTable(Vector<Vector<String>> rowData, Vector<String> columnNames){
		table = new JTable(new DefaultTableModel(rowData, columnNames){
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		});
		table.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		table.setRowSelectionAllowed(false);
		table.setShowGrid(false);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setBackground(new Color(204, 255, 255));
		table.setForeground(new Color(51, 0, 255));
		table.setGridColor(new Color(255, 255, 255));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
		TableColumn column = null;
	    for (int i = 0; i < 4; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setCellRenderer( centerRenderer );
	    }
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setBackground(new Color(204, 255, 255));
	}
}
