package voxspell.app;

import voxspell.gui.*;
import voxspell.toolbox.VoxDatabase;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

/**
 * Entry of the entire program.
 *
 */
public class VoxMain 
{
	public static boolean isUserBack = false;
	public static String username = "";
	
	/**
	 * Launch the application.
	 */
    public static void main( String[] args )
    {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIinitialiator();
					VoxDatabase.projectSetup();
					MainMenu mainmenu = MainMenu.getmainMenuWindow();
					mainmenu.setVisible(true);
					if (isUserBack){
						
						JOptionPane.showMessageDialog(mainmenu, "Welcome back! "+username+" :)");
						
						isUserBack = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
    
    /**
     * Populating the guis list in VoxModel.
     * MainMenu must always be the first element.
     * All elements in the list are children classes of WindowPattern.
     */
    private static void GUIinitialiator(){
    	MainMenu mainMenu = MainMenu.getmainMenuWindow();
    	NewGame newGame = NewGame.getNewGameWindow();
    	Stats stats = Stats.getStatsWindow();
    	Scoreboard scoreboard = Scoreboard.getScoreboardWindow();
    	Review review = Review.getReviewWindow();
    	
    	VoxModel.getVoxModel().addWindowImplementor(mainMenu);
    	VoxModel.getVoxModel().addWindowImplementor(newGame);
    	VoxModel.getVoxModel().addWindowImplementor(stats);
    	VoxModel.getVoxModel().addWindowImplementor(scoreboard);
    	VoxModel.getVoxModel().addWindowImplementor(review);
    }
}
