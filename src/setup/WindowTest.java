package setup;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import setup.Manager;
import setup.Window;

public class WindowTest implements Manager{
	
	public static final int WIDTH = 700 + 14, HEIGHT = 700 + 37, FPS = 60, TPS = 60;
	
	static Window win = new Window(WIDTH, HEIGHT, FPS, TPS, "Window Title", new WindowTest());
	
	int boxX = 1;
	int boxY = 500;
	int time = 0;
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.RED);
		g.fillRect(boxX, boxY, 100, 40);
		g.drawOval(200, 200, 150, 150);
		
		
	}
	
	public void tick() {
		time++;
		boxX += 1;
		boxY = boxX * boxX / 500;
		time = 0;
	}
	
	public static void main(String[] args) {
//		@SuppressWarnings("unused")
//		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getX());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getSource()); 
	}

	@Override
	public void keyPressed(int e) {
		// TODO Auto-generated method stub
		win.getJFrame().dispose();
	}

	@Override
	public void keyReleased(int e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(int e) {
		// TODO Auto-generated method stub
		
	}
}
