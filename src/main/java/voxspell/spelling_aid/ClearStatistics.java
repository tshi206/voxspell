package voxspell.spelling_aid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class ClearStatistics extends JFrame implements ActionListener, WindowListener {
	private JButton yes = new JButton("Yes");
	private JButton no = new JButton("no");
	private JFrame frame = null;
	private WindowListener WL = null;
	private ClearStatistics cl = this;
	
	protected ArrayList<File> sysfiles;
	protected ArrayList<ArrayList<String>> contents;
	protected ArrayList<String> filenames;
	
	public ClearStatistics(JFrame jf, WindowListener wl, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents, ArrayList<String> filenames) {
		super("Spelling Aid: Clear Statistics");
		this.addWindowListener(wl);
		this.sysfiles = sysfiles;
		this.contents = contents;
		this.filenames = filenames;
		this.WL = wl;
		frame = jf;
		frame.setVisible(false);
		setSize(400,400);
		setMinimumSize(new Dimension(400,400));
		ImageIcon welcomeMsg = new ImageIcon("./target/classes/voxspell/spelling_aid/ClearStatistics.JPG");
		welcomeMsg.setImage(welcomeMsg.getImage().getScaledInstance(welcomeMsg.getIconWidth(),
				welcomeMsg.getIconHeight(), Image.SCALE_DEFAULT));
		JLabel jl = new JLabel(welcomeMsg);
		jl.setIcon(welcomeMsg);
		JLabel promt1 = new JLabel("Are you sure you want to clear the statistics?");
		JLabel promt2 = new JLabel("This operation will delete all the history files.");
		JLabel promt3 = new JLabel("Continue? (Deleted files cannot be restored.)");
		promt3.setPreferredSize(new Dimension(350,20));
		yes.addActionListener(this);
		no.addActionListener(this);
		yes.addMouseListener(new Mouse(yes));
		no.addMouseListener(new Mouse(no));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jl.setVisible(true);
		JPanel jp = new JPanel();
		jp.setMaximumSize(new Dimension(400,400));
		jp.add(jl);
		jp.add(promt1);
		jp.add(promt2);
		jp.add(promt3);
		jp.setVisible(true);
		jp.add(yes);
		jp.add(no);
		add(jp);
		
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(yes)){
			final JFrame newFrame = new JFrame();
			newFrame.addWindowListener(WL);
			newFrame.addWindowListener(cl);
			newFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			newFrame.setLayout(new BorderLayout());
			JPanel np = new JPanel();
			np.setLayout(new BorderLayout());
			JLabel jlabel = new JLabel();
			jlabel.setText("Operation confirmed. You will be redirected to the main menu.");
			jlabel.setSize(100, 100);
			jlabel.setVisible(true);
			np.add(jlabel,BorderLayout.CENTER);
			ActionListener l = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					cl.dispose();
					newFrame.dispose();
				}
				
			};
			JButton jb = new JButton("OK");
			jb.addActionListener(l);
			np.add(jb, BorderLayout.SOUTH);
			newFrame.add(np);
			newFrame.setVisible(true);
			newFrame.setLocationRelativeTo(null);
			newFrame.pack();
			for (File f : sysfiles){
				f.delete();
			}
			for (ArrayList<String> as : contents){
				as.removeAll(as);
			}
		}else{
			final JFrame newFrame = new JFrame();
			newFrame.addWindowListener(WL);
			newFrame.addWindowListener(cl);
			newFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			newFrame.setLayout(new BorderLayout());
			JPanel np = new JPanel();
			np.setLayout(new BorderLayout());
			JLabel jlabel = new JLabel();
			jlabel.setText("Operation cancelled. You will be redirected to the main menu.");
			jlabel.setSize(100, 100);
			jlabel.setVisible(true);
			np.add(jlabel,BorderLayout.CENTER);
			ActionListener l = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					cl.dispose();
					newFrame.dispose();
				}
				
			};
			JButton jb = new JButton("OK");
			jb.addActionListener(l);
			np.add(jb, BorderLayout.SOUTH);
			newFrame.add(np);
			newFrame.setVisible(true);
			newFrame.setLocationRelativeTo(null);
			newFrame.pack();
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		this.dispose();
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
