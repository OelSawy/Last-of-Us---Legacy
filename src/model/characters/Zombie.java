package model.characters;

import java.awt.Point;

import engine.Game;
import model.world.CharacterCell;

public class Zombie extends Character {
	static int ZOMBIES_COUNT = 1;
	
	public Zombie() {
		super("Zombie " + ZOMBIES_COUNT, 40, 10);
		ZOMBIES_COUNT++;
	}

	@Override
	public void attack() {
		this.findAdjacent();
		this.chooseTarget();
		if (this.getTarget() == null) {
			return;
		}
		try {
			super.attack();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void chooseTarget() {
		for (int i = 0; i < this.getAdjacentPoints().size(); i++) {
			Point p = this.getAdjacentPoints().get(i);
			if (Game.map[p.x][p.y].getClass() == CharacterCell.class) {
				if (((CharacterCell) Game.map[p.x][p.y]).getCharacter() != null) {
					if (((CharacterCell) Game.map[p.x][p.y]).getCharacter() instanceof Hero) {
						this.setTarget(((CharacterCell) Game.map[p.x][p.y]).getCharacter());
						return;
					}
				}
			}
		}
	}

	@Override
	public void onCharacterDeath() {
		Game.zombies.remove(this);
		super.onCharacterDeath();
		Game.spawnZombieAfterDeath(1,this.getLocation());
	}
}


