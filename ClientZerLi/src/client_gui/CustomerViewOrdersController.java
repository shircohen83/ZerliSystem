package client_gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import client.ClientController;
import client.ClientHandleTransmission;
import client.OrderHandleController;
import client.popMessageHandler;
import entities_general.Cancellation;
import entities_general.Order;
import entities_users.Customer;
import enums.Branches;
import enums.OrderStatus;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CustomerViewOrdersController implements Initializable{

    @FXML
    private TableColumn<Order, String> ViewBranchID;

    @FXML
    private TableColumn<Order, String> ViewExpectDel;

    @FXML
    private TableColumn<Order, String> viewStatus;

    @FXML
    private TableColumn<Order, Number> ViewPrice;

    @FXML
    private TableColumn<Order, String> VieworderDate;

    @FXML
    private TableColumn<Order, String> VieworderID;

    @FXML
    private TableColumn<Order, Button> Viewshow;

    @FXML
    private Label accountStatus;

    @FXML
    private Button backBtn;
    
    @FXML
    private Label timer;
    
    @FXML
    private Button infoBtn;

    @FXML
    private TableColumn<Order, String> cancelBranchID;

    @FXML
    private TableColumn<Order, String> cancelExpectedDate;

    @FXML
    private TableColumn<Order, String> cancelOrderDate;

    @FXML
    private TableColumn<Order, String> cancelOrderID;

    @FXML
    private TableColumn<Order, Number> cancelPrice;
    
    @FXML
    private TableColumn<Order, String> cancelStatus;

    @FXML
    private TableColumn<Order, Button> cancelShow;
    
    @FXML
    private TableColumn<Order, String> reqcancelBranchID;

    @FXML
    private TableColumn<Order, String> reqcancelExpectedDate;

    @FXML
    private TableColumn<Order, String> reqcancelOrderDate;

    @FXML
    private TableColumn<Order, String> reqcancelOrderID;

    @FXML
    private TableColumn<Order, Number> reqcancelPrice;

    @FXML
    private TableColumn<Order, Button> reqcancelShow;

    @FXML
    private TableColumn<Order, String> reqcancelStatus;
    
    @FXML
    private Button refreshBtn;

    @FXML
    private Label employeeName;

    @FXML
    private Label employeeType;

    @FXML
    private Label massageLabelRemove;

    @FXML
    private Label phoneNumber;

    @FXML
    private Label priceLabel;

    @FXML
    private Button removeCancelOrderBTN;

    @FXML
    private TableView<Order> tableCancelOrder;

    @FXML
    private TableView<Order> tableViewOrder;
    
    @FXML
    private TableView<Order> tableCancelReqOrder;
    
    /**
     * Cancellation orders ObservableList for Preview.
     * History orders ObservableList for Preview.
     */
    ObservableList<Order> cancelationOrdersPreview = FXCollections.observableArrayList();
    ObservableList<Order> historyOrdersPreview = FXCollections.observableArrayList();
    ObservableList<Order> cancelRequestHistOrdersPreview = FXCollections.observableArrayList();
    
    /*
     *  SimpleDateFormat to parse or format Date object
     *  chooseDate - DateTime clicking of user on screen .
     *  expectedDate - DateTime of the order that choose on screen . 
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date chooseDate , expectedDate;
    /**
     * represent DateTime by List<Long> used in findDifference function. 
     */
    private List<Long> timeDifference;
    /**
     * Cancellation object for cancellation request by cancel order. 
     */
    private Cancellation cancellation;   
    
	/**
	 * 
	 * @param primaryStage main of catalog screen
	 * @throws Exception if there is problem with start of this stage
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/CustomerViewOrders.fxml"));

		Scene scene = new Scene(root);

		primaryStage.setTitle("View Orders");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}
	
	
	/** At the beginning of the screen 
	 * initialize all relevant preview object
	 *  @author Almog-Madar
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		/**
		 * bring side bar information to labels  
		 */
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, null, employeeType,
				((Customer) ClientController.user).toString());
		
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
		
		infoBtn.setOnMouseMoved(event -> {
			Tooltip tooltipCustom = new Tooltip("Dear Customer\n"
					+"Here You Can :\n"
					+ "1.View History.\n"
					+ "2.Cancel Orders.");
			tooltipCustom.setStyle("-fx-font-size: 20");
			Tooltip.install(infoBtn,tooltipCustom);
			
		});
	
		
							
		cancelOrderID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
		cancelBranchID.setCellValueFactory(new PropertyValueFactory<Order, String>("branchID"));
		cancelPrice.setCellValueFactory(new PropertyValueFactory<Order, Number>("price"));
		cancelOrderDate.setCellValueFactory(new PropertyValueFactory<Order, String>("orderDate"));
		cancelExpectedDate.setCellValueFactory(new PropertyValueFactory<Order, String>("expectedDelivery"));
		cancelStatus.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));
		cancelShow.setCellFactory(ShowButtonTableCell.<Order>forTableColumn("+", (Order o) -> {

			if(!OrderHandleController.isDetailsAllreadyOpen2())
			{
				if(o != null && o.getItems() != null) {
					OrderHandleController.setCustomerOrderView(o);
					OrderHandleController.setCustomerOrderDetails(o.getItems());
				}
				else 
					throw new NullPointerException();
			
				// new windows add send him the productInOrder list with the info
				Stage primaryStage = new Stage();
				CustomerViewOrderDetailsController customViewOrderDetails = new CustomerViewOrderDetailsController();
				try {
					customViewOrderDetails.start(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("No items");
				}
			}
			else
			{
				System.out.println("details allreaday open popup");
			}
			
			return o;
		}));
		
		
		VieworderID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
		ViewBranchID.setCellValueFactory(new PropertyValueFactory<Order, String>("branchID"));
		ViewPrice.setCellValueFactory(new PropertyValueFactory<Order, Number>("price"));
		VieworderDate.setCellValueFactory(new PropertyValueFactory<Order, String>("orderDate"));
		ViewExpectDel.setCellValueFactory(new PropertyValueFactory<Order, String>("expectedDelivery"));
		viewStatus.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));
		Viewshow.setCellFactory(ShowButtonTableCell.<Order>forTableColumn("+", (Order o) -> {

			if(!OrderHandleController.isDetailsAllreadyOpen2())
			{
				if(o != null && o.getItems() != null) {
					OrderHandleController.setCustomerOrderView(o);
					OrderHandleController.setCustomerOrderDetails(o.getItems());
				}
				else 
					throw new NullPointerException();
				
				// new windows add send him the productInOrder list with the info
				Stage primaryStage = new Stage();
				CustomerViewOrderDetailsController customViewOrderDetails = new CustomerViewOrderDetailsController();
				try {
					customViewOrderDetails.start(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("No items");
				}
			}
			else
			{
				System.out.println("details allreaday open popup");
			}
			
			return o;
		}));
		
		
		reqcancelOrderID.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
		reqcancelBranchID.setCellValueFactory(new PropertyValueFactory<Order, String>("branchID"));
		reqcancelPrice.setCellValueFactory(new PropertyValueFactory<Order, Number>("price"));
		reqcancelOrderDate.setCellValueFactory(new PropertyValueFactory<Order, String>("orderDate"));
		reqcancelExpectedDate.setCellValueFactory(new PropertyValueFactory<Order, String>("expectedDelivery"));
		reqcancelStatus.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));
		reqcancelShow.setCellFactory(ShowButtonTableCell.<Order>forTableColumn("+", (Order o) -> {

			if(!OrderHandleController.isDetailsAllreadyOpen2())
			{
				if(o != null && o.getItems() != null) {
					OrderHandleController.setCustomerOrderView(o);
					OrderHandleController.setCustomerOrderDetails(o.getItems());
				}
				else 
					throw new NullPointerException();
				
				// new windows add send him the productInOrder list with the info
				Stage primaryStage = new Stage();
				CustomerViewOrderDetailsController customViewOrderDetails = new CustomerViewOrderDetailsController();
				try {
					customViewOrderDetails.start(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("No items");
				}
			}
			else
			{
				System.out.println("details allreaday open popup");
			}
			
			return o;
		}));
		
		
		
		
		/**
		 * set cancellation orders on screen 
		 */
		loadCancelation();
		
			
		/**
		 * set history orders on screen 
		 */
		loadHistory();
		
		/**
		 * set cancellation waiting
		 */
		loadCancallationRequets();
		
	}
	  
	
	/** Back to previous screen. 
	 * 
	 * @param event - back to the previous screen 
	 * @throws Exception - FXML problem or page not found.
	 * @author Almog-Madar
	 */
    @FXML
    void back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CustomerPageController customerPage = new CustomerPageController();
		customerPage.start(primaryStage);
    }

    
    /* cancel order that was selected in the Table View .
     *  check in necessary to create cancellation request and updated order status 
     *  @author Almog-Madar
     */
    @FXML
    void cancelOrder(ActionEvent event) {
      	ObservableList<Order> orderSelected , allOrders;
    	allOrders=tableCancelOrder.getItems();
    	orderSelected=tableCancelOrder.getSelectionModel().getSelectedItems();

    	//only one product choose in table 
    	if(orderSelected.size()>0)
    	{

    		try {
    			chooseDate = sdf.parse(timer.getText());
    			System.out.println(sdf.format(chooseDate));
    			expectedDate = sdf.parse(orderSelected.get(0).getExpectedDelivery());
    			System.out.println(sdf.format(expectedDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    		/**
    		 * check time Difference between chooseDate click time to expectedDate of delivery order. 
    		 */
    		if(chooseDate.before(expectedDate))
    		{
    			timeDifference=findDifference(sdf.format(chooseDate),sdf.format(expectedDate));
    			if(timeDifference.size()>0)
    			{
    				checkTimeDifferenceAndCreateCancellation(orderSelected);
    				/**
    				 * pop up massage for screen of user. 
    				 */
					popMessageHandler.setMessage("Order("+ orderSelected.get(0).getOrderID() +") Cancellation request accepted and waiting to approve. \n"
							+ "We let you know by sms when the request will be handled \n"
							+ "Have a nice day :)");
    				/**
    				 * send Cancellation request to database 
    				 */
					if(orderSelected.get(0).getStatus().equals(OrderStatus.PENDING_WITH_DELIVERY) || orderSelected.get(0).getStatus().equals(OrderStatus.APPROVE_WITH_DELIVERY)) {
						cancellation.setStatus(OrderStatus.CANCEL_ORDER_DELIVERY_BY_CUSTOMER);
					}else {
						cancellation.setStatus(OrderStatus.CANCEL_ORDER_BY_CUSTOMER);
					}
    				if(ClientHandleTransmission.customerCancelOrder(cancellation))
    				{
    					massageLabelRemove.setText("Request Updated");
    					
    				}
    				else
    				{
    					massageLabelRemove.setText("Request Not Updated");
    				}
    					
    				/**
    				 * remove preview on screen
    				 */
    				orderSelected.forEach(allOrders::remove);
    			}
    		}
    		else if(chooseDate.after(expectedDate))
    		{
				popMessageHandler.setMessage("Your cancellation request for Order("+ orderSelected.get(0).getOrderID() +") can not be approved.\n"
    					+ "You choosen to cancel order after expected delivery.\n"
						+ "Have a nice day:)");
    		}
    		
			GenaralPopScroolBarUpController genaralPopScroolBarUpController = new GenaralPopScroolBarUpController();
			try {
				genaralPopScroolBarUpController.start(new Stage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
   
    	}
    }

    
    /*	check time Difference and calculate the refund that necessary 
     *  collected information inside Cancellation object.
     *  @author Almog-Madar  
     */
	private void checkTimeDifferenceAndCreateCancellation(ObservableList<Order> orderSelected) {
		/*
		 * full refund credit balance more then 4 hours 
		 * timeDifference.get(0) = years
		 * timeDifference.get(1) = days
		 * timeDifference.get(2) = hours
		 */
		if(timeDifference.get(0)>0 || timeDifference.get(1)>0 || timeDifference.get(2)>3)
		{
			System.out.println("here1");
			cancellation = new Cancellation(0,orderSelected.get(0).getOrderID() ,
					orderSelected.get(0).getCustomerID(),orderSelected.get(0).getPrice());
		}
		/*
		 * full refund credit balance more then 3 hours 
		 * timeDifference.get(2) = hours
		 * timeDifference.get(3) = minutes
		 * timeDifference.get(4) = seconds
		 */
		else if ( timeDifference.get(2)==3 && timeDifference.get(3)>0 && timeDifference.get(4)>0 )
		{
			System.out.println("here2");
			cancellation = new Cancellation(0,orderSelected.get(0).getOrderID() ,
					orderSelected.get(0).getCustomerID(),orderSelected.get(0).getPrice());
		}
		/* 50% refund credit balance between 1 to 3 hours.
		 * timeDifference.get(2) = hours
		 * timeDifference.get(3) = minutes
		 * timeDifference.get(4) = seconds
		 */
		else if ( (timeDifference.get(2)<=3 && timeDifference.get(2)>0) || (timeDifference.get(2)==3 && timeDifference.get(3)==0 && timeDifference.get(4)==0))
		{
			System.out.println("here3");
			cancellation = new Cancellation(0,orderSelected.get(0).getOrderID() ,
					orderSelected.get(0).getCustomerID(),orderSelected.get(0).getPrice()*0.5);
		}
		/** 50% refund credit balance less then 1 hour.
		 *  timeDifference.get(2) = hours
		 *  timeDifference.get(3) = minutes
		 *  timeDifference.get(4) = seconds
		 */
		else if ((timeDifference.get(2)<1 && timeDifference.get(3)>0)|| (timeDifference.get(2)==1 && timeDifference.get(3)==0 && timeDifference.get(4)==0))
		{
			System.out.println("here4");
			cancellation = new Cancellation(0,orderSelected.get(0).getOrderID() ,
					orderSelected.get(0).getCustomerID(),0);
		}
	}
    

	/**
	 * load cancellation orders on table
	 * @author Almog-Madar
	 */
	private void loadCancelation() {
		List<Order> cancelationOrders = ClientHandleTransmission.getCustomerOrdersForCancaltion();
		if(cancelationOrders.size()>0)
		{
			System.out.println(cancelationOrders);
			cancelationOrdersPreview.addAll(cancelationOrders);
			//change branchID to BranchName preview
			for(Order o :cancelationOrdersPreview) {
				for(Branches e : Branches.values())
				{
					if(o.getBranchID().equals(String.valueOf(e.getNumber()))){
						o.setBranchID(e.getName());
					}
				}
			}
			
			System.out.println(cancelationOrdersPreview);
			tableCancelOrder.setItems(cancelationOrdersPreview);
			OrderHandleController.setCancelationOrdersPreview(cancelationOrdersPreview);
		}
	}


	/**
	 * load history orders on table
	 * @author Almog-Madar
	 */
	private void loadHistory() {
		List<Order> historyOrders = ClientHandleTransmission.getCustomerOrdersHistory();
		if(historyOrders.size()>0)
		{
			System.out.println(historyOrders);
			historyOrdersPreview.addAll(historyOrders);
			//change branchID to BranchName preview
			for(Order o :historyOrdersPreview) {
				for(Branches e : Branches.values())
				{
					if(o.getBranchID().equals(String.valueOf(e.getNumber()))){
						o.setBranchID(e.getName());
					}
				}
			}
			System.out.println(historyOrdersPreview);
			tableViewOrder.setItems(historyOrdersPreview);
			OrderHandleController.setHistoryOrdersPreview(historyOrdersPreview);
		}
	}
	
	/**
	 * load waiting cancellation orders on table
	 * @author Almog-Madar
	 */
	private void loadCancallationRequets() {
		List<Order> cancelRequestHistOrders = ClientHandleTransmission.getCancellationRequetsOrder();
		if(cancelRequestHistOrders.size()>0)
		{
			System.out.println(cancelRequestHistOrders);
			cancelRequestHistOrdersPreview.addAll(cancelRequestHistOrders);
			//change branchID to BranchName preview
			for(Order o :cancelRequestHistOrdersPreview) {
				for(Branches e : Branches.values())
				{
					if(o.getBranchID().equals(String.valueOf(e.getNumber()))){
						o.setBranchID(e.getName());
					}
				}
			}
			
			System.out.println(cancelRequestHistOrdersPreview);
			tableCancelReqOrder.setItems(cancelRequestHistOrdersPreview);
			OrderHandleController.setCancelationOrdersPreview(cancelRequestHistOrdersPreview);
		}
	}

	
	/**
	 *  refresh screen orders information 
	 * @param event click on refresh button 
	 * @author Almog-Madar
	 */
    @FXML
    void refershScreenInfo(ActionEvent event) {
    	cancelationOrdersPreview.clear();
    	historyOrdersPreview.clear();
    	cancelRequestHistOrdersPreview.clear();
    	
    	
		/**
		 * set cancellation orders on screen 
		 */
		loadCancelation();
		
		/**
		 * set history orders on screen 
		 */
		loadHistory();
		/**
		 * set waiting request cancellation orders on screen 
		 */
		loadCancallationRequets();
    }
    
    /** find Difference between start date to end date.
     *  Difference in Years , Days , Hours , Minutes , Seconds by List<Long>.
     *  
     * @param start_date - String started date (yyyy-MM-dd HH:mm:ss) 
     * @param end_date  - String ended date (yyyy-MM-dd HH:mm:ss) 
     * @return List<Long> - Years,Days,Hours,Minutes,Seconds.
     * @author Almog-Madar
     */
    public List<Long> findDifference(String start_date,String end_date)
    {
    	
    	List<Long> timeResult = new ArrayList<Long>();
    	// SimpleDateFormat converts the
    	// string format to date object
    	// Try Class
    	try {

    		// parse method is used to parse
    		// the text from a string to
    		// produce the date
    		Date d1 = sdf.parse(start_date);
    		Date d2 = sdf.parse(end_date);
    		System.out.println(d1);
	
    		// Calculate time difference
    		// in milliseconds
    		long difference_In_Time= d2.getTime() - d1.getTime();
	
			// Calucalte time difference in seconds,
			// minutes, hours, years, and days
			long difference_In_Seconds= TimeUnit.MILLISECONDS.toSeconds(difference_In_Time)% 60;
			
			long difference_In_Minutes= TimeUnit.MILLISECONDS.toMinutes(difference_In_Time)% 60;
			
			long difference_In_Hours= TimeUnit.MILLISECONDS.toHours(difference_In_Time)% 24;
			
			long difference_In_Days= TimeUnit.MILLISECONDS.toDays(difference_In_Time)% 365;
			
			long difference_In_Years= TimeUnit.MILLISECONDS.toDays(difference_In_Time)/ 365l;
	
			// Print the date difference in
			// years, in days, in hours, in
			// minutes, and in seconds
			System.out.print("Difference between two dates is: ");
			
			// Print result
			System.out.println(
			difference_In_Years
			+ " years, "
			+ difference_In_Days
			+ " days, "
			+ difference_In_Hours
			+ " hours, "
			+ difference_In_Minutes
			+ " minutes, "
			+ difference_In_Seconds
			+ " seconds");
			
			//collect result information
    		timeResult.addAll(Arrays.asList(difference_In_Years,difference_In_Days,
    				difference_In_Hours,difference_In_Minutes,difference_In_Seconds));
    		
    	}
		catch (ParseException e) {
			e.printStackTrace();
		}
    	return timeResult;
     }

}

