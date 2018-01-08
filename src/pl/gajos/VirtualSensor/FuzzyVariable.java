package pl.gajos.VirtualSensor;

import java.util.Arrays;
import java.util.List;

public abstract class FuzzyVariable<T extends Enum<T>,U extends Number> {
	
	
	//Index of most force fuzzy variable
	protected int mostForceVariable = 0;
	/**
	 * Table of variable values.
	 */
	protected T[] variableValues;

	/**
	 * Table of forces for all variables.
	 */
	protected float[] variableForces;
	
	/**
	 * Membership functions in array.
	 */
	protected MembershipFunction<U>[] membershipFunctions;
	
	
	/**
	 * Method to count value of membership function.
	 * @param sharpValue
	 * @return
	 */
	protected final float getMenbershipFunctionValue(T fuzzyVariable, U sharpValue)
	{
		return membershipFunctions[fuzzyVariable.ordinal()].count(sharpValue);
	}
	
	/**
	 * Method to choose the strongest rule in fuzzy set.
	 */
	public final void chooseDomineVariable()
	{
		//Find the biggest force.
		for(int i = 0; i < variableValues.length; ++i)
		{
			if(variableForces[mostForceVariable] < variableForces[i])
				mostForceVariable = i;
		}
	}
	
	/**
	 * Method to get variable final value.
	 * @return
	 */
	public final T getValue()
	{
		return variableValues[mostForceVariable];
	}
	
	/**
	 * Method to get variable force.
	 * @return
	 */
	public final float getForce()
	{
		return variableForces[mostForceVariable];
	}
	
	protected final void initVariable(T[] variable)
	{
		//Set variableTable reference
		variableValues = variable;
		
		variableForces = new float[variableValues.length];
	}
	
	protected void initMembershipFunctions(U setMin, U setMax, List<MFPoint[]> membershipFunctionDefinitions) {
		
		membershipFunctions = membershipFunctionDefinitions.stream()
				.map( i -> new MembershipFunction<U>(setMin, setMax, i))
				.toArray(MembershipFunction[]::new);
	}
	
	public void fuzzyInput(U sharpValue)
	{
		//Count variable force by membership functions.
		for(int i = 0; i < variableValues.length; ++i)
		{
			variableForces[i] = getMenbershipFunctionValue(variableValues[i], sharpValue);
		}
	}
	
	public MFPoint[] getMFPoints(Enum variableValue)
	{
		return membershipFunctions[variableValue.ordinal()].getCharacteristicPoints();
	}
	
}
