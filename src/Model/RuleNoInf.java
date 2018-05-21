package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import Actions.NoInfAction;

public class RuleNoInf {
	
	public static int newLoc;
	public static int newLocIndex;
	
	public static ActionStep BuildActionStep(int agentIndex, int locationIndex, NoInfAction action) {
		//calculate propensity function (mass action)
		double propfunc = action.getRate() * GlobalManager.getAgentManager().GlobalMatrix[agentIndex][locationIndex];
		
		//set the list of variation
		LinkedList<AgentVariation> var = new LinkedList<>();
		//add to var the appropriate variations
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
		var.add( new AgentVariation(newAgentIndex2, locationIndex, +1));	}}	
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
			ArrayList<ArrayList<Integer>> neighbourhood = new ArrayList<ArrayList<Integer>>();
		    neighbourhood = GlobalManager.getLocationManager().getNeighBouncing(GlobalManager.getLocationManager().AllLoc.get(locationIndex), 1);
			double[] ProbLoc = new double[neighbourhood.size()];
			for (int j = 0; j < neighbourhood.size(); j++) {
				System.out.println(GlobalManager.getLocationManager().AllLoc.get(locationIndex) + " " + neighbourhood.get(j));
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
		
		//additional information (metadata)
		String log = new String();
		if (action.getSymbol() == "|>"){
		log = action.getName() + " " + action.getType() + " " + UpdateLog.UpdateLog(action.getSymbol()) + " x " + "(" + GlobalManager.getLocationManager().getAllLoc().get(locationIndex) + "," + GlobalManager.getLocationManager().getAllLoc().get(newLocIndex)  + ")" + " x";
		}else{
		log = action.getName() + " " + action.getType() + " " + UpdateLog.UpdateLog(action.getSymbol()) + " x " + GlobalManager.getLocationManager().getAllLoc().get(locationIndex) + " x";
		}		
			
		return new ActionStep(propfunc, var, log);
	}
}
