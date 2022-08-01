package client_gui;

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
import entities_users.ServiceExpert;
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

public class ServiceExpertPageController implements Initializable{

	@FXML
    private Label accountStatus;

    @FXML
    private Button logOutBtn;

    @FXML
    private Label phoneNumber;

    @FXML
    private Button serviceReportBtn;

    @FXML
    private Label userName;

    @FXML
    private Label userRole;

    @FXML
    private Label welcomeUser;
    
    @FXML
    private Label timer;
    
    public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ServiceExpertPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Service Expert Menu");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event ->{
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
			});	
	}
    @FXML
    void ServiceReport(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ServiceExpertViewReportsController viewReportPage = new ServiceExpertViewReportsController();
		try {
			viewReportPage.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void logOut(ActionEvent event) {
    	TransmissionPack tp = new TransmissionPack(Mission.USER_LOGOUT, null, ClientController.user);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		LoginController login = new LoginController();
		try {
			login.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(userName, phoneNumber, accountStatus, welcomeUser,
				userRole, ((ServiceExpert) ClientController.user).toString());
		
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
		
		
	}

}
