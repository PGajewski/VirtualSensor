package pl.gajos.VirtualSensor;

import java.util.ArrayList;
import java.util.List;

public abstract class Rule<T extends Enum, U extends Enum, W extends Enum> {
	/**
	 * Value of result
	 */
	private W resultValue;
	
	private T praemissaOne;
	
	private U praemissaTwo;
	
	/**
	 * Result force.
	 * @return
	 */
	private float resultForce;
	
	public W getResult()
	{
		return resultValue;
	}
	
	public float getForce()
	{
		return resultForce;
	}
	
	public T getPraemissaOne()
	{
		return praemissaOne;
	}
	
	public U getPraemissaTwo()
	{
		return praemissaTwo;
	}
	
	protected void initRule(T praemissaOne, U praemissaTwo, W resultValue, float resultForce)
	{
		this.praemissaOne = praemissaOne;
		this.praemissaTwo = praemissaTwo;
		this.resultValue = resultValue;
		this.resultForce = resultForce;
	}
	
	public Enum[] getPraemissas()
	{
		List<Enum> temp = new ArrayList<>();
		if(praemissaOne != null)
			temp.add(praemissaOne);
		if(praemissaTwo != null)
			temp.add(praemissaTwo);
		return temp.toArray(new Enum[0]);
	}
}
