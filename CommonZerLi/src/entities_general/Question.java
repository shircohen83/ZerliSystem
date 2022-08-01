package entities_general;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class Description: this class represented a question Object for each question
 * from the survey
 * 
 * @author Mor Ben Haim
 *
 */
public class Question implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Every question have number
	 */
	private int questionNumber;
	/**
	 * Every question have topic
	 */
	private String topic;
	/**
	 * the question it self
	 */
	private String question;
	/**
	 * Every question have date of taken the answer for it
	 */
	private String date;
	/**
	 * Every question have branchID which taken from
	 */
	private String branchID;
	/**
	 * Every question have targetAudience
	 */
	private String targetAudience;
	/**
	 * Every question have answer
	 */
	private int answer;

	public Question( String topic,int questionNumber, String question) {
		super();
		this.questionNumber = questionNumber;
		this.topic = topic;
		this.question = question;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}
	

	public String getTargetAudience() {
		return targetAudience;
	}

	public void setTargetAudience(String targetAudience) {
		this.targetAudience = targetAudience;
	}

	@Override
	public int hashCode() {
		return Objects.hash(answer, branchID, date, question, questionNumber, targetAudience, topic);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		return answer == other.answer && Objects.equals(branchID, other.branchID) && Objects.equals(date, other.date)
				&& Objects.equals(question, other.question) && questionNumber == other.questionNumber
				&& Objects.equals(targetAudience, other.targetAudience) && Objects.equals(topic, other.topic);
	}

	@Override
	public String toString() {
		return "Question [questionNumber=" + questionNumber + ", topic=" + topic + ", question=" + question + ", date="
				+ date + ", branchID=" + branchID + ", answer=" + answer + "]";
	}
	
}
