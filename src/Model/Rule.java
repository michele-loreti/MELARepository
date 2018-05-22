package Model;

@FunctionalInterface
public interface Rule {
	
	public ActionStep apply(AgentManager am, LocationManager lm, int locationIndex, int agentIndex);
	

}



