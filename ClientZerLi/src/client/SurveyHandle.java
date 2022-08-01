package client;
/**
 * Class Description:
 * this class help two screen to communicated to know which survey to show 
 * to the shopWorker
 * @author Mor Ben Haim
 *
 */
public class SurveyHandle {
	private static String topic;
	private static String targetAudience;

	public static String getTopic() {
		return topic;
	}

	public static void setTopic(String topic) {
		SurveyHandle.topic = topic;
	}

	public static String getTargetAudience() {
		return targetAudience;
	}

	public static void setTargetAudience(String targetAudience) {
		SurveyHandle.targetAudience = targetAudience;
	}

}
