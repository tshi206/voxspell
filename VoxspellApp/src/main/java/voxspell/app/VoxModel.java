package voxspell.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;

import voxspell.gui.*;
import voxspell.toolbox.ScoreBoardWorker;
import voxspell.toolbox.VoxDatabase;


/**
 * This is the model for the overall application.
 * It's responsible for managing general window swapping between each GUIs.
 * It's an ActionListener linked to all the WindowPattern classes.
 * @author mason23
 *
 */
public class VoxModel implements WindowListener, ActionListener{
	
	private ArrayList<WindowPattern> guis = new ArrayList<WindowPattern>();
	
	private static VoxModel model = null;
	
	private VoxModel(){};
	
	public static int Return_To_Main = 0;
	public static int From_Main_To_New_Game = 1;
	public static int From_Main_To_Stats = 2;
	public static int From_Main_To_Scoreboard = 3;
	public static int From_Main_To_Review = 4;
	
	public static VoxModel getVoxModel(){
		if (model == null){
			model = new VoxModel();
		}
		return model;
	}
	
	/**
	 * registering all gui frames to this model
	 * @param w
	 */
	public void addWindowImplementor(WindowPattern w){
		guis.add(w);
	}
	
	public ArrayList<WindowPattern> getGuis() {
		return guis;
	}

	/**
	 * Managing the swapping between windows.
	 * IF YOU WANT TO RETURN TO MAIN MENU, USE ---null--- AS THE FIRST PARAM.
	 * @param window - the current displaying GUI window
	 * @param options - see public static int options
	 */
	public void paintFrame(WindowPattern window, int option){
		if (option == 0){
			openMainMenu();
			if (window==null){
				for (int i = 1; i < guis.size(); i++){
					guis.get(i).setVisible(false);
				}
			}
		}else if (option == 1){
			window.setVisible(false);
			NewGame.getNewGameWindow().setVisible(true);
			NewGame.getNewGameWindow().setLocationRelativeTo(null);
		}else if (option == 2){
			window.setVisible(false);
			Stats.getStatsWindow().setVisible(true);
			Stats.getStatsWindow().setLocationRelativeTo(null);
			Stats.getStatsWindow().getComboBox().setSelectedIndex(Stats.lastSelectedIndex);
		}else if (option == 3){
			window.setVisible(false);
			Scoreboard.getScoreboardWindow().setVisible(true);
			Scoreboard.getScoreboardWindow().setLocationRelativeTo(null);
			ScoreBoardWorker sbw = new ScoreBoardWorker();
			sbw.execute();
		}else if (option == 4){
			window.setVisible(false);
			Review.getReviewWindow().setVisible(true);
			Review.getReviewWindow().setLocationRelativeTo(null);
			Review.getReviewWindow().updateModel(VoxDatabase.getContents().get(4));
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().getClass().getSimpleName().equals("JButton")){
			JButton source = (JButton)e.getSource();
			if (source.getText().equals("New Game")){
				paintFrame(MainMenu.getmainMenuWindow(), VoxModel.From_Main_To_New_Game);
			}else if (source.getText().equals("Back to main menu")){
				paintFrame(null, VoxModel.Return_To_Main);
			}else if (source.getText().equals("Stats")){
				paintFrame(MainMenu.getmainMenuWindow(), VoxModel.From_Main_To_Stats);
			}else if (source.getText().equals("Scoreboard")){
				paintFrame(MainMenu.getmainMenuWindow(), VoxModel.From_Main_To_Scoreboard);
			}else if (source.getText().equals("Review")){
				paintFrame(MainMenu.getmainMenuWindow(), VoxModel.From_Main_To_Review);
			}else if (source.getText().equals("Settings")){
				Settings.getSettingsWindow().setVisible(true);
				Settings.getSettingsWindow().setLocationRelativeTo(null);
			}else if (source.getText().equals("Video Reward")){
				VideoPlayer player = new VideoPlayer();
				player.setVisible(true);
			}
		}
	}
	
	/**
	 * create the end of category window
	 * @param wordCorrect
	 * @param wordFailed
	 * @param newGame
	 * @return EndOfCategory
	 */
	public static EndOfCategory createEndOfCategoryWindow(int wordCorrect, int wordFailed){
		EndOfCategory eofc = new EndOfCategory(wordCorrect, wordFailed);
		eofc.setVisible(true);
		return eofc;
	}

	/**
	 * open the main menu GUI
	 */
	private void openMainMenu(){
		MainMenu.getmainMenuWindow().setVisible(true);
		MainMenu.getmainMenuWindow().setLocationRelativeTo(null);
	}
	
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e) {
		openMainMenu();
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
