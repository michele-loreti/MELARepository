package Model;

import java.util.ArrayList;
import java.util.LinkedList;

import Actions.Action;
import Actions.EnvAction;
import Actions.InfAction;
import Actions.PassAction;

public class RuleEnv {
	
	public static PassAction chosenActionPass;
	public static int newPassLoc;
	public static int newPassLocIndex;
	public static String log;

	public static ActionStep BuildActionStepEnv(int agentIndex, EnvAction action, int passIndex, int passLoc) {		
		double propfunc = action.getRate() * GlobalManager.getAgentManager().Probability.get(action) * GlobalManager.getAgentManager().GlobalMatrix[agentIndex][0] * GlobalManager.getAgentManager().GlobalMatrix[passIndex][passLoc] ;
		LinkedList<AgentVariation> var = new LinkedList<>();	
		//state .
		var.add(new AgentVariation(agentIndex, 0, -1));
		String[] update = action.getUpdate();
		String getAgent = update[0];
		int newAgentIndex = GlobalManager.getAgentManager().AgentNames.indexOf(getAgent);		
		var.add(new AgentVariation(newAgentIndex, 0, +1));
		
        String nameToCheck = action.getName();
		
		for (int k = 0; k < GlobalManager.getAgentManager().AgentNames.size(); k++) {
			for (Action actionToCheckPass : GlobalManager.getAgentManager().Agents.get(k).getActionList()) {
				if (actionToCheckPass.getName().equals(nameToCheck) && actionToCheckPass.getType() == Action.ACTION_TYPE_Pass) {
					chosenActionPass = (PassAction) actionToCheckPass;										
				}}}
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
		String[] updatePass= action.getUpdate();
		
		// double check that (l) is removed	
		String getAgentPass = updatePass[0];
		
		int newAgentPassIndex = GlobalManager.getAgentManager().AgentNames.indexOf(getAgentPass);		
		var.add( new AgentVariation(newAgentPassIndex, passLoc, +1));
		}else if (action.getUpdate().length == 2){
		//state ||
		var.add( new AgentVariation(passIndex, passLoc, -1));		
		String[] updatePass = action.getUpdate();
		
		//double check that (l) is removed	
		String getAgent1 = updatePass[0];
		String getAgent2 = updatePass[1];
		
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
		
	
		if (passSymbol ==  "|>" ){
			//passive move
		    log = action.getName() + " " + action.getType() + "." + " " + passSymbol + " " + "-" + " " + " (" + GlobalManager.getLocationManager().getAllLoc().get(passLoc) + "," + GlobalManager.getLocationManager().getAllLoc().get(newPassLocIndex)  + ")";
			}else{
			//no movement actions
			log = action.getName() + " " + action.getType() + "." + " " + passSymbol + " " + "-" + " " + GlobalManager.getLocationManager().getAllLoc().get(passLoc);
			}
	
		return new ActionStep(propfunc, var, log);

	}
		
		
		public static ActionStep BuildActionStepNoEffectEnv(int agentIndex, EnvAction action, int passIndex, int passLoc) {		
			double noeffect = 1 - GlobalManager.getAgentManager().Probability.get(action);
			double propfunc = action.getRate() * noeffect * GlobalManager.getAgentManager().GlobalMatrix[agentIndex][0] * GlobalManager.getAgentManager().GlobalMatrix[passIndex][passLoc] ;
			LinkedList<AgentVariation> var = new LinkedList<>();
			//no variations
			String log = action.getName() + " " + action.getType() + "No change" +  " " + "No change" + " " + "-" + " " + GlobalManager.getLocationManager().getAllLoc().get(passLoc);
			return new ActionStep(propfunc, var, log);
		}
	
	public static void BuildActionStepTotalEnv(int agentIndex, EnvAction action, ArrayList<ActionStep> steps) {
		if (action.getInfSet() == GlobalManager.getLocationManager().AllLoc){
			for (ArrayList<Integer> s : GlobalManager.getLocationManager().AllLoc) {
				int passAgentIndex = GlobalManager.getAgentManager().PassiveAgentIndex.get(action.getName()); 
            	if (GlobalManager.getAgentManager().GlobalMatrix[passAgentIndex][GlobalManager.getLocationManager().MatrixLoc.get(s)] > 0) {
            		ActionStep act = BuildActionStepEnv(agentIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(s));
					ActionStep actNoEff = BuildActionStepNoEffectEnv(agentIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(s));						
					steps.add(act);
					steps.add(actNoEff);
	}}}else{
		for (ArrayList<Integer> s : action.getInfSet()) {
		int passAgentIndex = GlobalManager.getAgentManager().PassiveAgentIndex.get(action.getName()); 
    	if (GlobalManager.getAgentManager().GlobalMatrix[passAgentIndex][GlobalManager.getLocationManager().MatrixLoc.get(s)] > 0) {
    		ActionStep act = BuildActionStepEnv(agentIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(s));
			ActionStep actNoEff = BuildActionStepNoEffectEnv(agentIndex, action, passAgentIndex, GlobalManager.getLocationManager().MatrixLoc.get(s));						
			steps.add(act);
			steps.add(actNoEff);
    }}
		
}}}
