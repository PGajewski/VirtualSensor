package pl.gajos.VirtualSensor;
/**
 * Class for characteristic points of membership function i scale [0...1]
 * @author Gajos
 *
 */

public class MFPoint implements Cloneable{
	
	private float argument;
	private float membershipLevel;
	
	public void setArgument(float argument)
	{
		if(argument <= 1 && argument >= 0)
			this.argument = argument;
		else
			throw new IllegalArgumentException();
	}
	
	public void setLevel(float level)
	{
		if(level <= 1 && level >= 0)
			this.membershipLevel = level;
		else
			throw new IllegalArgumentException();
	}
	
	public float getArgument()
	{
		return this.argument;
	}
	
	public float getLevel()
	{
		return this.membershipLevel;
	}
	
	public MFPoint(float argument, float level)
	{
		setArgument(argument);
		setLevel(level);
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}
}
