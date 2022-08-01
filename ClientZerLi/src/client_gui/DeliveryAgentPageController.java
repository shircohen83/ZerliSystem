package client_gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ClientUI;
import communication.Mission;
import communication.TransmissionPack;
import entities_users.DeliveryAgent;
import entities_users.MarketingWorker;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DeliveryAgentPageController implements Initializable{

    @FXML
    private Button logoutBtn;

    @FXML
    private Label networkManagerName;

    @FXML
    private Label phoneNumber;

    @FXML
    private Label userRole;

    @FXML
    private Label userStatus;

    @FXML
    private Button viewDeliveriesBtn;

    @FXML
    private Label welcomeBackUserName;

    @FXML
    private Label branchDetails;

    @FXML
    private Label timer;
    
    private String branchID, branchName;
    
    @FXML
    public void start(Stage stage) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/client_gui/DeliveryAgentPage.fxml"));
    	Scene scene = new Scene(root);
    	stage.setTitle("Delivery Agent Menu");
    	stage.getIcons().add(new Image("/titleImg.jpg")); //main title
    	stage.setScene(scene);
    	stage.show();
    	stage.setResizable(false);
    	stage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
    }	

    @FXML
    void ViewDeliveries(ActionEvent event) throws IOException {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		DeliveryAgentViewDeliveriesController deliveryAgentViewDeliveriesController = new DeliveryAgentViewDeliveriesController();
		deliveryAgentViewDeliveriesController.start(primaryStage);
    }

	@FXML
	void LogOut(ActionEvent event) throws Exception {
		TransmissionPack tp = new TransmissionPack(Mission.USER_LOGOUT, null, ClientController.user);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		LoginController login = new LoginController();
		login.start(primaryStage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(networkManagerName, phoneNumber, userStatus, welcomeBackUserName, userRole,
				((DeliveryAgent) ClientController.user).toString());
		
		branchID = ((DeliveryAgent) ClientController.user).getBranchID();
		branchName = ClientHandleTransmission.getBranchName(branchID);
		branchDetails.setText(" " +branchName +" ("+branchID+")");
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
		
		if (((DeliveryAgent) ClientController.user).getAccountStatus() == AccountStatus.FROZEN) {
			viewDeliveriesBtn.setDisable(true);
		}
	}
}
