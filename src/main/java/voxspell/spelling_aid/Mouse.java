package voxspell.spelling_aid;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

class Mouse implements MouseListener {
	private Color c = null;
	private JButton button = null;

	protected Mouse(JButton jb){
		button = jb;
		c = jb.getBackground();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		button.setBackground(new Color(255,255,50));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		button.setBackground(c);
	}

}
