package voxspell.spelling_aid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
class ViewStatistics extends JFrame implements ActionListener {
	private JButton submit = new JButton("Return!");
	private JTextArea txtOutput = new JTextArea(30, 40);
	private JFrame frame = null;
	
	protected ArrayList<File> sysfiles;
	protected ArrayList<ArrayList<String>> contents;
	protected ArrayList<String> filenames;
	
	private JLabel selectPreferredLevel = new JLabel("select your preferred level:");

	protected ArrayList<ArrayList<String>> levelContents;
	private JComboBox<String> buttonList = new JComboBox<String>(SpellingAid.categories.toArray(new String[SpellingAid.categories.size()]));
	
	public ViewStatistics(JFrame jf, WindowListener wl, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, String[] levels, ArrayList<ArrayList<String>> levelContents) {
		super("Spelling Aid: View Statistics");
		this.addWindowListener(wl);
		this.sysfiles = sysfiles;
		this.contents = contents;
		this.filenames = filenames;
		this.levelContents = levelContents;
		frame = jf;
		frame.setVisible(false);
		setSize(775, 550);
		setMinimumSize(new Dimension(775,550));
		ImageIcon welcomeMsg = new ImageIcon("./target/classes/voxspell/spelling_aid/ViewStatistics.JPG");
		welcomeMsg.setImage(welcomeMsg.getImage().getScaledInstance(welcomeMsg.getIconWidth(),
				welcomeMsg.getIconHeight(), Image.SCALE_DEFAULT));
		JLabel jl = new JLabel(welcomeMsg);
		jl.setIcon(welcomeMsg);
		
		submit.addActionListener(this);
		submit.addMouseListener(new Mouse(submit));
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jl.setVisible(true);
		
		JPanel jp = new JPanel();
		jp.setSize(570, 365);
		jp.setMaximumSize(new Dimension(570,365));
		
		txtOutput.setText("");
		txtOutput.setEditable(false);
		txtOutput.setSize(570, 360);
		txtOutput.setMinimumSize(new Dimension(570, 360));
		
		jp.setVisible(true);
		JScrollPane scroll = new JScrollPane(txtOutput);
		scroll.setPreferredSize(new Dimension(570, 360));
		jp.add(scroll);
		
		JPanel jp1 = new JPanel();
		jp1.setLayout(new GridLayout(2,1));
		jp1.setSize(140, 50);
		jp1.setMaximumSize(new Dimension(140,50));
		jp1.add(selectPreferredLevel);
		
		jp1.add(buttonList);
		buttonList.addActionListener(this);
		buttonList.setSelectedIndex(0);
		buttonList.setPreferredSize(new Dimension(140,50));
		buttonList.setMaximumSize(new Dimension(140,50));
		
		JPanel jp2 = new JPanel();
		jp2.setSize(775, 365);
		jp2.setLayout(new BoxLayout(jp2, BoxLayout.X_AXIS));
		jp2.add(jp1);
		jp2.add(jp);
		
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		add(jl, BorderLayout.NORTH);
		add(jp2, BorderLayout.CENTER);
		add(submit, BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit){
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			this.dispose();
		}else{
			ArrayList<String> wordlistOfTheLevel = levelContents.get(buttonList.getSelectedIndex());
			LogWorker lw = new LogWorker(txtOutput, contents, wordlistOfTheLevel);
			lw.execute();
		}
	}
}

class Words implements Comparable<Words>{
	protected int mastered;
	protected int faulted;
	protected int failed;
	protected String name;
	
	public Words(int i, int j, int k, String s){
		mastered = i;
		faulted = j;
		failed = k;
		name = s;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public boolean equalsTo(Words w){
		if ((mastered==w.mastered)&&(faulted==w.faulted)&&(failed==w.failed)){
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Words w) {
		if (this.equalsTo(w)){
			return 0;
		}else{
			if (mastered>w.mastered){
				return 1;
			}else if (mastered==w.mastered){
				if (failed>w.failed){
					return -1;
				}else if (failed==w.failed){
					if (faulted>w.faulted){
						return -1;
					}else{
						return 1;
					}
				}else{
					return 1;
				}
			}
		}
		return -1;
	}
}

class LogWorker extends SwingWorker<Void, Void>{
	private JTextArea txtOutput;

	protected ArrayList<ArrayList<String>> contents;

	protected ArrayList<String> wordlistOfTheLevel;
	
	private String log;
	
	public LogWorker(JTextArea txtOutput, ArrayList<ArrayList<String>> contents,
			ArrayList<String> wordlistOfTheLevel){
		this.txtOutput = txtOutput;
		this.contents = contents;
		this.wordlistOfTheLevel = wordlistOfTheLevel;
	}
	
	@Override
	protected Void doInBackground() throws Exception { 
		log = logGenerator();
		return null;
	}
	
	@Override
	protected void done(){
		txtOutput.setText(log);
	}
	
	private String logGenerator(){
		
		ArrayList<String> temp = new ArrayList<String>();
		for (int i=0; i<3; i++){
			temp.addAll(contents.get(i));
		}
		
		ArrayList<Words> hist = new ArrayList<Words>();
		for (String str : temp){
			if (wordlistOfTheLevel.contains(str)){

				int mastered=0, faulted=0, failed=0;
				
				for (String t : contents.get(3)){
					if (str.equals(t)){
						mastered++;
					}
				}
				for (String t : contents.get(4)){
					if (str.equals(t)){
						faulted++;
					}
				}
				for (String t : contents.get(5)){
					if (str.equals(t)){
						failed++;
					}
				}
				hist.add(new Words(mastered, faulted, failed, str));

				Collections.sort(hist);
			}
		}
		
		String s = "";
		for (Words w : hist){
			s = String.format(" "+s+"%-10s :  Mastered: %-6d times.  Faulted: %-6d times.  Failed: %-6d times.\n", w, w.mastered, w.faulted, w.failed);
		}
		return s;
	}
}
