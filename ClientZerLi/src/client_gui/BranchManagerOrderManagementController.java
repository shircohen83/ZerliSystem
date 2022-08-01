package client_gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.EmailSending;
import client.OrderHandleController;
import communication.Response;
import entities_general.Order;
import entities_general.OrderPreview;
import entities_users.BranchManager;
import entities_users.Customer;
import entities_users.User;
import enums.OrderStatus;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class Description:
 * this class is handle the branch manager order screen controller the branch
 * manager can approve or cancel the request.
 * if the order was approve or the cancel request of the order was approve
 * by the branch manager the system will sent them an email to notice them of about
 * there request status
 * 
 * @author Mor Ben Haim
 * @author Dvir Bublil
 *
 */
public class BranchManagerOrderManagementController implements Initializable {

	@FXML
	private Button Back;
	@FXML
	private Label upadteFeedBack;

	@FXML
	private TableView<OrderPreview> Orders;

	@FXML
	private TableColumn<OrderPreview, String> branchIDCol;

	@FXML
	private TableColumn<OrderPreview, String> customerIDCol;

	@FXML
	private TableColumn<OrderPreview, String> expectedDeliveryCol;

	@FXML
	private TableColumn<OrderPreview, String> orderDateCol;

	@FXML
	private TableColumn<OrderPreview, String> orderIDCol;

	@FXML
	private TableColumn<OrderPreview, String> priceCol;

	@FXML
	private TableColumn<OrderPreview, Button> showCol;

	@FXML
	private TableColumn<OrderPreview, ComboBox<OrderStatus>> statusCol;

	private ObservableList<OrderPreview> listView = FXCollections.observableArrayList();

	@FXML
	private Label upadteFeedBackD;

	@FXML
	private TableView<OrderPreview> OrdersDelivery;

	@FXML
	private TableColumn<OrderPreview, String> branchIDColD;

	@FXML
	private TableColumn<OrderPreview, String> customerIDColD;

	@FXML
	private TableColumn<OrderPreview, String> expectedDeliveryColD;

	@FXML
	private TableColumn<OrderPreview, String> orderDateColD;

	@FXML
	private TableColumn<OrderPreview, String> orderIDColD;

	@FXML
	private TableColumn<OrderPreview, String> priceColD;

	@FXML
	private TableColumn<OrderPreview, Button> showColD;

	@FXML
	private TableColumn<OrderPreview, ComboBox<OrderStatus>> statusColD;

	private ObservableList<OrderPreview> listViewD = FXCollections.observableArrayList();
	
	@FXML
	private TableView<OrderPreview> OrdersDeliveryImdidate;

	@FXML
	private TableColumn<OrderPreview, String> branchIDColDI;

	@FXML
	private TableColumn<OrderPreview, String> customerIDColDI;

	@FXML
	private TableColumn<OrderPreview, String> expectedDeliveryColDI;

	@FXML
	private TableColumn<OrderPreview, String> orderDateColDI;

	@FXML
	private TableColumn<OrderPreview, String> orderIDColDI;

	@FXML
	private TableColumn<OrderPreview, String> priceColDI;

	@FXML
	private TableColumn<OrderPreview, Button> showColDI;

	@FXML
	private TableColumn<OrderPreview, ComboBox<OrderStatus>> statusColDI;

	private ObservableList<OrderPreview> listViewDI = FXCollections.observableArrayList();

	@FXML
	private Label upadteFeedBackC;

	@FXML
	private TableView<OrderPreview> OrdersCancel;

	@FXML
	private TableColumn<OrderPreview, String> branchIDColC;

	@FXML
	private TableColumn<OrderPreview, String> customerIDColC;

	@FXML
	private TableColumn<OrderPreview, String> expectedDeliveryColC;

	@FXML
	private TableColumn<OrderPreview, String> orderDateColC;

	@FXML
	private TableColumn<OrderPreview, String> orderIDColC;

	@FXML
	private TableColumn<OrderPreview, String> priceColC;

	@FXML
	private TableColumn<OrderPreview, Button> showColC;

	@FXML
	private TableColumn<OrderPreview, ComboBox<OrderStatus>> statusColC;

	private ObservableList<OrderPreview> listViewC = FXCollections.observableArrayList();

	@FXML
	private Label upadteFeedBackCD;

	@FXML
	private TableView<OrderPreview> OrdersDeliveryCancel;

	@FXML
	private TableColumn<OrderPreview, String> branchIDColCD;

	@FXML
	private TableColumn<OrderPreview, String> customerIDColCD;

	@FXML
	private TableColumn<OrderPreview, String> expectedDeliveryColCD;

	@FXML
	private TableColumn<OrderPreview, String> orderDateColCD;

	@FXML
	private TableColumn<OrderPreview, String> orderIDColCD;

	@FXML
	private TableColumn<OrderPreview, String> priceColCD;

	@FXML
	private TableColumn<OrderPreview, Button> showColCD;
	
    @FXML
    private Label timer;

	@FXML
	private TableColumn<OrderPreview, ComboBox<OrderStatus>> statusColCD;
	@FXML
    private TabPane tabPane;
	
    @FXML
    private Label accountStatus;
    
    @FXML
    private Label branchLabel;
    
    @FXML
    private Label employeeName;
    
    @FXML
    private Label employeeType;
    

    @FXML
    private Label entryGreeting;
    
    @FXML
    private Label phoneNumber;
    
    

	private ObservableList<OrderPreview> listViewCD = FXCollections.observableArrayList();
	

	@FXML
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/RequestManagementPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Order Management");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@FXML
	void Back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		BranchManagerPageController branchManagerPage = new BranchManagerPageController();
		branchManagerPage.start(primaryStage);
	}

	/**
	 * initialize the order page that is in pending status
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//timer on screen 
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
		
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((BranchManager) ClientController.user).toString());
		
		String branchID = ((BranchManager) ClientController.user).getBranchID();
		String branchName = ClientHandleTransmission.getBranchName(branchID);
		branchLabel.setText(" " + branchName + " (" + branchID + ")");
		
		initOrdinaryOrderTable();
		initDeliveryOrderTable();
		initDeliveryImidateOrderTable();
		initCanelOrdersTable();
		initCancelDeliveryOrdersTable();
		addUpdateFeedBackRest();
		

	}
	/**
	 * add listener to the tabPane it reset the text label of update feedback when the tab is change
	 */
	private void addUpdateFeedBackRest() {
		tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
	        upadteFeedBack.setText("");
	    });
	}
	
	  

	private void initCancelDeliveryOrdersTable() {
		showColCD.setCellFactory(ShowButtonTableCell.<OrderPreview>forTableColumn("Details", (OrderPreview o) -> {

			List<OrderPreview> details = OrderHandleController.getOrdersForBranchManagerCD();
			for (OrderPreview or : details) {
				if (or.equals(o)) {
					OrderHandleController.setOrder(or);
					System.out.println(or);
					break;
				}
			}

			
			Stage primaryStage = new Stage();
			BranchManagerOrderDetailsController ordersDetails = new BranchManagerOrderDetailsController();

			try {

				ordersDetails.start(primaryStage);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return o;
		}));
		
		statusColCD.setCellValueFactory(new PropertyValueFactory<OrderPreview, ComboBox<OrderStatus>>("comboStatus"));
		orderIDColCD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderID"));
		customerIDColCD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("customerID"));
		branchIDColCD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("branchID"));
		priceColCD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("price"));
		orderDateColCD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderDate"));
		expectedDeliveryColCD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("expectedDelivery"));

		OrderHandleController.setOrdersForBranchManagerCD(ClientHandleTransmission.getListOrderPreview().get(4));
		listViewCD.addAll(OrderHandleController.getOrdersForBranchManagerCD());
		OrdersDeliveryCancel.setItems(listViewCD);
	}

	private void initCanelOrdersTable() {
		showColC.setCellFactory(ShowButtonTableCell.<OrderPreview>forTableColumn("Details", (OrderPreview o) -> {

			List<OrderPreview> details = OrderHandleController.getOrdersForBranchManagerC();
			for (OrderPreview or : details) {
				if (or.equals(o)) {
					OrderHandleController.setOrder(or);
					System.out.println(or);
					break;
				}
			}

			// open screen of details -- > need to init before starting
			Stage primaryStage = new Stage();
			BranchManagerOrderDetailsController ordersDetails = new BranchManagerOrderDetailsController();

			try {

				ordersDetails.start(primaryStage);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return o;
		}));

		statusColC.setCellValueFactory(new PropertyValueFactory<OrderPreview, ComboBox<OrderStatus>>("comboStatus"));
		orderIDColC.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderID"));
		customerIDColC.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("customerID"));
		branchIDColC.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("branchID"));
		priceColC.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("price"));
		orderDateColC.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderDate"));
		expectedDeliveryColC.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("expectedDelivery"));



		OrderHandleController.setOrdersForBranchManagerC(ClientHandleTransmission.getListOrderPreview().get(3));
		listViewC.addAll(OrderHandleController.getOrdersForBranchManagerC());
		OrdersCancel.setItems(listViewC);
	}
	
	private void initDeliveryImidateOrderTable() {
		// show button function
		showColDI.setCellFactory(ShowButtonTableCell.<OrderPreview>forTableColumn("Details", (OrderPreview o) -> {


			List<OrderPreview> details = OrderHandleController.getOrdersForBranchManagerDI();
			for (OrderPreview or : details) {
				if (or.equals(o)) {
					OrderHandleController.setOrder(or);
					System.out.println(or);
					break;
				}
			}

			
			Stage primaryStage = new Stage();
			BranchManagerOrderDetailsController ordersDetails = new BranchManagerOrderDetailsController();

			try {

				ordersDetails.start(primaryStage);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return o;
		}));

		statusColDI.setCellValueFactory(new PropertyValueFactory<OrderPreview, ComboBox<OrderStatus>>("comboStatus"));
		orderIDColDI.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderID"));
		customerIDColDI.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("customerID"));
		branchIDColDI.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("branchID"));
		priceColDI.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("price"));
		orderDateColDI.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderDate"));
		expectedDeliveryColDI.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("expectedDelivery"));

	

		OrderHandleController.setOrdersForBranchManagerDI(ClientHandleTransmission.getListOrderPreview().get(2));
		listViewDI.addAll(OrderHandleController.getOrdersForBranchManagerDI());
		OrdersDeliveryImdidate.setItems(listViewDI);
	}

	private void initDeliveryOrderTable() {
		// show button function
		showColD.setCellFactory(ShowButtonTableCell.<OrderPreview>forTableColumn("Details", (OrderPreview o) -> {


			List<OrderPreview> details = OrderHandleController.getOrdersForBranchManagerD();
			for (OrderPreview or : details) {
				if (or.equals(o)) {
					OrderHandleController.setOrder(or);
					System.out.println(or);
					break;
				}
			}

			
			Stage primaryStage = new Stage();
			BranchManagerOrderDetailsController ordersDetails = new BranchManagerOrderDetailsController();

			try {

				ordersDetails.start(primaryStage);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return o;
		}));

		statusColD.setCellValueFactory(new PropertyValueFactory<OrderPreview, ComboBox<OrderStatus>>("comboStatus"));
		orderIDColD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderID"));
		customerIDColD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("customerID"));
		branchIDColD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("branchID"));
		priceColD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("price"));
		orderDateColD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderDate"));
		expectedDeliveryColD.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("expectedDelivery"));

	

		OrderHandleController.setOrdersForBranchManagerD(ClientHandleTransmission.getListOrderPreview().get(1));
		listViewD.addAll(OrderHandleController.getOrdersForBranchManagerD());
		OrdersDelivery.setItems(listViewD);
	}

	private void initOrdinaryOrderTable() {
		// show button function
		showCol.setCellFactory(ShowButtonTableCell.<OrderPreview>forTableColumn("+", (OrderPreview o) -> {

			List<OrderPreview> details = OrderHandleController.getOrdersForBranchManager();
			for (OrderPreview or : details) {
				if (or.equals(o)) {
					OrderHandleController.setOrder(or);
					System.out.println(or);
					break;
				}
			}

			// open screen of details -- > need to init before starting
			Stage primaryStage = new Stage();
			BranchManagerOrderDetailsController ordersDetails = new BranchManagerOrderDetailsController();

			try {

				ordersDetails.start(primaryStage);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return o;
		}));

		statusCol.setCellValueFactory(new PropertyValueFactory<OrderPreview, ComboBox<OrderStatus>>("comboStatus"));
		orderIDCol.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderID"));
		customerIDCol.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("customerID"));
		branchIDCol.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("branchID"));
		priceCol.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("price"));
		orderDateCol.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("orderDate"));
		expectedDeliveryCol.setCellValueFactory(new PropertyValueFactory<OrderPreview, String>("expectedDelivery"));

	

		OrderHandleController.setOrdersForBranchManager(ClientHandleTransmission.getListOrderPreview().get(0));
		listView.addAll(OrderHandleController.getOrdersForBranchManager());
		Orders.setItems(listView);
	}

	/**
	 * send mission to the server for updating the orders state
	 * 
	 * @param event
	 */
	@FXML
	void updateBtn(ActionEvent event) {
		List<OrderPreview> orderPreview = OrderHandleController.getOrdersForBranchManager();
		List<OrderPreview>orderPreviewD=OrderHandleController.getOrdersForBranchManagerD();
		List<String>approve=new ArrayList<>();
		List<String>cancel=new ArrayList<>();
		List<List<String>>details=new ArrayList<>();
		List<OrderPreview>orderPreviewDI=OrderHandleController.getOrdersForBranchManagerDI();
		List<OrderPreview>orderPreviewC=OrderHandleController.getOrdersForBranchManagerC();
		List<OrderPreview>orderPreviewCD=OrderHandleController.getOrdersForBranchManagerCD();
		List<List<OrderPreview>>orders=Arrays.asList(orderPreview,orderPreviewD,orderPreviewDI,orderPreviewC,orderPreviewCD);
		List<Order> order = new ArrayList<>();
		for(List<OrderPreview>list:orders) {
		for (OrderPreview or : list) {
			
			Order o = createOrderUpdate(order, or);
			addingCustomerToNotifyLists(approve, cancel, o);
			
		}
		}
		
		sendAllOrderToDB(approve, cancel, details, order);
	}
	/**
	 * send all the orders after there status change or not change and update
	 * the data in the DB
	 * @param approve
	 * @param cancel
	 * @param details
	 * @param order
	 */
	private void sendAllOrderToDB(List<String> approve, List<String> cancel, List<List<String>> details,
			List<Order> order) {
		if (ClientHandleTransmission.updateOrders(order) == Response.UPDATE_ORDER_SUCCEED) {
			upadteFeedBack.setText("Update Succeed");
			upadteFeedBack.setTextFill(Color.GREEN);
			details.addAll(Arrays.asList(approve,cancel));
			try {
	
				List<List<User>>users=ClientHandleTransmission.getCutomersUserDetails(details);
				List<User>userOrderApprove=new ArrayList<>();
				List<User>userOrderCancelApprove=new ArrayList<>();
				/**
				 * return List of user that there orders was approve
				 * by the branch manager
				 */
				userOrderApprove=users.get(0);
				/**
				 * return List of user that there orders cancel was approve
				 * by the branch manager
				 */
				userOrderCancelApprove=users.get(1);
				notifyAllCustomerOrdersApprove(userOrderApprove);
				notifyAllCutomerOrderCancelApprove(userOrderCancelApprove);
			} catch (Exception e) {
				
				e.printStackTrace();
			}

		} else {
			upadteFeedBack.setText("Update Failed");
			upadteFeedBack.setTextFill(Color.RED);
		}
	}
	/**
	 * Create new orders object and adding him to the list
	 * @param order
	 * @param or
	 * @return
	 */
	private Order createOrderUpdate(List<Order> order, OrderPreview or) {
		Order o = new Order(or.getOrderID(), or.getCustomerID(), or.getBranchID(), or.getPrice(),
				or.getGreetingCard(), or.getOrderDate(), or.getExpectedDelivery(), or.getItems());
		o.setStatus(or.getComboStatus().getValue());
		order.add(o);
		return o;
	}
	/**
	 * adding customers to notify list if there order status is equals
	 * to approve order or cancellation of any kind
	 * @param approve
	 * @param cancel
	 * @param o
	 */
	private void addingCustomerToNotifyLists(List<String> approve, List<String> cancel, Order o) {
		if(o.getStatus().equals(OrderStatus.APPROVE) || o.getStatus().equals(OrderStatus.APPROVE_WITH_DELIVERY) || o.getStatus().equals(OrderStatus.APPROVE_WITH_IMIDATE_DELIVERY)) {
			approve.add(o.getCustomerID());
		}
		if(o.getStatus().equals(OrderStatus.APPROVE_ORDER_CANCELATION) || o.getStatus().equals(OrderStatus.APPROVE_ORDER_DELIVERY_CANCELATION)) {
			
			cancel.add(o.getCustomerID());
		}
	}
	/**
	 * send email to all users that there orders request was approve
	 * @param userOrderCancelApprove
	 * @throws Exception
	 */
	private void notifyAllCutomerOrderCancelApprove(List<User> userOrderCancelApprove) throws Exception {
		for(User u:userOrderCancelApprove) {
			EmailSending.sendMail(u.getEmail(),u.getPhoneNumber(),u.getFirstName()+" Order Cancel was Approve ","Order Cancel");
		}
	}
	/**
	 * send email to all users that there orders cancel request was approve
	 * @param userOrderCancelApprove
	 * @throws Exception
	 */
	private void notifyAllCustomerOrdersApprove(List<User> userOrderApprove) throws Exception {
		for(User u:userOrderApprove) {
		EmailSending.sendMail(u.getEmail(),u.getPhoneNumber(),u.getFirstName()+" Order Approve ","Order Approve");
		}
	}

}
