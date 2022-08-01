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
import javafx.stage.Stage;

public class MarketingWorkerOpeningController implements Initializable {

	@FXML
	private Label accountStatus;

	@FXML
	private Label employeeName;

	@FXML
	private Label employeeType;

	@FXML
	private Label entryGreeting;

	@FXML
	private Button logOutBtn;

	@FXML
	private Button manageCatalogBTN;

	@FXML
	private Label phoneNumber;

	@FXML
	private Label timer;

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

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/MarketingWorkerOpeningPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("marketing worker menue");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@FXML
	void manageCatalogPressed(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		MarketingWorkerManageCatalogController manageCatalog = new MarketingWorkerManageCatalogController();
		manageCatalog.start(primaryStage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * get the user details to the left side of the screen
		 */
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((MarketingWorker) ClientController.user).toString());
		/**
		 * create a living clock on the screen
		 */
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();
		/**
		 * if the user is frozen we wont let him use the system freely
		 */
		if (((MarketingWorker) ClientController.user).getAccountStatus() == AccountStatus.FROZEN) {
			manageCatalogBTN.setDisable(true);
		}
	}
}
