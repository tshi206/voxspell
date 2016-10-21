package voxspell.toolbox;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class ImportListWorker extends SwingWorker<Void, Void> {

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void done(){
		JOptionPane.showMessageDialog(null, "Finish importing words file", "Operation complete", JOptionPane.INFORMATION_MESSAGE);
	}
}
