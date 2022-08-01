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
import entities_users.BranchManager;
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

public class BranchManagerPageController implements Initializable {

	@FXML
	private Button addNewCustomerBtn;


	@FXML
	private Button editCustomerInfoBtn;

	@FXML
	private Button logOutBtn;

	@FXML
	private Button requestManagmentBtn;

	@FXML
	private Button viewReportsBtn;

	@FXML
	private Button info;

	@FXML
	private Label branchLbl;

	@FXML
	private Label branchManagerName;

	@FXML
	private Label phoneNumber;

	@FXML
	private Label timer;

	@FXML
	private Label userRole;

	@FXML
	private Label userStatus;

	@FXML
	private Label welcomeUserName;

	String branchID, branchName;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/BranchManagerPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Manager Menu");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	@FXML
	void addNewCustomer(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		BranchManagerAddNewCustomerController addNewCustomer = new BranchManagerAddNewCustomerController();
		addNewCustomer.start(primaryStage);
	}


	@FXML
	void editCustomerInfo(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		BranchManagerEditUserController editUserController = new BranchManagerEditUserController();
		editUserController.start(primaryStage);
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
	void requestManagment(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		BranchManagerOrderManagementController branchManagerOrderManagementController = new BranchManagerOrderManagementController();
		branchManagerOrderManagementController.start(primaryStage);
	}

	@FXML
	void viewReports(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		BranchManagerViewReportPageController viewReportPage = new BranchManagerViewReportPageController();
		viewReportPage.start(primaryStage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ClientController.initalizeUserDetails(branchManagerName, phoneNumber, userStatus, welcomeUserName, userRole,
				((BranchManager) ClientController.user).toString());

		branchID = ((BranchManager) ClientController.user).getBranchID();
		branchName = ClientHandleTransmission.getBranchName(((BranchManager) ClientController.user).getBranchID().toString());
		branchLbl.setText(" " + branchName + " (" + branchID + ")");

		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();

		if (((BranchManager) ClientController.user).getAccountStatus() == AccountStatus.FROZEN) {
			addNewCustomerBtn.setDisable(true);
			editCustomerInfoBtn.setDisable(true);
			requestManagmentBtn.setDisable(true);
			viewReportsBtn.setDisable(true);
		}


	}

}
