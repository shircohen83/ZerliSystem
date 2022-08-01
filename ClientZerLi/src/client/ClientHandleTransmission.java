
/** In this class we are handling with the Transmissions , we creating client transmission (with specific mission ) 
 * And sending it to the server by using ClientUI.chat.accept method, and getting back the response (Transmission)
 * And after that doing the specific task.
 * 
 *
 */
package client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client_gui.BranchManagerPageController;
import client_gui.CustomerPageController;
import client_gui.CustomerServicePageController;
import client_gui.DeliveryAgentPageController;
import client_gui.GenaralPopUpController;
import client_gui.LoginController;
import client_gui.MarketingWorkerOpeningController;
import client_gui.NetworkManagerPageController;
import client_gui.ServiceExpertPageController;
import client_gui.ShopWorkerPageController;
import communication.Mission;
import communication.Response;
import communication.TransmissionPack;
import entities_catalog.Product;
import entities_catalog.ProductInBranch;
import entities_catalog.ProductInOrder;
import entities_general.Cancellation;
import entities_general.CustomersPreview;
import entities_general.Deliveries;
import entities_general.DeliveryPreview;
import entities_general.Login;
import entities_general.Order;
import entities_general.OrderPreview;
import entities_general.Question;
import entities_general.WorkersPreview;
import entities_reports.Complaint;
import entities_reports.ComplaintPreview;
import entities_reports.Report;
import entities_users.Customer;
import entities_users.ShopWorker;
import entities_users.User;
import enums.Branches;
import enums.DeliveryStatus;
import enums.OrderStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * in this class we handling all the client missions , we transfer the mission
 * to the server by using the TransmissionPack Obj , and then we getting the
 * server result/response. every method send to the server the mission that it
 * need to be done and the obj that need to handle in the server side. all this
 * information is held in the transmissions pack and it send to the server side
 * using ocsf framework
 * 
 * @author mor ben haim , almog mader, shir choen , omri shalev , dvir bublil
 *
 */
public class ClientHandleTransmission {

	/**
	 * In this method we are creating TransmissionPack that will contain ADDORDER
	 * mission ,and the information (order)- to insert into the DB, that will be
	 * send to the server by ClientUI.chat.accept , and the server will handle with
	 * the order
	 * 
	 * @param orderIdTxt      - order text field on the gui
	 * @param priceTxt        - price text field on the gui
	 * @param greetingCardTxt - greeting card text field on the gui
	 * @param colorTxt        - color text field on the gui
	 * @param dOrderTxt       - dOrder text field on the gui
	 * @param shopTxt         - shop text field on the gui
	 * @param dateTxt         - date text field on the gui
	 * @param orderDate       - order date text field on the gui
	 * @param statusTxt       - status text field on the gui
	 */

	/**
	 * In this method we are creating Transmission that will contain GETORDERS
	 * mission , then we sending it to the server by using ClientUI.chat.accept ,
	 * after that we getting back the result (response and the information-the order
	 * table from the sql)
	 * 
	 * @param listView-listView that will contain the orders from the sql table
	 * @param table             - tableView contain orders
	 * @param statusLabel       - label that present the status (success or failed)
	 * @param list              - temp list to handle with the orders on the method
	 */
	@SuppressWarnings("unchecked")
	public static void GET_ORDERS(ObservableList<Order> listView, TableView<Order> table, Label statusLabel) {
		TransmissionPack obj = new TransmissionPack(Mission.GET_ORDER, null, null);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getObj();
		if (obj.getResponse() == Response.FOUND_ORDER) {
			listView.clear();
			List<Order> list = new ArrayList<Order>();
			list = (List<Order>) obj.getInformation();
			for (int i = 0; i < list.size(); i++) {
				listView.add(list.get(i));
			}
			table.setItems(listView);
			statusLabel.setTextFill(Color.GREEN);
			statusLabel.setText("Upload Success");
		} else {
			statusLabel.setTextFill(Color.RED);
			statusLabel.setText("Upload Failed");
		}
	}

	/**
	 * this method getting the connect to server request from the client and pass it
	 * to the server to handle with it.the client check if there any socket server
	 * are listening to this port that he transfered from the gui if there is server
	 * socket that are listening to this port. if its success we starting the login
	 * screen
	 * 
	 * 
	 * @param event
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public static void CONNECT_TO_SERVER(ActionEvent event, String ip, String port) throws Exception {
		ClientUI.chat = new ClientController(ip, Integer.parseInt(port));// logic

		TransmissionPack obj = new TransmissionPack(Mission.SEND_CONNECTION_DETAILS, null, null);
		List<String> details = new ArrayList<>();
		// gui for design
		details.add(InetAddress.getLocalHost().getHostAddress());
		details.add(InetAddress.getLocalHost().getHostName());
		obj.setInformation(details);
		// logic: check the response
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getObj();
		if (obj.getResponse() == Response.UPDATE_CONNECTION_SUCCESS) {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
			Stage primaryStage = new Stage();
			LoginController login = new LoginController();
			login.start(primaryStage);
		} else {
			// TBD - if we will make this java application as web application we will had a
			// else case.
		}
	}

	/**
	 * in this method we passing the logout request from the client to the server ,
	 * and the specific user info.
	 */
	public static void logoutFromZerLi() {
		TransmissionPack tp = new TransmissionPack(Mission.USER_LOGOUT, null, ClientController.user);
		ClientUI.chat.accept(tp);
	}

	/**
	 * in this method we passing the disconnect request from the client to the
	 * server and , the server will execute the request
	 */
	public static void DISCONNECT_FROM_SERVER() {
		if (ClientController.user != null) {
			logoutFromZerLi();
		}
		TransmissionPack obj = new TransmissionPack(Mission.SEND_DISCONNECT_DETAILS, null, null);
		List<String> details = new ArrayList<>();
		try {
			details.add(InetAddress.getLocalHost().getHostAddress());
			details.add(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		obj.setInformation(details);
		ClientUI.chat.accept(obj);
	}

	/**
	 * in this method we passing the request from the client to login into the
	 * server , after we establish the connected to the server socket in this
	 * specific port the client insert his user name and password and the server
	 * will check if this user access to the system.
	 * 
	 * @param userTxt
	 * @param passwordTxt
	 * @param errorLabel
	 * @param event
	 */
	public static void USER_LOGIN(TextField userTxt, TextField passwordTxt, Label errorLabel, MouseEvent event) {
		userTxt.setStyle(null);
		passwordTxt.setStyle(null);
		Login login = new Login(userTxt.getText(), passwordTxt.getText());

		if (checkLoginValidationFilling(login, userTxt, passwordTxt, errorLabel)) {
			TransmissionPack tp = new TransmissionPack(Mission.USER_LOGIN, null, login);
			ClientUI.chat.accept(tp);
			tp = ClientUI.chat.getObj();
			switch (tp.getResponse()) {
			case USER_NAME_OR_PASSWORD_INCORRECT: {
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("User name or password incorrect");
				break;
			}
			case USER_EXIST:
				try {
					loadTheRightScreen(event, tp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case USER_NOT_EXIST: {
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("User doesn't exist");
				break;
			}
			case USER_ALREADY_LOGGEDIN: {
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("User Already loggedin");
				break;
			}
			default:
				break;
			}
		}
	}

	/**
	 * this method check if the user insert his details to the fields , and return
	 * true if yes , if not return false and doing some gui styling.
	 * 
	 * @param login
	 * @param userTxt
	 * @param passwordTxt
	 * @param errorLabel
	 * @return
	 */
	private static boolean checkLoginValidationFilling(Login login, TextField userTxt, TextField passwordTxt,
			Label errorLabel) {
		if (login.getUserName().equals(""))
			userTxt.setStyle("-fx-border-color: red");
		if (login.getPassword().equals("")) { // checking if the user didn't enter both username and password.
			passwordTxt.setStyle("-fx-border-color: red");
		}
		if (login.getUserName().equals("") || login.getPassword().equals("")) {
			errorLabel.setText("Please fill all the fields on the screen");
			return false;
		} else
			return true;
	}

	/**
	 * this method get the colors filter for catalog initilizedProductGrid
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getColorsForFilter() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_COLORS, null, null);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.FOUND_COLORS) {
			return (ArrayList<String>) tp.getInformation();
		}
		return null;
	}

	/**
	 * this method is adding the pending order of the specific customer the order is
	 * waiting for approve from the branch manager
	 * 
	 * @return
	 * 
	 */

	public static int addOrder(Branches branchName, String greetingCard, String orderDate, String expectedDelivery,
			String status) {

		// get Custom + regular products in order
		Map<String, List<ProductInOrder>> productInOrderFinallCart = OrderHandleController.getCustomProductInOrder();
		productInOrderFinallCart.put("Regular", OrderHandleController.getProductInOrder());
		Order order;

		// new customer order or not price updated
		if (((Customer) ClientController.user).getIsNewCustomer()) {
			order = new Order(null, ClientController.user.getID(), String.valueOf(branchName.getNumber()),
					OrderHandleController.getTotalPrice() * 0.8, greetingCard, orderDate, expectedDelivery,
					productInOrderFinallCart);
		} else {
			order = new Order(null, ClientController.user.getID(), String.valueOf(branchName.getNumber()),
					OrderHandleController.getTotalPrice(), greetingCard, orderDate, expectedDelivery,
					productInOrderFinallCart);
		}

		if (status.equals("delivery"))
			order.setStatus(OrderStatus.PENDING_WITH_DELIVERY);
		else if ((status.equals("takeaway")))
			order.setStatus(OrderStatus.PENDING);

		else if (status.equals("Imidiate delivery")) {
			order.setStatus(OrderStatus.PENDING_WITH_IMIDATE_DELIVERY);
		}
		TransmissionPack tp = new TransmissionPack(Mission.ADD_ORDER, null, order);

		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();

		if (tp.getResponse() == Response.INSERT_ORDER_SUCCESS) {
			OrderHandleController.clearAllOrderData();
			int orderID = (int) tp.getInformation();
			return orderID;
		}
		
		return 0;
	}

	/**
	 * this method return the ObserverList of the order that was not approved yet
	 * 
	 * @return
	 */
	public static List<List<OrderPreview>> getListOrderPreview() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_ORDER, null, null);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		List<OrderPreview> orderPreviewOrdinary = new ArrayList<>();
		List<OrderPreview> orderPreviewDelivery = new ArrayList<>();
		List<OrderPreview> orderPreviewDeliveryImidate = new ArrayList<>();
		List<OrderPreview> orderPreviewCancel = new ArrayList<>();
		List<OrderPreview> orderPreviewCancelDelivery = new ArrayList<>();
		List<List<OrderPreview>> orderPreviews = new ArrayList<>();
		System.out.println("here->>after mission");
		if (tp.getResponse() == Response.FOUND_ORDER) {

			@SuppressWarnings("unchecked")
			List<Order> orders = (List<Order>) tp.getInformation();
			for (Order order : orders) {

				OrderPreview screen = new OrderPreview(order.getOrderID(), order.getCustomerID(), order.getBranchID(),
						order.getPrice(), order.getGreetingCard(), order.getOrderDate(), order.getExpectedDelivery(),
						order.getItems());
				screen.getComboStatus().setValue(order.getStatus());
				switch (order.getStatus()) {
				case PENDING: {
					ObservableList<OrderStatus> list = FXCollections.observableArrayList(OrderStatus.PENDING,
							OrderStatus.APPROVE, OrderStatus.CANCEL);
					screen.getComboStatus().setItems(list);
					orderPreviewOrdinary.add(screen);
					break;
				}
				case PENDING_WITH_DELIVERY: {
					ObservableList<OrderStatus> list = FXCollections.observableArrayList(
							OrderStatus.PENDING_WITH_DELIVERY, OrderStatus.APPROVE_WITH_DELIVERY,
							OrderStatus.CANCEL_WITH_DELIVERY);
					screen.getComboStatus().setItems(list);
					orderPreviewDelivery.add(screen);
					break;
				}
				case PENDING_WITH_IMIDATE_DELIVERY: {
					ObservableList<OrderStatus> list = FXCollections.observableArrayList(
							OrderStatus.PENDING_WITH_IMIDATE_DELIVERY, OrderStatus.APPROVE_WITH_IMIDATE_DELIVERY,
							OrderStatus.CANCEL_WITH_DELIVERY);
					screen.getComboStatus().setItems(list);
					orderPreviewDeliveryImidate.add(screen);
					break;
				}
				case CANCEL_ORDER_BY_CUSTOMER: {
					ObservableList<OrderStatus> list = FXCollections.observableArrayList(
							OrderStatus.APPROVE_ORDER_CANCELATION, OrderStatus.DECLINE_ORDER_CANCELATION);
					screen.getComboStatus().setItems(list);
					orderPreviewCancel.add(screen);
					break;

				}
				case CANCEL_ORDER_DELIVERY_BY_CUSTOMER: {
					ObservableList<OrderStatus> list = FXCollections.observableArrayList(
							OrderStatus.APPROVE_ORDER_DELIVERY_CANCELATION,
							OrderStatus.DECLINE_ORDER_DELIVERY_CANCELATION);
					screen.getComboStatus().setItems(list);
					orderPreviewCancelDelivery.add(screen);
					break;
				}
				default:
					break;
				}

			}
			orderPreviews.addAll(Arrays.asList(orderPreviewOrdinary, orderPreviewDelivery, orderPreviewDeliveryImidate,
					orderPreviewCancel, orderPreviewCancelDelivery));
		}
		return orderPreviews;
	}

	/**
	 * this method send to the server mission to get all customer users that need to
	 * be notify.
	 * 
	 * @param details
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<List<User>> getCutomersUserDetails(List<List<String>> details) {
		TransmissionPack tp = new TransmissionPack(Mission.GET_CUTOMER_TO_NOTIFY, null, details); // The user is Branch
																									// manager
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.GET_ALL_CUTOMERS_TO_NOTIFIY_SUCCEED) {
			return (List<List<User>>) tp.getInformation();
		}
		return null;
	}

	/**
	 * in this method we loading the right user screen by using trampled method -
	 * pattern , we using down-cast and then using the toString of the subClass of
	 * the user.
	 * 
	 * @param event
	 * @param tp
	 * @throws Exception
	 */
	private static void loadTheRightScreen(MouseEvent event, TransmissionPack tp) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ClientController.user = (User) tp.getInformation();
		switch (tp.getInformation().toString()) {
		case "Customer": {
			CustomerPageController menu = new CustomerPageController();
			menu.start(primaryStage);
			break;
		}
		case "Branch Manager": {
			BranchManagerPageController menu = new BranchManagerPageController();
			menu.start(primaryStage);
			break;
		}
		case "Customer Service": {
			CustomerServicePageController menu = new CustomerServicePageController();
			menu.start(primaryStage);
			break;
		}
		case "Delivery Agent": {
			DeliveryAgentPageController menu = new DeliveryAgentPageController();
			menu.start(primaryStage);
			break;
		}
		case "Marketing Worker": {
			MarketingWorkerOpeningController menu = new MarketingWorkerOpeningController();
			menu.start(primaryStage);
			break;
		}
		case "Network Manager": {
			NetworkManagerPageController menu = new NetworkManagerPageController();
			menu.start(primaryStage);
			break;
		}

		case "Service Expert": {
			ServiceExpertPageController menu = new ServiceExpertPageController();
			menu.start(primaryStage);
			break;
		}
		case "Shop Worker": {
			ShopWorkerPageController menu = new ShopWorkerPageController();
			menu.start(primaryStage);
			break;
		}

		}

	}

	/**
	 * this method sending request to the server to get all the catalog products, if
	 * its success we return the products on list of products else we return null
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "incomplete-switch" })
	public static List<Product> getDataProduct() {
		TransmissionPack tp = new TransmissionPack(Mission.DATA_PRODUCTS, null, null);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();

		switch (tp.getResponse()) {
		case GET_DATA_PRODUCTS: {
			return (List<Product>) tp.getInformation();
		}

		case FAILD_DATA_PRODUCTS: {
			return null;
		}
		}
		return null;
	}

	/**
	 * this method sending request to the server to get all the catalog products by
	 * using filter, if its success we return the products on list of products else
	 * we return null
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "incomplete-switch" })
	public static List<Product> getDataProductByFilter(String color, String price, String type) {
		TransmissionPack tp = new TransmissionPack(Mission.DATA_PRODUCTS_BY_FILTER, null, null);
		// initilaze filters
		Map<String, String> filters = new HashMap<>();
		filters.put("color", color);
		filters.put("price", price);
		filters.put("type", type);

		// send to server side and get information.
		tp.setInformation(filters);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();

		switch (tp.getResponse()) {
		case GET_DATA_PRODUCTS_BY_FILTER: {
			return (List<Product>) tp.getInformation();
		}

		case FAILD_DATA_PRODUCTS_BY_FILTER: {
			return null;
		}
		}
		return null;
	}

	/**
	 * this method sending to the server mission to get the shopWorkers , we
	 * returning the shop worker as list.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ShopWorker> getShopWorkers() {
		
		TransmissionPack tp = new TransmissionPack(Mission.GET_SHOP_WORKERS, null, ClientController.user);
		ClientUI.chat.accept(tp);// tp sent to server and list of workers in the specific branch enters the tp
		tp = ClientUI.chat.getObj();
		if(tp.getInformation() !=null)
		return (List<ShopWorker>) tp.getInformation();
		else {
			return new ArrayList<>();
		}
	}

	/**
	 * this method sending request to Get all the customers who has PENDING_APPROVAL
	 * status
	 * 
	 * @return return the list of those customers
	 */
	@SuppressWarnings("unchecked")
	public static List<Customer> getPendingCustomers() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_PENDING_CUSTOMERS, null, ClientController.user);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return (List<Customer>) tp.getInformation();
	}

	/**
	 * this method Send an updated customer after changed his status and added him
	 * credit card, to the DB
	 * 
	 * @param updatedCustomer
	 */
	public static boolean approveNewCustomer(Customer updatedCustomer) {
		TransmissionPack tp = new TransmissionPack(Mission.APPROVE_NEW_CUSTOMER, null, updatedCustomer); // The user is
																											// Branch
																											// manager
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.APPROVE_NEW_CUSTOMER_SUCCESS) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get a list of the Credit cards we have in the DB
	 * 
	 * @return return the list of the credit cards numbers
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getCreditCards() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_CREDIT_CARDS, null, ClientController.user);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return (List<String>) tp.getInformation();

	}

	/**
	 * this method sending mission to the server to get all the approved customers
	 * we returning it as a arraylist
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Customer> getApprovedCustomers() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_APPROVED_CUSTOMERS, null, ClientController.user);
		ClientUI.chat.accept(tp);// tp sent to server through accept method, there its going through mission
									// analyze
		tp = ClientUI.chat.getObj();// the tp updated after the quarry and saved
		if (tp.getResponse() == Response.CUSTOMER_ARRIVED)
			return (List<Customer>) tp.getInformation();
		else
			return new ArrayList<Customer>();
	}

	/**
	 * the method sends a list of Customers from the list of CustomerPreview it gets, with the request to edit the changes on the DB 
	 *
	 */
	public static boolean sendEditedCustomersFromBranchManager(
			ObservableList<CustomersPreview> customersListView) { 
		List<Customer> customersToSendToDB = new ArrayList<>();// we can't send list of previewCustomers to DB so we
																// create customers list

		for (CustomersPreview cp : customersListView) {
			customersToSendToDB.add(new Customer(cp.getID(), cp.getFirstName(), cp.getLastName(), cp.getEmail(),
					cp.getPhoneNumber(), cp.getAccountStatusCB().getValue(), cp.getIsLoggedIn(), cp.getBalance(),
					cp.getIsNewCustomer(), cp.getCreditCard()));

		}
		TransmissionPack tp = new TransmissionPack(Mission.UPDATE_EDITED_CUSTOMERS, null, customersToSendToDB);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.CUSTOMER_EDITS_UPDATED)
			return true;
		else
			return false;
	}
	/**
	 * the method sends a list of workers from the list of workersPreview it gets, with the request to edit the changes on the DB 
	 *
	 */
	public static boolean sendEditedWorkersFromBranchManager(ObservableList<WorkersPreview> workersListView) {
																											
		List<ShopWorker> workersToSendToDB = new ArrayList<>();// we can't send list of previewWorkers to DB so we
																// create workers list

		for (WorkersPreview wp : workersListView) {
			workersToSendToDB.add(new ShopWorker(wp.getID(), wp.getFirstName(), wp.getLastName(), wp.getEmail(),
					wp.getPhoneNumber(), wp.getAccountStatus(), wp.getIsLoggedIn(), wp.getBranchID(),
					wp.getActivityStatusCB().getValue()));

		}
		TransmissionPack tp = new TransmissionPack(Mission.UPDATE_EDITED_WORKERS, null, workersToSendToDB);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.WORKER_EDITS_UPDATED)
			return true;
		else
			return false;

	}

	/**
	 * sending mission the the server for get all the complaints of the specific
	 * Customer Service employee
	 * 
	 * @param ID-CustomerSerive ID
	 * @return- list of Complaints of Complaints for the screen
	 */
	@SuppressWarnings("unchecked")
	public static List<ComplaintPreview> getComplaintsForCustomerService(String ID) {
		System.out.println(ID);
		TransmissionPack tp = new TransmissionPack(Mission.GET_COMPLAINTS, null, ID);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		List<Complaint> complaints = (List<Complaint>) tp.getInformation();
		List<ComplaintPreview> complainPreview = new ArrayList<>();
		for (Complaint c : complaints) {
			ComplaintPreview cp = new ComplaintPreview(c.getComplaintID(), c.getCustomerID(), c.getOrderID(),
					c.getCustomerServiceID(), c.getDescription(), c.getBranchID(), c.getComplaintOpening(),
					c.getTreatmentUntil(), c.getComplainState(), c.getComplaintsStatus());
			cp.getStatus().setValue(c.getComplainState());
			complainPreview.add(cp);
		}
		ComplaintsDataHandle.setComlaints(complainPreview);

		return complainPreview;
	}

	/**
	 * sending mission to the server to update all the complaints cases and if there
	 * is refund amount it will create new refund row in the refund row and update
	 * to the specific customer his new balance
	 * 
	 * @param complaintsUpdate
	 * @return
	 */
	public static Response updateComplaints(List<ComplaintPreview> complaintsUpdate) {

		List<ComplaintPreview> complainPreview = complaintsUpdate;
		List<Complaint> complain = new ArrayList<>();
		for (ComplaintPreview c : complainPreview) {
			Complaint cp = new Complaint(c.getComplaintID(), c.getCustomerID(), c.getOrderID(),
					c.getCustomerServiceID(), c.getDescription(), c.getBranchID(), c.getComplaintOpening(),
					c.getTreatmentUntil(), c.getComplainState(), c.getComplaintsStatus());
			cp.setComplainState(c.getStatus().getValue());
			System.out.println(c.getRefoundAmount());
			if (c.getRefoundAmount() != null) {
				cp.setRefoundAmount(c.getRefoundAmount());
			}
			complain.add(cp);
		}

		TransmissionPack tp = new TransmissionPack(Mission.UPDATE_COMPLAINTS, null, complain);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return tp.getResponse();

	}

	/**
	 * create new mission to the server to create new complaint case
	 * 
	 * @param c
	 * @return
	 */
	public static Response createComplaint(Complaint c) {
		TransmissionPack tp = new TransmissionPack(Mission.OPEN_COMPLAINT, null, c);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		
		return tp.getResponse();

	}

	/**
	 * in this method we getting the monthly report and convert it into string to be
	 * able to use it and present it into the screen.
	 */

	public static boolean getMonthlyReport(String branchID, String year, String month, String reportType) {
		if (year != null && month != null && reportType != null) {
			List<String> reportRequest = new ArrayList<>();
			Report returndReport;
			reportRequest.addAll(Arrays.asList(branchID, year, month, reportType));
			TransmissionPack tp = new TransmissionPack(Mission.GET_MONTHLY_REPORT, null, reportRequest);
			ClientUI.chat.accept(tp);
			tp = ClientUI.chat.getObj();
			if (tp.getInformation() == null) {
					tp.setResponse(Response.NOT_FOUND_MONTHLY_REPORT);
			} else {

				returndReport = (Report) tp.getInformation();

				ByteArrayInputStream stream = new ByteArrayInputStream(returndReport.getBlob());
				InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
				BufferedReader bufferedReader = new BufferedReader(streamReader);
				List<List<String>> reportOnList = new ArrayList<>();
				String line;
				try {

					while ((line = bufferedReader.readLine()) != null) {

						List<String> row = new ArrayList<String>(Arrays.asList(line.split(" ")));
						reportOnList.add(row);

					}
					ReportHandleController.setOrdersReportOnListMonth(reportOnList);

					ClientUI.chat.getObj().setInformation(returndReport);
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			ClientUI.chat.getObj().setInformation(null);
			return false;
		} else {
			ClientUI.chat.getObj().setInformation(null);
			return false;
		}
	}

	/*
	 * this method send mission to the server to get all branches
	 * 
	 * @return list of Enum Branches
	 * 
	 * @author Almog Madar
	 */
	@SuppressWarnings({ "incomplete-switch", "unchecked" })
	public static List<Branches> getBranches() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_BRANCHES, null, null);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();

		switch (tp.getResponse()) {
		case FOUND_BRANCHES: {
			return (List<Branches>) tp.getInformation();
		}

		case NOT_FOUND_BRANCHES: {
			return new ArrayList<Branches>();
		}
		}
		return new ArrayList<Branches>();
	}

	/*
	 * this method send mission to the server  Get Specific Product In branch .
	 * 
	 * @param Branches - specific branch
	 * 
	 * @author Almog Madar
	 */

	@SuppressWarnings({ "unchecked", "incomplete-switch" })
	public static List<ProductInBranch> getProductInSpecificBranch(Branches branch) {
		TransmissionPack tp = new TransmissionPack(Mission.GET_PRODUCT_IN_BRANCH, null, branch);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();

		switch (tp.getResponse()) {
		case FOUND_PRODUCT_IN_BRANCH: {
			return (List<ProductInBranch>) tp.getInformation();
		}

		case NOT_FOUND_PRODUCT_IN_BRANCH: {
			return new ArrayList<ProductInBranch>();
		}
		}
		return new ArrayList<ProductInBranch>();

	}
/**
 * in this method we sending to the server mission to add new delivery into the DB.
 * @param deliveryID
 * @param orderID
 * @param branchName
 * @param orderDate
 * @param expectedDelivery
 * @param reciverName
 * @param address
 * @param phoneNumber
 * @return
 */
	@SuppressWarnings("incomplete-switch")
	public static boolean addDelivery(int deliveryID, int orderID, Branches branchName, String orderDate,
			String expectedDelivery, String reciverName, String address, String phoneNumber) {
		// TODO Auto-generated method stub
		Deliveries deliveries = new Deliveries(deliveryID, String.valueOf(orderID),
				String.valueOf(branchName.getNumber()), ClientController.user.getID(),
				OrderHandleController.getShippingPrice(), orderDate, expectedDelivery, "", reciverName, address,
				phoneNumber, DeliveryStatus.WAIT_FOR_MANAGER_APPROVED, new ArrayList<ProductInOrder>());

		TransmissionPack tp = new TransmissionPack(Mission.ADD_DELIVERY, null, deliveries);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		switch (tp.getResponse()) {
		case ADD_DELIVERY_SUCCEED: {
			return true;
		}

		case FAILD_ADD_DELIVERY: {
			return false;
		}
		}

		return false;
	}
/**
 * this method send mission to the server to get all the customer orders that can be canceld
 * @return
 */
	@SuppressWarnings({ "incomplete-switch", "unchecked" })
	public static List<Order> getCustomerOrdersForCancaltion() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_CUSTOMER_ORDERS_CANCELATION, null,
				ClientController.user.getID());
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		switch (tp.getResponse()) {
		case GET_CUSTOMER_ORDERS_SUCCESS: {
			return (List<Order>) tp.getInformation();
		}

		case GET_CUSTOMER_ORDERS_FAILD: {
			return new ArrayList<Order>();
		}
		}

		return new ArrayList<Order>();
	}
/**
 * this method send mission to the server  to gett the customer order history.
 * @return
 */
	@SuppressWarnings({ "incomplete-switch", "unchecked" })
	public static List<Order> getCustomerOrdersHistory() {
		// TODO Auto-generated method stub
		TransmissionPack tp = new TransmissionPack(Mission.GET_CUSTOMER_ORDERS_HISTORY, null,
				ClientController.user.getID());
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		switch (tp.getResponse()) {
		case GET_CUSTOMER_ORDERS_SUCCESS: {
			return (List<Order>) tp.getInformation();
		}

		case GET_CUSTOMER_ORDERS_FAILD: {
			return new ArrayList<Order>();
		}
		}

		return new ArrayList<Order>();
	}
/**
 * this method send mission to the server to update the orders status
 * @param order
 * @return
 */
	public static Response updateOrders(List<Order> order) {
		TransmissionPack tp = new TransmissionPack(Mission.UPADTE_ORDER, null, order);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return tp.getResponse();
	}

	/**
	 * this method send mission to the server get the question survey from the DB
	 */
	@SuppressWarnings("unchecked")
	public static List<Question> getSurveyQuestion() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_SURVEY_QUESTIONS, null, SurveyHandle.getTopic());
		ClientUI.chat.accept(tp);
		System.out.println("here bofore query");
		tp = ClientUI.chat.getObj();
		System.out.println(tp);
		return (List<Question>) tp.getInformation();
		

	}
/**
 * this method send mission to the server to insert new survey result into the DB
 * @param question
 * @return
 */
	public static Response insertSurvey(List<Question> question) {
		TransmissionPack tp = new TransmissionPack(Mission.INSERT_SURVY, null, question);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return tp.getResponse();

	}

	/**
	 * 
	 * this method send mission to the server get the list of deliveries that are still not arrived to the customers
	 * 
	 * @return the list of this customers
	 */
	@SuppressWarnings("unchecked")
	public static List<DeliveryPreview> getDeliveries(String branchID) {
		TransmissionPack tp = new TransmissionPack(Mission.GET_DELIVERIES, null, branchID); // The user is Branch
																							// manager

		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		List<Deliveries> deliveries = (List<Deliveries>) tp.getInformation();
		List<DeliveryPreview> deliveryPreviews = new ArrayList<>();
		for (Deliveries d : deliveries) {
			DeliveryPreview dp = new DeliveryPreview(d.getDeliveryID(), d.getOrderID(), d.getBranchID(),
					d.getCustomerID(), d.getPrice(), d.getOrderDate(), d.getExpectedDelivery(), d.getArrivedDate(),
					d.getReceiverName(), d.getAddress(), d.getPhoneNumber(), d.getDeliveryStatus(),
					d.getOrderProducts());
			dp.getDeliveryStatusComboBox().setValue(d.getDeliveryStatus());
			deliveryPreviews.add(dp);
		}
		return deliveryPreviews;
	}

	/**
	 * Send to DB the updated statuses of the deliveries and then got back the
	 * response if the update was a success of not.
	 * 
	 * @param if the update was as success or a failure.
	 * @return
	 */
	public static Response UpdateDeliveriesStatus(List<DeliveryPreview> deliveriesList) {
		List<DeliveryPreview> deliveryPreviews = deliveriesList;
		List<Deliveries> deliveries = new ArrayList<>();
		for (DeliveryPreview dp : deliveryPreviews) {
			Deliveries d = new Deliveries(dp.getDeliveryID(), dp.getOrderID(), dp.getBranchID(), dp.getCustomerID(),
					dp.getPrice(), dp.getOrderDate(), dp.getExpectedDelivery(), dp.getArrivedDate(),
					dp.getReceiverName(), dp.getAddress(), dp.getPhoneNumber(), dp.getDeliveryStatus(),
					dp.getOrderProducts());
			deliveries.add(d);
		}
		TransmissionPack tp = new TransmissionPack(Mission.UPDATE_DELIVERIES_STATUSES, null, deliveries);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return tp.getResponse();
	}

	/*
	 * in this method we getting the quarter income report according to the branchId
	 * year and quarter , we sending the order into the server by using our
	 * transmissionpack
	 */
	public static boolean getQuarterIncomeReport(String branchID, String year, String quarter) {
		if (year != null && quarter != null && branchID != null) {
			List<String> reportRequest = new ArrayList<>();
			Report returndReport;
			reportRequest.addAll(Arrays.asList(branchID, year, quarter));
			TransmissionPack tp = new TransmissionPack(Mission.GET_QUARTER_INCOME_REPORT, null, reportRequest);
			ClientUI.chat.accept(tp);
			tp = ClientUI.chat.getObj();
			if (tp.getInformation() == null) {
				return false;
			} else {
				returndReport = (Report) tp.getInformation();

				ByteArrayInputStream stream = new ByteArrayInputStream(returndReport.getBlob());
				InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
				BufferedReader bufferedReader = new BufferedReader(streamReader);
				List<List<String>> reportOnList = new ArrayList<>();
				String line;
				try {
					while ((line = bufferedReader.readLine()) != null) {

						List<String> row = new ArrayList<String>(Arrays.asList(line.split(" ")));
						reportOnList.add(row);

					}
					ReportHandleController.setOrdersReportOnListQuarter(reportOnList);

					ClientUI.chat.getObj().setInformation(returndReport);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			ClientUI.chat.getObj().setInformation(null);
			return false;
		} else {
			ClientUI.chat.getObj().setInformation(null);
			return false;
		}
	}
/**
 * this method send mission to the server to get branch id
 * @return
 */
	public static String getBranchID() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_BRANCHID_BY_USER, null, ClientController.user);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return (String) tp.getInformation();

	}

	/**
	 * in this method we sending mission to the server to get all the years in specific duration and from spesific table.
	 * @param Duration
	 * @param Table
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getYearsForComboBox(String Duration, String Table) {
		List<String> getYears = new ArrayList<>();
		List<String> returnedYears = new ArrayList<>();
		getYears.add(Duration);
		getYears.add(Table);
		TransmissionPack tp = new TransmissionPack(Mission.GET_YEARS_FOR_COMOBOX, null, null);
		tp.setInformation(getYears);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		returnedYears = (List<String>) tp.getInformation();
		return returnedYears;

	}
/**
 * in this method we sending mission to the server to get service report 
 * @param BranchID
 * @param Year
 * @param Month
 * @param surveyType
 * @return
 */
	@SuppressWarnings("unchecked")
	public static boolean getServiceReport(String BranchID, String Year, String Month, String surveyType) {
		if (BranchID != null && Year != null && Month != null) {
			List<String> reportRequest = new ArrayList<>();
			reportRequest.addAll(Arrays.asList(BranchID, Year, Month, surveyType));
			TransmissionPack tp = new TransmissionPack(Mission.GET_SURVEY_REPORT, null, reportRequest);
			ClientUI.chat.accept(tp);
			tp = ClientUI.chat.getObj();
			if (tp.getResponse() == Response.SURVEY_REPORT_NOT_FOUND) {
				return false;
			} else {
				ReportHandleController.setSurveyReportResult((List<List<String>>) tp.getInformation());
				return true;
			}

		}
		return false;
	}
/**
 * in this method we sending mission to the server to get complaints quarter report
 * @param branchID
 * @param Quarter
 * @param Year
 * @return
 */
	public static boolean getComliantsQuarterlyReport(String branchID, String Quarter, String Year) {
		if (Year != null && Quarter != null && branchID != null) {
			List<String> reportRequest = new ArrayList<>();
			Report returndReport;
			reportRequest.addAll(Arrays.asList(branchID, Year, Quarter));
			TransmissionPack tp = new TransmissionPack(Mission.GET_QUARTER_COMPLAINTS_REPORT, null, reportRequest);
			ClientUI.chat.accept(tp);
			tp = ClientUI.chat.getObj();
			if (tp.getInformation() == null) {
				return false;
			} else {
				returndReport = (Report) tp.getInformation();

				ByteArrayInputStream stream = new ByteArrayInputStream(returndReport.getBlob());
				InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
				BufferedReader bufferedReader = new BufferedReader(streamReader);
				List<List<String>> reportOnList = new ArrayList<>();
				String line;
				try {
					while ((line = bufferedReader.readLine()) != null) {

						List<String> row = new ArrayList<String>(Arrays.asList(line.split(" ")));
						reportOnList.add(row);

					}
					ReportHandleController.setComplaintsReportResult(reportOnList);

					ClientUI.chat.getObj().setInformation(reportOnList);
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			ClientUI.chat.getObj().setInformation(null);
			return false;
		} else {
			ClientUI.chat.getObj().setInformation(null);
			return false;
		}

	}


	/**
	 * give full refund to the customer that his delivery was late we send here to
	 * the server the delivery details that we will update in the DB his refund.
	 * 
	 * @param delivery
	 */
	public static Response DeliveryWasLateRefund(DeliveryPreview delivery) {
		DeliveryPreview dp = delivery;
		Deliveries d = new Deliveries(dp.getDeliveryID(), dp.getOrderID(), dp.getBranchID(), dp.getCustomerID(),
				dp.getPrice(), dp.getOrderDate(), dp.getExpectedDelivery(), dp.getArrivedDate(), dp.getReceiverName(),
				dp.getAddress(), dp.getPhoneNumber(), DeliveryStatus.ARRIVED, dp.getOrderProducts());
		// System.out.println(dp.getDeliveryStatusComboBox());
		System.out.println(d.getDeliveryStatus());
		TransmissionPack tp = new TransmissionPack(Mission.DELIVERY_LATE_REFUND, null, d);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return tp.getResponse();
	}

	public static String getBranchName(String branchID) {
		if (branchID == null) {
			return null;
		}

		TransmissionPack tp = new TransmissionPack(Mission.GET_BRANCH_NAME_BY_ID, null, branchID);

		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		return (String) tp.getInformation();
	}

	/**
	 * this method sending mission request to the server to cancel order request by customer and create cancellation object ;
	 * 
	 * @param orderID
	 * @return
	 */
	@SuppressWarnings("incomplete-switch")
	public static boolean customerCancelOrder(Cancellation info) {

		TransmissionPack tp = new TransmissionPack(Mission.CANCEL_ORDER_BY_CUSTOMER, null, info);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		switch (tp.getResponse()) {
		case CANCEL_ORDER_BY_CUSTOMER_SUCCESS: {
			return true;
		}

		case CANCEL_ORDER_BY_CUSTOMER_FAILD: {
			return false;
		}
		}

		return false;
	}
	/**
	 * this method sending mission request to the server to get canellationRequestOrder
	 * @return
	 */
	@SuppressWarnings({ "incomplete-switch", "unchecked" })
	public static List<Order> getCancellationRequetsOrder() {
		// TODO Auto-generated method stub
		TransmissionPack tp = new TransmissionPack(Mission.GET_CUSTOMER_ORDERS_WAITING_CANCELATION, null,
				ClientController.user.getID());
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		switch (tp.getResponse()) {
		case GET_CUSTOMER_ORDERS_SUCCESS: {
			return (List<Order>) tp.getInformation();
		}

		case GET_CUSTOMER_ORDERS_FAILD: {
			return new ArrayList<Order>();
		}
		}

		return new ArrayList<Order>();
	}
/**
 * this method sending mission request to the server to get customer details 
 * @param customerID
 * @return
 */
	@SuppressWarnings("unchecked")
	public static List<String> getCustomerDetails(String customerID) {
		if (customerID == null) {
			return null;
		}
		TransmissionPack tp = new TransmissionPack(Mission.GET_CUSTOMER_DETAILS, null, customerID);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.GET_CUSTOMER_DETAILS_SUCCESS) {
			return (List<String>) tp.getInformation();
		}
		return null;
	}
/**
 * this method sending mission request to the server to get max product id from the DB
 * @return
 */
	public static String getMaxProductID() {
		TransmissionPack tp = new TransmissionPack(Mission.GET_MAX_PRODUCT_ID, null, null);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.GET_MAX_PRODUCT_ID_SUCCESS) {
			return (String) tp.getInformation();
		}
		return null;
	}
/**
 * this method sending mission request to the server to remove items from the catalog
 * @param removeProductsList
 * @return
 */
	public static boolean RemoveProductsFromCatalog(List<String> removeProductsList) {
		if (removeProductsList == null)
			return false;
		TransmissionPack tp = new TransmissionPack(Mission.REMOVE_PRODUCTS_FROM_CATALOG, null, removeProductsList);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.REMOVE_FROM_THE_CATALOG_SUCCESS) {
			return true;
		}
		return false;
	}
/**
 * this method sending mission request to the server to add new products to the catalog 
 * @param addProductsList
 * @return
 */
	public static boolean AddNewProductsToCatalog(List<Product> addProductsList) {
		if (addProductsList == null)
			return false;
		TransmissionPack tp = new TransmissionPack(Mission.ADD_PRODUCTS_TO_CATALOG, null, addProductsList);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.ADDING_TO_THE_CATALOG_SUCCESS) {
			return true;
		}
		return false;

	}

 /**
  * this method sending mission request to the server to  edit the products in the catalog 
  * @param editedProductsList
  * @return
  */
	public static boolean EditProductsInCatalog(List<Product> editedProductsList) {
		if (editedProductsList == null)
			return false;
		TransmissionPack tp = new TransmissionPack(Mission.EDIT_PRODUCTS_IN_CATALOG, null, editedProductsList);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		if (tp.getResponse() == Response.EDIT_PRODUCTS_ON_THE_CATALOG_SUCCESS) {
			return true;
		}
		return false;

	}
	
	/**
	 * this method loading popUp .
	 */
		public static void popUp(String body, String title) {
			GenaralPopUpController popUp = new GenaralPopUpController();
			popMessageHandler.setMessage(body);
			popMessageHandler.setTitle(title);
			Stage primaryStage = new Stage();
			try {
				popUp.start(primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
