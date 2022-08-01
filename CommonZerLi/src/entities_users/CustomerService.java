package entities_users;

import java.io.Serializable;

import enums.AccountStatus;

/**
 * class description : extends the user for customerService 
 * @author Omri Shalev 
 */

@SuppressWarnings("serial")
public class CustomerService extends User implements Serializable {
	/**
	 * 
	 * @param iD
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phoneNumber
	 * @param accountStatus
	 * @param isLoggedIn
	 */
	public CustomerService(String iD, String firstName, String lastName, String email, String phoneNumber,
			AccountStatus accountStatus, boolean isLoggedIn) {
		super(iD, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn);
	}
	
	@Override
	public String toString() {
		return "Customer Service";
	}
}
