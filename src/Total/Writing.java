package Total;

public class Writing {
	
	//used in SimulatorCells
	public static String List (){	
		String log = Total.Project + "Output/List/Output" + Total.MELAmodel + Total._SIMULATION_ID;
		return log;
	}

	//used in SimulatorCells
	public static String Meta (){	
		String meta = Total.Project + "Output/Meta/Output" + Total.MELAmodel +  Total._SIMULATION_ID;
		return meta;
	}
	
	//used in SimulatorCells
	public static String Data (){	
		String meta = Total.Project + "Output/Data/Output" + Total.MELAmodel +  Total._SIMULATION_ID;
		return meta;
	}

	//used in SimulatorCells
	public static String Cost (){	
		String meta = Total.Project + "Output/Cost/Output" + Total.MELAmodel +  Total._SIMULATION_ID;
		return meta;
	}
	
	//used in SimulatorCells
	public static String Agents (){	
		String meta = Total.Project + "Output/Agents/Output" + Total.MELAmodel +  Total._SIMULATION_ID;
		return meta;
	}

}
