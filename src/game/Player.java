package game;

import java.util.ArrayList;

public class Player {
	private ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
	private String username;
	private int armyX;
	private int armyY;
	
	public Player(String username, int armyX, int armyY) {
		this.username = username;
		this.armyX = armyX;
		this.armyY = armyY;
	}

	public ArrayList<Soldier> getSoldiers() {
		return soldiers;
	}

	public void addSoldier(Soldier soldier) {
		soldiers.add(soldier);
	}

	public String getUsername() {
		return username;
	}

	public int getArmyX() {
		return armyX;
	}

	public void setArmyX(int armyX) {
		this.armyX = armyX;
	}

	public int getArmyY() {
		return armyY;
	}

	public void setArmyY(int armyY) {
		this.armyY = armyY;
	}
}
