package entities_users;

import java.io.Serializable;

import enums.AccountStatus;

/**
 * class description : user object for all users in the system
 * @author Omri Shalev 
 */

@SuppressWarnings("serial")
public class User implements Serializable {
	/**
	 * Every user has ID - PrimaryKey of every user.
	 */
	protected String ID;
	/**
	 * Every user has a first name.
	 */
	protected String FirstName;
	/**
	 * Every user has a last name.
	 */
	protected String LastName;
	
	/**
	 * Every user has an email.
	 */
	protected String email;
	/**
	 * Every user has a phone number.
	 */
	protected String phoneNumber;
	/**
	 * Every user has a status is the system
	 */
	protected AccountStatus accountStatus;
	/**
	 * A state that sign if the user is already logged in or not.
	 */
	protected boolean isLoggedIn;

	/**
	 * 
	 * @param iD
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phoneNumber
	 * @param isLoggedIn
	 */
	public User(String iD, String firstName, String lastName, String email, String phoneNumber,
			AccountStatus accountStatus, boolean isLoggedIn) {
		this.ID = iD;
		this.FirstName = firstName;
		this.LastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.accountStatus = accountStatus;
		this.isLoggedIn = isLoggedIn;
	}

	/**
	 * @return ID
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param iD
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * returns the user first name
	 * 
	 * @return FirstName
	 */
	public String getFirstName() {
		return FirstName;
	}

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	/**
	 * returns the user last name
	 * 
	 * @return LastName
	 */
	public String getLastName() {
		return LastName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		LastName = lastName;
	}

	/**
	 * returns the email of the user
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * returns the phone number of the user
	 * 
	 * @return
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * returns the account status in the system of the user
	 * 
	 * @return accountStatus
	 */
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	/**
	 * @param accountStatus
	 */
	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	/**
	 * returns the state of the user login or not
	 * 
	 * @return isLoggedIn
	 */

	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	/**
	 * @param isLoggedIn
	 */
	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

}
