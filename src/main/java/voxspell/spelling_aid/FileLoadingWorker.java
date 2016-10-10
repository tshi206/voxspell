package voxspell.spelling_aid;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class FileLoadingWorker extends SwingWorker<Void, Void> {
	protected static FileReader wordlist;
	protected String[] fn;
	protected File[] files;
	protected ArrayList<File> sysfiles;
	protected ArrayList<ArrayList<String>> contents;
	protected ArrayList<String> filenames;
	protected ArrayList<ArrayList<String>> levelContents;
	protected String[] levels;
	
	public FileLoadingWorker(File[] files, String[] fn, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, ArrayList<ArrayList<String>> levelContents, String[] levels){
		this.fn = fn;
		this.files = files;
		this.sysfiles = sysfiles;
		this.contents = contents;
		this.filenames = filenames;
		this.levelContents = levelContents;
		this.levels = levels;
	}
	
	@Override
	protected Void doInBackground(){
		
		System.out.println("Project files loading starts......");
		if (levelContents.isEmpty()){
			try {
				wordlist = new FileReader(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/NZCER-spelling-lists.txt");
				for (@SuppressWarnings("unused") String s : levels){
					levelContents.add(new ArrayList<String>());
				}
				Scanner scanner = new Scanner(wordlist);
				int count = -1;
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					if (line.contains("%")){
						count++;
						continue;
					}
					levelContents.get(count).add(line);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				final JFrame newFrame = new JFrame();
				newFrame.setLayout(new BorderLayout());
				JPanel np = new JPanel();
				np.setLayout(new BorderLayout());
				JLabel jlabel = new JLabel();
				jlabel.setText("Error! Required file(s) cannot be found! "
						+ "Please check if NZCER-spelling-lists.txt file have been modified or deleted.");
				jlabel.setSize(100, 100);
				jlabel.setVisible(true);
				np.add(jlabel,BorderLayout.CENTER);
				ActionListener l = new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						newFrame.setVisible(false);
						System.exit(1);
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
		loadFiles(files, fn, sysfiles, contents, filenames, levelContents);
		videoSetup();
		return null;
	}
	
	private void loadFiles(File[] files, String[] fn, ArrayList<File> sysfiles, ArrayList<ArrayList<String>> contents,
			ArrayList<String> filenames, ArrayList<ArrayList<String>> levelContents){
		try {
			for (File file : files){
				if (!(file.exists())){
					file.createNewFile();
				}
			}
			int i = 0;
			for (File file : files){
				Scanner scanner = new Scanner(new FileReader(file));
				contents.add(new ArrayList<String>());
				while (scanner.hasNext()){
					String line = scanner.nextLine();
					contents.get(i).add(line);
				}
				i++;
				scanner.close();
			}
		} catch (IOException e) {
			final JFrame newFrame = new JFrame();
			newFrame.setLayout(new BorderLayout());
			JPanel np = new JPanel();
			np.setLayout(new BorderLayout());
			JLabel jlabel = new JLabel();
			jlabel.setText("Error! Fatal error occurs! "
					+ "Program will not be functioning correctly.");
			jlabel.setSize(100, 100);
			jlabel.setVisible(true);
			np.add(jlabel,BorderLayout.CENTER);
			ActionListener l = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					newFrame.setVisible(false);
					System.exit(1);
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
		
		for (int i=0; i<fn.length; i++){
			sysfiles.add(files[i]);
			filenames.add(fn[i]);
		}
	}
	
	private void videoSetup(){
		ProcessBuilder p = null;
		try {
			File bunny = new File(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/big_buck_bunny_1_minute.avi");
			String path = bunny.getCanonicalPath();
			path = path.substring(0, path.length()-bunny.getName().length());
			File bunny_negative = new File(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/big_buck_bunny_1_minute_converted.avi");
			if (!(bunny_negative.exists())){ //dead code LOL
				p = new ProcessBuilder("/bin/bash","-c", "ffmpeg -i "+bunny.getCanonicalPath()+" -vf negate "+
						path+"big_buck_bunny_1_minute_converted.avi");
			}
		} catch (IOException e2) {
			final JFrame newFrame = new JFrame();
			newFrame.setLayout(new BorderLayout());
			JPanel np = new JPanel();
			np.setLayout(new BorderLayout());
			JLabel jlabel = new JLabel();
			jlabel.setText("Unresolvable error occurs!! "
					+ "Required video file does not exist.");
			jlabel.setSize(100, 100);
			jlabel.setVisible(true);
			np.add(jlabel,BorderLayout.CENTER);
			ActionListener l = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					newFrame.setVisible(false);
					System.exit(1);
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
		Process process = null;
		try {
			process = p.start();
			int exit = process.waitFor();
			if (exit==0){
				System.out.println("successfully converted video file.");
			}
		} catch (IOException | InterruptedException e1) {
			final JFrame newFrame = new JFrame();
			newFrame.setLayout(new BorderLayout());
			JPanel np = new JPanel();
			np.setLayout(new BorderLayout());
			JLabel jlabel = new JLabel();
			jlabel.setText("Unresolvable error occurs!! "
					+ "Underlying commands are possibly not functioning.");
			jlabel.setSize(100, 100);
			jlabel.setVisible(true);
			np.add(jlabel,BorderLayout.CENTER);
			ActionListener l = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					newFrame.setVisible(false);
					System.exit(1);
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
	protected void done(){
		System.out.println("Project files loading done.");
	}
}
