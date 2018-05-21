package ParserMELA;

import java.util.ArrayList;
import java.util.HashMap;


public class ModelParser {

	
	public static int _SIMULATION_ID;
	public static int numberOfRuns = 1;

	public static void main(String[] args) throws Exception {
			
     	_SIMULATION_ID = 1;
		for(int i = 1; i <= numberOfRuns; i++)
		{   MELAparser Parser= new MELAparser();
		    Parser.parseFromFile("/Users/ludovicaluisavissat/workspacejSSTL/NEW_MELA/src/SIR1.mela");
 		    System.out.println("Model parsed correctly."); 
//	        System.out.println("Simulation -> " + _SIMULATION_ID ); 
//			MELArules Parser2= new MELArules();
//			Parser2.parseFromFile("/Users/ludovicaluisavissat/GitHub/MELATotalDistance/Rules.txt");
//			_SIMULATION_ID++;
		}	
	}
}



