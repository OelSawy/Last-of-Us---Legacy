package model.collectibles;

import java.awt.Point;
import java.util.Random;

import engine.Game;
import model.characters.Hero;
import model.world.CharacterCell;

public class Vaccine implements Collectible {

	public Vaccine() {
		
	}

	@Override
	public void pickUp(Hero h) {
		h.addVaccine(this);
		Game.vaccinesCount--;
	}

	@Override
	public void use(Hero h) {
		h.removeVaccine(this);
		Point p = h.getTarget().getLocation();
		Game.zombies.remove(h.getTarget());
		Random r = new Random();
		int x = r.nextInt(Game.availableHeroes.size());
		Hero h1 = Game.availableHeroes.get(x);
		h1.setLocation(p);
		Game.availableHeroes.remove(h1);
		Game.heroes.add(h1);
		h1.findAdjacent();
		Game.map[p.x][p.y] = new CharacterCell(h1);
		Game.updateVisibleCells(h1);
		Game.vaccinesCount--;
		h.setActionsAvailable(h.getActionsAvailable() - 1);
	}

}
