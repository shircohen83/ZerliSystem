package client_gui;

import java.net.URL;

import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ComplaintsDataHandle;
import entities_users.CustomerService;
import enums.ComplaintsStatus;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * controller for the Description of the complaint the Customer Service can see
 * here the complaint description and refund the specific customer if he want
 * 
 * @author Mor Ben Haim
 *
 */
public class ComplaintsDescriptionPageController implements Initializable {

	@FXML
	private Label details;

	@FXML
	private Button finishBtn;

	@FXML
	private TextField refound;
	@FXML
	private Label employeeName;
	@FXML
	private Label entryGreeting;

	@FXML
	private Label accountStatus;
	@FXML
	private Label phoneNumber;
	@FXML
	private Label employeeType;
	@FXML
	private Label validCheck;

	@FXML
	private ComboBox<ComplaintsStatus> status = ComplaintsDataHandle.getStatus();
	private ObservableList<ComplaintsStatus> box = ComplaintsDataHandle.getBox();
	@FXML
	private RadioButton refoundCheck;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ComplaintsDescriptionPage.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title

		primaryStage.setTitle("Complaints Description Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	/**
	 * event when customer press confirm this event adding the order to the DB after
	 * the customer finish his order
	 * 
	 * @param event
	 */
	@FXML
	void finish(ActionEvent event) {
		/** if the refund input is not valid it will display on the screen error msg */
		if (!refound.getText().matches("[0-9.]+[0-9]") && refoundCheck.isSelected()) {
			validCheck.setText("Insert Numbers Only!");
			validCheck.setTextFill(Color.RED);

		} else {
			/**
			 * if the refund text field isn't empty it will set him and the specific
			 * complaint
			 */
			if (!refound.getText().isEmpty()) {
				ComplaintsDataHandle.getComplaint().setRefoundAmount(refound.getText());

			}
			((Node) event.getSource()).getScene().getWindow().hide();
			ComplaintsDataHandle.getComplaint().getStatus().setValue(status.getValue());

		}

	}

	/**
	 * if the refundBtn is disable it enable him and vice versa
	 * 
	 * @param event
	 */
	@FXML
	void refoundCheck(ActionEvent event) {
		if (refoundCheck.isSelected()) {
			refound.setDisable(false);
		} else {
			refound.setDisable(true);
		}
	}

	/**
	 * load the screen with specific complaint description and his status
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		details.setText(details.getText() + "\n" + ComplaintsDataHandle.getComplaint().getDescription());
		System.out.println(refound.getText());
		status.setItems(box);
		status.setValue(ComplaintsDataHandle.getComplaint().getStatus().getValue());
		System.out.println(ComplaintsDataHandle.getComplaint().getStatus().getValue());
		refound.setDisable(true);

		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((CustomerService) ClientController.user).toString());
		System.out.println((CustomerService) ClientController.user);

	}

}
