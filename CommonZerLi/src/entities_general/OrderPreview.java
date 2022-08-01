package entities_general;

import java.util.List;
import java.util.Map;

import entities_catalog.ProductInOrder;
import enums.OrderStatus;
import javafx.scene.control.ComboBox;
/**
 * class for holding the Orders for presenting it into the screen
 * it his subclass of the Order
 * @author shir cohen , almog mader
 *
 */
public class OrderPreview extends Order {

	/**
	 * every OrderPreview have it own combobox with the specific state
	 */
	private ComboBox<OrderStatus> comboStatus = new ComboBox<>();

	public ComboBox<OrderStatus> getComboStatus() {
		return comboStatus;
	}

	public void setComboStatus(ComboBox<OrderStatus> comboStatus) {
		this.comboStatus = comboStatus;
	}

	private static final long serialVersionUID = 1L;
/**
 * 
 * @param orderID
 * @param customerID
 * @param branchID
 * @param price
 * @param greetingCard
 * @param orderDate
 * @param expectedDelivery
 * @param items
 */
	public OrderPreview(String orderID, String customerID, String branchID, double price, String greetingCard,
			String orderDate, String expectedDelivery, Map<String, List<ProductInOrder>> items) {
		super(orderID, customerID, branchID, price, greetingCard, orderDate, expectedDelivery, items);

	}

}
