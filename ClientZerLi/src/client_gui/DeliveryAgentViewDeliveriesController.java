package client_gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import client.DeliveriesController;
import client.EmailSending;
import communication.Response;
import entities_catalog.ProductInOrder;
import entities_general.Deliveries;
import entities_general.DeliveryPreview;
import entities_users.DeliveryAgent;
import enums.DeliveryStatus;
import javafx.animation.AnimationTimer;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DeliveryAgentViewDeliveriesController implements Initializable {

	@FXML
	private TableView<DeliveryPreview> deliveriesTable;

	@FXML
	private TableView<DeliveryPreview> datesTable;

	@FXML
	private TableColumn<DeliveryPreview, Integer> deliveryIDCol;

	@FXML
	private TableColumn<DeliveryPreview, String> orderIDCol;

	@FXML
	private TableColumn<DeliveryPreview, String> customerIDCol;

	@FXML
	private TableColumn<DeliveryPreview, String> branchIDCol;

	@FXML
	private TableColumn<DeliveryPreview, Double> priceCol;

	@FXML
	private TableColumn<DeliveryPreview, String> orderDateCol;

	@FXML
	private TableColumn<DeliveryPreview, String> expectedDeliveryCol;

	@FXML
	private TableColumn<DeliveryPreview, String> receiverNameCol;

	@FXML
	private TableColumn<DeliveryPreview, String> addressCol;

	@FXML
	private TableColumn<DeliveryPreview, String> phoneNumberCol;

	@FXML
	private TableColumn<DeliveryPreview, ComboBox<DeliveryStatus>> statusCol;

	@FXML
	private TableColumn<DeliveryPreview, String> deliveryIDdatesTable;

	@FXML
	private Button BackBtn;

	@FXML
	private Button showOrderBtn;

	@FXML
	private Button updateBtn;

	@FXML
	private Label timer;

	@FXML
	private Label networkManagerName;

	@FXML
	private Label branchNameLbl;

	@FXML
	private Label phoneNumber;

	@FXML
	private Label userRole;

	@FXML
	private Label userStatus;

	@FXML
    private Label branchDetails;
	
	@FXML
	private Label SuccessFailedLbl;

	static boolean showOrderFlag = false;// false = can click, true = can not click

	private ObservableList<DeliveryPreview> deliveriesView = FXCollections.observableArrayList();
	private ObservableList<DeliveryPreview> dates = FXCollections.observableArrayList();
	private List<DeliveryPreview> deliveriesList;
	private DeliveryPreview currDeliveryPreview = null;

	String branchID, branchName, branchIDTitle;

	/**
	 * @param stage
	 * @throws IOException
	 */
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/DeliveryAgentViewDeliveries.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Delivery Agent View Delivieries");
		stage.getIcons().add(new Image("/titleImg.jpg")); //main title
		stage.setScene(scene);
		stage.show();
    	stage.setResizable(false);
		stage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	/**
	 * initialize the user details and the table view on the page.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientController.initalizeUserDetails(networkManagerName, phoneNumber, userStatus, null,
				userRole, ((DeliveryAgent) ClientController.user).toString());
	
		branchID = ((DeliveryAgent) ClientController.user).getBranchID();
		branchName = ClientHandleTransmission.getBranchName(branchID);
		branchIDTitle = branchNameLbl.getText() + " " + branchName + "(" + branchID + ")";
		branchNameLbl.setText(branchIDTitle);
		branchDetails.setText(" " + branchName +" ("+branchID+")");
		AnimationTimer time = new AnimationTimer() {
			@Override
			public void handle(long now) {
				timer.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		time.start();

		showOrderBtn.setDisable(true);
		deliveryIDCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, Integer>("deliveryID"));
		orderIDCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, String>("orderID"));
		customerIDCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, String>("customerID"));
		priceCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, Double>("price"));
		receiverNameCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, String>("receiverName"));
		addressCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, String>("address"));
		phoneNumberCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, String>("phoneNumber"));
		statusCol.setCellValueFactory(
				new PropertyValueFactory<DeliveryPreview, ComboBox<DeliveryStatus>>("deliveryStatusComboBox"));
		/*
		 * get the deliveries from the DB and add them to list that we will present in
		 * the table view
		 */
		deliveriesList = ClientHandleTransmission.getDeliveries(branchID);
		for (Deliveries delivery : deliveriesList) {
			deliveriesView.add(new DeliveryPreview(delivery.getDeliveryID(), delivery.getOrderID(),
					delivery.getBranchID(), delivery.getCustomerID(), delivery.getPrice(), delivery.getOrderDate(),
					delivery.getExpectedDelivery(), delivery.getArrivedDate(), delivery.getReceiverName(),
					delivery.getAddress(), delivery.getPhoneNumber(), delivery.getDeliveryStatus(),
					delivery.getOrderProducts()));

			System.out.println("delivery ID is: " + delivery.getOrderProducts()); // check
		}
		deliveriesTable.setItems(deliveriesView);

		/**
		 * create a table view with the dates that connect to the delivery we point at
		 * at the moment
		 */
		deliveryIDdatesTable.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, String>("deliveryID"));
		orderDateCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, String>("orderDate"));
		expectedDeliveryCol.setCellValueFactory(new PropertyValueFactory<DeliveryPreview, String>("expectedDelivery"));
	}

	/**
	 * open pop up with the items that belongs to the specific order we are pointing
	 * on.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void ShowOrder(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		DeliveryAgentViewOrderController deliveryAgentViewOrderController = new DeliveryAgentViewOrderController();
		if (DeliveriesController.getDelivery().getOrderProducts() != null
				&& DeliveryAgentViewOrderController.flag == false) {
			for (DeliveryPreview delivery : deliveriesList) {
				if (delivery.getDeliveryID() == (currDeliveryPreview.getDeliveryID())) {
					DeliveriesController.setDelivery(currDeliveryPreview);
					deliveryAgentViewOrderController.start(primaryStage);
					DeliveryAgentViewDeliveriesController.showOrderFlag = true;
					showOrderBtn.setDisable(true);
					return;
				}
			}
		}
	}

	/**
	 * Update the statuses of the deliveries by the Delivery Agent
	 * 
	 * @param event
	 */
	@SuppressWarnings("unlikely-arg-type")
	@FXML
	void Update(ActionEvent event) {
		/**
		 * update the list that we see on the screen
		 */
		for (DeliveryPreview dp : deliveriesView) {
			dp.setDeliveryStatusComboBox(statusCol.getCellData(dp));
			dp.setDeliveryStatus(dp.getDeliveryStatusComboBox().getValue());
		}
		/**
		 * update the list that we will send to the server with the delivery arrival
		 * time.
		 */
		for (int i = 0; i < deliveriesList.size(); i++) {
			deliveriesList.get(i).setDeliveryStatus(deliveriesView.get(i).getDeliveryStatus());
			if (deliveriesList.get(i).getDeliveryStatus() == DeliveryStatus.ARRIVED) {
				deliveriesList.get(i).setArrivedDate(timer.getText());
			}
		}

		/**
		 * send the updated list of deliveries to the DB by the result we will change
		 * the label that notice on success/fail text and color. if update was a success
		 * so remove the deliveries from the list. else, present a message that update
		 * failed.
		 */
		if (ClientHandleTransmission
				.UpdateDeliveriesStatus(deliveriesList) == Response.UPDATE_DELIVERIES_STATUS_SUCCESS) {

			SuccessFailedLbl.setText("Update Success!");
			SuccessFailedLbl.setTextFill(Color.GREEN);
			List<Integer> indexes = new ArrayList<>();
			/**
			 * going through the deliveries we have and if they arrived to the destination
			 * we will check if the customer got it on time or the delivery was late. if it
			 * was late so the customer get full refund.
			 */
			for (int i = 0; i < deliveriesList.size(); i++) {
				if (deliveriesList.get(i).getDeliveryStatus() == DeliveryStatus.ARRIVED) {
					indexes.add(i);

					Date expecteDate = null;
					Date arrivedDate = null;

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						arrivedDate = sdf.parse(deliveriesList.get(i).getArrivedDate());
						System.out.println(arrivedDate);
						expecteDate = sdf.parse(deliveriesList.get(i).getExpectedDelivery());
						System.out.println(expecteDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();

					c1.setTime(arrivedDate);
					c2.setTime(expecteDate);

					if (arrivedDate.after(expecteDate)) {
						System.out.println(deliveriesList.get(i).getDeliveryStatus());
						if (ClientHandleTransmission.DeliveryWasLateRefund(
								deliveriesList.get(i)) == Response.UPDATE_DELIVERY_LATE_REFUND_SUCCESS) {

							List<String> customerDetails = ClientHandleTransmission
									.getCustomerDetails(deliveriesList.get(i).getCustomerID());
							if (customerDetails != null) {
								try {
									/**
									 * send email to the customer with apologizing message.
									 */
									EmailSending.sendMail(customerDetails.get(1),
											deliveriesList.get(i).getPhoneNumber(),
											customerDetails.get(0)
													+ " We are apologizing for the delivery was late.\n You will get full refund for this order\n. Total Refund: "
													+ deliveriesList.get(i).getPrice(),
											"Zerli Refund Message");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			for (int j = 0; j < indexes.size(); j++) {
				deliveriesList.remove(indexes.get(j));
				deliveriesView.remove(indexes.get(j));
			}
		} else if (ClientHandleTransmission
				.UpdateDeliveriesStatus(deliveriesList) == Response.UPDATE_DELIVERIES_STATUS_FAILED) {
			SuccessFailedLbl.setText("Update Failed!");
			SuccessFailedLbl.setTextFill(Color.RED);
		}
	}

	/**
	 * while pointing on specific delivery we hold its details and can open a window
	 * with the order items
	 * 
	 * @param event
	 */
	@FXML
	void ChooseDelivery(MouseEvent event) {
		try {

			/**
			 * insert the current delivery details to currDelivery parameter
			 */
			DeliveriesController.setDelivery((DeliveryPreview) deliveriesTable.getSelectionModel().getSelectedItem());
			currDeliveryPreview = DeliveriesController.getDelivery();
			if (currDeliveryPreview != null && DeliveriesController.getDelivery().getOrderProducts()!=null) {
				/**
				 * lock order button and clear the products list
				 */
				if (DeliveriesController.getDelivery() != null
						&& DeliveryAgentViewDeliveriesController.showOrderFlag == false) {
					showOrderBtn.setDisable(false);
				} else if (DeliveryAgentViewDeliveriesController.showOrderFlag == true) {
					showOrderBtn.setDisable(true);
				}

				/**
				 * build the dates table
				 */
				if (dates.size() > 0)
					dates.clear();
				/**
				 * update dates table
				 */
				dates.add(DeliveriesController.getDelivery());
				datesTable.setItems(dates);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * go back to the previous page
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void Back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		DeliveryAgentPageController deliveryAgentPageController = new DeliveryAgentPageController();
		deliveryAgentPageController.start(primaryStage);
	}

	

}