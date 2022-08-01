package entities_general;

import java.io.Serializable;

/**
 *  
 *  This class is for login witch contains the username and password
 *  that the user enters, in addition there are 2 more parameters in this class
 *  the userID and the userType that we will get them from DB.
 * 
 * @author Mor Ben Haim.
 *  
 *  @version 20/04/2022
 */
@SuppressWarnings("serial")
public class Login implements Serializable{
	
	/**
	 * Class members description:
	 */	
	
	/**
	 * each user has userName.
	 */
	private String userName;
	
	/**
	 * each user has password .
	 */
	private String password;
	
	/**
	 * This is a constructor for the Login class
	 * we receive from the customer only an userName and password
	 * we create an Login object using these 2 parameters and we initialize other parameters to null
	 * we need only there 2 parameters before sending the object to the server.
	 * @param userName
	 * @param password
	 */
	public Login(String userName,String password) {
		this.userName=userName;
		this.password = password;

	}
	
	/**
	 * This section is for the 
	 * Setters and Getters of the 
	 * Login Customer for all types
	 */
	public String getUserName(){
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUserName(String userName) {
		this.userName=userName;
	}
	
	public void setPassword(String password) {
		this.password=password;
	}

	/**
	 * toString for the login class
	 */
	@Override
	public String toString() {
		return "UserName: " + userName +"\nPassword: " + password ;
	}
}
