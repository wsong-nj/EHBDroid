package ehb.global;

/**
 * Options for ehb
 * */
public class EHBOptions {

	/**
	 * when set true, EHB will use static analysis to analyze the events, and get the activity jumping 
	 * events and non activity events. In this way, the time of execution will be longer.
	 * */

	public static EHBOptions options  = null;
		
	private EHBOptions() {
		
	}
		
	public static EHBOptions v(){
		if(options==null){
			options = new EHBOptions();
		}
		return options;
	}
}
