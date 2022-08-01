package client;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import client_gui.CartPageController;
import entities_catalog.ProductInBranch;
import entities_catalog.ProductInOrder;
import entities_general.Order;
import entities_general.OrderCartPreview;
import entities_general.OrderCustomCartPreview;
import entities_general.OrderPreview;
import entities_users.Customer;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

/**
 * Cancellation controller , branchmanager controller, customer controller ,catalog controller, (the logic controllers) are cancel and united to this class 
 * this class is handling with the orders , here we handling with the
 * invitations ,canaling . the main part of this class is to share common
 * information between different screens
 * 
 * @author almog mader
 *
 */
public class OrderHandleController implements nofityOrderListner {

	// this table will handle only custom products.
	private static Map<String, List<ProductInOrder>> customProductInOrder = new HashMap<>();
	// every order have his products the map saves the custom product , key=
	// customName , value=product
	private static Map<String, List<ProductInOrder>> customProductInOrderFinallCart = new HashMap<>();
	// the list saving the regular product
	private static List<ProductInOrder> productInOrder = new ArrayList<>();
	public static int quantityOfRegularProducts = 0;
	public static int quantityOfCustomProducts = 0;
	private static double totalPrice = 0;
	private static Label priceLabel = new Label("0");
	private static Label newCustomer = new Label("0");
	private static boolean detailsAllreadyOpen = false;
	private static boolean detailsAllreadyOpen2 = false;
	private static boolean disableRemoveCustomButton = false;
	private static double shippingPrice = 20.55;
	static DecimalFormat df = new DecimalFormat("#,###.##");

	/**
	 * get product in branch view
	 */
	private static List<ProductInBranch> productInBranch = new ArrayList<ProductInBranch>();

	/**
	 * map to view quantity chosen by customer. String nameProdcut(key) ,
	 * List<Integer>=[productID,quantity]
	 */
	private static Map<String, List<Integer>> quntityImageInBranch = new HashMap<String, List<Integer>>();
	/**
	 * set to view product with problematic bigger quantity -> <ProductID>
	 */
	private static Set<String> problemticProducts = new HashSet<>();

	/**
	 * Massage note to user if there is problem with quantity.
	 */
	private static String msg;
	/**
	 * boolean eventToClose
	 */
	private static boolean closeEvent = false;

	/**
	 * list of all ordinary orders that in pending status
	 */
	private static List<OrderPreview> ordersForBranchManager = new ArrayList<>();
	/**
	 * list of all order delivery that in pending status
	 */
	private static List<OrderPreview> ordersForBranchManagerD = new ArrayList<>();
	/**
	 * list of all order immediate delivery that in pending status
	 */
	private static List<OrderPreview> ordersForBranchManagerDI = new ArrayList<>();
	/**
	 * list of all cancel ordinary orders that the customer ask to cancel
	 */
	private static List<OrderPreview> ordersForBranchManagerC = new ArrayList<>();
	/**
	 * list of all cancel delivery orders that the customer ask to cancel
	 */
	private static List<OrderPreview> ordersForBranchManagerCD = new ArrayList<>();
	private static OrderPreview order;

	public static List<OrderPreview> getOrdersForBranchManagerDI() {
		return ordersForBranchManagerDI;
	}

	public static void setOrdersForBranchManagerDI(List<OrderPreview> ordersForBranchManagerDI) {
		OrderHandleController.ordersForBranchManagerDI = ordersForBranchManagerDI;
	}

	public static List<OrderPreview> getOrdersForBranchManagerD() {
		return ordersForBranchManagerD;
	}

	public static void setOrdersForBranchManagerD(List<OrderPreview> ordersForBranchManagerD) {
		OrderHandleController.ordersForBranchManagerD = ordersForBranchManagerD;
	}

	public static List<OrderPreview> getOrdersForBranchManagerC() {
		return ordersForBranchManagerC;
	}

	public static void setOrdersForBranchManagerC(List<OrderPreview> ordersForBranchManagerC) {
		OrderHandleController.ordersForBranchManagerC = ordersForBranchManagerC;
	}

	public static List<OrderPreview> getOrdersForBranchManagerCD() {
		return ordersForBranchManagerCD;
	}

	public static void setOrdersForBranchManagerCD(List<OrderPreview> ordersForBranchManagerCD) {
		OrderHandleController.ordersForBranchManagerCD = ordersForBranchManagerCD;
	}

	/*
	 * cancellation orders preview for view on screen.
	 */
	private static List<Order> cancelationOrdersPreview = new ArrayList<>();
	/*
	 * history orders preview for view on screen.
	 */
	private static List<Order> historyOrdersPreview = new ArrayList<>();
	/*
	 * cancelRequesthistory orders preview for view on screen.
	 */
	private static List<Order> cancelRequestHistOrdersPreview = new ArrayList<>();
	/*
	 * order view to pop up details screen .
	 */
	private static Order customerOrderView;
	/*
	 * order view for custom products to pop up details screen .
	 */
	private static Map<String, List<ProductInOrder>> customerOrderDetails = new HashMap<>();;

	/*
	 * orderPreview used in screen details
	 */

	public static OrderPreview getOrder() {
		return order;
	}

	/*
	 * orderPreview used in screen details
	 */
	public static void setOrder(OrderPreview order) {
		OrderHandleController.order = order;
	}

	public static List<OrderPreview> getOrdersForBranchManager() {
		return ordersForBranchManager;
	}

	public static void setOrdersForBranchManager(List<OrderPreview> ordersForBranchManager) {
		OrderHandleController.ordersForBranchManager = ordersForBranchManager;
	}

	/**
	 * customer section method
	 * 
	 * @return cancelationOrdersPreview to screen show .
	 */
	public static List<Order> getCancelationOrdersPreview() {
		return cancelationOrdersPreview;
	}

	/*
	 * @param - List<Order> cancelationOrdersPreview to screen show .
	 */
	public static void setCancelationOrdersPreview(List<Order> cancelationOrdersPreview) {
		OrderHandleController.cancelationOrdersPreview = cancelationOrdersPreview;
	}

	/*
	 * @param - List<Order> getHistoryOrdersPreview to screen show .
	 */
	public static List<Order> getHistoryOrdersPreview() {
		return historyOrdersPreview;
	}

	/*
	 * @param - List<Order> getCancelRequestHistOrdersPreview to screen show .
	 */
	public static List<Order> getCancelRequestHistOrdersPreview() {
		return cancelRequestHistOrdersPreview;
	}

	public static void setCancelRequestHistOrdersPreview(List<Order> cancelRequestHistOrdersPreview) {
		OrderHandleController.cancelRequestHistOrdersPreview = cancelRequestHistOrdersPreview;
	}

	public static void setHistoryOrdersPreview(List<Order> historyOrdersPreview) {
		OrderHandleController.historyOrdersPreview = historyOrdersPreview;
	}

	public static Order getCustomerOrderView() {
		return customerOrderView;
	}

	public static void setCustomerOrderView(Order customerOrderView) {
		OrderHandleController.customerOrderView = customerOrderView;
	}

	public static Map<String, List<ProductInOrder>> getCustomerOrderDetails() {
		return customerOrderDetails;
	}

	public static void setCustomerOrderDetails(Map<String, List<ProductInOrder>> customerOrderDetails) {
		OrderHandleController.customerOrderDetails = customerOrderDetails;
	}

	public static Map<String, List<ProductInOrder>> getCustomProductInOrderFinallCart() {
		return customProductInOrderFinallCart;
	}

	/**
	 * add temporary custom product to Final cart .
	 * 
	 * @param key                            - name of custom product
	 * @param customProductInOrderFinallCart - list of productInOrder To add to
	 *                                       Final cart
	 * @author Almog-Madar
	 */
	public static void addCustomProductInOrderFinallCart(String key,
			List<ProductInOrder> moveToCart) {
		if(OrderHandleController.customProductInOrderFinallCart.containsKey(key)) {
		List<ProductInOrder> temp=	OrderHandleController.customProductInOrderFinallCart.get(key);
			for(int k =0;k<moveToCart.size();k++) {
				if(temp.contains(moveToCart.get(k))) {
					for(int j=0;j<temp.size();j++) {
						if(temp.equals(moveToCart.get(k))) {
							temp.get(j).setQuantity(temp.get(j).getQuantity()+moveToCart.get(k).getQuantity());
							break;
						}
					}
				}else {
					temp.add(moveToCart.get(k));
				}
			}
			
			
		}else {
		OrderHandleController.customProductInOrderFinallCart.put(key, moveToCart);
		}
	}

	public static Map<String, List<ProductInOrder>> getCustomProductInOrder() {
		return customProductInOrder;
	}

	public static void setCustomProductInOrder(Map<String, List<ProductInOrder>> customProductInOrder) {
		OrderHandleController.customProductInOrder = customProductInOrder;
	}

	/**
	 * add productInOrder to specific custom.
	 * 
	 * @param key                - name of custom
	 * @param productInOrderList - List of productInOrder added to custom product.
	 * @author Almog-Madar
	 */
	public static void addCustomProductInOrder(String key, List<ProductInOrder> productInOrderList) {
		OrderHandleController.customProductInOrder.put(key, productInOrderList);
		int price = 0;
		for (ProductInOrder p : productInOrderList)
			price += (double) p.getProductQuantityInCart() * p.getPrice();
		OrderHandleController.totalPrice += price;
		priceLabel.setText(String.valueOf(totalPrice));
	}

	public static List<ProductInOrder> getProductInOrder() {
		return productInOrder;
	}

	/**
	 * add productInOrder to regular List.
	 * 
	 * @param productInOrder - add productInOrder to regular list.
	 * @author Almog-Madar
	 */
	public static void addProductInOrder(ProductInOrder productInOrder) {
		OrderHandleController.productInOrder.add(productInOrder);
		OrderHandleController.totalPrice += (double) productInOrder.getProductQuantityInCart()
				* productInOrder.getPrice();
		priceLabel.setText(String.valueOf(totalPrice));
	}

	/**
	 * To manage the custom item HashMap Add productInOrder to Custom product that
	 * exist.
	 * 
	 * @param key            - name of custom
	 * @param productInOrder - List of productInOrder added to custom product.
	 */
	public static void addToExistItemOnList(String key, ProductInOrder productInOrder) {
		List<ProductInOrder> productlist = customProductInOrder.get(key);
		if (productlist.contains(productInOrder)) {
			for (ProductInOrder i : productlist) {
				if (i.equals(productInOrder)) {
					i.setProductQuantityInCart(
							productInOrder.getProductQuantityInCart() + i.getProductQuantityInCart());
					customProductInOrder.get(key).remove(productInOrder);
					productInOrder.setProductQuantityInCart(i.getProductQuantityInCart());
					customProductInOrder.get(key).add(productInOrder);
					return;
				}
			}
		} else {
			customProductInOrder.get(key).add(productInOrder);
		}

		// price calculate to custom
		OrderHandleController.totalPrice += (double) productInOrder.getProductQuantityInCart()
				* productInOrder.getPrice();
		priceLabel.setText(String.valueOf(totalPrice));
	}

	// to manage the not custom item list
	// add to regular item quantity to same one.
	public static void addToExistItemOnListNotCustom(ProductInOrder productInOrder2) {

		for (int i = 0; i < productInOrder.size(); i++) {

			if (productInOrder.get(i).equals(productInOrder2)) {

				System.out.println("List-Regualr->" + productInOrder);

				System.out.println("inside->order2" + productInOrder2.getProductQuantityInCart());
				System.out.println("inside->order1" + productInOrder.get(i).getProductQuantityInCart());

				int totalQuntity = productInOrder.get(i).getProductQuantityInCart()
						+ productInOrder2.getProductQuantityInCart();
				productInOrder.get(i).setProductQuantityInCart(totalQuntity);
				// price calculate to custom

				System.out.println("quantityTotal" + totalQuntity);

				OrderHandleController.totalPrice += (double) productInOrder2.getProductQuantityInCart()
						* productInOrder2.getPrice();
				priceLabel.setText(String.valueOf(totalPrice));
				return;
			}

		}

	}

	public static int getQuantityOfCustomProducts() {
		return quantityOfCustomProducts;
	}

	public static int getQuantityOfRegularProducts() {

		return quantityOfRegularProducts;
	}

	public static double getTotalPrice() {
		return totalPrice;
	}

	public static void setTotalPrice(double totalPrice) {
		OrderHandleController.totalPrice = totalPrice;
	}

	public static Label getNewCustomer() {
		return newCustomer;
	}

	public static void setNewCustomer(Label newCustomer) {
		OrderHandleController.newCustomer = newCustomer;
	}

	public static Label getPriceLabel() {
		return priceLabel;
	}

	public static void setPriceLabel(Label priceLabel) {
		OrderHandleController.priceLabel = priceLabel;
	}

	public static int getCartCounter() {
		return quantityOfRegularProducts + quantityOfCustomProducts;
	}

	/**
	 * remove Custom product on the screen , need to remove on the back side also
	 * (here back). product selected from screen and need to remove.
	 * 
	 * @param ObservableList<OrderCustomCartPreview> productSelected - product
	 *                                               selected from screen .
	 * @author Almog-Madar
	 */
	@Override
	public void removeFromOrderCustom(ObservableList<OrderCustomCartPreview> productSelected) {
		// TODO Auto-generated method stub

		for (OrderCustomCartPreview ocp : productSelected) {
			totalPrice -= ocp.getTotalprice();
			customProductInOrderFinallCart.remove(ocp.getName());
			quantityOfCustomProducts--;
		}

		System.out.println("remove listner custom hashmap->" + customProductInOrderFinallCart);

	}

	/**
	 * Remove regular product on the screen , need to remove on the back side also
	 * (here back).
	 * 
	 * @param ObservableList<OrderCartPreview> productSelected - product selected
	 *                                         from screen.
	 * @author Almog-Madar
	 */
	@Override
	public void removeFromOrderRegular(ObservableList<OrderCartPreview> productSelected) {
		// TODO Auto-generated method stub
		for (OrderCartPreview ocp : productSelected) {
			for (ProductInOrder pd : productInOrder) {
				System.out.println(pd.getName());
				System.out.println(ocp.getName());

				if (pd.getName().equals(ocp.getName())) {

					System.out.println("here!!");
					totalPrice -= ocp.getPrice() * ocp.getQuantity();
					productInOrder.remove(pd);
					quantityOfRegularProducts -= ocp.getQuantity();
					break;
				}

			}
		}

		System.out.println("remove listner regular list->" + productInOrder);
	}

	/**
	 * remove productInOrder inside custom product on screen ,need to remove on the
	 * back side also (here back)
	 * 
	 * @param ObservableList<ProductInOrder> productSelected - product selected from
	 *                                       screen.
	 * @param String                         customName - name of custom product.
	 * @author Almog-Madar
	 */
	@Override
	public void removeProductInOrderInsideCustom(ObservableList<ProductInOrder> productSelected, String customName) {
		// TODO Auto-generated method stub
		List<ProductInOrder> customProducts = customProductInOrderFinallCart.get(customName);
		List<ProductInOrder> productSelectedRegualrList = new ArrayList<ProductInOrder>();
		/**
		 * change from ObservableList to List
		 */
		productSelectedRegualrList.addAll(productSelected);
		/**
		 * remove on the back side it productInOrder inside custom
		 */
		customProducts.removeAll(productSelectedRegualrList);

		/**
		 * if we remove the last one in custom product
		 */
		if (customProducts.size() == 0) {
			customProductInOrderFinallCart.remove(customName);
			quantityOfCustomProducts--;
		}

		/**
		 * calculate total price
		 */
		for (ProductInOrder p : productSelectedRegualrList) {
			totalPrice -= p.getPrice() * (double) p.getProductQuantityInCart();
			priceLabel.setText(String.valueOf(totalPrice));
		}
		/**
		 * testing print - need to remove
		 */
		System.out.println("customProducts list->" + customProducts);
		System.out.println("after remove deatails page ->" + customProductInOrderFinallCart.get(customName));
		System.out.println("total custom products ->" + customProductInOrderFinallCart);
		System.out.println("quantityOfCustomProducts->" + quantityOfCustomProducts);
		System.out.println("totalPrice->" + totalPrice);
	}

	/**
	 * update label totalPrice on cartPage .
	 */
	public static void updateTotalPrice() {
		/**
		 * update price label after remove regular product
		 */
		if (((Customer) ClientController.user).getIsNewCustomer()) {
			System.out.println("here1");
			newCustomer.setVisible(true);

			try {
				priceLabel.setText(df.format(OrderHandleController.getTotalPrice() * 0.8) + " ¤");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			System.out.println("here2");
			newCustomer.setVisible(false);

			try {
				priceLabel.setText(df.format(OrderHandleController.getTotalPrice()) + " ¤");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("total price updated ->" + priceLabel.getText());
		System.out.println(OrderHandleController.getTotalPrice());
	}

	public static boolean isDetailsAllreadyOpen() {
		return detailsAllreadyOpen;
	}

	public static void setDetailsAllreadyOpen(boolean detailsAllreadyOpen) {
		OrderHandleController.detailsAllreadyOpen = detailsAllreadyOpen;
	}

	public static boolean isDetailsAllreadyOpen2() {
		return detailsAllreadyOpen2;
	}

	public static void setDetailsAllreadyOpen2(boolean detailsAllreadyOpen2) {
		OrderHandleController.detailsAllreadyOpen2 = detailsAllreadyOpen2;
	}

	public static boolean isDisableRemoveCustomButton() {
		return disableRemoveCustomButton;
	}

	public static void setDisableRemoveCustomButton(boolean disableRemoveCustomButton) {
		OrderHandleController.disableRemoveCustomButton = disableRemoveCustomButton;
	}

	public static List<ProductInBranch> getProductInBranch() {
		return productInBranch;
	}

	public static void setProductInBranch(List<ProductInBranch> productInBranch) {
		OrderHandleController.productInBranch = productInBranch;
	}

	/**
	 * check if order quantity is corrected by branch quantity. quntityImageInBranch
	 * - map to view quantity chosen by customer. problemticProducts - which product
	 * are problematic.
	 * 
	 * @return boolean - true if problemticProducts exist else false.
	 * @author Almog-Madar
	 */
	public static boolean checkQuantityInOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append("There was an exception in the stock.\n");

		/**
		 * clear all information before run
		 */
		quntityImageInBranch.clear();
		problemticProducts.clear();

		/*
		 * collect total quantity MAP IMAGE for REGUALR.
		 */
		for (ProductInOrder productInOr : productInOrder) {
			addToMapQuntityInBranch(productInOr);
		}

		/*
		 * collect total quantity MAP IMAGE for CUSTOM.
		 */
		for (List<ProductInOrder> customProductList : customProductInOrderFinallCart.values()) {
			for (ProductInOrder productInOr : customProductList) {
				addToMapQuntityInBranch(productInOr);
			}
		}

		System.out.println("Map-view-> " + quntityImageInBranch);

		/*
		 * check total quantity between mapQuntityInBranch and productInBranch
		 */
		Set<String> productsNames = quntityImageInBranch.keySet();
		for (String productName : productsNames) {
			for (ProductInBranch productInB : productInBranch) {
				/*
				 * check if same productId
				 */
				String productID = String.valueOf(quntityImageInBranch.get(productName).get(0));
				int productQuantity = quntityImageInBranch.get(productName).get(1);

				if (productInB.getProductID().equals(productID)) {
					// System.out.println("ProductInBranch-> " +productInB.getProductID());
					// System.out.println("ProductIDInMap-> " +productID);

					/**
					 * Quantity in branch is smaller then total Customer choose.
					 */
					if (productInB.getQuantity() < productQuantity) {

						/*
						 * add to problematic productID set
						 */
						problemticProducts.add(productName);
						System.out.println("problem-set:" + problemticProducts);
						// System.out.println("QunitityInBranch-> " +productInB.getQuantity());
						// System.out.println("CustomerTotalQuantity -> "
						// +quntityImageInBranch.get(productID));

						/*
						 * build massage view
						 */
						sb.append("Total " + productName + " in branch:\n");
						sb.append(productInB.getQuantity() + "\n");
						sb.append("You choose:\n");
						sb.append(productQuantity + "\n");
						sb.append("Please remove -> " + (productQuantity - productInB.getQuantity()) + " product units."
								+ "\n");

					}
				}
			}
		}

		sb.append("Or another option is to change branch :)\n");
		/**
		 * set massage pop-up screen.
		 */
		msg = sb.toString();

		/**
		 * if there is problem quantity.
		 */
		if (problemticProducts.size() != 0)
			return false;

		return true;
	}

	/**
	 * add to map of quntityInBranch + set problemticProducts
	 * 
	 * @param productInOr - add product Map quantity choose of the customer.
	 * @author Almog-Madar
	 */
	private static void addToMapQuntityInBranch(ProductInOrder productInOr) {
		int quntity;

		System.out.println("productInOr ProductID->" + productInOr.getName());
		// if not inside the map
		if (!quntityImageInBranch.containsKey(productInOr.getName())) {
			quntityImageInBranch.put(productInOr.getName(),
					Arrays.asList(Integer.parseInt(productInOr.getID()), productInOr.getProductQuantityInCart()));
		} else // inside the map need to add quantity to same item
		{
			quntity = quntityImageInBranch.get(productInOr.getName()).get(1);
			quntityImageInBranch.remove(productInOr.getName());
			quntityImageInBranch.put(productInOr.getName(), Arrays.asList(Integer.parseInt(productInOr.getID()),
					quntity + productInOr.getProductQuantityInCart()));
		}

	}

	public static String getMsg() {
		return msg;
	}

	public static void setMsg(String msg) {
		OrderHandleController.msg = msg;
	}

	public static Set<String> getProblemticProducts() {
		return problemticProducts;
	}

	/**
	 * Clear all dynamic data.
	 * 
	 * @author Almog-Madar.
	 */
	public static void clearAllOrderData() {
		customProductInOrder.clear();
		customProductInOrderFinallCart.clear();
		CartPageController.listViewCustom.clear();
		productInOrder.clear();
		quantityOfRegularProducts = 0;
		quantityOfCustomProducts = 0;
		totalPrice = 0;
		updateTotalPrice();
	}

	public static boolean isCloseEvent() {
		return closeEvent;
	}

	public static void setCloseEvent(boolean closeEvent) {
		OrderHandleController.closeEvent = closeEvent;
	}

	public static double getShippingPrice() {
		return shippingPrice;
	}

	public static void setShippingPrice(double shippingPrice) {
		OrderHandleController.shippingPrice = shippingPrice;
	}

}
