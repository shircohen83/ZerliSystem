package entities_general;

import java.io.Serializable;
import java.util.Objects;

import enums.OrderStatus;

/** object represent customer cancellation request at cancel process
 * 
 * @author Almog-Madar
 *
 */
public class Cancellation implements Serializable {

	private static final long serialVersionUID = 1L;
	int cancellationID;
	String orderID;
	String customerID;
	double expectedRefund;
	private OrderStatus status;
	
	/**
	 * 
	 * @param cancellationID
	 * @param orderID
	 * @param customerID
	 * @param expectedRefund
	 */
	public Cancellation(int cancellationID, String orderID, String customerID, double expectedRefund) {
		this.cancellationID = cancellationID;
		this.orderID = orderID;
		this.customerID = customerID;
		this.expectedRefund = expectedRefund;
		
	}


	public OrderStatus getStatus() {
		return status;
	}


	public void setStatus(OrderStatus status) {
		this.status = status;
	}


	public int getCancellationID() {
		return cancellationID;
	}


	public void setCancellationID(int cancellationID) {
		this.cancellationID = cancellationID;
	}


	public String getOrderID() {
		return orderID;
	}


	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}


	public String getCustomerID() {
		return customerID;
	}


	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}


	public double getExpectedRefund() {
		return expectedRefund;
	}


	public void setExpectedRefund(double expectedRefund) {
		this.expectedRefund = expectedRefund;
	}


	@Override
	public int hashCode() {
		return Objects.hash(cancellationID, orderID);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cancellation other = (Cancellation) obj;
		return cancellationID == other.cancellationID && Objects.equals(orderID, other.orderID);
	}
	
	
		
}
