package client_gui;

import client.ClientHandleTransmission;
import client.EmailSending;
import client.MissionAnalyzeClient;
import client.zerliClientListeners;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField passwordTxt;

	@FXML
	private TextField userTxt;

	@FXML
	private Label errorLabel;

	@FXML
	private Button loginBtn;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/LoginScreen.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("ZerLi Login");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		MissionAnalyzeClient.addClientListener(new zerliClientListeners() {
			@Override
			public void userIsCustomer() {
				MissionAnalyzeClient.removeClientListener(this);
				
			}
			
			@Override
			public void userIsBranchManager() {
				MissionAnalyzeClient.removeClientListener(this);
			}
			@Override
			public void userIsCustomerService() {
				MissionAnalyzeClient.removeClientListener(this);
				
			}
			@Override
			public void userIsDeliveryAgent() {
				MissionAnalyzeClient.removeClientListener(this);
				
			}
			@Override
			public void userIsMarketingWorker() {
				MissionAnalyzeClient.removeClientListener(this);
				
			}
			@Override
			public void userIsNetworkManager() {
				MissionAnalyzeClient.removeClientListener(this);
				
			}
			@Override
			public void userIsServiceExpert() {
				MissionAnalyzeClient.removeClientListener(this);
				
			}
			@Override
			public void userIsShopWorker() {
				MissionAnalyzeClient.removeClientListener(this);
				
			}
			

		});
		
		
		////////////////////////////////////////////////////////////////////////////////////////////
		primaryStage.setOnCloseRequest(event -> {ClientHandleTransmission.DISCONNECT_FROM_SERVER();});
	}

	@FXML
	void LoginClick(MouseEvent event) {
		ClientHandleTransmission.USER_LOGIN(userTxt, passwordTxt, errorLabel, event);
		

	}
}
