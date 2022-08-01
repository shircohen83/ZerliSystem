package client_gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ClientUI;
import client.MissionAnalyzeClient;
import client.zerliClientListeners;
import communication.Mission;
import communication.TransmissionPack;

import entities_users.CustomerService;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CustomerServicePageController implements Initializable {
	@FXML
	private Button logOutBtn;
	@FXML
	private Label employeeName;
	@FXML
	private Label entryGreeting;
	@FXML
	private static GenaralPopUpController genaralPopUpController;

	@FXML
	private Label accountStatus;
	@FXML
	private Label phoneNumber;
	@FXML
	private Label employeeType;
    @FXML
    private Label timer;
	@FXML
	private Button viewcomplaintsBtn;

	@FXML
	private Button viewQuarterlyReportBtn;

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/CustomerServiceScreen.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Customer Service Menu");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
		MissionAnalyzeClient.addClientListener(new zerliClientListeners() {
			@Override
			public void notifyCustomerService() {
				MissionAnalyzeClient.removeClientListener(this);
				notifyCustomerServiceMsg("you have Open complaint that are still open");

			}
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
	void viewcomplaintsBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ComplaintsPageController complaintsPageController = new ComplaintsPageController();
		complaintsPageController.start(primaryStage);
	}

	@FXML
	void openComplaintBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ComplaintOpenComplaintController complaintOpenComplaintController = new ComplaintOpenComplaintController();
		complaintOpenComplaintController.start(primaryStage);

	}

	private void notifyCustomerServiceMsg(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				FXMLLoader loader = new FXMLLoader();
				Pane root;
				try {
					Stage Stage = new Stage();
					Stage.setResizable(false);
					root = loader.load(getClass().getResource("/client_gui/GeneralPopUp.fxml").openStream());
					GenaralPopUpController genaralPopUpController = new GenaralPopUpController();

					genaralPopUpController.setMainLabel(message);
					try {
						genaralPopUpController.start(Stage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimationTimer time = addingTimer();
		time.start();
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((CustomerService) ClientController.user).toString());

	}
	/**
	 * add Thread timer that give the current Time on the screen
	 * @return
	 */
	private AnimationTimer addingTimer() {
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		return time;
	}

}
