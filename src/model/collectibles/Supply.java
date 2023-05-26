package model.collectibles;

import engine.Game;
import model.characters.Hero;

public class Supply implements Collectible  {

	

	
	public Supply() {
		
	}

	@Override
	public void pickUp(Hero h) {
		h.addSupply(this);
		Game.suppliesCount--;
	}

	@Override
	public void use(Hero h) {
		h.removeSupply(this);
		h.setSpecialAction(true);
	}


	
		
		

}
