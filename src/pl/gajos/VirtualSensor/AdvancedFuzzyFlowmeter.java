package pl.gajos.VirtualSensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class AdvancedFuzzyFlowmeter extends FuzzyFlowmeter {
	
	
	private final static int NUMBERS_OF_INTERVALS = 100;
	
	private final static int LEVEL_OF_TRUST = 1;
	
	
	/**
	 * Method to filter data
	 */
	private void filterData()
	{
		initFuzzySets();
		
		//Create lists for all groups.
		List<List<FlowmeterDataRow>> groups = new ArrayList<>(NUMBERS_OF_INTERVALS);
		for (int i = 0; i < NUMBERS_OF_INTERVALS; ++i)
		{
			groups.add(new ArrayList<FlowmeterDataRow>());
		}
		
		List<List<List<FlowmeterDataRow>>> innerGroups = new ArrayList<>(NUMBERS_OF_INTERVALS);
		for (int i = 0; i < NUMBERS_OF_INTERVALS; ++i)
		{
			List<List<FlowmeterDataRow>> innerGroup = new ArrayList<>(NUMBERS_OF_INTERVALS);
			for (int j = 0; j < NUMBERS_OF_INTERVALS; ++j)
			{
				innerGroup.add(new ArrayList<FlowmeterDataRow>());
			}
			innerGroups.add(innerGroup);
		}
		
		//Create inner groups.
		
		//Set size of pressure interval.
		final float pDiffIntervalLength = (pDiffMax - pDiffMin)/NUMBERS_OF_INTERVALS;
		
		//Set size of opening level interval.
		final float oValveIntervalLength = (oValveMax - oValveMin)/NUMBERS_OF_INTERVALS;

		
		//Part data from intervals.
		dataList.parallelStream()
				.forEach( d -> {
					try
					{
						groups.get((int)Math.floor((d.getPressureOne() - d.getPressureTwo()-pDiffMin)/pDiffIntervalLength)).add(d);

					}catch(IndexOutOfBoundsException e)
					{
						//Ignore IndexOutOfBoundsException - try add maximum value.
					}
						});
		
		//Part each group for inner group by opening level.
		for(int i = 0; i < NUMBERS_OF_INTERVALS; ++i)
		{
			final int index = i;
			groups.get(index).parallelStream()
			.forEach( d -> {
				try
				{
					innerGroups.get(index).get((int)Math.floor((d.getOpenValveValue()-this.oValveMin)/oValveIntervalLength)).add(d);
				}
				catch(IndexOutOfBoundsException | NullPointerException e)
				{
					//Ignore IndexOutOfBoundsException - try add maximum value.
				}
			});
		}
		
		//Filter values from main datastore.
		innerGroups.stream()
			.forEach(g -> g.stream()
						.forEach(ig -> {
							Float[] flows = ig.stream()
									.filter(f -> f != null)
								.map(d -> d.getFlow())
								.toArray(Float[]::new);
							final Statistics stat = new Statistics(flows);
							double mean = stat.getMean();
							double stdDev = stat.getStdDev();
							final double compareValueMin = stat.getMean() - LEVEL_OF_TRUST * stdDev;
							final double compareValueMax = stat.getMean() + LEVEL_OF_TRUST * stdDev;
							
							//Delete data rows if this isn't in range.
							ig.stream()
								.filter(f -> f != null)
								.filter(d -> (d.getFlow() >= compareValueMax) ||  (d.getFlow() <= compareValueMin))
								.forEach(d -> this.dataList.remove(d));
						}));
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AdvancedFuzzyFlowmeter test = new AdvancedFuzzyFlowmeter();
        test.setInputFile("nauka.xls");
        test.loadLogic();
        test.filterData();
        test.learn();
        
        File testWorkbook = new File("pomiary.xls");
        Workbook w;
        try {
            w = Workbook.getWorkbook(testWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over load values.

            //Load time. First line are headers.
            for (int i = 1; i < sheet.getRows(); i++) {

            	test.setInputs(new Number[]{
            			Float.parseFloat(sheet.getCell(1, i).getContents().replace(",", ".")) - Float.parseFloat(sheet.getCell(2, i).getContents().replace(",", ".")),
            			Float.parseFloat(sheet.getCell(6, i).getContents().replace(",", "."))});
            	test.run();
            	System.out.println(test.getOutput());
           }
            
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
  	}

	
	private class Statistics 
	{
	    Float[] data;
	    int size;   

	    public Statistics(Float[] data) 
	    {
	        this.data = data;
	        size = data.length;
	    }   

	    double getMean()
	    {
	        double sum = 0.0;
	        for(double a : data)
	            sum += a;
	        return sum/size;
	    }

	    double getVariance()
	    {
	        double mean = getMean();
	        double temp = 0;
	        for(double a :data)
	            temp += (a-mean)*(a-mean);
	        return temp/(size-1);
	    }

	    double getStdDev()
	    {
	        return Math.sqrt(getVariance());
	    }

	    public double median() 
	    {
	       Arrays.sort(data);

	       if (data.length % 2 == 0) 
	       {
	          return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
	       } 
	       return data[data.length / 2];
	    }
	}
	
}
