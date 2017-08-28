package ehb.event;

public abstract class EventRecognizerForCode implements EventRecognizer{
	public enum AndroidEvent{
		ViewEvent,DialogEvent,ReceiverEvent,ServiceEvent,InterAppEvent,MenuEvent;
	}
	
	public abstract AndroidEvent parseEvent();
	
}

