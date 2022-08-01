package entities_users;

import java.io.Serializable;

import entities_general.CreditCard;
import enums.AccountStatus;

/**
 * class description : extends the user for customer 
 * @author Omri Shalev 
 */

@SuppressWarnings("serial")
public class Customer extends User implements Serializable{

	/**
	 * Every customer have a balance in his account (debit or credit)
	 */
	private String balance;
	/**
	 * A state that sign if the user is new customer or not.
	 */
	private boolean isNewCustomer;
	/**
	 * every customer has credit card
	 */
	private CreditCard creditCard;

	/**
	 * 
	 * @param iD
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phoneNumber
	 * @param accountStatus
	 * @param isLoggedIn
	 * @param balance
	 * @param isNewCustomer
	 * @param creditCard
	 */
	public Customer(String iD, String firstName, String lastName, String email, String phoneNumber,
			AccountStatus accountStatus, boolean isLoggedIn, String balance, boolean isNewCustomer,
			CreditCard creditCard) {
		super(iD, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn);
		this.balance = balance;
		this.isNewCustomer = isNewCustomer;
		this.creditCard = creditCard;
	}

	/**
	 * returns the balance of the customer in the shop
	 * 
	 * @return balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}

	/**
	 * returns if the customer is new or not
	 * 
	 * @return isNewCustomer
	 */
	public boolean getIsNewCustomer() {
		return isNewCustomer;
	}

	/**
	 * @param isNewCustomer
	 */
	public void setNewCustomer(boolean isNewCustomer) {
		this.isNewCustomer = isNewCustomer;
	}

	/**
	 * returns the credit card details of the customer
	 * 
	 * @return creditCard
	 */
	public CreditCard getCreditCard() {
		return creditCard;
	}

	/**
	 * @param creditCard
	 */
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	
	@Override
	public String toString() {
		return "Customer";
	}
}
