package voxspell.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import voxspell.toolbox.VoxDatabase;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

@SuppressWarnings("serial")
public class VideoPlayer extends JFrame implements ActionListener, WindowListener {

	private JPanel contentPane;
	private JPanel selectionPane;

	private static String additionalPath = VoxDatabase.currentWorkingDirectory;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private File bunny = new File(VoxDatabase.videosDirectory+"big_buck_bunny_1_minute.avi");
	private File bunny_negative = new File(VoxDatabase.videosDirectory+"big_buck_bunny_1_minute_converted.avi");
	private File video;
	private EmbeddedMediaPlayer player;
	private JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 60);

	private JButton jb1;
	private JButton jb2;

	/**
	 * Create the frame.
	 */
	public VideoPlayer() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setAlwaysOnTop (true);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 255, 255));
		panel.setBounds(0, 0, 434, 261);
		contentPane.add(panel);
		panel.setLayout(null);
		selectionPane = panel;

		JLabel lblChooseReward = new JLabel("Choose Your Reward!");
		lblChooseReward.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseReward.setFont(new Font("Comic Sans MS", Font.BOLD, 34));
		lblChooseReward.setForeground(new Color(102, 0, 255));
		lblChooseReward.setBounds(28, 26, 380, 105);
		panel.add(lblChooseReward);

		JButton btnNewButton = new JButton("Normal One");
		btnNewButton.setForeground(new Color(204, 255, 255));
		btnNewButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		btnNewButton.setBackground(new Color(51, 0, 255));
		btnNewButton.setBounds(10, 142, 195, 77);
		panel.add(btnNewButton);
		jb1 = btnNewButton;

		JButton btnNewButton_1 = new JButton("Scary One");
		btnNewButton_1.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		btnNewButton_1.setBackground(new Color(51, 0, 255));
		btnNewButton_1.setForeground(new Color(204, 255, 255));
		btnNewButton_1.setBounds(215, 142, 209, 77);
		panel.add(btnNewButton_1);
		jb2 = btnNewButton_1;
		
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		addWindowListener(this);
	}

	public static void setAdditionalPath(String s){
		additionalPath = s;
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
		if (e.getSource().equals(this)){
			if (!(player==null)){
				player.stop();
			}
		}
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb1)){
			video = bunny;
		}else if (e.getSource().equals(jb2)){
			video = bunny_negative;
		}

		String path = VoxDatabase.currentWorkingDirectory+"/src";
		System.out.println(path);

		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/usr/lib"+":"+path+":"+path+"/target/classes/voxspell/"
				+additionalPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		JFrame frame = this;
		selectionPane.setVisible(false);

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

		final EmbeddedMediaPlayer video = mediaPlayerComponent.getMediaPlayer();
		player = video;

		JPanel p = new JPanel();

		p.setPreferredSize(new Dimension(1050,600));
		p.setMaximumSize(new Dimension(1050,600));
		p.setSize(1050, 600);
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(1050, 600);
		panel.setMaximumSize(new Dimension(1050,600));
		panel.add(mediaPlayerComponent, BorderLayout.CENTER);
		p.add(panel);
		JPanel statsbar = new JPanel();
		statsbar.setLayout(new BoxLayout(statsbar, BoxLayout.Y_AXIS));
		statsbar.setSize(1050, 50);
		p.add(statsbar);
		JPanel functionbar = new JPanel();
		functionbar.setLayout(new BoxLayout(functionbar, BoxLayout.X_AXIS));
		functionbar.setSize(1050, 30);

		statsbar.add(jpb);
		jpb.setSize(1050, 20);
		jpb.setStringPainted(true);
		Timer timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long time = (long)(video.getTime()/1000.0);
				jpb.setValue((int)time);
				jpb.setString(String.valueOf(time)+" seconds");
			}
		});
		timer.start();

		JButton btnStart = new JButton("Start");
		functionbar.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.start();
			}
		});


		JButton btnPause = new JButton("Pause");
		functionbar.add(btnPause);
		btnPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.pause();
			}
		});


		JButton btnStop = new JButton("Stop");
		functionbar.add(btnStop, BorderLayout.NORTH);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
			}
		});


		JButton btnMute = new JButton("Mute");
		functionbar.add(btnMute);
		btnMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.mute();
			}
		});


		JButton btnSkipBack = new JButton("<<");
		functionbar.add(btnSkipBack);
		btnSkipBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(-3000);
			}
		});


		JButton btnSkip = new JButton(">>");
		functionbar.add(btnSkip);
		btnSkip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(3000);
			}
		});


		statsbar.add(functionbar);

		frame.setContentPane(p);
		frame.setPreferredSize(new Dimension(1050,673));
		frame.setMinimumSize(new Dimension(1050,673));
		frame.setMaximumSize(new Dimension(1050,673));
		frame.setSize(1050, 673);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		p.setVisible(true);

		try {
			video.playMedia(this.video.getCanonicalPath());
			video.mute(false);
		} catch (IOException e1) {
			//dead code LOL
			e1.printStackTrace();
		}
	}
}
