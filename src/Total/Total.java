package Total;

import java.util.ArrayList;
import java.util.Random;

import ParserMELA.MELAparser;
import ParserRules.MELArules;

public class Total {
	
	public static int numberOfRuns = 1;
	public static double simulationTime = 0.1;
	
	public static String Project = "/Users/ludovicaluisavissat/workspacejSSTL/NEW_MELA/src/";
	public static String MELAmodel = "SIR1.mela";
	
	public static int _SIMULATION_ID;
	
	//cost analysis - TO IMPLEMENT!!!
	public static String[] ListAction = {};
	public static double[] ListCost = {};
	
	//agents to count - TO IMPLEMENT!!!
	public static String[] AgentName = {"S", "X", "I", "R"};
	
	public static void main(String[] args) throws Exception {
     	_SIMULATION_ID = 1;
		for(int i = 1; i <= numberOfRuns; i++)
		{   MELAparser Parser= new MELAparser();
		    Parser.parseFromFile(Project + MELAmodel);
 		    System.out.println("Model parsed correctly."); 
	        System.out.println("Simulation -> " + _SIMULATION_ID ); 
			MELArules Parser2= new MELArules();
			Parser2.parseFromFile(Project + "Rules.txt");
			_SIMULATION_ID++;
		}	
	}


}

