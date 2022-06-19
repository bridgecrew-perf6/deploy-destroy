package game;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.TimeUnit;

public class Soldier {
	private static int width;
	private static int height;
	

	private int posX;
	private int posY;
	private int strength;
	private String army;
	
	private int prevX;
	private int prevY;
	
	private boolean isHovered;
	private boolean isSelected;
	private boolean isDeployed;
	private boolean isDestroyable;
	private boolean isDestroyed;
	
	public Soldier(String army, int strength) {
		this.army = army;
		this.strength = strength;
		this.isHovered = false;
		this.isDestroyable = false;
		width = 45;
		height = 45;
	}
	
	// Draw the soldier depending on army
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		if (!this.isDestroyed()) {
			if (this.army == "Player1") {
				g.setColor(Color.CYAN);
				g.fillOval(this.posX, this.posY, Soldier.width, Soldier.height);
				g.setColor(Color.BLACK);
				g.drawString(String.valueOf(this.strength), this.posX + 10, this.posY + 37);
				g.setColor(Color.BLACK);
			}
			else if (this.army == "Player2") {
				g.setColor(Color.RED);
				g.fillOval(this.posX, this.posY, Soldier.width, Soldier.height);
				g.setColor(Color.WHITE);
				g.drawString(String.valueOf(this.strength), this.posX + 10, this.posY + 37);
				g.setColor(Color.BLACK);
			}
			
			Shape circle = new Ellipse2D.Float(this.getPosX(), this.getPosY(), Soldier.getWidth(), Soldier.getHeight());
			g2.setStroke(new BasicStroke(1));
			g2.draw(circle);
		}
	}
	
	public void deploy(Board board, int targetX, int targetY) {
		for (Box box: board.getField()) {
			if (!box.isOccupied() && targetX > box.getPosX() && targetX < box.getPosX()+Box.getWidth() && targetY > box.getPosY() && targetY < box.getPosY()+Box.getHeight()) {
				this.setPosX(box.getPosX());
				this.setPosY(box.getPosY());
				this.setPrevX(box.getPosX());
				this.setPrevY(box.getPosY());
				
				this.setSelected(false);
				this.setDeployed(true);
				
				box.setSoldier(this);
				box.setOccupied(true);
				
				board.switchTurn();
				board.setTurnSwitches(0);
				board.addSoldierOnField();
				if (board.getSoldiersOnField() == board.getPlayers().size() * 8) {
					board.setPhase("Destroy Phase");
					board.updateDestroyables();
				}
				
				break;
			}
			else {
				this.setPosX(this.getPrevX());
				this.setPosY(this.getPrevY());
			}
		}
	}
	
	public void destroy(Board board) {
		int boxIndex = -1;
		
		for(Box box: board.getField()) {
			if(this.equals(box.getSoldier())) {
				boxIndex = board.getField().indexOf(box);
			}
		}
		
		if(this.isDestroyable(board)) {
			board.getField().get(boxIndex).setSoldier(null);
			board.getField().get(boxIndex).setOccupied(false);
			
			this.setDestroyed(true);
			this.setPosX(-50);
			this.setPosY(-50);
			this.setPrevX(-50);
			this.setPrevY(-50);
		}
			
		board.setTurnSwitches(0);
		board.switchTurn();
		board.updateDestroyables();
}
	
	
	public int getPrevX() {
		return prevX;
	}

	public void setPrevX(int prevX) {
		this.prevX = prevX;
	}

	public int getPrevY() {
		return prevY;
	}

	public void setPrevY(int prevY) {
		this.prevY = prevY;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public static int getWidth() {
		return width;
	}

	public static void setWidth(int width) {
		Soldier.width = width;
	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		Soldier.height = height;
	}

	public boolean isHovered() {
		return isHovered;
	}

	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}

	public String getArmy() {
		return army;
	}

	public int getStrength() {
		return strength;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isDeployed() {
		return isDeployed;
	}

	public void setDeployed(boolean isDeployed) {
		this.isDeployed = isDeployed;
	}

	public boolean isDestroyable(Board board) {
		int attack = 0;
		
		int boxIndex = -1;
		Soldier leftBox = null;
		Soldier rightBox = null;
		
		// finidng the box where the soldier is
		for (Box box: board.getField()) {
			if(this.equals(box.getSoldier())) {
				boxIndex = board.getField().indexOf(box);
				
				try {
					leftBox = board.getField().get(boxIndex - 1).getSoldier();
				}
				catch (IndexOutOfBoundsException err) {
					leftBox = null;
				}
				
				try {
					rightBox = board.getField().get(boxIndex + 1).getSoldier();
				}
				catch (IndexOutOfBoundsException err) {
					rightBox = null;
				}
					
				// if the neighbours are null count them as 0
				if (leftBox == null) {
					attack += 0;
				}
				else {
					attack += leftBox.getStrength();
				}
				
				if (rightBox == null) {
					attack += 0;
				}
				else {
					attack += rightBox.getStrength();
				}
			}
		}
		
		if (board.getPhase() == "Destroy Phase" && boxIndex != -1) {
			if (leftBox != null && this.getArmy() == leftBox.getArmy()) {
				attack -= leftBox.getStrength();
			}
			
			if (rightBox != null && this.getArmy() == rightBox.getArmy()) {
				attack -= rightBox.getStrength();
			}
			
			if (this.getStrength() < attack) {
				this.setDestroyable(true);
			}
			else {
				this.setDestroyable(false);
			}
		}
		
		return isDestroyable;
	}

	public void setDestroyable(boolean isDestroyable) {
		this.isDestroyable = isDestroyable;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	@Override
	public String toString() {
		return "Soldier [army=" + army + ", posX=" + posX + ", posY=" + posY + ", strength=" + strength + ", isHovered="
				+ isHovered + "]";
	}	
}
