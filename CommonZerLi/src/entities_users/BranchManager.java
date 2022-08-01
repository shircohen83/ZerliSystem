package entities_users;

import java.io.Serializable;

import enums.AccountStatus;
/**
 * class description : extends the user for branchManager 
 * @author Omri Shalev 
 */


@SuppressWarnings("serial")
public class BranchManager extends User implements Serializable {
	/**
	 * the branch ID that the manager manage.
	 */
	private String branchID;

	/**
	 * @param iD
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phoneNumber
	 * @param accountStatus
	 * @param isLoggedIn
	 * @param branchID
	 */
	public BranchManager(String iD, String firstName, String lastName, String email, String phoneNumber,
			AccountStatus accountStatus, boolean isLoggedIn, String branchID) {
		super(iD, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn);
		this.branchID = branchID;
	}

	/**
	 * returns the branch ID that the manager manage.
	 * 
	 * @return BranchID
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

	@Override
	public String toString() {
		return "Branch Manager";
	}

}
