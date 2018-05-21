package Model;

import java.util.ArrayList;
import java.util.LinkedList;

import Actions.Action;
import Actions.InfAction;
import Actions.PassAction;

public class RuleInf {
	
	public static int newLoc;
	public static int newLocIndex;
	public static int newPassLoc;
	public static int newPassLocIndex;
	public static PassAction chosenActionPass;
	
	public static ActionStep BuildActionStep(int agentIndex, int locationIndex, InfAction action, int passIndex, int passLoc) {				
		double propfunc = action.getRate() * Simulator.SimulatorMELA.Probability.get(action.getName()) * GlobalManager.getAgentManager().GlobalMatrix[agentIndex][locationIndex] * GlobalManager.getAgentManager().GlobalMatrix[passIndex][passLoc] ;
		LinkedList<AgentVariation> var = new LinkedList<>();
		//Add to var the appropriate variations - INF AGENT
		String symbol = action.getSymbol();
		//demo+	
		if (symbol == ">>"){
		var.add( new AgentVariation(agentIndex, locationIndex, +1));		
		//demo-		
		}else if(symbol == "<<"){
		var.add( new AgentVariation(agentIndex, locationIndex, -1));		
		//state 
		}else if (symbol == "."){
		//state .
		if (action.getUpdate().length == 1){	
		var.add( new AgentVariation(agentIndex, locationIndex, -1));
		String[] update = action.getUpdate();
		
		// double check that (l) is removed		
		String getAgent = update[0];
		
		int newAgentIndex = GlobalManager.getAgentManager().AgentNames.indexOf(getAgent);		
		var.add( new AgentVariation(newAgentIndex, locationIndex, +1));
		}else if (action.getUpdate().length == 2){
		//state ||
		var.add( new AgentVariation(agentIndex, locationIndex, -1));		
		String[] update = action.getUpdate();
		
		// double check that (l) is removed		
		String getAgent1 = update[0];
		String getAgent2 = update[1];
		
		int newAgentIndex1 = GlobalManager.getAgentManager().AgentNames.indexOf(getAgent1);
		int newAgentIndex2 = GlobalManager.getAgentManager().AgentNames.indexOf(getAgent2);		
		var.add( new AgentVariation(newAgentIndex1, locationIndex, +1));
		var.add( new AgentVariation(newAgentIndex2, locationIndex, +1));}}
		else if (symbol == "|>"){
		//spatial
		if(GlobalManager.getLocationManager().boundary.equals("Periodic")){
			ArrayList<ArrayList<Integer>> neighbourhood = GlobalManager.getLocationManager().getNeigh(GlobalManager.getLocationManager().AllLoc.get(locationIndex), 1);
			double[] ProbLoc = new double[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				ProbLoc[j] = (1.0 / neighbourhood.size());
			}
			int[] neighbourhoodEntries = new int[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				neighbourhoodEntries[j] = j;
			}
			newLoc = Samples.getDiscrete(neighbourhoodEntries, ProbLoc);
			newLocIndex = GlobalManager.getLocationManager().AllLoc.indexOf(neighbourhood.get(newLoc));}
		else if (GlobalManager.getLocationManager().boundary.equals("Bouncing")){	
			ArrayList<ArrayList<Integer>> neighbourhood = GlobalManager.getLocationManager().getNeighBouncing(GlobalManager.getLocationManager().AllLoc.get(locationIndex), 1);
			double[] ProbLoc = new double[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				ProbLoc[j] = (1.0 / neighbourhood.size());
			}
			int[] neighbourhoodEntries = new int[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				neighbourhoodEntries[j] = j;
			}
			newLoc = Samples.getDiscrete(neighbourhoodEntries, ProbLoc);
			newLocIndex = GlobalManager.getLocationManager().AllLoc.indexOf(neighbourhood.get(newLoc));}		
		var.add( new AgentVariation(agentIndex, locationIndex, -1));
		var.add( new AgentVariation(agentIndex, newLocIndex, +1));}	
		
		
		//look for the passive action with the same name
		String nameToCheck = action.getName();
		
		for (int k = 0; k < GlobalManager.getAgentManager().AgentNames.size(); k++) {
			for (Action actionToCheckPass : GlobalManager.getAgentManager().Agents.get(k).getActionList()) {
				if (actionToCheckPass.getName().equals(nameToCheck) && actionToCheckPass.getType() == Action.ACTION_TYPE_Pass) {
					chosenActionPass = (PassAction) actionToCheckPass;
				}
				}}
		
		String passSymbol = chosenActionPass.getSymbol();
		//demo+	
		if (passSymbol == ">>"){
		var.add( new AgentVariation(passIndex, passLoc, +1));
		
		//demo-		
		}else if(passSymbol == "<<"){
		var.add( new AgentVariation(passIndex, passLoc, -1));
		
		//state 
		}else if (passSymbol == "."){
		//state .
		if (action.getUpdate().length == 1){	
		var.add( new AgentVariation(passIndex, passLoc, -1));
		String[] update = action.getUpdate();
		
		//  double check that (l) is removed		
		String getAgent = update[0];
		
		int newAgentIndex = GlobalManager.getAgentManager().AgentNames.indexOf(getAgent);		
		var.add( new AgentVariation(newAgentIndex, passLoc, +1));
		}else if (action.getUpdate().length == 2){
		//state ||
		var.add( new AgentVariation(passIndex, passLoc, -1));		
		String[] update = action.getUpdate();
		
		//  double check that (l) is removed		
		String getAgent1 = update[0];
		String getAgent2 = update[1];
		
		int newAgentIndex1 = GlobalManager.getAgentManager().AgentNames.indexOf(getAgent1);
		int newAgentIndex2 = GlobalManager.getAgentManager().AgentNames.indexOf(getAgent2);		
		var.add( new AgentVariation(newAgentIndex1, passLoc, +1));
		var.add( new AgentVariation(newAgentIndex2, passLoc, +1));	}}	
		
		//spatial
		else if (passSymbol == "|>"){
		if(GlobalManager.getLocationManager().boundary.equals("Periodic")){
			ArrayList<ArrayList<Integer>> neighbourhood = GlobalManager.getLocationManager().getNeigh(GlobalManager.getLocationManager().AllLoc.get(passLoc), 1);
			double[] ProbLoc = new double[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				ProbLoc[j] = (1.0 / neighbourhood.size());
			}
			int[] neighbourhoodEntries = new int[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				neighbourhoodEntries[j] = j;
			}
			newPassLoc = Samples.getDiscrete(neighbourhoodEntries, ProbLoc);
			newPassLocIndex = GlobalManager.getLocationManager().AllLoc.indexOf(neighbourhood.get(newPassLoc));}		
		else if (GlobalManager.getLocationManager().boundary.equals("Bouncing")){	
			ArrayList<ArrayList<Integer>> neighbourhood = GlobalManager.getLocationManager().getNeighBouncing(GlobalManager.getLocationManager().AllLoc.get(passLoc), 1);
			double[] ProbLoc = new double[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				ProbLoc[j] = (1.0 / neighbourhood.size());
			}
			int[] neighbourhoodEntries = new int[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				neighbourhoodEntries[j] = j;
			}
			newPassLoc = Samples.getDiscrete(neighbourhoodEntries, ProbLoc);
			newPassLocIndex = GlobalManager.getLocationManager().AllLoc.indexOf(neighbourhood.get(newPassLoc));}		
		var.add( new AgentVariation(passIndex, passLoc, -1));
		var.add( new AgentVariation(passIndex, newPassLoc, +1));}		
		
		//additional information (metadata) 
		String log = new String();
		if (action.getSymbol() == "|>"){
			if (passSymbol ==  "|>" ){
			//both move
		    log = action.getName() + " " + action.getType() + " " + UpdateLog.UpdateLog(action.getSymbol()) + " " + UpdateLog.UpdateLog(passSymbol) + " " + "(" + GlobalManager.getLocationManager().getAllLoc().get(locationIndex) + "," + GlobalManager.getLocationManager().getAllLoc().get(newLocIndex)  + ")" + " (" + GlobalManager.getLocationManager().getAllLoc().get(passLoc) + "," + GlobalManager.getLocationManager().getAllLoc().get(newPassLocIndex)  + ")";
		    }else{
		    	 //just active move
		    	 log = action.getName() + " " + action.getType() + " " + UpdateLog.UpdateLog(action.getSymbol()) + " " + UpdateLog.UpdateLog(passSymbol) + " " + "(" + GlobalManager.getLocationManager().getAllLoc().get(locationIndex) + "," + GlobalManager.getLocationManager().getAllLoc().get(newLocIndex)  + ")" + " " + GlobalManager.getLocationManager().getAllLoc().get(passLoc);
			}}else{
			if (passSymbol ==  "|>" ){
				//just passive move
			    log = action.getName() + " " + action.getType() + " " + UpdateLog.UpdateLog(action.getSymbol()) + " " + UpdateLog.UpdateLog(passSymbol) + " " + GlobalManager.getLocationManager().getAllLoc().get(locationIndex) + " " + " (" + GlobalManager.getLocationManager().getAllLoc().get(passLoc) + "," + GlobalManager.getLocationManager().getAllLoc().get(newPassLocIndex)  + ")";
				}else{
				//no movement actions
				log = action.getName() + " " + action.getType() + " " + UpdateLog.UpdateLog(action.getSymbol()) + " " + UpdateLog.UpdateLog(passSymbol) + " " + GlobalManager.getLocationManager().getAllLoc().get(locationIndex) + " " + GlobalManager.getLocationManager().getAllLoc().get(passLoc);
				}}
		return new ActionStep(propfunc, var, log);

	}
	
	public static ActionStep BuildActionStepNoEffect(int agentIndex, int locationIndex, InfAction action, int passIndex, int passLoc) {		
		double noeffect = 1 - Simulator.SimulatorMELA.Probability.get(action.getName());
		double propfunc = action.getRate() * noeffect * GlobalManager.getAgentManager().GlobalMatrix[agentIndex][locationIndex] * GlobalManager.getAgentManager().GlobalMatrix[passIndex][passLoc] ;
		LinkedList<AgentVariation> var = new LinkedList<>();
		//no variations
		String log = action.getName() + " " + action.getType() + "No change" +  " " + "No change" + " " + GlobalManager.getLocationManager().getAllLoc().get(locationIndex) + " " + GlobalManager.getLocationManager().getAllLoc().get(passLoc);
		return new ActionStep(propfunc, var, log);
	}
		
	
	//this method builds a step for each combination, given the influence action
	public static void BuildActionStepTotal(int agentIndex, int locationIndex, InfAction action, ArrayList<ActionStep> steps) {
		if (action.getInfSet() == "l") {
			int passIndex = Simulator.SimulatorMELA.PassiveAgentIndex.get(action.getName());
			int passLocation = locationIndex;
			ActionStep act = BuildActionStep(agentIndex, locationIndex, action, passIndex, passLocation);
			ActionStep actNoEff = BuildActionStepNoEffect(agentIndex, locationIndex, action, passIndex, passLocation);
			steps.add(act);
			steps.add(actNoEff);
		}		
		if (action.getInfSet().contains("N")) {			
			if (GlobalManager.getLocationManager().boundary.equals("Periodic")){
				for (ArrayList<Integer> t : GlobalManager.getLocationManager().getNeigh(GlobalManager.getLocationManager().AllLoc.get(locationIndex), action.getRangeNeigh())) {								
					int passAgentIndex = Simulator.SimulatorMELA.PassiveAgentIndex.get(action.getName()); 
					if (GlobalManager.getAgentManager().GlobalMatrix[passAgentIndex][GlobalManager.getLocationManager().MatrixLoc.get(t)] > 0) {
						ActionStep act = BuildActionStep(agentIndex, locationIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(t));
						ActionStep actNoEff = BuildActionStepNoEffect(agentIndex, locationIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(t));						
						steps.add(act);
						steps.add(actNoEff);
					}}}else if (GlobalManager.getLocationManager().boundary.equals("Bouncing")){
						for (ArrayList<Integer> t : GlobalManager.getLocationManager().getNeighBouncing(GlobalManager.getLocationManager().AllLoc.get(locationIndex), action.getRangeNeigh())) {								
							int passAgentIndex = Simulator.SimulatorMELA.PassiveAgentIndex.get(action.getName()); 
							if (GlobalManager.getAgentManager().GlobalMatrix[passAgentIndex][GlobalManager.getLocationManager().MatrixLoc.get(t)] > 0) {
								ActionStep act = BuildActionStep(agentIndex, locationIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(t));
								ActionStep actNoEff = BuildActionStepNoEffect(agentIndex, locationIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(t));						
								steps.add(act);
								steps.add(actNoEff);
					}}}}
			if (action.getInfSet() == "all"){
				for (ArrayList<Integer> s : GlobalManager.getLocationManager().AllLoc) {
					int passAgentIndex = Simulator.SimulatorMELA.PassiveAgentIndex.get(action.getName()); 
                	if (GlobalManager.getAgentManager().GlobalMatrix[passAgentIndex][GlobalManager.getLocationManager().MatrixLoc.get(s)] > 0) {
                		ActionStep act = BuildActionStep(agentIndex, locationIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(s));
						ActionStep actNoEff = BuildActionStepNoEffect(agentIndex, locationIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(s));						
						steps.add(act);
						steps.add(actNoEff);
		}}}
		
	}
}
