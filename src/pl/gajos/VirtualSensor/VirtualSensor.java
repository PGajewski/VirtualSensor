/**
 * 
 */
package pl.gajos.VirtualSensor;

/**
 * @author Pawe³ Gajewski
 *
 */
public abstract class VirtualSensor<T> implements Learnable, Runnable {
	
	
	/**
	 * State of inputs.
	 */
	protected Number[] inputs;
	
	/**
	 * Output
	 */
	protected T output;
	
	public void setInputs(Number[] inputs) {
		this.inputs = inputs;
	}
	
	public T getOutput() {
		return this.output;
	}
	
	/**
	 * Method to get result of working virtual sensor.
	 * @return
	 */
	public void run()
	{
		defuzzy(DefuzzyficationType.COS);
	}
	
	protected abstract void defuzzy(DefuzzyficationType type, FuzzyVariable...fuzzyVariables);
	
	
}
