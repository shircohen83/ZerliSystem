package client;

import entities_general.DeliveryPreview;
/**
 * handler to enable multiple screen to communicated with the current values
 * @author Mor Ben Haim
 *
 */
public class DeliveriesController {
	private static DeliveryPreview deliveryItems;

	public static DeliveryPreview getDelivery() {
		return deliveryItems;
	}

	public static void setDelivery(DeliveryPreview delivery) {
		DeliveriesController.deliveryItems = delivery;
	}
	
}
