package Model;

@FunctionalInterface
public interface Rule {
	
	public ActionStep apply( int locationIndex, int agentIndex);
	

}



