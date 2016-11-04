package mode.adapter;

/** Interface Barrel appears after Tank and TankImpl
 *  In order not to change Tank and TankImpl, Adapter mode is used
 * */
public class BarrelAdapter implements Barrel {
	
	protected final Tank tank;
	
	public BarrelAdapter(Tank tank){
		this.tank = tank;
	}
	
	public float getCubage() {
		// if the method name is not same, Adapter get Extend effort.
		return tank.getCapacity();
	}
	
	public float getVolume(){
		/* BarrelAdapter extends two classes(or implements two interfaces): Barrel and Tank
		 it brings benifit of using two classes' methods.*/
		return tank.getVolume();
	}
	
	/*
	 * new method can added, but not changes the existed class or interface for Example Tank or TankImpl
	 * */
	protected void checkVolume()throws AssertionError{
		float v = getVolume();
		float c = getCubage();
		if(!(v >= 0.0 && v <= c)) throw new AssertionError();
	}
	
	public synchronized void transferWater(float amount){
		// checkVolume is used in Before/After mode
		checkVolume();
		try{
			tank.transferWater(amount);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			checkVolume();
		}
	}

}
