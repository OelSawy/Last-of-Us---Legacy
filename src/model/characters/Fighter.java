package model.characters;

import exceptions.NoAvailableResourcesException;

public class Fighter extends Hero{

	
	public Fighter(String name,int maxHp, int attackDmg, int maxActions) {
		super( name, maxHp,  attackDmg,  maxActions) ;
		
	}

	@Override
	public void useSpecial() throws NoAvailableResourcesException {
		if (this.getSupplyInventory().size() == 0) {
			throw new NoAvailableResourcesException("You have no supplies");
		}
		if (this.isSpecialAction() == true) {
			return;
		}
		this.setSpecialAction(true);
		this.getSupplyInventory().get(0).use(this);
	}

	

	
	
	
	

}
