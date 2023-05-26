package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

public class Game {
	
	public static Cell [][] map = new Cell[15][15] ;
	public static ArrayList <Hero> availableHeroes = new ArrayList<Hero>();
	public static ArrayList <Hero> heroes =  new ArrayList<Hero>();
	public static ArrayList <Zombie> zombies =  new ArrayList<Zombie>();
	
	public static int vaccinesCount = 5;
	public static int suppliesCount = 5;
	public static int trapsCount = 5;
	
		
	public static void loadHeroes(String filePath)  throws IOException {
		
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Hero hero=null;
			switch (content[1]) {
			case "FIGH":
				hero = new Fighter(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3]));
				break;
			case "MED":  
				hero = new Medic(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3])) ;
				break;
			case "EXP":  
				hero = new Explorer(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3]));
				break;
			}
			availableHeroes.add(hero);
			line = br.readLine();
			
			
		}
		br.close();

		
		
	}

	public static void startGame(Hero h) {

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				Game.map[i][j] = new CharacterCell(null);
			}
		}
		h.setLocation(new Point(0,0));
		h.findAdjacent();
		heroes.add(h);
		availableHeroes.remove(h);
		Game.map[0][0] = new CharacterCell(h);
		spawnSupply();
		spawnTraps();
		spawnVaccines();
		spawnZombies(10);
		updateVisibleCells(h);
	}

	public static void spawnVaccines() {
		Random r = new Random();

		int vaccines = 5;

		while (vaccines != 0) {
			int x = r.nextInt(map.length);
			int y = r.nextInt(map.length);
			Cell c = Game.map[x][y];
			if (c.getClass() == CharacterCell.class && ((CharacterCell) c).getCharacter() == null) {
				Game.map[x][y] = new CollectibleCell(new Vaccine());
				vaccines--;
			}
		}
	}

	public static void spawnSupply() {
		Random r = new Random();

		int supplies = 5;

		while (supplies != 0) {
			int x = r.nextInt(map.length);
			int y = r.nextInt(map.length);
			Cell c = Game.map[x][y];
			if (c.getClass() == CharacterCell.class && ((CharacterCell) c).getCharacter() == null) {
				Game.map[x][y] = new CollectibleCell(new Supply());
				supplies--;
			}
		}
	}

	public static void spawnTraps() {
		Random r = new Random();

		int traps = 5;

		while (traps != 0) {
			int x = r.nextInt(map.length);
			int y = r.nextInt(map.length);
			Cell c = Game.map[x][y];
			if (c.getClass() == CharacterCell.class && ((CharacterCell) c).getCharacter() == null) {
				Game.map[x][y] = new TrapCell();
				traps--;
			}
		}
	}

	public static void spawnZombies(int count) {
		Random r = new Random();

		int zombie = count;

		while (zombie != 0) {
			int x = r.nextInt(map.length);
			int y = r.nextInt(map.length);
			Cell c = Game.map[x][y];
			if (c.getClass() == CharacterCell.class && ((CharacterCell) c).getCharacter() == null) {
				Zombie z = new Zombie();
				z.setLocation(new Point(x, y));
				z.findAdjacent();
				zombies.add(z);
				Game.map[x][y] = new CharacterCell(z);
				zombie--;
			}
		}
	}

	public static void spawnZombieAfterDeath(int count, Point p) {
		Random r = new Random();

		int zombie = count;

		while (zombie != 0) {
			int x = r.nextInt(map.length);
			int y = r.nextInt(map.length);
			Cell c = Game.map[x][y];
			if (!(x == p.x && y == p.y)) {
				if (c.getClass() == CharacterCell.class && ((CharacterCell) c).getCharacter() == null) {
					Zombie z = new Zombie();
					z.setLocation(new Point(x, y));
					z.findAdjacent();
					zombies.add(z);
					Game.map[x][y] = new CharacterCell(z);
					zombie--;
				}
			}
		}
	}

	public static boolean checkWin() {
		for (int i = 0; i < heroes.size(); i++) {
			if (heroes.get(i).getVaccineInventory().size() != 0) {
				return false;
			}
		}
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (Game.map[i][j].getClass() == CollectibleCell.class) {
						CollectibleCell c = (CollectibleCell) Game.map[i][j];
					if (c.getCollectible().getClass() == Vaccine.class) {
						return false;
					}
				}
			}
		}
		return heroes.size() >= 5;
	}

	public static boolean checkGameOver() {
		for (int i = 0; i < heroes.size(); i++) {
			if (heroes.get(i).getVaccineInventory().size() != 0) {
				return false;
			}
		}
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (Game.map[i][j].getClass() == CollectibleCell.class) {
						CollectibleCell c = (CollectibleCell) Game.map[i][j];
					if (c.getCollectible().getClass() == Vaccine.class) {
						return false;
					}
				}
			}
		}
		return heroes.size() < 5;
	}

	public static void endTurn() {
		for (int i = 0; i < zombies.size(); i++) {
			zombies.get(i).attack();
			zombies.get(i).setTarget(null);
		}
		for (int i = 0; i < Game.map.length; i++) {
			for (int j = 0; j < Game.map[i].length; j++) {
				Game.map[i][j].setVisible(false);
			}
		}
		spawnZombies(1);
		for (int i = 0; i < heroes.size(); i++) {
			heroes.get(i).setActionsAvailable(heroes.get(i).getMaxActions());
			heroes.get(i).setTarget(null);
			heroes.get(i).setSpecialAction(false);
			heroes.get(i).findAdjacent();
			updateVisibleCells(heroes.get(i));
		}
		
	}

	public static void updateVisibleCells(Hero h) {
		Point p = h.getLocation();
		map[p.x][p.y].setVisible(true);
		for (int i = 0; i < h.getAdjacentPoints().size(); i++) {
			Point p1 = h.getAdjacentPoints().get(i);
			if (map[p1.x][p1.y] != null){
				map[p1.x][p1.y].setVisible(true);
			}
		}
	}

	public static void setAllCellsVisible() {
		for (int i = 0; i < Game.map.length; i++) {
			for (int j = 0; j < Game.map[i].length; j++) {
				Game.map[i][j].setVisible(true);
			}
		}
	}
}
