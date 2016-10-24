package voxspell.gui;

import java.awt.Color;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;


/**
 * Displaying Scoreboard Mode's GUI contents.
 * This GUI is managed by both MainMenu (open & close) and by its ScoreBoardWorker (displaying results).
 * @author mason23
 *
 */
@SuppressWarnings("serial")
public class Scoreboard extends WindowPattern {

	private JTable table;
	private JScrollPane scrollPane;

	private static Scoreboard scoreboardGUI = null;

	/**
	 * Create the frame.
	 */
	private Scoreboard() {
		super();
	}

	public static Scoreboard getScoreboardWindow(){
		if (scoreboardGUI == null){
			scoreboardGUI = new Scoreboard();
		}
		return scoreboardGUI;
	}

	@Override
	void paintWindow() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JLabel lblScoreboard = new JLabel("Scoreboard");
		lblScoreboard.setFont(new Font("Comic Sans MS", Font.BOLD, 58));
		lblScoreboard.setForeground(new Color(102, 0, 255));
		lblScoreboard.setHorizontalAlignment(SwingConstants.CENTER);
		lblScoreboard.setBounds(132, 37, 430, 74);
		panel.add(lblScoreboard);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(62, 122, 590, 294);
		panel.add(scrollPane);
		this.scrollPane = scrollPane;
		
		table = new JTable();
		table.setBackground(new Color(255, 255, 255));
		table.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		table.setForeground(new Color(102, 0, 255));
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setShowGrid(false);
		scrollPane.setViewportView(table);
		
		JButton btnBackToMain = new JButton("Back to main menu");
		btnBackToMain.addActionListener(voxModel);
		btnBackToMain.setForeground(new Color(102, 0, 255));
		btnBackToMain.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnBackToMain.setBounds(201, 429, 283, 34);
		panel.add(btnBackToMain);
	}

	/**
	 * Called by ScoreBoardWorker to update the table's contents
	 * @param rowData
	 * @param columnNames
	 */
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
		table.setBackground(new Color(255, 255, 255));
		table.setForeground(new Color(51, 0, 255));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
		TableColumn column = null;
	    for (int i = 0; i < 4; i++) {
	        column = table.getColumnModel().getColumn(i);
	        if (i == 3) {
	            column.setPreferredWidth(173);
	            column.setCellRenderer( centerRenderer );
	        }else if (i == 2) { 
	        	column.setPreferredWidth(203);
	        	column.setCellRenderer( centerRenderer );
	    	}else {
	            column.setPreferredWidth(107);
	            column.setCellRenderer( centerRenderer );
	        }
	    }
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setBackground(new Color(255, 255, 255));
	}
	
}
