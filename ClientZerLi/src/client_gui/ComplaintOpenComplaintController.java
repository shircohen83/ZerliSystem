package client_gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import communication.Response;
import entities_reports.Complaint;
import entities_users.CustomerService;
import enums.Branches;
import enums.ComplaintsStatus;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller for customerService for open complaint for customer on specific
 * order
 * 
 * @author Mor Ben Haim
 *
 */
public class ComplaintOpenComplaintController implements Initializable {
	@FXML
	private Button backBtn;

	@FXML
	private Label employeeName;
	@FXML
	private Label phoneNumber;
	@FXML
	private Label accountStatus;
	@FXML
	private Label timer;
	@FXML
	private Label entryGreeting;
	@FXML
	private Label feedbackMessage;
	@FXML
	private TextField customerID;
	@FXML
	private TextField customerServiceID;
	@FXML
	private Label numberValidationCheck;

	@FXML
	private TextField orderID;

	@FXML
	private TextArea description;
	List<TextField> field = new ArrayList<>();

	@FXML
	private Label numberValidationCheck2;

	@FXML
	private Label numberValidationCheck3;
	@FXML
	private Label employeeType;
	@FXML
	private ComboBox<Branches> branches = new ComboBox<>();

	@FXML
	private ObservableList<Branches> branchName = FXCollections.observableArrayList();

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ComplaintOpenComplaintControllerPage.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setTitle("Open Complaint Page");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	/**
	 * go back the customerService menu
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void back(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CustomerServicePageController customerServicePageController = new CustomerServicePageController();
		customerServicePageController.start(primaryStage);

	}

	/**
	 * create new complaint case and and insert him to the DB
	 * 
	 * @param event
	 */
	@FXML
	void create(ActionEvent event) {

		if (checkValidation()) {

			Complaint c = new Complaint(null, customerID.getText(), orderID.getText(), customerServiceID.getText(),
					description.getText(), String.valueOf(branches.getValue().getNumber()), timer.getText(), null,
					ComplaintsStatus.OPEN, null);
			if (ClientHandleTransmission.createComplaint(c) == Response.OPEN_COMPLAINT_SUCCEED) {
				feedbackMessage.setText("Open Complaint Succeed");
				feedbackMessage.setTextFill(Color.GREEN);
			} else {
				feedbackMessage.setText("Open Complaint Failed");
				feedbackMessage.setTextFill(Color.RED);
			}
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnimationTimer time = runTimer();
		
		time.start();
		initEmployeeDetails();
		addingListenerToValues();
		
	}
	/**
	 * check validation of the field with all terms
	 * 
	 * @return true if the both condition are false and false other wise
	 */
	private boolean checkValidation() {
		boolean con1 = checkFillField();
		boolean con2 = checkValidField();

		return !con1 && !con2;
	}

	/**
	 * check if one of the field is in valid
	 * 
	 * @return if one of the field is invalid or false if all the fields are valid
	 */
	private boolean checkValidField() {

		boolean flag = false;

		if (!customerID.getText().matches("[0-9]+")) {
			flag = true;
			numberValidationCheck.setText("Insert Numbers Only!");
			numberValidationCheck.setTextFill(Color.RED);
		} else {
			numberValidationCheck.setText("");
			numberValidationCheck.setTextFill(Color.TRANSPARENT);
		}
		if (!customerServiceID.getText().matches("[0-9]+")) {
			flag = true;
			numberValidationCheck2.setText("Insert Numbers Only!");
			numberValidationCheck2.setTextFill(Color.RED);
		} else {
			numberValidationCheck2.setText("");
			numberValidationCheck2.setTextFill(Color.WHITE);
		}
		if (!orderID.getText().matches("[0-9]+")) {
			flag = true;
			numberValidationCheck3.setText("Insert Numbers Only!");
			numberValidationCheck3.setTextFill(Color.RED);
		} else {
			numberValidationCheck3.setText("");
			numberValidationCheck3.setTextFill(Color.WHITE);
		}
		return flag;
	}

	/**
	 * check if one of the field is empty
	 * 
	 * @return if one of the field is invalid or false if all the fields are valid
	 */
	private boolean checkFillField() {
		boolean flag = false;
		if (branches.getValue() == null) {
			branches.setStyle("-fx-border-color: red");
			flag = true;
		}

		if (customerID.getText().equals("")) {
			flag = true;
			customerID.setStyle("-fx-border-color: red");
		}
		if (customerServiceID.getText().equals("")) {
			flag = true;
			customerServiceID.setStyle("-fx-border-color: red");
		}
		if (orderID.getText().equals("")) {
			flag = true;
			orderID.setStyle("-fx-border-color: red");
		}

		if (description.getText().equals("")) {
			flag = true;
			description.setStyle("-fx-border-color: red");
		}

		return flag;
	}


	/**
	 * adding listener to all textfield,comboBox and textArea they will response
	 * when field will change and be able to check valid Field when the employee
	 * insert the fields
	 */
	private void addingListenerToValues() {
		createListenerToComboBox();
		branchName.addAll(ClientHandleTransmission.getBranches());
		branches.setItems(branchName);
		createListenerToTextFeilds();
		createListenerToTextArea();
	}

	/**
	 * create listener to textArea, the listener reset the border and the
	 * feedBackMessage to change after the validation check were finish
	 */
	private void createListenerToTextArea() {
		description.textProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != newValue) {
				description.setStyle("-fx-border-color: transparent");
				feedbackMessage.setText("");
			}
		});
	}

	/**
	 * create listener to all textFields, the listeners resets the borders and the
	 * feedBackMessage to change after the validation check were finish
	 */
	private void createListenerToTextFeilds() {
		field = Arrays.asList(customerID, customerServiceID, orderID);
		for (TextField t : field)
			t.textProperty().addListener((observable, oldValue, newValue) -> {
				if (oldValue != newValue) {
					t.setStyle("-fx-border-color: transparent");
					feedbackMessage.setText("");

				}
			});
	}
	
	/**
	 * create listener to ComboBox, the listener reset the borders and the
	 * feedBackMessage to change after the validation check were finish
	 */
	private void createListenerToComboBox() {
		branches.valueProperty().addListener(new ChangeListener<Branches>() {

			@Override
			public void changed(ObservableValue<? extends Branches> observable, Branches oldValue, Branches newValue) {
				if (oldValue != newValue) {
					feedbackMessage.setText("");
					branches.setStyle("-fx-border-color: transparent");

				}

			}
		});
	}
	/**
	 * init employee details when screen is load,
	 * int set the customerService textField in his cell
	 */
	private void initEmployeeDetails() {
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((CustomerService) ClientController.user).toString());
		customerServiceID.setText(ClientController.user.getID());
	}
	/**
	 * Timer that load thread on the screen that display the current date and time
	 * @return current date and time
	 */
	private AnimationTimer runTimer() {
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		return time;
	}
}
