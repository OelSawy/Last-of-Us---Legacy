package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;

public class Medic extends Hero {
	//Heal amount  attribute - quiz idea
	

	public Medic(String name,int maxHp, int attackDmg, int maxActions) {
		super( name, maxHp,  attackDmg,  maxActions) ;
		
		
	}

	@Override
	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if (this.getTarget() == null) {
			throw new InvalidTargetException("You have to select a target");
		}
		if (this.getTarget().getClass() == Zombie.class) {
			throw new InvalidTargetException("You have to select a hero");
		}
		this.findAdjacent();
		Character target = this.getTarget();
		boolean isAdjacent = false;
		for (int i = 0; i < this.getAdjacentPoints().size(); i++) {
			if (this.getAdjacentPoints().get(i).equals(target.getLocation())) {
				isAdjacent = true;
			}
		}
		if (!isAdjacent && this != this.getTarget()) {
			throw new InvalidTargetException("Target not in range");
		}
		if (this.getSupplyInventory().size() == 0) {
			throw new NoAvailableResourcesException("You have no supplies");
		}
		this.setSpecialAction(true);
		this.getSupplyInventory().get(0).use(this);
		this.heal(target);
	}
	


}
