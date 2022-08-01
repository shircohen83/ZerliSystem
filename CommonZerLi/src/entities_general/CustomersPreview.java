package entities_general;

import entities_users.Customer;
import enums.AccountStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
/**
 * class for holding the customers for presenting it into the screen
 * it his subclass of the Order
 * @author shir cohen , almog mader
 *
 */
public class CustomersPreview extends Customer
{

	private static final long serialVersionUID = 1L;
	private ComboBox<AccountStatus> accountStatusCB=new ComboBox<>();
	
	public CustomersPreview(String iD, String firstName, String lastName, String email, String phoneNumber,
	AccountStatus accountStatus, boolean isLoggedIn, String balance, boolean isNewCustomer,CreditCard creditCard) 
	{
		super(iD, firstName, lastName, email, phoneNumber, accountStatus, isLoggedIn, balance, isNewCustomer, creditCard);
		ObservableList<AccountStatus> frozenNotFrozen=FXCollections.observableArrayList(AccountStatus.CONFIRMED,AccountStatus.FROZEN);
		this.accountStatusCB.setItems(frozenNotFrozen);//list to combo box
		this.accountStatusCB.setValue(accountStatus);//the default choice
	}
	
	public ComboBox<AccountStatus> getAccountStatusCB() 
	{//will be used for displaying customers with combobox as their status
		return accountStatusCB;
	}

	public void setAccountStatusCB(ComboBox<AccountStatus> accountStatusCB) {
		this.accountStatusCB = accountStatusCB;
	}
	
	

}