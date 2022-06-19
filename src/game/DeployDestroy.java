package game;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import setup.Manager;
import setup.Window;

public class DeployDestroy implements Manager{
	
	public static final int WIDTH = 900 + 14, HEIGHT = 500 + 37, FPS = 60, TPS = 60;
	
	static Window win = new Window(WIDTH, HEIGHT, FPS, TPS, "DEPLOY - DESTROY", new DeployDestroy());
	
	static Image img;
	static Image coinFlip;
	static Board board; // Initiate board (board will have players, phase and boxes
	
	static Soldier soldierSelected;
	static int time = 0;
	static int highlightOpacity = 0;
	
	static boolean isIncreasing = true;
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		g.drawImage(img, 0, 0, 900, 500, null);
		
		g.setFont(new Font("Arial", 0, 45));
		
		g.setColor(Color.BLACK);
		
		// Spawn Boxes
		for (Box box: board.getField()) {
			box.draw(g);
		}
		
		// Spawn soldiers
		for (Player player: board.getPlayers()) {
			for (Soldier soldier: player.getSoldiers()) {
				soldier.draw(g);
				
				if (soldier.isHovered() || soldier.isSelected()) { // Hover effect
					Shape circle = new Ellipse2D.Float(soldier.getPosX()-4, soldier.getPosY()-4, Soldier.getWidth()+8, Soldier.getHeight()+8);
					g2.setColor(Color.WHITE);
					g2.setStroke(new BasicStroke(5));
					g2.draw(circle);
					g2.setColor(Color.BLACK);
				}
			}
		}
		
		if (board.getPhase() == "Coin Flip" && time > 60) {
			Color highlightPlayer = new Color(255, 255, 255, highlightOpacity);
			
			g.setColor(highlightPlayer);
			
			if (board.getTurn() == 0) {
				g.fillRect(0, 0, 900, 500/2);
			}
			else if (board.getTurn() == 1) {
				g.fillRect(0, 500/2, 900, 500/2);
			}
			
			
			g.setColor(Color.BLACK);
			
//			System.out.println(highlightOpacity);
			
			if (isIncreasing && !(highlightOpacity > 100)) {
				highlightOpacity += 2;
			}
			else{
				highlightOpacity -= 2;
				isIncreasing = false;
				
				if(highlightOpacity < 30) {
					isIncreasing = true;
				}
			}
		}
		
		if (board.getPhase() == "Coin Flip" && time < 180) {
			Color coinGold = new Color(254, 214, 112);
			g.setColor(coinGold);
			g.fillOval(900/2 - 175, 500/2 - 175, 350, 350);
			g.setColor(Color.BLACK);
			
			
			Color gold = new Color(254, 190, 112);
			Shape circle = new Ellipse2D.Float(900/2 - 175, 500/2 - 175, 350, 350);
			g2.setColor(gold);
			g2.setStroke(new BasicStroke(10));
			g2.draw(circle);
			g2.setColor(Color.BLACK);
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", 0, 300));
			
			if (board.getTurn() == 0) {
				g.drawString("H", 900/2 - 103, 500/2 + 105);
			}
			else if (board.getTurn() == 1) {
				g.drawString("T", 900/2 - 93, 500/2 + 105);
			}
			
			g.setColor(Color.BLACK);
		}
		else if (board.getPhase() == "Destroy Phase" && time < 210) {
			if (time > 60 && time < 150) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 900, 500);
				
				g.setColor(Color.WHITE);
				g.setFont(new Font("Arial", 0, 120));
				g.drawString(board.getPhase(), 60, 280);
				g.setColor(Color.BLACK);
			}
			
			if (board.getTurn() == 0) {
				g.drawString("H", 900/2 - 103, 500/2 + 105);
			}
			else if (board.getTurn() == 1) {
				g.drawString("T", 900/2 - 93, 500/2 + 105);
			}
			
			time++;
		}
		
		if (board.getPhase() == "Endgame") {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 0, 70));
			g.drawString("GAME OVER", 100, 400);
			g.setFont(new Font("Arial", 0, 35));
			g.drawString(board.getWinner(), 100, 450);
			g.setColor(Color.BLACK);
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 0, 10));
		g.drawString("© https://www.teahub.io/viewwp/Thom_floral-cool-wallpaper-cool-backgrounds-for-gmail/" ,0 , 500);
		
	}
	
	public void tick() {
		if (board.getPhase() == "Coin Flip") {
			if (time < 60 && time % 5 == 0) {
				board.setTurn(((time + 3) % 2));
			}
			else if (time == 60) {
				board.setTurn((int)(Math.random() * 2));
			}
			else if (time > 240) {
				board.flipCoin();
			}
			
			time++;
		}
		else if (board.getTurn() == 1 && board.getPhase() != "Endgame") {
			int strength = (int)(Math.random() * 8);
			int boxIndex = (int)(Math.random() * board.getField().size());
			
			// REMINDER --- to avoid possibly long loop, solve this solution with arrays
			if (board.getPhase() == "Deploy Phase") {
				while ((board.getPlayers().get(1).getSoldiers().get(strength).isDeployed() || board.getField().get(boxIndex).isOccupied())) {
					strength = (int)(Math.random() * 8);
					boxIndex = (int)(Math.random() * board.getField().size());
				}
				
				Soldier soldier = board.getPlayers().get(1).getSoldiers().get(strength);
				Box box = board.getField().get(boxIndex);
				soldier.deploy(board, box.getPosX()+1, box.getPosY()+1);
			}
			else if(board.getPhase() == "Destroy Phase" && time > 209) {
				
				if (board.getAllyDestroyables() != 0) {
					while (board.getPlayers().get(0).getSoldiers().get(strength).isDestroyed() || !board.getPlayers().get(0).getSoldiers().get(strength).isDestroyable(board)) {
						strength = (int)(Math.random() * 8);
					}
					
					Soldier soldier = board.getPlayers().get(0).getSoldiers().get(strength);
					soldier.destroy(board);
				}
				else {
					board.switchTurn();
				}
			}
		}
		else if (board.getTurn() == 0 && board.getPhase() != "Endgame" && board.getEnemyDestroyables() == 0) {			
			board.switchTurn();
		}
	}
	
	public static void main(String[] args) {
//		@SuppressWarnings("unused")
		board = new Board(); // Set board
		
		
		
		// Set and add players
		board.addPlayer(new Player("Player1", 900/15, 45));
		board.addPlayer(new Player("Player2", 900/15, 400));
		
		// Make soldier objects and add to list
		for (Player player: board.getPlayers()) {
			for (int j = 1; j < 9; j++) {
				player.addSoldier(new Soldier(player.getUsername(), j));
			}		
		}
		
		// Position soldiers
		for (Player player: board.getPlayers()) {
			for (Soldier soldier: player.getSoldiers()) {
				int x = player.getArmyX() * soldier.getStrength() + 155;
				int y = player.getArmyY();
				
				soldier.setPosX(x);
				soldier.setPrevX(x);
				soldier.setPosY(y);
				soldier.setPrevY(y);
			}
		}
		
		
		// Make and position Boxes
		for (int i = 0; i < board.getPlayers().size() * 8; i++) {
			board.addBox(new Box(900 * (i + 2) / 20, HEIGHT/2 - 45));
		}
		
		try {
			img = ImageIO.read(new File("src/game/background_img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
		// If a soldier is being dragged, move it (check mouse pressed)
		for (Player player: board.getPlayers()) {
			for (Soldier soldier: player.getSoldiers()) {
				if(soldier.isSelected() && e.getX() > soldier.getPosX() && e.getX() < soldier.getPosX() +Soldier.getWidth() && e.getY() > soldier.getPosY() && e.getY() < soldier.getPosY()+Soldier.getHeight()) {
					soldier.setPosX(e.getX() - 45/2);
					soldier.setPosY(e.getY() - 45/2);
				}
			}
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		// Hover effect
		for (Player player: board.getPlayers()) {
			for (Soldier soldier: player.getSoldiers()) {
				if (!soldier.isDestroyed()) {
					if(soldier.getArmy() == "Player1" && !soldier.isDeployed() && e.getX() > soldier.getPosX() && e.getX() < soldier.getPosX() + Soldier.getWidth() && e.getY() > soldier.getPosY() && e.getY() < soldier.getPosY()+Soldier.getHeight()) {
						soldier.setHovered(true);
					}
					else if(board.getPhase() == "Destroy Phase" && soldier.getArmy() == "Player2" && soldier.isDeployed() && e.getX() > soldier.getPosX() && e.getX() < soldier.getPosX() + Soldier.getWidth() && e.getY() > soldier.getPosY() && e.getY() < soldier.getPosY()+Soldier.getHeight()) {
						soldier.setHovered(true);
					}
					else {
						soldier.setHovered(false);
					}
				}
			}
		}
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
		
		// When mouse is pressed down on a soldier, that soldier will be dragged
		for (Player player: board.getPlayers()) {
			for (Soldier soldier: player.getSoldiers()) {
				if (board.getPhase() == "Deploy Phase" && board.getTurn() == 0 && soldier.getArmy() == "Player1" && !soldier.isDeployed() && e.getX() > soldier.getPosX() && e.getX() < soldier.getPosX() +Soldier.getWidth() && e.getY() > soldier.getPosY() && e.getY() < soldier.getPosY()+Soldier.getHeight()) {
					soldier.setSelected(true);
					soldierSelected = soldier;
				}
				else if (board.getPhase() == "Destroy Phase" && board.getTurn() == 0 && soldier.getArmy() == "Player2" && !soldier.isDestroyed() && soldier.isDeployed() && soldier.isDestroyable(board) && e.getX() > soldier.getPosX() && e.getX() < soldier.getPosX() +Soldier.getWidth() && e.getY() > soldier.getPosY() && e.getY() < soldier.getPosY()+Soldier.getHeight()) {
					soldier.setSelected(true);
					soldierSelected = soldier;
				}
				else {
					soldier.setSelected(false);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
		// When mouse is released the soldier will not be dragged anymore
		
		if (board.getPhase() == "Deploy Phase" && soldierSelected != null && !soldierSelected.isDeployed()) {
			soldierSelected.deploy(board, e.getX(), e.getY());
		}
		else if (board.getPhase() == "Destroy Phase" && soldierSelected != null && soldierSelected.isDeployed() && !soldierSelected.isDestroyed() && board.getEnemyDestroyables() != 0 && soldierSelected.isSelected()) {
			soldierSelected.destroy(board);
		}
		else {
			soldierSelected = null;
		}
	}

	@Override
	public void keyPressed(int e) {
		// TODO Auto-generated method stub
		System.out.println("<=================>\n");
		
		board.setPhase("Coin Flip");
		
		if(e == KeyEvent.VK_SPACE) {
			for (Box box: board.getField()) {
				if (box.getSoldier() != null) {
					System.out.print(box.getSoldier().getStrength() + " ");
				}
			}
			
			System.out.println();
			
			for (Box box: board.getField()) {
				System.out.print(board.getField().indexOf(box) + " ");
			}
		}
		
		System.out.println("\nPhase - " + board.getPhase());
		System.out.println("\n<=================>");
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