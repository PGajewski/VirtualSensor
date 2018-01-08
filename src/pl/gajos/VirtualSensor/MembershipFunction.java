package pl.gajos.VirtualSensor;

import java.util.Arrays;

public class MembershipFunction<U extends Number> {
		
	/**
	 * Minimum value of set.
	 */
	private float setMinimum;
	
	/**
	 * Maximum value of set.
	 */
	private float setMaximum;
	
	/**
	 * Characteristic points of membership function.
	 */
	private MFPoint[] characteristicPoints;
	
	/**
	 * Scaled characteristic points.
	 */
	private Float[] scaledArguments;

	public MFPoint[] getCharacteristicPoints()
	{
		return this.characteristicPoints;
	}
	public MembershipFunction(U setMin, U setMax, MFPoint[] characteristicPoints)
	{
		//Check arguments of characteristic points.
		float lastArgument = characteristicPoints[0].getArgument();
		for(int i = 1; i < characteristicPoints.length; ++i)
		{
			if(lastArgument >= characteristicPoints[i].getArgument())
				throw new IllegalArgumentException();
			lastArgument = characteristicPoints[i].getArgument();
		}
		this.characteristicPoints = characteristicPoints;
		
		this.setMinimum = setMin.floatValue();
		this.setMaximum = setMax.floatValue();
		
		//Scale characteristic points.
		this.scaleCharacteristicPoints();
		
	}
	
	public float count(U sharpValue)
	{
		//
		float fSharpValue = sharpValue.floatValue();
		
		//Keep last value
		if(fSharpValue <= scaledArguments[0])
			return characteristicPoints[0].getLevel();
			
		if(fSharpValue >= scaledArguments[characteristicPoints.length - 1])
			return characteristicPoints[characteristicPoints.length - 1].getLevel();
		
		//Interpolate value.
		//Find first point with argument bigger than value.
		int i;
		for(i = 1; i < characteristicPoints.length; ++i)
		{
			if(fSharpValue <= scaledArguments[i])
			{
				break;
			}
		}
		
		//Count value from linear interpolation from this and previous point.
		return characteristicPoints[i-1].getLevel() + (characteristicPoints[i].getLevel()-characteristicPoints[i-1].getLevel())*(fSharpValue - scaledArguments[i-1])/(scaledArguments[i] - scaledArguments[i-1]);
	}

	private void scaleCharacteristicPoints()
	{
		this.scaledArguments = Arrays.stream(this.characteristicPoints)
				.map(p -> p.getArgument()*(setMaximum - setMinimum) + setMinimum)
				.toArray(Float[]::new);
	}
	
	public float scaleArgument(float argument)
	{
		return argument * (setMaximum - setMinimum) + setMinimum;
	}
	
	
	public static float interpolateArgument(MFPoint x1, MFPoint x0, float level)
	{
		return (level - x0.getLevel())*(x1.getArgument() - x0.getArgument())/(x1.getLevel() - x0.getLevel()) + x0.getArgument();

	}
	
	public static float interpolateLevel(MFPoint x1, MFPoint x0, float argument)
	{
		return x0.getLevel() + (x1.getLevel()-x0.getLevel())*(argument - x0.getArgument())/(x1.getArgument() - x0.getArgument());

	}
	
	public static float countCenterOfArea(MFPoint[] area)
	{
		float dividend = 0;
		for(int i = 0; i < area.length - 1; ++i)
		{
			dividend += (area[i+1].getArgument() - area[i].getArgument())*((2*area[i+1].getArgument() + area[i].getArgument())*area[i+1].getLevel() + (2*area[i].getArgument() + area[i+1].getArgument())*area[i].getLevel());
		}
		float divider = 0;
		for(int i = 0; i < area.length - 1; ++i)
		{
			divider += (area[i+1].getArgument() - area[i].getArgument())*(area[i+1].getLevel() + area[i].getLevel());
		}
		if(divider != 0)
			return dividend/(3*divider);
		else
			return 0;
	}
	
	public static float countArea(MFPoint[] area)
	{
		float fArea = 0;
		for(int i = 1; i < area.length; ++i)
		{
			fArea += 0.5*(area[i].getLevel() + area[i-1].getLevel())*(area[i].getArgument() - area[i-1].getArgument());
		}
		return fArea;
	}
}
