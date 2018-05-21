package Actions;

import Model.Agent;
import Model.GlobalManager;
import Model.Location;

public class PropensityFunction {
	private String name;
	private Action action;
	private double rate;
	
	public PropensityFunction(String name, Action action, double rate) {
		this.name = name;
		this.action = action;
		this.rate = rate;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Action getAction() {
		return action;
	}
	public void setActionIndex(Action action) {
		this.action = action;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}

}
