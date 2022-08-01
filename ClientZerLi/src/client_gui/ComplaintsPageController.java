package client_gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.ComplaintsDataHandle;
import communication.Response;
import entities_reports.ComplaintPreview;
import entities_users.CustomerService;
import enums.ComplaintsStatus;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * controller for CustomerServiceComplaintPage this class create an
 * ObservableList table that contain all the complaints of the specific
 * CustomerService
 * 
 * @author Mor Ben Haim
 *
 */
public class ComplaintsPageController implements Initializable {
	@FXML
	private Button Back;
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
	private TableView<ComplaintPreview> Complaints;

	@FXML
	private TableColumn<ComplaintPreview, String> complaintIDCol;

	@FXML
	private TableColumn<ComplaintPreview, String> customerIDCol;
	@FXML
	private TableColumn<ComplaintPreview, String> orderIDCol;

	@FXML
	private TableColumn<ComplaintPreview, String> complaintOpeningCol;

	@FXML
	private TableColumn<ComplaintPreview, String> treatmentUntilCol;
	@FXML
	private Label feedbackMsg;

	@FXML
	private TableColumn<ComplaintPreview, String> priceCol;
	@FXML
	private TableColumn<ComplaintPreview, String> branchIDCol;

	@FXML
	private TableColumn<ComplaintPreview, Button> showCol;
	@FXML
	private TableRow<ComplaintPreview> table;

	@FXML
	private TableColumn<ComplaintPreview, ComboBox<ComplaintsStatus>> statusCol;

	private ObservableList<ComplaintPreview> listView = FXCollections.observableArrayList();

	@FXML
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/ComplaintsPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Complaints");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); //main title
		primaryStage.setScene(scene);
		primaryStage.show();
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
	void Back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CustomerServicePageController customerServicePageController = new CustomerServicePageController();
		customerServicePageController.start(primaryStage);
	}

	/**
	 * initialize the screen with all the complaints of the specific CustomerSerivce
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		showCol.setCellFactory(
				ShowButtonTableCell.<ComplaintPreview>forTableColumn("description", (ComplaintPreview c) -> {

					Stage primaryStage = new Stage();
					ComplaintsDescriptionPageController complaintsDescriptionPageController = new ComplaintsDescriptionPageController();
					/**
					 * loop that find the specific complaits from the table
					 */
					for (ComplaintPreview co : ComplaintsDataHandle.getComlaints()) {
						if (co.equals(c)) {
							ComplaintsDataHandle.setComplaint(c);

							break;
						}
					}
//				
					try {
						complaintsDescriptionPageController.start(primaryStage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					return c;
				}));
		complaintIDCol.setCellValueFactory(new PropertyValueFactory<ComplaintPreview, String>("complaintID"));

		customerIDCol.setCellValueFactory(new PropertyValueFactory<ComplaintPreview, String>("customerID"));
		orderIDCol.setCellValueFactory(new PropertyValueFactory<ComplaintPreview, String>("orderID"));
		branchIDCol.setCellValueFactory(new PropertyValueFactory<ComplaintPreview, String>("branchID"));

		complaintOpeningCol.setCellValueFactory(new PropertyValueFactory<ComplaintPreview, String>("complaintOpening"));
		treatmentUntilCol.setCellValueFactory(new PropertyValueFactory<ComplaintPreview, String>("treatmentUntil"));

		statusCol.setCellValueFactory(new PropertyValueFactory<ComplaintPreview, ComboBox<ComplaintsStatus>>("status"));

		statusCol.setOnEditCommit(e -> {
 
            // Do what you want to update the row
            System.out.printf("Updated supplier from %d to %d, %s%n",
                    e.getOldValue(), e.getNewValue(), e.getRowValue().getComplainState());
             
        });

         
		
			
			
		
		ClientController.initalizeUserDetails(employeeName, phoneNumber, accountStatus, entryGreeting, employeeType,
				((CustomerService) ClientController.user).toString());
		List<ComplaintPreview>cp=ClientHandleTransmission.getComplaintsForCustomerService(ClientController.user.getID());
		createListenerToComboBoxChanges(cp);
		listView.addAll(cp);

		Complaints.setItems(listView);
		
		
		/**
		 * this listener paint the complaint row in red if the complaint still opening after 24 hours
		 */
		Complaints.setRowFactory(tv -> new TableRow<ComplaintPreview>() {
			@SuppressWarnings("unused")
			@Override
			protected void updateItem(ComplaintPreview item, boolean empty) {
				
				super.updateItem(item, empty);
				if (item == null || item.getComplaintsStatus() == ComplaintsStatus.STILL_GOT_TIME) {
					String style=getStyle();
					setStyle("-fx-background-color: transparent");
					
				} else if (item.getComplaintsStatus() == ComplaintsStatus.DELAY) {
					
					setStyle("-fx-background-color: red;");
				}

			}
			
		});
	}
	/**
	 * create Listener to combobox in each complaint that display on the screen
	 * in reset the feedBackMSg 
	 * @param cp
	 */
	private void createListenerToComboBoxChanges(List<ComplaintPreview> cp) {
		for(ComplaintPreview c:cp) {
			c.getStatus().valueProperty().addListener(new ChangeListener<ComplaintsStatus>() {

				@Override
				public void changed(ObservableValue<? extends ComplaintsStatus> observable, ComplaintsStatus oldValue,
						ComplaintsStatus newValue) {
					if (oldValue != newValue) {
						feedbackMsg.setText("");

					}
					
				}
			});
		}
	}

	/**
	 * update event that send to the db all the complaints to update there status
	 * 
	 * @param event
	 */
	@FXML
	void update(ActionEvent event) {
		/**
		 * check if the updated mission was finish and display the user feed back
		 * message with the specific details
		 */
		if (ClientHandleTransmission
				.updateComplaints(ComplaintsDataHandle.getComlaints()) == Response.COMPLAINTS_UPDATE_SUCCEED) {
			feedbackMsg.setText("Update Succeed");
			feedbackMsg.setTextFill(Color.GREEN);
		} else {
			feedbackMsg.setText("Update Failed");
			feedbackMsg.setTextFill(Color.RED);
		}

	}

}
