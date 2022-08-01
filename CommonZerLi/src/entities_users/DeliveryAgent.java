package entities_users;

import java.io.Serializable;

import enums.AccountStatus;

/**
 * class description : extends the user for delieveryAgent 
 * @author Omri Shalev 
 */

@SuppressWarnings("serial")
public class DeliveryAgent extends User implements Serializable{
	/**
	 * Delivery agents has their main branch
	 */
	private String branchID;

	/**
	 * 
	 * @param iD
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phoneNumber
	 * @param accountStatus
	 * @param isLoggedIn
	 * @param branchID
	
	 */
	public DeliveryAgent(String iD, String firstName, String lastName, String email, String phoneNumber,
			AccountStatus accountStatus, boolean isLoggedIn, String branchID) {
		super(iD, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn);
		this.branchID = branchID;
	}

	@Override
	public String toString() {
		return "Delivery Agent";
	}

	/**
	 * returns the branch ID
	 * 
	 * @return branchID
	 */
	public String getBranchID() {
		return branchID;
	}

	/**
	 * @param branchID
	 */
	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}




}
