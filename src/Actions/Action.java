package Actions;


public interface Action {
	public final static int ACTION_TYPE_NoInf = 1;
	public final static int ACTION_TYPE_Inf = 2;
	public final static int ACTION_TYPE_Pass = 3;
	public final static int ACTION_TYPE_Env = 4;
	
	public int getType();
	
	public String getName();
	
	public String[] getUpdate();
	
	public String printStr(); 

	
}
