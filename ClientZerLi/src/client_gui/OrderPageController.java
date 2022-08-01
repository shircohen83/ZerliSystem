package client_gui;

import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.OrderHandleController;
import client.popMessageHandler;
import entities_catalog.ProductInBranch;
import entities_users.Customer;
import enums.Branches;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
/**
 * @author Mor Ben Haim , Almog Madar
 * */
public class OrderPageController implements Initializable{

	@FXML
	private Button backBtn;

	@FXML
	private Button confirmBtn;
	
	@FXML
	private TextField greetingCard;
	
	@FXML
    private ComboBox<Branches> getBranchName=new ComboBox<>();
	
    @FXML
    private RadioButton ImidiateOrderRadio;

    @FXML
    private Label OrderMassageLabel;
    
    @FXML
    private RadioButton bleesingCardRadio;
    
    @FXML
    private DatePicker datePickUP;
   
    @FXML
    private TextField deliveryAddressTxtField;
    
    @FXML
    private TextField deliveryPersonNameTxtField;
	
    @FXML
    private TextField deliveryPhoneEndTxtField;
	
    @FXML
    private TextField deliveryPhoneStartTxtField;
    
    @FXML
    private RadioButton deliveryRadio;
    
    @FXML
    private Label timer;
    
    @FXML
    private Button infoBtn;

    
    @FXML
    private ComboBox<Time> hoursPickUpComboBox;
    
    @FXML
    private HBox hbox1;

    @FXML
    private HBox hbox2;

    @FXML
    private HBox hbox3;

    @FXML
    private HBox hbox4;
    
    @FXML
    private Label accountStatus;
    
    @FXML
    private Label employeeName;
    
    @FXML
    private Label employeeType;
    
    @FXML
    private Label entryGreeting;

    @FXML
    private Label phoneNumber1;
    
    @FXML
    private Label deliveryPriceLabel;
    
    @FXML
    private TextField deliveryLastNameTxtField;
    
    @FXML
    private Label newCustomerLabel;
    
    @FXML
    private Label totalPriceLabel;
    
    @FXML
    private RadioButton takeAwayRadio;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
        
    private ObservableList<Branches> branchOptions=FXCollections.observableArrayList();
    int orderID;
    String reciverName ,reciverLastName, phoneNumberStart,phoneNumberEnd , address ;
    boolean successCreateDeilivery=false , successImidiateOrder=false ;
    DecimalFormat df = new DecimalFormat("#,###.##");
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	StringBuilder expectedDelivery = new StringBuilder();
	Date orderDate = new Date();
	Date d1,d2;
	Calendar c1;

    
    
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/OrderPage.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("Order Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event ->{
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
			});	
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(employeeName, phoneNumber1, accountStatus,null, employeeType,
				((Customer) ClientController.user).toString());
		
		// Progress bar state - 85%
		progressIndicator.setStyle("-fx-color: #D0F6DD ; -fx-accent: green;");
		progressIndicator.setProgress(0.85f);
		// turn off blessing card 
		greetingCard.setVisible(false);
		deliveryOptionsSelection("close");
		// hours pick up comboBox 
		ObservableList<Time> orderTimesPickUp = FXCollections.observableArrayList();
		timeInit8To20(orderTimesPickUp);
		hoursPickUpComboBox.setItems(orderTimesPickUp);
		
		//timer on screen 
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
		//info button 
		infoBtn.setOnMouseMoved(event -> {
			Tooltip tooltipCustom = new Tooltip("Dear Customer\n"
					+"Here You Can:\n"
					+ "1.Add Blessing Card.\n"
					+ "2.Choose Take Away.\n"
					+ "3.Choose Imidiate Delivery.\n"
					+ "4.Choose Regualr Delivery.");
			tooltipCustom.setStyle("-fx-font-size: 20");
			Tooltip.install(infoBtn,tooltipCustom);
			
		});
		
		if(LocalTime.now().getMinute()>30)
		{
			hoursPickUpComboBox.setValue(new Time(LocalTime.now().getHour()+4,0,0));
		}
		else
		{
			hoursPickUpComboBox.setValue(new Time(LocalTime.now().getHour()+3,30,0));
		}
		
		datePickUP.setValue(LocalDate.now());
		
		
		if(((Customer) ClientController.user).getIsNewCustomer())
		{
			totalPriceLabel.setText(df.format(OrderHandleController.getTotalPrice()*0.8)+" ¤");
		}
		else
			totalPriceLabel.setText(df.format(OrderHandleController.getTotalPrice())+" ¤");
		
    	//cancel date option pickup 
    	datePickUP.setDisable(true);
    	hoursPickUpComboBox.setDisable(true);
    	
	
		//get branches from database 
		
		List<Branches> branches = ClientHandleTransmission.getBranches();
		if(branches.size()!=0)
		{
				branchOptions.addAll(branches);
		}
		this.getBranchName.setItems(branchOptions);
		this.getBranchName.setValue(Branches.KARMIEL);
		
	}
	
	
	/**
	 * event when customer press confirm this event 
	 * adding the order to the DB after the customer 
	 * finish his order
	 * adding delivers also if necessary 
	 * @param event
	 * @author Almog Madar , Mor Ben-Haim
	 */
	@FXML
	void confirm(ActionEvent event) {
		if((!ImidiateOrderRadio.isSelected() && !takeAwayRadio.isSelected() && !deliveryRadio.isSelected()) ) {
			OrderMassageLabel.setText("You should choose delivery method");
			return;
		}
		//get product in branch and set on OrderHandleController .
		List<ProductInBranch> productInBranch = ClientHandleTransmission.getProductInSpecificBranch(getBranchName.getValue());
		System.out.println(productInBranch);
		if(productInBranch.size()==0)
		{
			OrderMassageLabel.setText("Products out of stock in "+ getBranchName.getValue().name());
		}
		else
		{
			OrderHandleController.setProductInBranch(productInBranch);
			
			if(!OrderHandleController.checkQuantityInOrder()) {
				System.out.println(OrderHandleController.getMsg());
				//set massage to pop-up screen
				popMessageHandler.setMessage(OrderHandleController.getMsg());
				popMessageHandler.setTitle("Wrong Quantity Problem");
				
				GenaralPopScroolBarUpController genaralPopScroolBarUpController = new GenaralPopScroolBarUpController();
				try {
					genaralPopScroolBarUpController.start(new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else  // everything is OK ready to send order to database and create delivery.
			{
				if(takeAwayRadio.isSelected())
				{
					Date d3=Calendar.getInstance().getTime();
					//System.out.println("priceLabel->"+Double.parseDouble(totalPriceLabel.getText()));
					orderID=ClientHandleTransmission.addOrder(getBranchName.getValue(),greetingCard.getText(),dateFormat.format(d3),dateFormat.format(d3),"takeaway");
				}	
				else if(deliveryRadio.isSelected() || ImidiateOrderRadio.isSelected()  ) {
					reciverName = deliveryPersonNameTxtField.getText();
					reciverLastName=deliveryLastNameTxtField.getText();
					address=deliveryAddressTxtField.getText();
					phoneNumberStart=deliveryPhoneStartTxtField.getText();
					phoneNumberEnd=deliveryPhoneEndTxtField.getText();
					
					if(deliveryRadio.isSelected())
					{
						expectedDelivery=new StringBuilder();
						expectedDelivery.append(datePickUP.getValue().toString()+" ");
						expectedDelivery.append(hoursPickUpComboBox.getValue().toString());
					}
					
					
					if(reciverName.equals(""))
					{
						OrderMassageLabel.setText("No Name set , please write one");
						return;
					}
					else if(reciverLastName.equals(""))
					{
						OrderMassageLabel.setText("No LastName set , please write one");
						return;
					}
					else if (address.equals(""))
					{
						OrderMassageLabel.setText("No Address set , please write one");
						return;
					}
					else if(phoneNumberStart.equals("05#"))
					{
						OrderMassageLabel.setText("No area code set , please write one");
						return;
					}
					else if (phoneNumberStart.length()>3 || phoneNumberStart.length()<3 || !phoneNumberStart.matches("[0-9]+")) {
						OrderMassageLabel.setText("Wrong area code , please write correct one");
						return;
					}
					else if (phoneNumberEnd.length()>7 ||  phoneNumberEnd.length()<7 || !phoneNumberEnd.matches("[0-9]+")) {
						OrderMassageLabel.setText("Wrong phone number , please write correct one");
						return;
					}
					
					
					
					if(deliveryRadio.isSelected()) 
					{
						System.out.println(expectedDelivery.toString());
						orderDate=Calendar.getInstance().getTime();
						System.out.println(dateFormat.format(orderDate));
						//System.out.println("priceLabel->"+Double.parseDouble(totalPriceLabel.getText()));	
						orderID=ClientHandleTransmission.addOrder(getBranchName.getValue(),greetingCard.getText(),dateFormat.format(orderDate),expectedDelivery.toString(),"delivery");
					}
					else if (ImidiateOrderRadio.isSelected())
					{
						d1 = new Date();
						d2 = new Date();
						c1 = Calendar.getInstance();
						// d1 current time
						d1=c1.getTime();
						c1.add(Calendar.HOUR,3);
						// d2 current time
						d2=c1.getTime();
						
						System.out.println("current->" + dateFormat.format(d1));
						System.out.println("3 later ->" + dateFormat.format(d2));
						//System.out.println("priceLabel->"+Double.parseDouble(totalPriceLabel.getText()));
						
						orderID=ClientHandleTransmission.addOrder(getBranchName.getValue(),greetingCard.getText(),dateFormat.format(d1),dateFormat.format(d2),"Imidiate delivery");
					}
					
				}
				
				//order fine and set
				if(orderID!=0)         // if delivery regular
					if(deliveryRadio.isSelected())
					{
						successCreateDeilivery = ClientHandleTransmission.addDelivery(0,orderID,getBranchName.getValue(),dateFormat.format(orderDate),expectedDelivery.toString()
								,reciverName+" "+reciverLastName,address,phoneNumberStart+phoneNumberEnd);
						
						if(!successCreateDeilivery) {
							System.out.println("problem with create delivery ");
						}
					}
					else if(ImidiateOrderRadio.isSelected())
					{
						successCreateDeilivery = ClientHandleTransmission.addDelivery(0,orderID,getBranchName.getValue(),dateFormat.format(d1),dateFormat.format(d2)
								,reciverName+" "+reciverLastName,address,phoneNumberStart+phoneNumberEnd);
						
						if(!successCreateDeilivery) {
							System.out.println("problem with create delivery ");
						}
					}
				System.out.println("here " +orderID);
								
					progressIndicator.setProgress(1f);
					//label order screen
					OrderMassageLabel.setText("Order("+ orderID +") accepted and waiting to approved ");
					//pop-up massage init
					if(deliveryRadio.isSelected() || ImidiateOrderRadio.isSelected())
						popMessageHandler.setMessage("Order("+ orderID +") accepted and waiting to approved \n"
								+ "We let you know by Email when delivery on the way \n"
								+ "Have a nice day :)");
					else {
						popMessageHandler.setMessage("Order("+ orderID +") accepted and waiting to approved \n"
								+ "We let you know by Email when to pickup\n"
								+ "Have a nice day :)");
					}
					
					popMessageHandler.setTitle("Order Completed");
					
					OrderHandleController.clearAllOrderData();
					
					//next order without discount 
					if(((Customer) ClientController.user).getIsNewCustomer()) {
						((Customer) ClientController.user).setNewCustomer(false);
					}
				
					//open Customer Main screen 
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
					Stage primaryStage = new Stage();
					CustomerPageController custom = new CustomerPageController();
					try {
						custom.start(primaryStage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					//open PopUp with approved about order. 
					GenaralPopScroolBarUpController genaralPopScroolBarUpController = new GenaralPopScroolBarUpController();
					try {
						genaralPopScroolBarUpController.start(new Stage());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
			}	
		}		
	}


	@FXML
	void back(ActionEvent event) throws Exception {
		//clear static screen
		CartPageController.listViewCustom.clear();
		
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CartPageController cartPageController = new CartPageController();
		cartPageController.start(primaryStage);
	}

	
	
	/**  customer request 
	 * 
	 * @param event - request blessing card ticket
	 */
    @FXML
    void bleesingCardRadioSelected(ActionEvent event) {
    	if(bleesingCardRadio.isSelected())
    		greetingCard.setVisible(true);
    	else
    		greetingCard.setVisible(false);
    }
	

    @FXML
    void ImidiateOrderSelected(ActionEvent event) {
    	
    	//step progress 
    	progressIndicator.setProgress(0.92f);
    	
    	//open date option pickup 
    	datePickUP.setDisable(true);
    	hoursPickUpComboBox.setDisable(true); 
    	
    	
    	if(deliveryRadio.isSelected()) {
    		//close Immediate option
    		deliveryRadio.setSelected(false);
    	}
    	else
    	{
    		takeAwayRadio.setSelected(false);
    		//open options Visibility
    		deliveryOptionsSelection("open");
    	}
    	
    }
    
    /**   click radio button to request delivery 
     * 
     * @param event of click on delivery radio button 
     */
    @FXML
    void DeliverySelected(ActionEvent event) {
  
    	//step progress 
    	progressIndicator.setProgress(0.92f);
    	
    	//open date option pickup 
    	datePickUP.setDisable(false);
    	hoursPickUpComboBox.setDisable(false); 
    	
    	
    	if(deliveryRadio.isSelected()) {
    		//close Immediate option
    		ImidiateOrderRadio.setSelected(false);
    		takeAwayRadio.setSelected(false);
    		//open options Visibility
    		deliveryOptionsSelection("open");
    	}

    }
    
    /**   click radio button to request TakeAway  
     * 
     * @param event of click on TakeAway radio button 
     */
    @FXML
    void takeAwaySelected(ActionEvent event) {
    	//step progress 
    	progressIndicator.setProgress(0.92f);
    	
    	//cancel date option pickup 
    	datePickUP.setDisable(true);
    	hoursPickUpComboBox.setDisable(true);    	
    	
    	if(takeAwayRadio.isSelected()) {
    		//close Immediate option
    		deliveryRadio.setSelected(false);
    		ImidiateOrderRadio.setSelected(false);
    		deliveryOptionsSelection("close");
    	}
 
    }
    
    
    
    /** open or close  delivery options on screen 
     * 
     * 	@param mission - open or close hbox area
     * 	@author Almog-Madar
     */
    private void deliveryOptionsSelection(String mission) {
    	
    	if(mission.equals("open")) {
    		hbox1.setVisible(true);
    		hbox2.setVisible(true);
    		hbox3.setVisible(true);
    		hbox4.setVisible(true);
    		deliveryPriceLabel.setVisible(true);
    		
    		if(((Customer) ClientController.user).getIsNewCustomer()) {
    			totalPriceLabel.setText(df.format(OrderHandleController.getTotalPrice()*0.8+OrderHandleController.getShippingPrice())+" ¤");
    			newCustomerLabel.setVisible(true);
    		}
    		else
    		{
    			totalPriceLabel.setText(df.format(OrderHandleController.getTotalPrice()+OrderHandleController.getShippingPrice())+" ¤");
    			newCustomerLabel.setVisible(false);
    		}
    	}
    	else
    	{
    		hbox1.setVisible(false);
    		hbox2.setVisible(false);
    		hbox3.setVisible(false);
    		hbox4.setVisible(false);
    		deliveryPriceLabel.setVisible(false);
    		
    		if(((Customer) ClientController.user).getIsNewCustomer()) {
    			totalPriceLabel.setText(df.format(OrderHandleController.getTotalPrice()*0.8)+" ¤");
    			newCustomerLabel.setVisible(true);
    		}
    		else
    		{
    			totalPriceLabel.setText(df.format(OrderHandleController.getTotalPrice())+" ¤");
    			newCustomerLabel.setVisible(false);
    		}
    		
    	}

    }
    
    
    
    /**Initialize time comboBox with object of Time (08:00 until 20:00)
     * 
     * @param ObservableList<Time> orderTimesPickUp - list to add time Initialize
     * @author Almog-Madar
     */
	@SuppressWarnings("deprecation")
	private void timeInit8To20(ObservableList<Time> orderTimesPickUp) {
		Time time;
		int hours=8,min=0;
		
		for(int i=0;i<12;i++) 
		{
			for(int j=0;j<3;j++) 
			{
				if(j==0){
					min=0;
					
				}
				else if(j==1){
					min=30;
				}
				else{
					hours++;
					min=0;
				}
				time = new Time(hours,min,0);
				orderTimesPickUp.add(time);
			}
		}
	}
   	

}