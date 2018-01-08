/**
 * 
 */
package pl.gajos.VirtualSensor;

/**
 * @author Pawe³ Gajewski
 *
 */
public interface Learnable <T>{
	
	/**
	 * Method to load working logic from file
	 * @param file
	 */
	public void loadLogic();
	
	/**
	 * Method to learn object.
	 */
	public void learn();

}
