package pl.gajos.VirtualSensor;

public final class FlowmeterRule extends Rule<FuzzyPressureValue, FuzzyOpeningLevelValue, FuzzyFlowValue> {

	FlowmeterRule(FuzzyPressureValue pressure, FuzzyOpeningLevelValue openingLevel, FuzzyFlowValue flow, float ruleForce)
	{
		initRule(pressure, openingLevel, flow, ruleForce);
	}
}
