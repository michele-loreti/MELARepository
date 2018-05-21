package Model;

import java.util.List;

public class ActionStep {

	private final double propfunc;
	private final List<AgentVariation> v;
	private final String log;
	
	public ActionStep(double propfunc, List<AgentVariation> v, String log) {
		super();
		this.propfunc = propfunc;
		this.v = v;
		this.log = log;
	}

	public double getPropfunc() {
		return propfunc;
	}

	public List<AgentVariation> getVariation() {
		return v;
	}

	public String getLog() {
		return log;
	}
	
	
}
