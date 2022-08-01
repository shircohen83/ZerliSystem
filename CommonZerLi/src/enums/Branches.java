package enums;

/**
 * Constant Enum that contain all the branches in Zerli with there identify
 * numbers
 * 
 * @author Mor Ben Haim
 *
 */
public enum Branches {
	KARMIEL("Karmiel",1010),
	HAIFA("Haifa",2525),
	TEL_AVIV("Tel-Aviv",5555);
	
	

	private int number;
	private String name;

	private Branches(String name,int number) {
	 this.number=number;
	 this.name=name;
	 
	}
	
	
	public int getNumber() {
		return number;
	}
	
	public String getName() {
		return name;
	}
	

}
