package client_gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientHandleTransmission;
import entities_general.CreditCard;
import entities_users.BranchManager;
import entities_users.Customer;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This class is to GUI of add new customer from the branch manager side. Here
 * there is a table with the pending customer who wait for approval.
 * 
 * @author Omri Shalev
 */
public class BranchManagerAddNewCustomerController implements Initializable {
	/**
	 * those labels are to update the left side of the screen with the user details
	 */
	@FXML
	private Label accountStatusLbl;

	@FXML
	private Label branchLbl;

	@FXML
	private Label branchManagerNameLbl;

	@FXML
	private Label phoneNumberLbl;

	@FXML
	private Label userRoleLbl;

	@FXML
	private Label welcomeUserNameLbl;
//////////////////////////////////////////////////////////////
	@FXML
	private Label CreditCardNumberLbl;

	@FXML
	private Label CvvLbl;

	@FXML
	private Label ExpirationDateLbl;

	@FXML
	private Button ApproveBtn;

	@FXML
	private Button BackBtn;

	@FXML
	private Button creditCardInfo;

	@FXML
	private Button cvvInfo;

	@FXML
	private TextField CvvTxt;

	@FXML
	private Label SuccessFailedLbl;

	@FXML
	private Label timer;

	@FXML
	private ComboBox<String> MonthComboBox;

	@FXML
	private ComboBox<String> YearComboBox;

	@FXML
	private TextField creditCardNumTxt;

	@FXML
	private TableColumn<Customer, String> IDCol;

	@FXML
	private TableColumn<Customer, String> emailCol;

	@FXML
	private TableColumn<Customer, String> firstNameCol;

	@FXML
	private TableColumn<Customer, String> lastNameCol;

	@FXML
	private TableColumn<Customer, String> phoneNumberCol;

	@FXML
	private TableColumn<Customer, String> accountStatusCol;

	/**
	 * table view to show the pending customers
	 */
	@FXML
	private TableView<Customer> table;

	/**
	 * observable list that contains the pending customers that we will present on
	 * the screen
	 */
	private ObservableList<Customer> customers = FXCollections.observableArrayList();

	/**
	 * observable list that contains the years that the credit card can expired
	 */
	private ObservableList<String> years;

	/**
	 * observable list that contains the months that the credit card can expired
	 */
	private ObservableList<String> months;

	/**
	 * the list that will contain the customers from DB we will use this list to get
	 * details from the DB and then send this details to an observable list.
	 */
	private List<Customer> pendingCustomers; //

	private Customer customer = null;

	private CreditCard cc = null;

	String branchID, branchName;

	/**
	 * initialize the page at the start create the pending customer table and fill
	 * the combo box options
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * get the user details to the left side of the screen
		 */
		ClientController.initalizeUserDetails(branchManagerNameLbl, phoneNumberLbl, accountStatusLbl,
				welcomeUserNameLbl, userRoleLbl, ((BranchManager) ClientController.user).toString());

		branchID = ((BranchManager) ClientController.user).getBranchID();
		branchName = ClientHandleTransmission.getBranchName(branchID);
		branchLbl.setText(" " + branchName + " (" + branchID + ")");
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
		 * at start lock the remove product button
		 */
		ApproveBtn.setDisable(true);

		IDCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("ID"));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastName"));
		phoneNumberCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));
		emailCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
		accountStatusCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("accountStatus"));

		months = FXCollections.observableArrayList("--", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
				"11", "12");
		MonthComboBox.setItems(months); // set the months in combo box
		years = FXCollections.observableArrayList("----", "2022", "2023", "2023", "2024", "2025", "2026", "2027",
				"2028");
		YearComboBox.setItems(years); // set the years in combo box

		pendingCustomers = ClientHandleTransmission.getPendingCustomers();
		for (Customer customer : pendingCustomers) {
			customers.add(customer);
		}
		table.setItems(customers);

	}

	/**
	 * upload the page AddNewCustomer
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client_gui/AddNewCustomerPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Add New Customer");
		primaryStage.getIcons().add(new Image("/titleImg.jpg")); // main title
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			ClientHandleTransmission.DISCONNECT_FROM_SERVER();
		});
	}

	/**
	 * Approve new customer choose the customer we want to approve, insert credit
	 * card details and approve him. in the process we check that the credit card is
	 * and the numbers are correct without letters
	 * 
	 * @param event
	 */
	@FXML
	public void ApproveCustomer(ActionEvent event) {

		boolean number = false, cvv = false, expiration = false;
		String creditCardNumber = null, creditCardCVV = null, ExpirationMonth = null, ExpirationYear = null;

		creditCardNumber = creditCardNumTxt.getText();
		creditCardCVV = CvvTxt.getText();
		ExpirationMonth = MonthComboBox.getValue();
		ExpirationYear = YearComboBox.getValue();
		List<String> cardsNumbers = ClientHandleTransmission.getCreditCards();
		for (String card : cardsNumbers) {
			if (card.equals(creditCardNumber)) {
				CreditCardNumberLbl.setText("Credit Card Already Exist");
				CreditCardNumberLbl.setTextFill(Color.RED);
				SuccessFailedLbl.setText("Customer Not Approved!");
				SuccessFailedLbl.setTextFill(Color.RED);
				return;
			}
		}

		// if one of the fields is empty we can not approve this account
		if (creditCardNumTxt.getText().length() != 16) {
			CreditCardNumberLbl.setText("Incorrect number!");
			CreditCardNumberLbl.setTextFill(Color.RED);
		} else if (DigitsAreNumbers(creditCardNumTxt.getText()) == true) {
			number = true;
			CreditCardNumberLbl.setText("");
			CreditCardNumberLbl.setTextFill(Color.GREEN);
		} else {
			CreditCardNumberLbl.setText("Can not write letters!");
			CreditCardNumberLbl.setTextFill(Color.RED);
		}
		if (CvvTxt.getText().length() != 3) {
			CvvLbl.setText("Incorrect number!");
			CvvLbl.setTextFill(Color.RED);
		} else if (DigitsAreNumbers(CvvTxt.getText()) == true) {
			cvv = true;
			CvvLbl.setText("");
			CvvLbl.setTextFill(Color.GREEN);
		} else {
			CvvLbl.setText("Can not write letters!");
			CvvLbl.setTextFill(Color.RED);
		}
		if (MonthComboBox.getValue() == "--" || MonthComboBox.getValue() == null || YearComboBox.getValue() == "----"
				|| YearComboBox.getValue() == null) {
			ExpirationDateLbl.setText("Missing Details");
			ExpirationDateLbl.setTextFill(Color.RED);
		} else {
			expiration = true;
			ExpirationDateLbl.setText("");
			ExpirationDateLbl.setTextFill(Color.GREEN);
		}
		if (number == false || cvv == false || expiration == false) {
			SuccessFailedLbl.setText("Credit Card Not Approved");
			SuccessFailedLbl.setTextFill(Color.RED);
			return;
		} else {
			// create credit card
			cc = new CreditCard(creditCardNumber, creditCardCVV, ExpirationMonth + "-" + ExpirationYear);

			// if every thing is OK so we approve the customer
			customer.setCreditCard(cc);
			if (ClientHandleTransmission.approveNewCustomer(customer) == true) { // send the updated customer to the DB
				SuccessFailedLbl.setText("Customer Approved!");
				SuccessFailedLbl.setTextFill(Color.GREEN);
				CreditCardNumberLbl.setText("");
				CvvLbl.setText("");
				ExpirationDateLbl.setText("");
				pendingCustomers.remove(customer); // after the approval we remove this customer from pending list
				customers.remove(customer);
				ApproveBtn.setDisable(true);
			} else {
				SuccessFailedLbl.setText("Customer Not Approved!");
				SuccessFailedLbl.setTextFill(Color.RED);
			}
		}
	}

	/**
	 * check if the digits of the credit card are numbers. if there are letters -
	 * return false. else return true.
	 * 
	 * @param creditCardNunmber
	 * @return
	 */
	private boolean DigitsAreNumbers(String data) {
		if (data == null) {
			return false;
		}
		try {
			Long.parseLong(data);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * every click the user will choose a customer to deal with. can swap between
	 * customers.
	 * 
	 * @param event
	 */
	@FXML
	public void OnMouseClickChooseCustomer(MouseEvent event) {
		try {
			customer = table.getSelectionModel().getSelectedItem();
			if (customer != null)
				ApproveBtn.setDisable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * go back to branch manager page by loading this page.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void Back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		BranchManagerPageController branchManagerPage = new BranchManagerPageController();
		branchManagerPage.start(primaryStage);
	}

}
