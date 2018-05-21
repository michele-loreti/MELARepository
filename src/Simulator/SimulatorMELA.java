package Simulator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import Actions.Action;
import Actions.EnvAction;
import Actions.InfAction;
import Actions.NoInfAction;
import Actions.PassAction;
import Model.ActionStep;
import Model.GlobalManager;
import Model.RuleEnv;
import Model.RuleInf;
import Model.RuleNoInf;
import Model.Samples;
import Total.Total;
import Total.Writing;

public class SimulatorMELA {
	
	static double time;
	static String outputFileList;
	static String outputFileData;
	static String outputFileMeta;
	static String outputFileCost;
	public static double[][][][] dataFinal;
	public static double[][][] data;
	public static double[] timeArray;
	public static double cost;
	
	//now here instead of AgentManager
    //name of the action -> index of passive agent
    public static HashMap<String, Integer> PassiveAgentIndex = new HashMap<>();	   
    //name of the action -> probability
    public static HashMap<String, Double> Probability = new HashMap<>();
	
	public static void main(String[] args) {	
		
		double totalTime = Total.simulationTime;	
		outputFileList = Writing.List();
		outputFileData = Writing.Data();
		outputFileMeta = Writing.Meta();
		outputFileCost = Writing.Cost();
		PrintWriter writer_list = null, writer_data = null, writer_meta = null, writer_cost = null;
    	try {
			writer_list = new PrintWriter(outputFileList+"_list.txt", "UTF-8");
			writer_data = new PrintWriter(outputFileData+"_data.txt", "UTF-8");
			writer_meta = new PrintWriter(outputFileMeta+"_meta.txt", "UTF-8");	
			writer_cost = new PrintWriter(outputFileCost+"_cost.txt", "UTF-8");	
	    }catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//list of agents+locations (as indicator for data file)
		for (int i = 0; i < GlobalManager.getAgentManager().AgentNames.size(); i++) {
			for (int j = 0; j < GlobalManager.getLocationManager().AllLoc.size(); j++) {
				String formatedString = GlobalManager.getLocationManager().AllLoc.get(j).toString().replace("[","").replace("]","").trim();
				String formatedString2 = formatedString.replaceAll("\\s","");
				writer_list.print(GlobalManager.getAgentManager().AgentNames.get(i) + "(" + formatedString2 + ") ");			
			}
		}
		
		//passive action hashmaps
		for (Model.Agent a: GlobalManager.getAgentManager().Agents) {
			for (Action ac : a.getActionList()){
		        if (ac.getType() == 3){	
					PassAction passac = (PassAction) ac;
					PassiveAgentIndex.put(ac.getName(), GlobalManager.getAgentManager().AgentNames.indexOf(a.getName()));
		        	Probability.put(ac.getName(), passac.getInfProb());
		}}}
		
		
		//initial conditions - data and meta
		writer_data.print(time + " ");
		for (int i = 0; i < GlobalManager.getAgentManager().AgentNames.size(); i++) {
			for (int j = 0; j < GlobalManager.getLocationManager().AllLoc.size(); j++) {
				writer_data.print(GlobalManager.getAgentManager().GlobalMatrix[i][j] + " ");
			}
		}
		writer_data.println(" ");
		writer_meta.print("Initial condition - time 0.0 - no actions ");
		writer_meta.println(" ");
	
		//for final matrix
		ArrayList<Double> TimeArrayList = new ArrayList<>();
		TimeArrayList.add(time);
		
		while (time < totalTime) {
			
			System.out.println("Simulation step " + time);
			ArrayList<ActionStep> steps = new ArrayList<>();
			
			for (int agentIndex = 0; agentIndex < GlobalManager.getAgentManager().Agents.size(); agentIndex++) {
				for (int locationIndex = 0; locationIndex < GlobalManager.getLocationManager().AllLoc.size(); locationIndex++) {
					if (GlobalManager.getAgentManager().GlobalMatrix[agentIndex][locationIndex] != 0){
					for (Action act : GlobalManager.getAgentManager().Agents.get(agentIndex).getActionList()) {
						if (act.getType()==1){
							NoInfAction actToCalculate = (NoInfAction) act;
							ActionStep step = RuleNoInf.BuildActionStep(agentIndex, locationIndex, actToCalculate);
							steps.add(step);
						}
						if (act.getType()==2){
							InfAction actToCalculate = (InfAction) act;
							RuleInf.BuildActionStepTotal(agentIndex, locationIndex, actToCalculate, steps);						
						}
						if (act.getType()==3){
							//nothing
						}
						if (act.getType()==4){
							EnvAction actToCalculate = (EnvAction) act;
							RuleEnv.BuildActionStepTotalEnv(agentIndex, actToCalculate, steps);						
							}
						}}}}
			
			double sumPropFunc = sum(steps);
			
			if (sumPropFunc == 0) {
				System.out.println("End - null population");
				break;
			} else {
				double time_passed = Samples.getExp(sumPropFunc);				
				time = time + time_passed;
				ActionStep step = Samples.select(sumPropFunc, steps);
				GlobalManager.getAgentManager().applyUpdate(step.getVariation());
						
				
		    //from GlobalMatrix to data file
			writer_data.print(time + " ");
		    for (int i = 0; i < GlobalManager.getAgentManager().AgentNames.size(); i++) {
				for (int j = 0; j < GlobalManager.getLocationManager().AllLoc.size(); j++) {
					writer_data.print(GlobalManager.getAgentManager().GlobalMatrix[i][j] + " ");
				}
			}
			writer_data.println(" ");
				
		    //for SSTL analysis
            TimeArrayList.add(time);
				
            //meta data file
			String log = step.getLog();
			System.out.println("CHECK " + log);
			writer_meta.print(log);
			writer_meta.println(" ");

	    }
		}
		
		writer_list.close();
		writer_data.close();
		writer_meta.close();

	    //data for SSTL analysis
//		int intLoc = GlobalManager.getLocationManager().AllLoc.size();
//		int intTime = TimeArrayList.size();
//		int intAgents = GlobalManager.getAgentManager().AgentNames.size();
//		data = new double [intLoc][intTime][intAgents];		
//		
//		for (int i=0; i< intTime; i++){
//			for (int j = 0; j < intAgents; j++){
//				for (int k=0; k< intLoc; k++){
//					ArrayList<Integer> Key = new ArrayList<>();
//					Key.add(i);
//					Key.add(j);
//					Key.add(k);
//					data[k][i][j]= SaveAll.get(Key);
//				}
//			  }
//			}
//
//		
//		timeArray = new double[TimeArrayList.size()];
//		
//		for(int j=0; j < TimeArrayList.size(); j++){
//			timeArray[j] = TimeArrayList.get(j);
//		}
	}

	// methods

	public static double sum(ArrayList<ActionStep> steps) {
		double sum = 0;
		for (int i = 0; i < steps.size(); i++) {
			sum = sum + steps.get(i).getPropfunc();
		}
		return sum;
	}

	public static void addAction(Action a, ArrayList<Action> list) {
		list.add(a);
	}

	public static void addName(String name, ArrayList<String> list) {
		list.add(name);
	}

	public static void addLoc(Integer loc, ArrayList<Integer> list) {
		list.add(loc);
	}

	public static double getTime() {
		return time;
	}

}
