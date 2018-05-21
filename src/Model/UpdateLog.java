package Model;

public class UpdateLog {

	public static String UpdateLog (String symbol){
		if (symbol == ">>"){
			return "Demo+";
		}else if(symbol == "<<"){
			return "Demo-";
		}else if (symbol == "."){
			return "State";
		}else if (symbol == "|>"){
			return "Spatial";
		}else{ 
			return "Unknown update";}
		}

}
