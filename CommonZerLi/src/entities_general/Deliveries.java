
package entities_general;

import java.io.Serializable;
import java.util.List;

import entities_catalog.ProductInOrder;
import enums.DeliveryStatus;

/**
 * This class used for deliveries object
 * 
 * @author Omri Shalev
 *
 */
public class Deliveries implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderID, customerID, branchID, orderDate, expectedDelivery, arrivedDate, receiverName, address,
			phoneNumber;
	private int deliveryID;
	private double price;
	private DeliveryStatus status;
	private List<ProductInOrder> orderProducts;

	/**
	 * 
	 * @param deliveryID
	 * @param orderID
	 * @param branchID
	 * @param customerID
	 * @param price
	 * @param orderDate
	 * @param expectedDelivery
	 * @param arrivedDate
	 * @param receiverName
	 * @param address
	 * @param phoneNumber
	 * @param status
	 * @param orderProducts
	 */
	public Deliveries(int deliveryID, String orderID, String branchID, String customerID, double price,
			String orderDate, String expectedDelivery, String arrivedDate, String receiverName, String address,
			String phoneNumber, DeliveryStatus status, List<ProductInOrder> orderProducts) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.branchID = branchID;
		this.orderDate = orderDate;
		this.expectedDelivery = expectedDelivery;
		this.arrivedDate = arrivedDate;
		this.receiverName = receiverName;
		this.phoneNumber = phoneNumber;
		this.deliveryID = deliveryID;
		this.price = price;
		this.status = status;
		this.address = address;
		this.setOrderProducts(orderProducts);
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

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getExpectedDelivery() {
		return expectedDelivery;
	}

	public void setExpectedDelivery(String expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}

	public String getArrivedDate() {
		return arrivedDate;
	}

	public void setArrivedDate(String arrivedDate) {
		this.arrivedDate = arrivedDate;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getDeliveryID() {
		return deliveryID;
	}

	public void setDeliveryID(int deliveryID) {
		this.deliveryID = deliveryID;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public DeliveryStatus getDeliveryStatus() {
		return status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDeliveryStatus(DeliveryStatus status) {
		this.status = status;
	}

	public List<ProductInOrder> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<ProductInOrder> orderProducts) {
		this.orderProducts = orderProducts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deliveryID;
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
		Deliveries other = (Deliveries) obj;
		if (deliveryID != other.deliveryID)
			return false;
		return true;
	}

}
