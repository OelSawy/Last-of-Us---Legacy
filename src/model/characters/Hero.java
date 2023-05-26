package model.characters;

import java.awt.Point;
import java.util.ArrayList;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;


public abstract class Hero extends Character {
	

		private int actionsAvailable;
		private int maxActions;
		private ArrayList<Vaccine> vaccineInventory;
		private ArrayList<Supply> supplyInventory;
		private boolean specialAction;


		public Hero(String name,int maxHp, int attackDmg, int maxActions) {
			super(name,maxHp, attackDmg);
			this.maxActions = maxActions;
			this.actionsAvailable = maxActions;
			this.vaccineInventory = new ArrayList<Vaccine>();
			this.supplyInventory=new ArrayList<Supply>();
			this.specialAction=false;
		
		}

		
	


		public boolean isSpecialAction() {
			return specialAction;
		}



		public void setSpecialAction(boolean specialAction) {
			this.specialAction = specialAction;
		}



		public int getActionsAvailable() {
			return actionsAvailable;
		}



		public void setActionsAvailable(int actionsAvailable) {
			this.actionsAvailable = actionsAvailable;
		}



		public int getMaxActions() {
			return maxActions;
		}



		public ArrayList<Vaccine> getVaccineInventory() {
			return vaccineInventory;
		}


		public ArrayList<Supply> getSupplyInventory() {
			return supplyInventory;
		}

		public void addSupply(Supply s) {
			this.supplyInventory.add(s);
		}
		
		public void addVaccine(Vaccine v) {
			this.vaccineInventory.add(v);
		}
	
		public void removeSupply(Supply s) {
			this.supplyInventory.remove(s);
		}

		public void removeVaccine(Vaccine v) {
			this.vaccineInventory.remove(v);
		}

		@Override
		public void attack() throws InvalidTargetException, NotEnoughActionsException{
			if (this.getTarget() == null) {
				throw new InvalidTargetException("You have to select a target");
			}
			if (this.getTarget().getClass() != Zombie.class) {
				throw new InvalidTargetException("You have to select a zombie");
			}
			this.findAdjacent();
			Character target = this.getTarget();
			boolean isAdjacent = false;
			for (int i = 0; i < this.getAdjacentPoints().size(); i++) {
				if (this.getAdjacentPoints().get(i).equals(target.getLocation())) {
					isAdjacent = true;
				}
			}
			if (isAdjacent == false) {
				throw new InvalidTargetException("Target not in range");
			}
			if (this.actionsAvailable == 0) {
				if (this.getClass() == Fighter.class) {
					if (this.specialAction == false) {
						throw new NotEnoughActionsException("You don't have actions points");
					}
				} else {
					throw new NotEnoughActionsException("You don't have actions points");
				}
			}
			super.attack();
			if (this.getClass() != Fighter.class) {
				this.actionsAvailable--;
			} else {
				if (!this.specialAction) {
					this.actionsAvailable--;
				} else {
					return;
				}
			}
		}





		@Override
		public void onCharacterDeath() {
			Game.heroes.remove(this);
			super.onCharacterDeath();
		}

		public void move(Direction d) throws NotEnoughActionsException,MovementException {
			if (this.getCurrentHp() == 0) {
				this.onCharacterDeath();
				return;
			}
			if (this.getActionsAvailable() != 0) {
				Point p = this.getLocation();
				if (d == Direction.UP) {
					if (p.x == 14) {
						throw new MovementException("Out of bounds");
					} else if ((Game.map[p.x+1][p.y].getClass() == CharacterCell.class && ((CharacterCell) Game.map[p.x+1][p.y]).getCharacter() != null)) {
						throw new MovementException("Cell is occupied");
					} else {
						if (Game.map[p.x+1][p.y].getClass() == CollectibleCell.class) {
							((CollectibleCell) Game.map[p.x+1][p.y]).getCollectible().pickUp(this);
						} else if (Game.map[p.x+1][p.y].getClass() == TrapCell.class) {
							Game.trapsCount--;
							this.takeDamage(((TrapCell) Game.map[p.x+1][p.y]).getTrapDamage());
							if (this.getCurrentHp() == 0) {
								this.onCharacterDeath();
								Game.map[p.x+1][p.y] = new CharacterCell(null);
								return;
							} else {
								Game.map[p.x+1][p.y] = new CharacterCell(this);
							}
						}
						Game.map[p.x+1][p.y] = new CharacterCell(this);
						this.findAdjacent();
						Game.updateVisibleCells(this);
						this.setLocation(new Point(p.x+1,p.y));
						Game.map[p.x][p.y] = new CharacterCell(null);
					}
				} else if (d == Direction.DOWN) {
					if (p.x == 0) {
						throw new MovementException("Out of bounds");
					} else if ((Game.map[p.x-1][p.y].getClass() == CharacterCell.class && ((CharacterCell) Game.map[p.x-1][p.y]).getCharacter() != null)) {
						throw new MovementException("Cell is occupied");
					}	else {
						if (Game.map[p.x-1][p.y].getClass() == CollectibleCell.class) {
							((CollectibleCell) Game.map[p.x-1][p.y]).getCollectible().pickUp(this);
						} else if (Game.map[p.x-1][p.y].getClass() == TrapCell.class) {
							Game.trapsCount--;
							this.takeDamage(((TrapCell) Game.map[p.x-1][p.y]).getTrapDamage());
							if (this.getCurrentHp() == 0) {
								this.onCharacterDeath();
								Game.map[p.x-1][p.y] = new CharacterCell(null);
								return;
							} else {
								Game.map[p.x-1][p.y] = new CharacterCell(this);
							}
						}
						Game.map[p.x-1][p.y] = new CharacterCell(this);
						this.findAdjacent();
						Game.updateVisibleCells(this);
						this.setLocation(new Point(p.x-1,p.y));
						Game.map[p.x][p.y] = new CharacterCell(null);
					}
				} else if (d == Direction.LEFT) {
					if (p.y == 0) {
						throw new MovementException("Out of bounds");
					} else if ((Game.map[p.x][p.y-1].getClass() == CharacterCell.class && ((CharacterCell) Game.map[p.x][p.y-1]).getCharacter() != null)) {
						throw new MovementException("Cell is occupied");
					} else {
						if (Game.map[p.x][p.y-1].getClass() == CollectibleCell.class) {
							((CollectibleCell) Game.map[p.x][p.y-1]).getCollectible().pickUp(this);
						} else if (Game.map[p.x][p.y-1].getClass() == TrapCell.class) {
							Game.trapsCount--;
							this.takeDamage(((TrapCell) Game.map[p.x][p.y-1]).getTrapDamage());
							if (this.getCurrentHp() == 0) {
								this.onCharacterDeath();
								Game.map[p.x][p.y-1] = new CharacterCell(null);
								return;
							} else {
								Game.map[p.x][p.y-1] = new CharacterCell(this);
							}
						}
						Game.map[p.x][p.y-1] = new CharacterCell(this);
						this.setLocation(new Point(p.x,p.y-1));
						this.findAdjacent();
						Game.updateVisibleCells(this);
						Game.map[p.x][p.y] = new CharacterCell(null);
					}
				} else {
					if (p.y == 14) {
						throw new MovementException();
					} else if ((Game.map[p.x][p.y+1].getClass() == CharacterCell.class && ((CharacterCell) Game.map[p.x][p.y+1]).getCharacter() != null)) {
						throw new MovementException("Cell is occupied");
					} else {
						if (Game.map[p.x][p.y+1].getClass() == CollectibleCell.class) {
							((CollectibleCell) Game.map[p.x][p.y+1]).getCollectible().pickUp(this);
						} else if (Game.map[p.x][p.y+1].getClass() == TrapCell.class) {
							Game.trapsCount--;
							this.takeDamage(((TrapCell) Game.map[p.x][p.y+1]).getTrapDamage());
							if (this.getCurrentHp() == 0) {
								this.onCharacterDeath();
								Game.map[p.x][p.y+1] = new CharacterCell(null);
								return;
							} else {
								Game.map[p.x][p.y+1] = new CharacterCell(this);
							}
						}
						Game.map[p.x][p.y+1] = new CharacterCell(this);
						this.setLocation(new Point(p.x,p.y+1));
						this.findAdjacent();
						Game.updateVisibleCells(this);
						Game.map[p.x][p.y] = new CharacterCell(null);
					}
				}
				this.findAdjacent();
				Game.updateVisibleCells(this);
				this.actionsAvailable--;
			} else {
				throw new NotEnoughActionsException("You don't have actions points");
			}
		}

		public abstract void useSpecial() throws NoAvailableResourcesException, InvalidTargetException;

		public void cure() throws InvalidTargetException, NoAvailableResourcesException, NotEnoughActionsException{
			this.findAdjacent();
			if (this.getTarget() == null) {
				throw new InvalidTargetException("You have to select a target");
			}
			if (this.getTarget().getClass() != Zombie.class) {
				throw new InvalidTargetException("You have to select a zombie");
			}
			Character target = this.getTarget();
			boolean isAdjacent = false;
			for (int i = 0; i < this.getAdjacentPoints().size(); i++) {
				if (this.getAdjacentPoints().get(i).equals(target.getLocation())) {
					isAdjacent = true;
				}
			}
			if (isAdjacent == false) {
				throw new InvalidTargetException("Target not in range");
			}
			if (this.getActionsAvailable() <= 0) {
				throw new NotEnoughActionsException("You don't have actions points");
			}
			if (this.vaccineInventory.size() == 0) {
				throw new NoAvailableResourcesException("You have no vaccines");
			}
			
			this.vaccineInventory.get(0).use(this);
		}

		public void heal(Character c) {
			c.setCurrentHp(c.getMaxHp());
			this.setSpecialAction(false);
		}
}
