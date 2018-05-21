package Actions;

import java.util.ArrayList;
import java.util.List;

import Model.Location;

public class EnvAction implements Action  {
	private String name;
	private double rate;
	private ArrayList<ArrayList<Integer>> infset;
	private String[] update;
	
	public EnvAction(String name, double rate, ArrayList<ArrayList<Integer>> infset, String[] update){
		this.name = name;
		this.rate = rate;
		this.infset = infset;
		this.update = update;
	}

@Override
public int getType() {
	return Action.ACTION_TYPE_Env;
}

public String getName() {
	return name;
}

public Double getRate() {
	return rate;
}

public ArrayList<ArrayList<Integer>> getInfSet() {
	return infset;
}

@Override
public String[] getUpdate() {
	return update;
}

@Override
public String printStr() {
	String ret = "->{" + infset + "}(" + name + "," + rate + ")." + update;
	return ret;
}


}



