package voxspell.toolbox;

import javax.swing.SwingWorker;

import voxspell.gui.Settings;

public class UsrInfoLoadingWorker extends SwingWorker<Void, Void> {

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		Settings.getSettingsWindow(); //TODO
		return null;
	}

}
