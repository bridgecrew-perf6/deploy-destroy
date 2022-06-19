package game;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Board {
	private String phase;
	private ArrayList<Player> players;
	private ArrayList<Box> field;
	private int soldiersOnField;
	private int turn;
	private int turnSwitches;
	
	private int allyDestroyables = 0;
	private int enemyDestroyables = 0;
	
	private ArrayList<Soldier> allyVeterans;
	private ArrayList<Soldier> enemyVeterans;

	private String winner = "";
	
	public Board() {
		this.phase = "";
		this.players = new ArrayList<Player>();
		this.field = new ArrayList<Box>();
		
		this.soldiersOnField = 0;
		this.turn = 0;
		
		this.allyDestroyables = -1;
		this.enemyDestroyables = -1;
		
		this.allyVeterans = new ArrayList<Soldier> ();
		this.enemyVeterans = new ArrayList<Soldier> ();
	}
	
	public void flipCoin() {
		setPhase("Deploy Phase");
		System.out.println(getPhase());
	}
	
	public void updateDestroyables() {
		allyDestroyables = 0;
		enemyDestroyables = 0;
		
		for (Box box: this.getField()) {
			if (box.getSoldier() != null && box.getSoldier().isDestroyable(this) && box.getSoldier().getArmy() == "Player1") {
				allyDestroyables++;
			}
			else if (box.getSoldier() != null && box.getSoldier().isDestroyable(this) && box.getSoldier().getArmy() == "Player2") {
				enemyDestroyables++;
			}
		}
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
		DeployDestroy.time = 0;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player){
		this.players.add(player);
	}

	public ArrayList<Box> getField() {
		return field;
	}
	
	public void addBox(Box box) {
		this.field.add(box);
	}

	public int getSoldiersOnField() {
		return soldiersOnField;
	}
	
	public void setSoldiersOnField(int soldiersOnField) {
		this.soldiersOnField = soldiersOnField;
	}

	public void addSoldierOnField() {
		this.soldiersOnField++;
	}

	public int getTurn() {
		return turn;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}

	public void switchTurn() {
		if (this.getTurnSwitches() < 2) {
			this.turn = (this.turn + 3) % 2;
			this.turnSwitches++;
		}
		else {
			this.setPhase("Endgame");
			
			for (Box box: this.getField()) {
				Soldier soldier = box.getSoldier();
				
				if (soldier != null && soldier.getArmy() == "Player1") {
					allyVeterans.add(soldier);
				}
				else if (soldier != null && soldier.getArmy() == "Player2") {
					enemyVeterans.add(soldier);
				}
			}
			
			if (allyVeterans.size() > enemyVeterans.size()) {
				winner = "Player 1 Wins!";
			}
			else if (enemyVeterans.size() > allyVeterans.size()) {
				winner = "Player 2 Wins!";
			}
			else if (allyVeterans.size() == enemyVeterans.size()) {
				int allyScore = 0;
				int enemyScore = 0;
				
				for (Soldier soldier: allyVeterans) {
					allyScore += soldier.getStrength();
				}
				
				for (Soldier soldier: enemyVeterans) {
					enemyScore += soldier.getStrength();
				}
				
				if (allyScore > enemyScore) { 
					winner = "Player 1 Wins!";
				}
				else if(enemyScore > allyScore) {
					winner = "Player 2 Wins!";
				}
				else {
					winner = "Draw!";
				}
			}
		}
	}

	public int getTurnSwitches() {
		return turnSwitches;
	}

	public void setTurnSwitches(int turnSwitches) {
		this.turnSwitches = turnSwitches;
	}

	public int getAllyDestroyables() {
		return allyDestroyables;
	}

	public void setAllyDestroyables(int allyDestroyables) {
		this.allyDestroyables = allyDestroyables;
	}

	public int getEnemyDestroyables() {
		return enemyDestroyables;
	}

	public void setEnemyDestroyables(int enemyDestroyables) {
		this.enemyDestroyables = enemyDestroyables;
	}

	public ArrayList<Soldier> getAllyVeterans() {
		return allyVeterans;
	}

	public void setAllyVeterans(ArrayList<Soldier> allyVeterans) {
		this.allyVeterans = allyVeterans;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}
}
