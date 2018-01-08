package pl.gajos.VirtualSensor;

import java.util.Arrays;

public class FuzzyFlow extends FuzzyVariable<FuzzyFlowValue, Float> {
	FuzzyFlow(Float setMin, Float setMax)
	{
		initVariable(FuzzyFlowValue.values());
		/*
		initMembershipFunctions(setMin, setMax, Arrays.asList(new MFPoint[][] {
			{new MFPoint(0,1), new MFPoint(0.25f,0)},								//First triangle function.
			{new MFPoint(0,0), new MFPoint(0.25f,1), new MFPoint(0.5f,0)},			//Second triangle function.
			{new MFPoint(0.25f,0), new MFPoint(0.5f, 1), new MFPoint(0.75f, 0)},	//Third triangle function.	
			{new MFPoint(0.5f, 0), new MFPoint(0.75f, 1), new MFPoint(1, 0)},		//Fourth triangle function.
			{new MFPoint(0.75f, 0), new MFPoint(1, 1)}								//Fifth triangle function.
		}));
		*/
		
		initMembershipFunctions(setMin, setMax, Arrays.asList(new MFPoint[][] {
			{new MFPoint(0,1), new MFPoint(0.125f,0)},									//First triangle function.
			{new MFPoint(0,0), new MFPoint(0.125f,1), new MFPoint(0.25f,0)},			//Second triangle function.
			{new MFPoint(0.125f,0), new MFPoint(0.25f, 1), new MFPoint(0.375f, 0)},		//Third triangle function.	
			{new MFPoint(0.25f, 0), new MFPoint(0.375f, 1), new MFPoint(0.5f, 0)},		//Fourth triangle function.
			{new MFPoint(0.375f, 0), new MFPoint(0.5f, 1), new MFPoint(0.625f, 0)},		//Fifth triangle function.
			{new MFPoint(0.5f, 0), new MFPoint(0.625f, 1), new MFPoint(0.75f, 0)},		//Sixth triangle function.
			{new MFPoint(0.625f, 0), new MFPoint(0.75f, 1), new MFPoint(0.875f, 0)},	//Seventh triangle function.
			{new MFPoint(0.75f, 0), new MFPoint(0.875f, 1), new MFPoint(1, 0)},			//Eighth triangle function.
			{new MFPoint(0.875f, 0), new MFPoint(1, 1)},								//Ninth triangle function.

		}));
		
	}	
}
