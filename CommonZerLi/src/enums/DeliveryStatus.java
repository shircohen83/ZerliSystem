package enums;
/**
 * this class define the options for delivery status
 * @author omri shalev 
 *
 */
public enum DeliveryStatus {

	READY_TO_GO("READY_TO_GO", 0),
	ARRIVED("ARRIVED", 1),
	WAIT_FOR_MANAGER_APPROVED("WAIT_FOR_MANAGER_APPROVED", 2);


	private String name;
	
	DeliveryStatus(String name, int i) {
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
