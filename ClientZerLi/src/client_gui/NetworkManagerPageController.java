package client_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ClientUI;
import communication.Mission;
import communication.TransmissionPack;
import entities_users.NetworkManager;
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

public class NetworkManagerPageController implements Initializable {

	@FXML
	private Button logOutBtn;

	@FXML
	private Button viewReportsBtn;

	@FXML
    private Label phoneNumber;

    @FXML
    private Label userName;

    @FXML
    private Label userRole;
	
    @FXML
    private Label welcome;

    @FXML
    private Label accountStatus;

	
	@FXML
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/NetworkManagerPage.fxml"));
		Scene scene = new Scene(root);
    	stage.getIcons().add(new Image("/titleImg.jpg")); //main title
		stage.setTitle("Network Manager Menu");
		stage.setScene(scene);
		stage.show();
    	stage.setResizable(false);
    	stage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});

	}

	@FXML
	void logOut(ActionEvent event) throws Exception {
		TransmissionPack tp = new TransmissionPack(Mission.USER_LOGOUT, null, ClientController.user);
		ClientUI.chat.accept(tp);
		tp = ClientUI.chat.getObj();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		LoginController login = new LoginController();
		login.start(primaryStage);
	}


	@FXML
	void viewReports(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		NetworkManagerViewReportsController networkManagerViewReportsController = new NetworkManagerViewReportsController();
		networkManagerViewReportsController.start(primaryStage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(userName, phoneNumber, accountStatus, welcome,
				userRole, ((NetworkManager) ClientController.user).toString());

	}

}