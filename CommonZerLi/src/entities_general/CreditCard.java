package entities_general;

import java.io.Serializable;
/**
 * class that representing the credit card object
 * @author omri shalev
 *
 */
@SuppressWarnings("serial")
public class CreditCard implements Serializable{
	String creditCardNumber, creditCardCvvCode, creditCardDateOfExpiration;
/**
 * 
 * @param creditCardNumber
 * @param creditCardCvvCode
 * @param creditCardDateOfExpiration
 */
	public CreditCard(String creditCardNumber, String creditCardCvvCode, String creditCardDateOfExpiration) {
		super();
		this.creditCardNumber = creditCardNumber;
		this.creditCardCvvCode = creditCardCvvCode;
		this.creditCardDateOfExpiration = creditCardDateOfExpiration;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardCvvCode() {
		return creditCardCvvCode;
	}

	public void setCreditCardCvvCode(String creditCardCvvCode) {
		this.creditCardCvvCode = creditCardCvvCode;
	}

	public String getCreditCardDateOfExpiration() {
		return creditCardDateOfExpiration;
	}

	public void setCreditCardDateOfExpiration(String creditCardDateOfExpiration) {
		this.creditCardDateOfExpiration = creditCardDateOfExpiration;
	}
}
