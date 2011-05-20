package liviu.apps.clickcombat.managers;

public class ActivityIdProvider {

	// constants
	private final String TAG = "ActivityIDProvider";
	
	private static ActivityIdProvider instance;
	private static int				  lastId;
	
	private ActivityIdProvider(){
		lastId = 0;
	}
	
	public static ActivityIdProvider getInstance(){
		if(instance == null){
			instance = new ActivityIdProvider();
			return instance;
		}
		else
			return instance;
	}
	
	public int getNewId(){
		lastId++;
		return lastId;
	}
}
