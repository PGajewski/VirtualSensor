package pl.gajos.VirtualSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class WangMenderVirtualSensor<T> extends VirtualSensor<T> {

	protected FuzzyVariable[] variableList;
	
	/**
	 * List of rules for Wang-Mendel method.
	 */
	protected List<Rule> rules;
		
	protected MFPoint[] igniteRule(Rule rule)
	{
		return null;
	}

	public void loadLogic() {
		// TODO Auto-generated method stub
		
	}
	
	private MFPoint[] igniteRule(Rule rule, FuzzyVariable...fuzzyVariables)
	{
		//Find the smallest ignite level from fuzzy variables
		//Last fuzzy variable is output.
		float igniteLevel = 1;
		Enum[] praemissas = rule.getPraemissas();
		for(int i = 0; i < fuzzyVariables.length - 1; ++i)
		{
			float tempIgniteLevel = fuzzyVariables[i].getMenbershipFunctionValue(praemissas[i], this.inputs[i]);
			if(igniteLevel > tempIgniteLevel)
			{
				igniteLevel = tempIgniteLevel;
			}
		}
		
		//Get characteristic points of membership function for output.
		MFPoint[] outputMFPoints = fuzzyVariables[fuzzyVariables.length - 1].getMFPoints(rule.getResult());
		
		//Temporary list of new characteristic points in output function
		List<MFPoint> temp = new ArrayList<>(outputMFPoints.length);
		
		//Check last point was bigger than ignition level.
		boolean upThanIgnition = false;
		
		//Check characteristic points of output membership function.
		if(outputMFPoints[0].getLevel() >= igniteLevel)
		{
			temp.add(new MFPoint(outputMFPoints[0].getArgument(), igniteLevel));
			upThanIgnition = true;
		}
		else
		{
			try {
				temp.add((MFPoint) outputMFPoints[0].clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i = 1; i < outputMFPoints.length; ++i)
		{
			if(upThanIgnition && outputMFPoints[i].getLevel() < igniteLevel)
			{
				//Find a argument for ignition level in interval.
				temp.add(new MFPoint(MembershipFunction.interpolateArgument(outputMFPoints[i], outputMFPoints[i-1], igniteLevel),igniteLevel));
				try {
					temp.add((MFPoint) outputMFPoints[i].clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				upThanIgnition = false;
			}
			else if(!upThanIgnition && outputMFPoints[i].getLevel() > igniteLevel)
			{
				temp.add(new MFPoint(MembershipFunction.interpolateArgument(outputMFPoints[i], outputMFPoints[i-1], igniteLevel),igniteLevel));
				temp.add(new MFPoint(outputMFPoints[i].getArgument(), igniteLevel));
				upThanIgnition = true;
			}
			else if(upThanIgnition) {
				temp.add(new MFPoint(outputMFPoints[i].getArgument(), igniteLevel));
			}
			else
			{
				try {
					temp.add((MFPoint) outputMFPoints[i].clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return temp.toArray(new MFPoint[0]);
		
	}
	
	/**
	 * Found sum of areas.
	 * @param ruleArea
	 * @return
	 */
	
	protected void defuzzy(DefuzzyficationType type, FuzzyVariable... fuzzyVariables)
	{
		//Ignition of all rules.
		List<MFPoint[]>ruleAreas = rules.stream()
							.map(r -> igniteRule(r, fuzzyVariables))
							.collect(Collectors.toList());
		switch(type)
		{
			case COS: defuzzyByCOS(fuzzyVariables[fuzzyVariables.length - 1], ruleAreas);
		}
	}
	
	//////////////////Defuzzyfication method/////////////////////
	
	private float sumOfArea;
	
	private void defuzzyByCOS (FuzzyVariable outputVariable, List<MFPoint[]>ruleAreas)
	{
		sumOfArea = 0;
		double fOutput = ruleAreas.stream()
				.mapToDouble(a -> 
				{
					double actualArea = MembershipFunction.countArea(a);
					sumOfArea += actualArea;
					return MembershipFunction.countCenterOfArea(a)*actualArea;
				})
				.sum();

		this.output = (T) new Double(outputVariable.membershipFunctions[0].scaleArgument((float)fOutput/sumOfArea));
		
	}
}
