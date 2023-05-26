package model.characters;

import java.awt.Point;
import java.util.ArrayList;

import engine.Game;
import model.world.CharacterCell;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;


public abstract class Character {
	private String name;
	private Point location;
	private int maxHp;
	private int currentHp;
	private int attackDmg;
	private Character target;
	private ArrayList<Point> adjacentPoints;

	
	public Character() {
	}
	

	public Character(String name, int maxHp, int attackDmg) {
		this.name=name;
		this.maxHp = maxHp;
		this.currentHp = maxHp;
		this.attackDmg = attackDmg;
		this.adjacentPoints = new ArrayList<Point>();
	}
		
	public Character getTarget() {
		return target;
	}

	public void setTarget(Character target) {
		this.target = target;
	}
	
	public String getName() {
		return name;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		if(currentHp < 0) {
			this.currentHp = 0;
		}
		else if(currentHp > maxHp) 
			this.currentHp = maxHp;
		else 
			this.currentHp = currentHp;
	}

	public int getAttackDmg() {
		return attackDmg;
	}

	public ArrayList<Point> getAdjacentPoints() {
		return adjacentPoints;
	}
	
	public void attack() throws InvalidTargetException,NotEnoughActionsException {
		this.getTarget().takeDamage(this.getAttackDmg());
		if (this.getTarget().getCurrentHp() == 0) {
			this.getTarget().onCharacterDeath();
		}
		this.getTarget().defend(this);
		if (this.getCurrentHp() == 0) {
			this.onCharacterDeath();
		}
		if (this.getTarget().getCurrentHp() == 0) {
			this.setTarget(null);
		}
	}

	public void defend(Character c) {
		c.takeDamage((this.getAttackDmg()/2));
	}


	public void takeDamage(int damage) {
		this.setCurrentHp(this.currentHp - damage);
	}

	public void onCharacterDeath() {
		Point p = this.getLocation();
		((CharacterCell) Game.map[p.x][p.y]).setCharacter(null);
	}

	public boolean isAdjacent(Point p) {
		int diffX = Math.abs(this.getLocation().x - p.x);
		int diffY = Math.abs(this.getLocation().y - p.y);
		if (diffX == 0 && diffY == 0) {
			return false;
		}
		if (diffX <= 1 && diffY <=1) {
			return true;
		} else {
			return false;
		}
	}

	public void findAdjacent() {
		this.adjacentPoints.clear();
		for (int i = 0; i < Game.map.length; i++) {
			for (int j = 0; j < Game.map[i].length; j++) {
				Point p = new Point(i, j);
				if (this.isAdjacent(p)) {
					this.adjacentPoints.add(p);
				}
			}
		}
	}
}