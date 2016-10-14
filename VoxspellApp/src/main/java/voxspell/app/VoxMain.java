package voxspell.app;

import voxspell.gui.*;
import java.awt.EventQueue;

import javax.swing.JFrame;

import voxspell.gui.MainMenu;

/**
 * Entry of the entire program.
 *
 */
public class VoxMain 
{
	/**
	 * Launch the application.
	 */
    public static void main( String[] args )
    {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu mainmenu = new MainMenu();
					mainmenu.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}
