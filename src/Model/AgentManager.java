package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgentManager {
	  
	public ArrayList<Agent> Agents = new ArrayList<>(); 
	public ArrayList<String> AgentNames = new ArrayList<>(); 
    public HashMap<String, Integer> MatrixAgent = new HashMap<>();	
   
    
    //name of the action -> index of passive agent
    public HashMap<String, Integer> PassiveAgentIndex = new HashMap<>();	
    
    //name of the action -> probability
    public HashMap<String, Double> Probability = new HashMap<>();	
    
    public int[][] GlobalMatrix;  
    public ArrayList<ArrayList<Integer>> Global = new ArrayList<ArrayList<Integer>>();
    
	public void GlobalMatrixCreation() {
	       GlobalMatrix = new int[AgentNames.size()][GlobalManager.getLocationManager().AllLoc.size()];
	}
       
//	public static int[][] GlobalMatrixCreationversion2() {
//	       int[][] newMatrix = new int[AgentNames.size()][LocationManager.AllLoc.size()];
//	       GlobalMatrix = newMatrix;
//	       return GlobalMatrix;
//	}
		
    public HashMap<String, Integer> MatrixAgentCreation() {
    	for(int i=0; i< AgentNames.size(); i++){
    		MatrixAgent.put(AgentNames.get(i),i);
    	}
    	return MatrixAgent;
    }
    
    public void addAgent(Agent a) {
    	Agents.add(a);
    }
    
	 public void addAgentName(String Name){
		AgentNames.add(Name);
	}
	 
	 public void applyUpdate( List<AgentVariation> update ) {
		for (AgentVariation agentOccupancy : update) {
			this.GlobalMatrix[agentOccupancy.getAgentIndex()][agentOccupancy.getLocationIndex()] += agentOccupancy.getVariation();
		}
	 }
        
}


