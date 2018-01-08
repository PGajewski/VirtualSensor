package pl.gajos.VirtualSensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class FuzzyFlowmeter extends WangMenderVirtualSensor<Float> {
	
	//Statistic variables.
	protected float pDiffMin;
	protected float pDiffMax;
	protected float oValveMin;
	protected float oValveMax;
	protected float flowMin;
	protected float flowMax;
	
	//Lists with data from Excel sheet.
	protected List<FlowmeterDataRow> dataList = new ArrayList<FlowmeterDataRow>();

	//Fuzy variables.
	/**
	 * Fuzzy pressure difference.
	 */
	protected FuzzyPressure pressureDiff;
	
	/**
	 * Fuzzy valve opening level.
	 */
	protected FuzzyOpeningLevel valveOpen;
	
	/**
	 * Fuzzy flow by flowmeter.
	 */
	protected FuzzyFlow flow;
	
	@Override
	public void loadLogic() {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over load values.

            //Load time. First line are headers.
            for (int i = 1; i < sheet.getRows(); i++) {
            	dataList.add(new FlowmeterDataRow(
            			Integer.parseInt(sheet.getCell(0, i).getContents()),
            			Float.parseFloat(sheet.getCell(1, i).getContents().replace(",", ".")),
            			Float.parseFloat(sheet.getCell(2, i).getContents().replace(",", ".")),
            			Float.parseFloat(sheet.getCell(3, i).getContents().replace(",", ".")),
            			Float.parseFloat(sheet.getCell(4, i).getContents().replace(",", ".")),
            			Float.parseFloat(sheet.getCell(5, i).getContents().replace(",", ".")),
            			Float.parseFloat(sheet.getCell(6, i).getContents().replace(",", ".")),
            			Float.parseFloat(sheet.getCell(7, i).getContents().replace(",", "."))
            			));
           }
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
	}

	protected void initFuzzySets()
	{
		// Gather info about data to create membership function.
				pDiffMin = dataList.stream()
						.map(d -> d.getPressureOne() - d.getPressureTwo())
						.min(Float::compare).get();
			
				pDiffMax = dataList.stream()
						.map(d -> d.getPressureOne() - d.getPressureTwo())
						.max(Float::compare).get();
				
				oValveMin = dataList.stream()
						.map(d -> d.getOpenValveValue())
						.min(Float::compare).get();
				
				oValveMax = dataList.stream()
						.map(d -> d.getOpenValveValue())
						.max(Float::compare).get();
				
				flowMin = dataList.stream()
						.map(d -> d.getFlow())
						.min(Float::compare).get();
				
				flowMax = dataList.stream()
						.map(d -> d.getFlow())
						.max(Float::compare).get();
				
				//Init fuzzy sets.
				pressureDiff = new FuzzyPressure(pDiffMin, pDiffMax);
				
				valveOpen = new FuzzyOpeningLevel(oValveMin, oValveMax);
				
				flow = new FuzzyFlow(flowMin, flowMax);
	}
	
	@Override
	public void learn() {
		
		initFuzzySets();
		
		//Create rules.
		
		//Create rule array.
		
		FlowmeterRule[][] ruleArray = new FlowmeterRule[FuzzyPressureValue.values().length][FuzzyOpeningLevelValue.values().length];
		
		//Fill rules and fill rule array for filter content.
		dataList.stream()
				.map( d -> {
					pressureDiff.fuzzyInput(d.getPressureOne() - d.getPressureTwo());
					pressureDiff.chooseDomineVariable();
					valveOpen.fuzzyInput(d.getOpenValveValue());
					valveOpen.chooseDomineVariable();
					flow.fuzzyInput(d.getFlow());
					flow.chooseDomineVariable();
					return new FlowmeterRule(pressureDiff.getValue(),
											valveOpen.getValue(),
											flow.getValue(),
											pressureDiff.getForce()*valveOpen.getForce()*flow.getForce());
					})
				.forEach( r -> { FlowmeterRule temp = ruleArray[r.getPraemissaOne().ordinal()][r.getPraemissaTwo().ordinal()];
								if(temp == null || temp.getForce() < r.getForce())
									ruleArray[r.getPraemissaOne().ordinal()][r.getPraemissaTwo().ordinal()] = r;
				});
		
		//Change array to list.
		rules = Arrays.stream(ruleArray)
				.flatMap( a -> Arrays.stream(a))
				.filter( r -> r != null)
				.collect(Collectors.toList());
	}

	@Override
	public void run()
	{
		defuzzy(DefuzzyficationType.COS, pressureDiff, valveOpen, flow);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FuzzyFlowmeter test = new FuzzyFlowmeter();
        test.setInputFile("nauka.xls");
        test.loadLogic();
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

    private String inputFile;

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }
}
