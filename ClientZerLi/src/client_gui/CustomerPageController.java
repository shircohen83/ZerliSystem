package client_gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ClientUI;
import client.OrderHandleController;
import communication.Mission;
import communication.TransmissionPack;
import entities_users.Customer;
import enums.AccountStatus;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * customer menu controller handle the options of customer in the gui , display catalog or his orders.
 * @author almog madar
 *
 */
public class CustomerPageController implements Initializable {


	@FXML
	private Button logOutBtn;

	@FXML
	private Button viewCatalogBtn;
	
    @FXML
    private Button viewOrdersBtn;
	
    @FXML
    private Label accountStatus;
    
    @FXML
    private Label employeeName;
    
    @FXML
    private Label employeeType;
    
    @FXML
    private Label entryGreeting;

    @FXML
    private Label phoneNumber;
    
    @FXML
    private Label timer;
    
    @FXML
    private Button infoBtn;
    
    
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/CustomerPage.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("Customer Menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event ->{
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
			});	
	}
	

    @FXML
    void orderView(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CustomerViewOrdersController customerViewOrders = new CustomerViewOrdersController();
		customerViewOrders.start(primaryStage);	
    }
	

	@FXML
	void logOut(ActionEvent event) throws Exception {
		OrderHandleController.clearAllOrderData();
		
		TransmissionPack tp = new TransmissionPack(Mission.USER_LOGOUT, null, ClientController.user);
		ClientUI.chat.accept(tp);
		
		tp = ClientUI.chat.getObj();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		LoginController login = new LoginController();
		login.start(primaryStage);
	}

	

	@FXML
	void viewCatalog(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CatalogScreenController catalog = new CatalogScreenController();
		catalog.start(primaryStage);
	}

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
		//info button 
		infoBtn.setOnMouseMoved(event -> {
			Tooltip tooltipCustom = new Tooltip("Welcome Dear Customer\n"
					+"Here You Can:\n"
					+ "1.View Catalog products.\n"
					+ "2.Create Order.\n"
					+ "3.View Order History.\n"
					+ "4.Request Cancellation.");
			tooltipCustom.setStyle("-fx-font-size: 20");
			Tooltip.install(infoBtn,tooltipCustom);
			
		});
		
		
		if(((Customer) ClientController.user).getAccountStatus()==AccountStatus.FROZEN || ((Customer) ClientController.user).getAccountStatus()==AccountStatus.PENDING_APPROVAL) {
			viewOrdersBtn.setDisable(true);
		}
		
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((Customer) ClientController.user).toString());
		
	}

}
