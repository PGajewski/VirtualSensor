package pl.gajos.VirtualSensor;

public class FlowmeterDataRow extends DataRow {
	
	private float pressureOne;
	
	private float pressureTwo;
	
	private float temperature;
	
	private float flow;
	
	private float controlValue;
	
	private float openValveValue;
	
	private float physicValue;
	
	//Getters & setters.
	
	public float getPressureOne()
	{
		return pressureOne;
	}
	
	public float getPressureTwo()
	{
		return pressureTwo;
	}
	
	public float getTemperature()
	{
		return temperature;
	}
	
	public float getFlow()
	{
		return flow;
	}
	
	public float getControlValue()
	{
		return controlValue;
	}
	
	public float getOpenValveValue()
	{
		return openValveValue;
	}
	
	FlowmeterDataRow(int time, float pressureOne, float pressureTwo, float temperature, float flow, float controlValue, float openValveValue, float physicValue)
	{
		super(time);
		this.pressureOne = pressureOne;
		this.pressureTwo = pressureTwo;
		this.temperature = temperature;
		this.flow = flow = flow;
		this.controlValue = controlValue;
		this.openValveValue = openValveValue;
		this.physicValue = physicValue;
	}
}
