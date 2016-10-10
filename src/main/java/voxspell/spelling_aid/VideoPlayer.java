package voxspell.spelling_aid;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

@SuppressWarnings("serial")
public class VideoPlayer extends JFrame implements ActionListener, WindowListener {
	
	private static String additionalPath = SpellingAid.currentWorkingDirectory;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private WindowListener wl;
	private File bunny = new File(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/big_buck_bunny_1_minute.avi");
	private File bunny_negative = new File(SpellingAid.currentWorkingDirectory+"/target/classes/voxspell/spelling_aid/big_buck_bunny_1_minute_converted.avi");
	private File video;
	private EmbeddedMediaPlayer player;
	private JButton jb1 = new JButton("normal");
	private JButton jb2 = new JButton("negative film");
	private JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 60);
	private JPanel l;
	private JPanel jp;
	
	public VideoPlayer(WindowListener wl){
		super("VideoPlayer");
		this.wl = wl;

		setSize(400, 100);
		setMinimumSize(new Dimension(400,100));
		setMaximumSize(new Dimension(400,100));
		l = new JPanel();
		l.setSize(400, 50);
		l.setMaximumSize(new Dimension(400,50));
		l.setLayout(new BoxLayout(l, BoxLayout.Y_AXIS));
		l.add(new JLabel("Please choose one of the video versions to play:"));
		jp = new JPanel();
		jp.setSize(400, 50);
		jp.setMaximumSize(new Dimension(400,50));
		jb1.setPreferredSize(new Dimension(150, 40));
		jb2.setPreferredSize(new Dimension(150, 40));
		jb1.setLocation(100, jb1.getLocation().y);
		jb2.setLocation(200, jb2.getLocation().y);
		jb1.addMouseListener(new Mouse(jb1));
		jb2.addMouseListener(new Mouse(jb2));
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jp.add(jb1);
		jp.add(jb2);
		add(l, BorderLayout.NORTH);
		add(jp, BorderLayout.SOUTH);
		addWindowListener(wl);
		addWindowListener(this);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
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
		
		String path = SpellingAid.currentWorkingDirectory+"/src";
		System.out.println(path);

		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/usr/lib"+":"+path+":"+path+"/target/classes/voxspell/spelling_aid"
				+additionalPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		JFrame frame = this;
		l.setVisible(false);
		jp.setVisible(false);
		
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
        btnStart.addMouseListener(new Mouse(btnStart));
        
        JButton btnPause = new JButton("Pause");
        functionbar.add(btnPause);
        btnPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.pause();
			}
		});
        btnPause.addMouseListener(new Mouse(btnPause));
        
        JButton btnStop = new JButton("Stop");
        functionbar.add(btnStop, BorderLayout.NORTH);
        btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
			}
		});
        btnStop.addMouseListener(new Mouse(btnStop));
        
        JButton btnMute = new JButton("Mute");
        functionbar.add(btnMute);
        btnMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.mute();
			}
		});
        btnMute.addMouseListener(new Mouse(btnMute));

        JButton btnSkipBack = new JButton("<<");
        functionbar.add(btnSkipBack);
        btnSkipBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(-3000);
			}
		});
        btnSkipBack.addMouseListener(new Mouse(btnSkipBack));
        
        JButton btnSkip = new JButton(">>");
        functionbar.add(btnSkip);
        btnSkip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(3000);
			}
		});
        btnSkip.addMouseListener(new Mouse(btnSkip));
        
        statsbar.add(functionbar);
        
        Point point = frame.getLocationOnScreen();
        
        frame.setLocation(point.x-350, point.y-300);
        frame.setContentPane(p);
        frame.setPreferredSize(new Dimension(1050,673));
        frame.setMinimumSize(new Dimension(1050,673));
        frame.setMaximumSize(new Dimension(1050,673));
        frame.setSize(1050, 673);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        
        
        try {
			video.playMedia(this.video.getCanonicalPath());
		} catch (IOException e1) {
			//dead code LOL
			e1.printStackTrace();
		}
	}
}
