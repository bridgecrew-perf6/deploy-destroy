package game;

import java.awt.*;

public class Box {
	private static int width;
	private static int height;
	
	private int posX;
	private int posY;
	private boolean isOccupied;
	private Soldier soldier;
	
	public Box(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		width = 45;
		height = 45;
		this.isOccupied = false;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect(this.posX, this.posY, width, height);
		g.setColor(Color.BLACK);
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
		Box.width = width;
	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		Box.height = height;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public Soldier getSoldier() {
		return soldier;
	}

	public void setSoldier(Soldier soldier) {
		this.soldier = soldier;
	}
}
