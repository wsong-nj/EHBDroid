package ehb.global;

import com.app.test.event.UIEventHandler;

/**
 * Options for ehb
 * */
public class EHBOptions {
	
	/**
	 * Three are three strategies in ehb. When click the menu-item, the triggerring events
	 * can be one events, an event sequence, and many event sequences. 
	 * <ul>
	 * ONEEVENT: when click the menu-item, ehb will trigger one event. 
	 * </ul>
	 *  <ul>
	 * ONESEQ: when click the menu-item, ehb will trigger all the events one time. 
	 * seq=e1,e2,...,en 
	 * </ul>
	 * <ul>
	 * MANYSEQ: when click the menu-item, ehb will trigger all the events n times. 
	 * <li>seq1=e1,e2,...,en
	 * <li>seq2=e2,e1,...en
	 * <li>seqn=en,en-1,...e1.
	 * The default value of n is fixSeq.
	 * </ul>
	 * */
	public static final String ONEEVENT = UIEventHandler.ONEEVENT;
	public static final String ONESEQ = UIEventHandler.ONESEQ;
	public static final String MANYSEQ = UIEventHandler.MANYSEQ;
	
	/**
	 * when set true, EHB will use static analysis to analyze the events, and get the activity jumping 
	 * events and non activity events. In this way, the time of execution will be longer.
	 * */
	public boolean staticAnalysis;
	
	public int fixSeq = UIEventHandler.fixSeq;
	
	public int getFixSeq() {
		return fixSeq;
	}

	public void setFixSeq(int fixSeq) {
		this.fixSeq = fixSeq;
	}

	private String strategy;
	
	public static EHBOptions options  = null;
		
	private EHBOptions() {
		
	}
		
	public static EHBOptions v(){
		if(options==null){
			options = new EHBOptions();
		}
		return options;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public boolean isStaticAnalysis() {
		return staticAnalysis;
	}

	public void setStaticAnalysis(boolean staticAnalysis) {
		this.staticAnalysis = staticAnalysis;
	}
	
}
